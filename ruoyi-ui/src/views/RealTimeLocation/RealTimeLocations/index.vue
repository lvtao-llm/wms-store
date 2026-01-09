<template>
  <div class="map-container">
    <div id="container"></div>
  </div>
</template>

<script>
import dayjs from "dayjs";
import TimeLine from "@/components/TimeLine/timeline-canvas.vue";
import MultiplierSelector from "@/components/Preset/index";
import {
  DynamicCanvasLayer,
  simplifyPath,
  parseTimeString,
  calculateSegmentAngle,
  createSvgIcon,
  convertTrajectoryPoints,
  convertTrajectoryPointsWithBMap,
} from "../../utils";

import {
  listLanya_device_card_sender_log_by_name_card_type,
  wms_vehicle_record,
} from "@/api/system/lanya_device_card_sender_log";

export default {
  name: "MapIndex",
  components: {
    TimeLine,
    MultiplierSelector,
  },
  data() {
    const start = new Date();
    start.setHours(0, 0, 0, 0); // 当天 00:00:00
    const end = new Date();
    return {
      // 响应式数据
      queryParams: {},
      selectData: [],
      selectCarData: [],
      tableData: [],
      dateRange: [
        "2025-05-23 00:00:00", // 可换任意格式化函数
        "2025-05-23 24:00:00",
      ],
      selectedMultiplier: 10,
      isAutoPlay: true,
      time_line: null,
      time_range: ["2023-01-02 00:00:00", "2023-01-02 23:59:59"],
      timeIndex: 0,
      getTimeLineOne: "",
      isShowD: false,
      isShowX: false,
      isBf: false,
      isShowQ: false,
      isToggle: false,
      inDate: "",
      // 坐标转换开关：true=启用转换(WGS84转BD09), false=不转换
      enableCoordinateConvert: true,

      // 地图相关
      map: null,
      polyline: null,
      points: [],
      markers: [],
      startMeddleTime: "",
      savedPointGroups: [],
      presetOverlays: {
        items: [],
        animationId: null,
        arrows: [],
        isTracking: false,
      },
      ws: null,
      // 存储所有自定义标记和标签，方便后续管理
      customMarkers: [],
    };
  },
  mounted() {
    this.initMap();
    this.$nextTick(() => {
      this.showWarehouse();
    });
  },
  created() {
    this.onLoad();
  },
  beforeDestroy() {
    // 移除地图缩放监听
    if (this.map) {
      this.map.removeEventListener("zoomend", this.adjustMarkersPosition);
    }
    // 清理自定义标记
    this.customMarkers.forEach((item) => {
      this.map.removeOverlay(item.marker);
      this.map.removeOverlay(item.label);
    });
  },
  methods: {
    onLoad() {
      const baseUrl = process.env.VUE_APP_BASE_URL.substr(
        5,
        process.env.VUE_APP_BASE_URL.length - 1
      );
      const wsuri = `ws:${baseUrl}/system/lanya-transfer/ws/dingwei`;
      this.ws = new WebSocket(wsuri);
      const that = this;
      this.ws.onopen = function () {
        that.text_content = that.text_content + "已经打开连接!" + "\n";
      };
      this.ws.onmessage = function (event) {
        that.text_content = event.data + "\n";
        const data = JSON.parse(event.data);

        if (data.msgType === "currentPersonLocation") {
          that.removeAllCustomMarkers();
          const points = convertTrajectoryPoints(data.data, 0.00015, -0.0001);
          // const points = data.data;
          points.forEach((i) => {
            that.addCustomMarker(i.longitude, i.latitude, i.realName);
          });
        }
      };
      this.ws.onclose = function () {
        that.text_content = that.text_content + "已经关闭连接!" + "\n";
      };
    },
    // 初始化地图
    initMap() {
      const BMap = window.BMap;
      this.map = new BMap.Map("container");
      this.map.centerAndZoom(new BMap.Point(125.05, 46.59), 15);
      this.map.setMinZoom(11);
      this.map.setMaxZoom(17);
      this.map.enableScrollWheelZoom(true);

      // 添加控件
      this.map.addControl(
        new BMap.NavigationControl({
          anchor: window.BMAP_ANCHOR_TOP_RIGHT,
          type: window.BMAP_NAVIGATION_CONTROL_SMALL,
        })
      );
      this.map.addControl(new BMap.ScaleControl());
      this.map.addControl(new BMap.OverviewMapControl());

      // 监听地图缩放事件，缩放结束后调整标记位置（关键修复）
      this.map.addEventListener("zoomend", this.adjustMarkersPosition);

      // 右键添加标记
      this.map.addEventListener("rightclick", (e) => {});
    },

    // 新增：地图缩放后重新校准所有标记位置
    adjustMarkersPosition() {
      const BMap = window.BMap;
      this.customMarkers.forEach((item) => {
        // 重新设置标记位置（强制校准）
        item.marker.setPosition(item.point);

        // 重新计算标签位置
        if (item.label && item.marker) {
          // 获取标记的像素位置
          const pixel = this.map.pointToOverlayPixel(item.marker.getPosition());
          // 重新设置标签位置（在标记上方）
          item.label.setPosition(
            this.map.overlayPixelToPoint(
              new BMap.Pixel(pixel.x, pixel.y - (item.iconHeight || 50) - 10)
            )
          );
        }
      });
    },

    // 修复：添加自定义图片标记（带文字标签）
    /**
     * 添加自定义图片标记
     * @param {number} lng - 经度
     * @param {number} lat - 纬度
     * @param {string} name - 要显示的名称
     * @param {string} iconUrl - 自定义图片路径（可选，默认使用默认图标）
     * @param {number} iconWidth - 图标宽度（可选，默认32）
     * @param {number} iconHeight - 图标高度（可选，默认50）
     */
    addCustomMarker(
      lng,
      lat,
      name,
      iconUrl = require("@/assets/images/icon.png"),
      iconWidth = 32,
      iconHeight = 50
    ) {
      const BMap = window.BMap;
      // 创建坐标点
      const point = new BMap.Point(lng, lat);

      // 1. 创建自定义图标 - 关键修复：正确设置锚点
      const icon = new BMap.Icon(
        iconUrl,
        new BMap.Size(iconWidth, iconHeight),
        {
          // 锚点设置为图标底部中点（核心！确保标记精准定位到坐标点）
          anchor: new BMap.Size(iconWidth / 2, iconHeight),
          // 图片偏移设为0
          imageOffset: new BMap.Size(0, 0),
          // 确保图标尺寸固定，不随地图缩放变形
          imageSize: new BMap.Size(iconWidth, iconHeight),
        }
      );

      // 2. 创建标记 - 关键修复：移除手动offset，使用百度地图原生定位
      const marker = new BMap.Marker(point, {
        icon: icon,
        // 启用批量清除
        enableMassClear: true,
        // 禁用拖拽（如果不需要）
        enableDragging: false,
      });

      // 3. 创建文字标签（显示在图标上方）
      const label = new BMap.Label(name, {
        // 标签偏移：基于标记点向上偏移（图标高度+10px）
        offset: new BMap.Size(-10, -15),
      });

      // 设置标签样式
      label.setStyle({
        color: "#165F8F", // 文字颜色
        fontSize: "11px", // 字体大小
        fontWeight: "bold", // 加粗
        backgroundColor: "rgba(255, 255, 255, 0.9)", // 背景色（半透明白色）
        border: "1px solid #ccc", // 边框
        borderRadius: "4px", // 圆角
        padding: "2px 8px", // 内边距
        whiteSpace: "nowrap", // 不换行
        boxShadow: "0 2px 4px rgba(0,0,0,0.1)", // 阴影
        textAlign: "center", // 文字居中
        lineHeight: "10px", // 行高
        zIndex: 9999, // 确保在最上层
        // 关键：禁用标签的绝对定位，让百度地图自动管理
        position: "absolute",
        // 防止标签被地图元素遮挡
        pointerEvents: "none",
      });

      // 4. 给标记添加标签
      marker.setLabel(label);

      // 5. 添加到地图
      this.map.addOverlay(marker);

      // 6. 存储标记和标签，方便后续管理（新增图标尺寸信息）
      this.customMarkers.push({
        marker: marker,
        label: label,
        point: point,
        name: name,
        iconWidth: iconWidth,
        iconHeight: iconHeight,
      });

      // 可选：添加点击事件
      marker.addEventListener("click", () => {
        console.log(`点击了标记: ${name}`);
        // 可以添加点击标记后的逻辑，比如弹窗、定位等
        this.map.centerAndZoom(point, 16);
      });

      return marker;
    },

    // 新增：移除所有自定义标记
    removeAllCustomMarkers() {
      this.customMarkers.forEach((item) => {
        this.map.removeOverlay(item.marker);
        this.map.removeOverlay(item.label);
      });
      this.customMarkers = [];
    },

    // 原有方法：简单标记（保留，可作为备用）
    addMarker(lng, lat, title) {
      const BMap = window.BMap;
      const point = new BMap.Point(lng, lat);
      const marker = new BMap.Marker(point);
      marker.setTitle(title);
      this.map.addOverlay(marker);
      return marker;
    },

    showWarehouse() {
      let arr = [
        {
          points: [
            {
              lat: 46.594102,
              lng: 125.048662,
            },
            {
              lat: 46.592294,
              lng: 125.050638,
            },
            {
              lat: 46.591526,
              lng: 125.051087,
            },
            {
              lat: 46.589817,
              lng: 125.052973,
            },
            {
              lat: 46.587488,
              lng: 125.057016,
            },
            {
              lat: 46.587315,
              lng: 125.057716,
            },
            {
              lat: 46.589012,
              lng: 125.058076,
            },
            {
              lat: 46.588702,
              lng: 125.059495,
            },
            {
              lat: 46.588107,
              lng: 125.059836,
            },
            {
              lat: 46.588504,
              lng: 125.062244,
            },
            {
              lat: 46.592009,
              lng: 125.061489,
            },
            {
              lat: 46.592529,
              lng: 125.059621,
            },
            {
              lat: 46.593074,
              lng: 125.05919,
            },
            {
              lat: 46.594313,
              lng: 125.059369,
            },
            {
              lat: 46.595985,
              lng: 125.049308,
            },
            {
              lat: 46.594102,
              lng: 125.048662,
            },
          ],
        },
      ];

      this.map.clearOverlays();
      arr.forEach((group, groupIndex) => {
        // 绘制多边形（包括填充和边框）
        if (group.points.length >= 3) {
          const pathPoints = group.points.map(
            (p) => new window.BMap.Point(p.lng, p.lat)
          );

          // 创建多边形填充
          const polygon = new window.BMap.Polygon(pathPoints, {
            strokeColor: "#4CAF50",
            strokeWeight: 3,
            strokeOpacity: 0.8,
            strokeStyle: "solid",
            fillColor: "#4CAF50",
            fillOpacity: 0.3, // 半透明填充
          });
          this.map.addOverlay(polygon);
        } else if (group.points.length >= 2) {
          // 如果只有2个点，绘制线条
          const pathPoints = group.points.map(
            (p) => new window.BMap.Point(p.lng, p.lat)
          );
          const groupPolyline = new window.BMap.Polyline(pathPoints, {
            strokeColor: "#4CAF50", // 修复：原代码中color未定义
            strokeWeight: 3,
            strokeOpacity: 0.8,
            strokeStyle: "solid",
          });
          this.map.addOverlay(groupPolyline);
          console.log("线条绘制完成");
        }
      });

      console.log("所有标记组显示完成");
    },

    // 清除所有标记
    clearAll() {
      this.map.clearOverlays();
      this.points = [];
      this.markers = [];
      this.polyline = null;
      this.savedPointGroups = [];
      this.presetOverlays = {
        items: [],
        animationId: null,
        arrows: [],
        isTracking: false,
      };
      // 同时清除自定义标记
      this.removeAllCustomMarkers();
    },

    // 清理动画（原方法，保留）
    cleanupAnimation() {
      if (this.presetOverlays.animationId) {
        cancelAnimationFrame(this.presetOverlays.animationId);
        this.presetOverlays.animationId = null;
      }
    },
  },
};
</script>

<style scoped lang="scss">
// 原有样式保持不变
.map-container {
  position: relative;
  width: 100%;
  height: calc(100vh - 100px);
}

#container {
  width: 100%;
  height: 100%;
}

// 其他样式省略（保持原有样式不变）
</style>
