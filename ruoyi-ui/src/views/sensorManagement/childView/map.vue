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
      <div v-show="radiusShow" class="radiusBox">
        <div class="radius-header">
          <div class="header-title">
            <i class="el-icon-location-outline"></i>
            <span>标记点 {{ selectPoints.pointIndex || 1 }}</span>
          </div>
          <i class="el-icon-circle-close close-btn" @click="closeRadiusBox"></i>
        </div>

        <div class="radius-content">
          <div class="radius-label">
            <i class="el-icon-aim"></i>
            <span>设置高度 (米)</span>
          </div>
          <el-input
            v-model="selectPoints.altitude"
            type="number"
            placeholder="请输入高度值"
            class="radius-input"
          >
            <!-- <template slot="append">米</template> -->
          </el-input>
        </div>

        <div class="radius-footer">
          <el-button size="mini" @click="closeRadiusBox">取消</el-button>
          <el-button type="primary" size="mini" @click="confirmRadius"
            >确认</el-button
          >
        </div>
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
      selectPoints: [],
      selectRadius: "",
      radiusShow: false,
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
        // 对话框打开时，确保地图容器已准备好
        this.$nextTick(async () => {
          setTimeout(async () => {
            if (!this.map) {
              await this.initMap();
            } else {
            }
          }, 100);
        });
      } else {
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
      this.$nextTick(() => {
        setTimeout(() => {
          if (this.map) {
            console.log(row);
            if (row.longitude && row.latitude) {
              this.savedPointGroups = [
                {
                  points: [{ lat: row.latitude, lng: row.longitude }],
                  altitude: row.altitude,
                },
              ];
              this.showAllSavedGroups();
            }
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
        // 等待百度地图API加载
        await this.waitForBMap();

        // 检查容器是否存在
        const container = document.getElementById("container");
        if (!container) {
          return;
        }

        const BMap = window.BMap;
        this.map = new BMap.Map("container");

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
      } catch (error) {}
    },

    // 保存当前标记点
    savePoints() {
      console.log(this.savedPointGroups);
      this.$emit("editPoints", this.savedPointGroups[0]);
      this.clearAll();
      this.close();
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
        return;
      }
      this.map.clearOverlays();
      this.savedPointGroups.forEach((group, groupIndex) => {
        group.points.forEach((point, pointIndex) => {
          // 验证坐标数据
          if (!point.lng || !point.lat) {
            return;
          }
          const bPoint = new window.BMap.Point(point.lng, point.lat);
          this.addSavedMarker(
            bPoint,
            groupIndex + 1,
            group.altitude || 0,
            "red",
            groupIndex
          );
        });
      });
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
    addSavedMarker(point, label, size, color, groupIndex) {
      try {
        const BMap = window.BMap;
        const marker = new BMap.Marker(point);

        const markerLabel = new BMap.Label("  高度: " + size, {
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
        const that = this;
        marker.addEventListener("click", function (e) {
          that.radiusShow = true;
          that.selectPoints = {
            ...that.savedPointGroups[groupIndex],
            groupIndex,
            pointIndex: groupIndex + 1,
            altitude: that.savedPointGroups[groupIndex].altitude || 0,
          };
        });
        this.map.addOverlay(marker);
      } catch (error) {}
    },

    addNewMarker(point) {
      this.map.clearOverlays();
      const BMap = window.BMap;
      const marker = new BMap.Marker(point, { enableDragging: true });

      const label = new BMap.Label("  高度: 0", {
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

      // this.points.push({ lat: point.lat, lng: point.lng });
      this.points = { lat: point.lat, lng: point.lng };
      // this.savedPointGroups.push({
      //   points: [{ lat: point.lat, lng: point.lng, radius: 0 }],
      // });
      this.savedPointGroups = [
        {
          points: [{ lat: point.lat, lng: point.lng }],
          altitude: 0,
        },
      ];
      marker.setLabel(label);
      this.map.addOverlay(marker);
      this.markers.push(marker);

      const that = this;
      marker.addEventListener("click", function (e) {
        const groupIndex = Number(e.target.z.label.content.substring(0, 1)) - 1;
        that.radiusShow = true;
        that.selectPoints = {
          ...that.savedPointGroups[groupIndex],
          groupIndex,
          pointIndex: groupIndex + 1,
          altitude: that.savedPointGroups[groupIndex].altitude || 0,
        };
      });

      marker.addEventListener("dragend", (e) => {
        const index = this.markers.indexOf(marker);
        if (index !== -1) {
          this.points[index] = { lat: e.point.lat, lng: e.point.lng };
          this.savedPointGroups[index] = {
            points: [{ lat: e.point.lat, lng: e.point.lng }],
            altitude: 0,
          };
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
          } catch (e) {}
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

    close() {
      this.show = false;
      this.clearAll();
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

    // 关闭半径设置弹窗
    closeRadiusBox() {
      this.radiusShow = false;
      this.selectPoints = {};
    },

    // 确认半径设置
    confirmRadius() {
      if (!this.selectPoints.altitude || this.selectPoints.altitude <= 0) {
        this.$message.warning("请输入有效的高度值");
        return;
      }
      this.savedPointGroups[0].altitude = parseInt(this.selectPoints.altitude);

      // 重新显示所有标记点以更新标签
      this.showAllSavedGroups();

      this.$message.success("高度设置成功");

      this.closeRadiusBox();
    },
  },
};
</script>
<style scoped lang="scss">
.radiusBox {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 320px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  overflow: hidden;
  animation: slideInUp 0.3s ease-out;

  .radius-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px 16px;
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);

    .header-title {
      display: flex;
      align-items: center;
      color: white;
      font-size: 16px;
      font-weight: 600;

      i {
        margin-right: 8px;
        font-size: 18px;
        color: #ffd700;
      }
    }

    .close-btn {
      color: rgba(255, 255, 255, 0.8);
      font-size: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      padding: 4px;
      border-radius: 50%;

      &:hover {
        color: white;
        background: rgba(255, 255, 255, 0.2);
        transform: scale(1.1);
      }
    }
  }

  .radius-content {
    padding: 24px;

    .radius-label {
      display: flex;
      align-items: center;
      margin-bottom: 16px;
      color: white;
      font-size: 14px;
      font-weight: 500;

      i {
        margin-right: 8px;
        font-size: 16px;
        color: #ffd700;
      }
    }

    .radius-input {
      ::v-deep .el-input__inner {
        background: rgba(255, 255, 255, 0.9);
        border: 2px solid transparent;
        border-radius: 8px;
        color: #333;
        font-size: 14px;
        padding: 12px 16px;
        transition: all 0.3s ease;

        &:focus {
          border-color: #ffd700;
          box-shadow: 0 0 0 3px rgba(255, 215, 0, 0.2);
        }

        &::placeholder {
          color: #999;
        }
      }

      ::v-deep .el-input-group__append {
        background: #ffd700;
        border: none;
        color: #333;
        font-weight: 600;
        border-radius: 0 8px 8px 0;
      }
    }
  }

  .radius-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    padding: 16px 24px 24px;
    background: rgba(255, 255, 255, 0.05);

    .el-button {
      border-radius: 8px;
      font-weight: 500;
      transition: all 0.3s ease;

      &.el-button--mini {
        padding: 8px 16px;
      }

      &:not(.el-button--primary) {
        background: rgba(255, 255, 255, 0.2);
        border: 1px solid rgba(255, 255, 255, 0.3);
        color: white;

        &:hover {
          background: rgba(255, 255, 255, 0.3);
          transform: translateY(-1px);
        }
      }

      &.el-button--primary {
        // background: linear-gradient(45deg, #ffd700, #ffed4e);
        border: none;
        color: #333;
        font-weight: 600;

        &:hover {
          // background: linear-gradient(45deg, #ffed4e, #ffd700);
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(255, 215, 0, 0.4);
        }
      }
    }
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translate(-50%, -30%);
  }
  to {
    opacity: 1;
    transform: translate(-50%, -50%);
  }
}
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
