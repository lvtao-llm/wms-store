<template>
  <div class="map-container">
    <el-dialog
      :visible.sync="show"
      width="95%"
      @close="close"
      append-to-body
      :close-on-click-modal="false"
    >
      <div style="height: 80vh; width: 100%" id="container"></div>
      <div style="margin-top: 10px" v-if="this.pageType !== 'view'">
        <el-button type="primary" size="mini" @click="savePoints">
          保存当前标记点</el-button
        >
        <el-button type="primary" size="mini" @click="clearAll">
          清除全部标记</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
<script>
import dayjs from "dayjs";
import TimeLine from "@/components/TimeLine/timeline-canvas.vue";
import MultiplierSelector from "@/components/Preset/index";
// import { resData as animatedLineData } from "../codeApplication/data";
import { positionHistoryPositionFindPersonHistoryList } from "@/api/lanya_transfer";

import { listLanya_device_card_sender_log_by_name_card_type } from "@/api/system/lanya_device_card_sender_log";

import { getArea, updateArea } from "@/api/system/wms_area";

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
      show: false,
      queryParams: {},
      selectData: [],
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
      pageType: "add",

      // 地图相关
      map: null,
      polyline: null,
      points: [],
      choosePoints: [],
      markers: [],
      savedPointGroups: [],
      form: {},
      presetOverlays: {
        items: [],
        animationId: null,
        arrows: [],
        isTracking: false,
      },
    };
  },
  async mounted() {
    // 延迟初始化地图，确保DOM已渲染
    this.$nextTick(async () => {
      setTimeout(async () => {
        await this.initMap();
        this.showAllSavedGroups();
      }, 100);
    });
  },
  watch: {
    async show(newVal) {
      if (newVal) {
        console.log("对话框打开，开始初始化地图...");
        // 对话框打开时，确保地图容器已准备好
        this.$nextTick(async () => {
          setTimeout(async () => {
            if (!this.map) {
              console.log("地图未初始化，开始初始化...");
              await this.initMap();
              console.log("地图初始化完成");
            } else {
              console.log("地图已存在，无需重新初始化");
            }
          }, 100);
        });
      } else {
        console.log("对话框关闭");
        // 对话框关闭时清理
        this.cleanupAnimation();
      }
    },
  },
  beforeDestroy() {
    this.cleanupAnimation();
  },
  methods: {
    openDia(row, type = "add") {
      this.show = true;
      this.pageType = type;
      this.form = row;
      if (this.form.keyPoints) {
        try {
          this.savedPointGroups = JSON.parse(this.form.keyPoints);
          // this.choosePoints = JSON.parse(this.form.keyPoints);
          this.points = this.savedPointGroups[0].points;
          // 等待对话框和地图完全初始化后再显示标记点
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
        } catch (error) {
          this.$modal.msgError("区域数据格式错误");
        }
      } else {
        this.map.clearOverlays();
      }
    },
    openDiaAll(data, type = "view") {
      this.show = true;
      this.pageType = type;
      this.savedPointGroups = data;
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
    },
    // 等待百度地图API加载完成
    waitForBMap() {
      return new Promise((resolve) => {
        const checkBMap = () => {
          if (window.BMap) {
            resolve();
          } else {
            setTimeout(checkBMap, 100);
          }
        };
        checkBMap();
      });
    },

    // 初始化地图
    async initMap() {
      try {
        console.log("开始初始化地图...");

        // 等待百度地图API加载
        await this.waitForBMap();
        console.log("百度地图API已加载");

        // 检查容器是否存在
        const container = document.getElementById("container");
        if (!container) {
          console.error("地图容器不存在");
          return;
        }
        console.log("地图容器存在:", container);

        const BMap = window.BMap;
        this.map = new BMap.Map("container");
        console.log("地图初始化成功", this.map);

        this.map.centerAndZoom(new BMap.Point(125.05, 46.59), 13);
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
          // this.updatePolyline();
        });

        console.log("地图控件添加完成");
      } catch (error) {
        console.error("地图初始化失败:", error);
      }
    },

    // 保存当前标记点
    savePoints() {
      if (this.points.length === 0) {
        alert("没有可保存的标记点！");
        return;
      }
      const groupId = Date.now().toString();
      const timestamp = new Date().toLocaleString();

      // this.savedPointGroups.push({
      //   id: groupId,
      //   timestamp: timestamp,
      //   areaName: this.form.areaName,
      //   points: [...this.points],
      // });
      this.savedPointGroups = [
        {
          id: groupId,
          timestamp: timestamp,
          areaName: this.form.areaName,
          points: [...this.points],
        },
      ];

      const params = {
        ...this.form,
        keyPoints: JSON.stringify(this.savedPointGroups),
      };
      this.close();
      this.$emit("save", params);
    },

    // 清空当前标记（不删除保存的组）
    clearCurrentPoints() {
      // 只清空当前正在绘制的标记点，保留已保存的区域
      this.points = [];
      this.markers = [];
      if (this.polyline) {
        this.map.removeOverlay(this.polyline);
        this.polyline = null;
      }

      // 重新显示所有已保存的区域
      this.showAllSavedGroups();
    },

    // 显示所有保存的标记组
    showAllSavedGroups() {
      if (!this.map) {
        console.error("地图未初始化，无法显示标记点");
        return;
      }
      if (!this.savedPointGroups || this.savedPointGroups.length === 0) {
        console.log("没有标记组数据需要显示");
        return;
      }

      this.map.clearOverlays();
      console.log(this.savedPointGroups);

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
        // if (group.points.length >= 3) {
        //   console.log(
        //     `为标记组 ${groupIndex + 1} 绘制多边形，共 ${
        //       group.points.length
        //     } 个点`
        //   );
        //   const pathPoints = group.points.map(
        //     (p) => new window.BMap.Point(p.lng, p.lat)
        //   );

        //   // 创建多边形填充
        //   const polygon = new window.BMap.Polygon(pathPoints, {
        //     strokeColor: color,
        //     strokeWeight: 3,
        //     strokeOpacity: 0.8,
        //     strokeStyle: "solid",
        //     fillColor: color,
        //     fillOpacity: 0.3, // 半透明填充
        //   });
        //   this.map.addOverlay(polygon);

        //   // 添加区域文字标签
        //   const areaName = group.areaName || `区域${groupIndex + 1}`;
        //   this.addAreaLabel(pathPoints, areaName, color);
        //   console.log("多边形绘制完成");
        // } else if (group.points.length >= 2) {
        //   // 如果只有2个点，绘制线条
        //   const pathPoints = group.points.map(
        //     (p) => new window.BMap.Point(p.lng, p.lat)
        //   );
        //   const groupPolyline = new window.BMap.Polyline(pathPoints, {
        //     strokeColor: color,
        //     strokeWeight: 3,
        //     strokeOpacity: 0.8,
        //     strokeStyle: "solid",
        //   });
        //   this.map.addOverlay(groupPolyline);
        //   console.log("线条绘制完成");
        // }
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

    addNewMarker(point) {
      const BMap = window.BMap;
      const marker = new BMap.Marker(point, { enableDragging: true });

      const labelNumber = this.points.length + 1;
      const label = new BMap.Label(labelNumber.toString(), {
        offset: new BMap.Size(15, -10),
        position: point,
      });

      label.setStyle({
        color: "white",
        fontSize: "12px",
        backgroundColor: "#3388ff",
        padding: "2px 5px",
        border: "none",
        borderRadius: "10px",
      });

      this.points.push({ lat: point.lat, lng: point.lng });
      marker.setLabel(label);
      this.map.addOverlay(marker);
      this.markers.push(marker);

      marker.addEventListener("dragend", (e) => {
        const index = this.markers.indexOf(marker);
        if (index !== -1) {
          this.points[index] = { lat: e.point.lat, lng: e.point.lng };
        }
        // this.updatePolyline();
      });
    },
    // 清理动画
    cleanupAnimation() {
      if (this.presetOverlays.animationId) {
        cancelAnimationFrame(this.presetOverlays.animationId);
        this.presetOverlays.animationId = null;
      }

      // 清除箭头
      if (this.presetOverlays.arrows) {
        this.presetOverlays.arrows.forEach((arrow) => {
          try {
            this.map.removeOverlay(arrow.marker);
          } catch (e) {
            console.warn("移除箭头标记时出错:", e);
          }
        });
        this.presetOverlays.arrows = [];
      }

      // 清除其他覆盖物
      this.clearPresetOverlays();
    },

    clearPresetOverlays() {
      if (!this.map) return;

      this.presetOverlays.items.forEach((item) => {
        if (item.type === "canvas") {
          item.instance.destroy();
        } else if (
          item instanceof BMap.Marker ||
          item instanceof BMap.Polyline
        ) {
          this.map.removeOverlay(item);
        }
      });

      if (this.presetOverlays.character) {
        this.map.removeOverlay(this.presetOverlays.character);
      }

      this.presetOverlays.items = [];
      delete this.presetOverlays.character;
    },

    // 更新连线
    updatePolyline() {
      if (this.polyline) {
        this.map.removeOverlay(this.polyline);
      }

      if (this.points.length < 2) return;

      const pathPoints = this.points.map(
        (p) => new window.BMap.Point(p.lng, p.lat)
      );

      if (this.points.length >= 3) {
        pathPoints.push(
          new window.BMap.Point(this.points[0].lng, this.points[0].lat)
        );
      }

      this.polyline = new window.BMap.Polyline(pathPoints, {
        strokeColor: "#3388ff",
        strokeWeight: 3,
        strokeOpacity: 0.8,
        strokeStyle: "solid",
      });

      this.map.addOverlay(this.polyline);
    },

    close() {
      this.show = false;
      this.savedPointGroups = [];
      this.points = [];
    },
    // 清除所有标记
    clearAll() {
      this.map.clearOverlays();
      this.points = [];
      this.markers = [];
      this.polyline = null;
      this.savedPointGroups = [];
      this.presetOverlays = [];
    },
  },
};
</script>
<style scoped lang="scss">
.map-container {
  position: relative;
  width: 100%;
  height: calc(100vh - 100px);
}

#container {
  width: 100%;
  height: 100%;
}

#porBox:hover #hoverPor {
  display: block;
}

#hoverPor {
  display: none;
}

#hoverPor2 {
  display: block;
}

#porBox {
  // width: 33%;

  // padding-left: 5%;
  position: relative;

  #hoverPor {
    position: absolute;
    left: 0;
    top: 100%;
    height: 240px;
    width: 180px;
    background-color: rgba(27, 26, 26, 0.2);
    border-radius: 10px;

    .porItem {
      vertical-align: middle;
      color: #fff;
      text-align: left;
      padding-left: 20px;
      height: 40px;
      line-height: 40px;
      border: 1px solid #409eff;
      cursor: pointer;
      border-radius: 10px;
    }
  }

  #hoverPor2 {
    position: absolute;
    left: 180px;
    top: 100%;
    height: 240px;
    width: 280px;
    color: #fff;
    background-color: rgba(27, 26, 26, 0.2);
    border-radius: 10px;
    border: 1px solid #409eff;
  }
}

.porItem .el-icon {
  vertical-align: middle; /* 图标垂直居中 */
  margin-right: 5px; /* 图标右侧间距 */
}

.width25 {
  width: 25%;
}

.width33 {
  width: 30%;
}

.controls {
  position: absolute;
  top: 20px;
  left: 30%;
  z-index: 1000;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  width: 45vw;
  height: 50px;
  border-radius: 30px;
  background-color: black;

  .form {
    width: 100%;
    margin: 9px 0 0 9px;
  }
}

.timeline {
  position: absolute;
  bottom: 1px;
  width: 100%;
  z-index: 1000;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;

  .timeAn2 {
    position: absolute;
    left: 10px;
    top: 43%;
    color: #fff;
    font-size: 20px;
  }

  .timeAn {
    position: absolute;
    left: 60px;
    top: 50%;
    color: #fff;
    font-size: 20px;
  }
}

// .searchIcon {
//   color: #fff;
//   width: 25px;
//   height: 25px;
// }
.searchIconBox {
  vertical-align: top;
  // height: 25px;
}

.searchIconBox .searchIcon {
  padding-top: 5px;
}

::v-deep .el-date-editor,
.el-range-editor,
.el-input__inner,
.el-date-editor--datetimerange,
.el-range-editor--medium {
  width: 150%;

  .el-range-input {
    background-color: black;
    color: white;
  }

  background-color: black;
}

::v-deep .el-input__icon,
.el-range__icon,
.el-icon-time .el-range-input {
  background-color: black;
}

::v-deep .el-input__suffix .el-select__caret,
.el-input__icon,
.el-icon-arrow-up {
  display: none;
}

::v-deep.el-range-input {
  background-color: black;
}

.control-btn {
  padding: 8px 16px;
  background-color: rgba(255, 255, 255, 0.9);
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
}

.control-btn:hover {
  background-color: #f5f5f5;
  transform: translateY(-1px);
}

::v-deep.searchSelect .el-input__inner {
  width: 100%;
  border-radius: 30px;
  // background-color: #5ba5ef;
  background-color: black;
}

::v-deep.controls .el-select__wrapper,
.el-tooltip__trigger,
.el-tooltip__trigger {
  border-radius: 30px !important;
  background-color: black;
}

::v-deep.el-popover {
  background: #000022;
  border-color: #000022;
  border-radius: 10px;
  opacity: 0.92;
  color: #fff;
}

::v-deep.el-popper.is-light {
  background: black;
  border: 1px solid #273f70;
}

::v-deep.controls .el-select-dropdown__item.hover {
  background: transparent;
  border: none;
  color: #04faa0;
}

::v-deep.controls .el-select-dropdown__item {
  background: transparent;
  border: none;
  color: #000 !important;
}

::v-deep.el-popper.is-light .el-popper__arrow::before {
  border: 1px solid #fff;
  background: black;
  right: 0;
}

::v-deep.controls .el-select__selected-item,
::v-deep.controls .el-select__placeholder {
  color: #fff !important;
  font-weight: 400;
}

::v-deep.el-range-input {
  color: #fff !important;

  &::placeholder {
    color: #fff;
  }
}
.queryBtn {
  width: 30px;
  height: 30px;
}
.queryBtn span {
  font-size: 10px;
}

::v-deep .el-input__inner {
  color: white;
}
::v-deep .el-form-item__content .el-date-editor,
.el-range-editor,
.el-input__inner,
.el-date-editor--datetimerange,
.el-range-editor--medium {
  width: 100%;
}
</style>
