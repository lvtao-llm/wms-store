<template>
  <div>
    <iframe
      :key="indexKey"
      style="width: 100%; height: 100vh"
      id="contentFrame"
      :src="imgSrc"
    ></iframe>
  </div>
</template>
<script>
import defaultSettings from "@/settings";
export default {
  data() {
    return {
      show: false,
      indexKey: 0,
      imgSrc: defaultSettings.iframeUrl,
    };
  },
  mounted() {
    this.onLoad();
    // this.loadSavedGroups();
    this.initMap();
    // this.showAllSavedGroups();
    this.$nextTick(() => {
      this.initPieChart();
      this.showWarehouse();
    });
  },
  beforeDestroy() {
    // this.cleanupAnimation();
  },
  methods: {
    onLoad() {
      const wsuri = "ws://112.98.110.101:10030/system/lanya-transfer/ws/aaa";
      this.ws = new WebSocket(wsuri);
      const that = this;
      this.ws.onopen = function (event) {
        that.text_content = that.text_content + "已经打开连接!" + "\n";
      };
      this.ws.onmessage = function (event) {
        that.text_content = event.data + "\n";
        const data = JSON.parse(event.data);
        const map = [
          [
            () => data.msgType === "currentPersonLocation", //人员
            () => {
              that.tableDataTl = data;
            },
          ],
          [
            () => data.msgType === "currentVehicleLocation", //车辆
            () => {
              that.tableDataBl = data;
            },
          ],
          [
            () => data.msgType === "materialLog", //物料
            () => {
              that.tableDataTr = data.data;
            },
          ],
          [
            () => data.msgType === "areaLog", //物料
            () => {
              that.tableDataBr = data.data;
            },
          ],
        ];
        const target = map.find((m) => m[0]());
        if (target) target[1]();
      };
      this.ws.onclose = function (event) {
        this.text_content = this.text_content + "已经关闭连接!" + "\n";
      };
    },
    exit() {
      if (this.ws) {
        this.ws.close();
        this.ws = null;
      }
    },
    initPieChart() {
      // 获取 .echarts1 容器 DOM 元素
      const chartDom = document.querySelector(".echarts1");
      if (!chartDom) {
        console.warn("未找到 .echarts1 容器，无法初始化饼图");
        return;
      }

      // 创建 ECharts 实例
      this.pieChartInstance = echarts.init(chartDom);
      // 配置项
      const option = {
        backgroundColor: "transparent", // 背景透明，与父容器保持一致

        series: [
          {
            name: "人员类别",
            type: "pie",
            radius: "50%", // 环形饼图，也可用 '50%' 做实心圆
            data: this.pieChartData.map((item) => ({
              name: item.name,
              value: item.value,
            })),
            label: {
              show: true, // ✅ 必须设置为 true 才显示文字
              position: "outside", // 或 'inside', 'center'，outside 会配合 labelLine
              color: "#fff", // 文字颜色
              formatter: "{b}: \n{c}人", // 显示名称和数值，比如 "正式员工: 12人"
            },
            labelLine: {
              show: true, // ✅ 必须设置为 true 才显示指示线
              length: 10, // 指示线第一段长度
              length2: 15, // 第二段长度（向外延伸）
              lineStyle: {
                color: "#fff", // 指示线颜色
              },
            },
            itemStyle: {
              borderRadius: 5,
              borderColor: "#fff",
              borderWidth: 1,
            },
          },
        ],
      };

      // 设置配置项并渲染图表
      this.pieChartInstance.setOption(option);
    },

    handleZf() {
      this.isZf = !this.isZf;
      if (this.isZf) {
        this.$nextTick(() => {
          setTimeout(() => {
            if (this.map) {
              this.showAllSavedGroups();
            } else {
              // 如果地图还没初始化，等待地图初始化完成
              const checkMap = setInterval(() => {
                if (this.map) {
                  clearInterval(checkMap);
                  this.showAllSavedGroups();
                }
              }, 100);
            }
          }, 200);
        });
      } else {
        this.map.clearOverlays();
        this.showWarehouse();
      }
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
            strokeColor: color,
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

    // 显示所有保存的标记组
    showAllSavedGroups() {
      this.savedPointGroups = [
        {
          areaName: "啊啊",
          id: "1760690241468",
          points: [
            {
              lat: 46.6565,
              lng: 124.823946,
            },
            {
              lat: 46.654916,
              lng: 124.920101,
            },
            {
              lat: 46.622048,
              lng: 124.911621,
            },
            {
              lat: 46.622543,
              lng: 124.854417,
            },
            {
              lat: 46.631554,
              lng: 124.823946,
            },
            {
              lat: 46.644721,
              lng: 124.81561,
            },
          ],
        },
      ];

      if (!this.map) {
        console.error("地图未初始化，无法显示标记点");
        return;
      }
      if (!this.savedPointGroups || this.savedPointGroups.length === 0) {
        console.log("没有标记组数据需要显示");
        return;
      }

      this.map.clearOverlays();
      this.showWarehouse();

      this.savedPointGroups.forEach((group, groupIndex) => {
        console.log(`处理第 ${groupIndex + 1} 个标记组:`, group);

        // 检查数据结构
        if (!group.points || !Array.isArray(group.points)) {
          console.warn(`标记组 ${groupIndex + 1} 的 points 数据无效:`, group);
          return;
        }

        const color = this.getColorByIndex(groupIndex);
        console.log(`使用颜色: ${color}`);

        group.points.forEach((point, pointIndex) => {
          console.log(`添加标记点 ${pointIndex + 1}:`, point);

          // 验证坐标数据
          if (!point.lng || !point.lat) {
            console.warn(`标记点 ${pointIndex + 1} 坐标数据无效:`, point);
            return;
          }

          const bPoint = new window.BMap.Point(point.lng, point.lat);
          this.addSavedMarker(bPoint, pointIndex + 1, color);
        });

        // 绘制多边形（包括填充和边框）
        if (group.points.length >= 3) {
          console.log(
            `为标记组 ${groupIndex + 1} 绘制多边形，共 ${
              group.points.length
            } 个点`
          );
          const pathPoints = group.points.map(
            (p) => new window.BMap.Point(p.lng, p.lat)
          );

          // 创建多边形填充
          const polygon = new window.BMap.Polygon(pathPoints, {
            strokeColor: color,
            strokeWeight: 3,
            strokeOpacity: 0.8,
            strokeStyle: "solid",
            fillColor: color,
            fillOpacity: 0.3, // 半透明填充
          });
          this.map.addOverlay(polygon);

          // 添加区域文字标签
          const areaName = group.areaName || `区域${groupIndex + 1}`;
          this.addAreaLabel(pathPoints, areaName, color);
          console.log("多边形绘制完成");
        } else if (group.points.length >= 2) {
          // 如果只有2个点，绘制线条
          const pathPoints = group.points.map(
            (p) => new window.BMap.Point(p.lng, p.lat)
          );
          const groupPolyline = new window.BMap.Polyline(pathPoints, {
            strokeColor: color,
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

    // 为不同组生成不同颜色
    getColorByIndex(index) {
      const colors = [
        "#FF5722",
        "#4CAF50",
        "#2196F3",
        "#9C27B0",
        "#FF9800",
        "#607D8B",
        "#795548",
        "#E91E63",
      ];
      return colors[index % colors.length];
    },
    // 添加已保存的标记
    addSavedMarker(point, label, color) {
      try {
        const BMap = window.BMap;
        const marker = new BMap.Marker(point);

        const markerLabel = new BMap.Label(label.toString(), {
          offset: new BMap.Size(15, -10),
          position: point,
        });

        markerLabel.setStyle({
          color: "white",
          fontSize: "12px",
          backgroundColor: color,
          padding: "2px 5px",
          border: "none",
          borderRadius: "10px",
        });

        marker.setLabel(markerLabel);
        this.map.addOverlay(marker);
        console.log(
          `标记点 ${label} 添加成功，坐标: (${point.lng}, ${point.lat})`
        );
      } catch (error) {
        console.error(`添加标记点 ${label} 失败:`, error);
      }
    },
    // 添加区域文字标签
    addAreaLabel(points, text, color) {
      try {
        const BMap = window.BMap;

        // 计算多边形的中心点
        const centerPoint = this.calculatePolygonCenter(points);

        // 创建文字标签
        const label = new BMap.Label(text, {
          position: centerPoint,
          offset: new BMap.Size(0, 0),
        });

        // 设置标签样式
        label.setStyle({
          color: "white",
          fontSize: "16px",
          fontWeight: "bold",
          backgroundColor: "rgba(0, 0, 0, 0.6)",
          padding: "8px 12px",
          border: `2px solid ${color}`,
          borderRadius: "6px",
          textAlign: "center",
          boxShadow: "0 2px 6px rgba(0,0,0,0.3)",
        });

        // 添加标签到地图
        this.map.addOverlay(label);
        console.log(
          `区域标签 "${text}" 添加成功，位置: (${centerPoint.lng}, ${centerPoint.lat})`
        );

        return label;
      } catch (error) {
        console.error(`添加区域标签 "${text}" 失败:`, error);
      }
    },
    // 计算多边形中心点
    calculatePolygonCenter(points) {
      const BMap = window.BMap;
      let sumLng = 0;
      let sumLat = 0;

      points.forEach((point) => {
        sumLng += point.lng;
        sumLat += point.lat;
      });

      const centerLng = sumLng / points.length;
      const centerLat = sumLat / points.length;

      return new BMap.Point(centerLng, centerLat);
    },

    toggleDialog1() {
      this.isMovedOutTf = !this.isMovedOutTf;
    },
    toggleDialog2() {
      this.isMovedOutTr = !this.isMovedOutTr;
    },
    toggleDialog3() {
      this.isMovedOutBf = !this.isMovedOutBf;
    },
    toggleDialog4() {
      this.isMovedOutBr = !this.isMovedOutBr;
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

      // 右键添加标记
      this.map.addEventListener("rightclick", (e) => {
        this.addNewMarker(e.point);
        this.updatePolyline();
      });
    },
  },
};
</script>
