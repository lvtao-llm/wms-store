<template>
  <div class="map-container">
    <div id="container"></div>
    <div class="controls">
      <el-form class="form" :model="queryParams" ref="queryRef" :inline="true">
        <el-row>
          <el-col :span="6">
            <el-form-item>
              <el-select
                popper-class="popperClass"
                placeholder="请输入关键词搜索用户"
                filterable
                remote
                @change="handleSelectChange"
                reserve-keyword
                :popper-append-to-body="false"
                :remote-method="remoteSearchUser"
                class="searchSelect"
                v-model="queryParams.status"
                clearable
                style=""
              >
                <el-option
                  v-for="dict in selectData"
                  :key="dict.id"
                  :label="dict.label"
                  :value="dict"
                />
              </el-select> </el-form-item
          ></el-col>
          <el-col :span="12">
            <el-form-item>
              <el-date-picker
                @change="handleSelectChange"
                value-format="yyyy-MM-dd HH:mm:ss"
                v-model="dateRange"
                popper-class="customDatePicker"
                type="datetimerange"
                range-separator="To"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
              /> </el-form-item
          ></el-col>
          <!-- <el-col :span="2">
            <el-button class="queryBtn" @click="handleSelectChange"
              >查询</el-button
            >
          </el-col> -->
          <el-col :span="6">
            <el-form-item id="porBox">
              <span class="searchIconBox" style="color: #fff; cursor: pointer">
                <i
                  class="el-icon-setting searchIcon"
                  style="vertical-align: top; font-size: 20px"
                ></i>
                历史轨迹播放设置</span
              >

              <div id="hoverPor">
                <div class="porItem" @click="savePoints">
                  <i class="el-icon-upload" style="font-size: 20px"></i>
                  保存当前标记点
                </div>
                <div class="porItem" @click="clearAll">
                  <i class="el-icon-delete" style="font-size: 20px"></i>
                  清除全部标记
                </div>
                <div class="porItem" @click="showPresetPoints">
                  <i
                    class="el-icon-success"
                    style="font-size: 20px"
                    v-if="isShowD"
                  ></i>
                  <i
                    class="el-icon-circle-close"
                    style="font-size: 20px"
                    v-else
                  ></i>
                  加载预设点
                </div>
                <div class="porItem" @click="showPresetLines">
                  <i
                    class="el-icon-success"
                    style="font-size: 20px"
                    v-if="isShowX"
                  ></i>
                  <i
                    class="el-icon-circle-close"
                    style="font-size: 20px"
                    v-else
                  ></i>
                  加载预设线
                </div>
                <div class="porItem" @click="showAnimatedLine">
                  <i
                    class="el-icon-success"
                    style="font-size: 20px"
                    v-if="isShowQ"
                  ></i>
                  <i
                    class="el-icon-circle-close"
                    style="font-size: 20px"
                    v-else
                  ></i>
                  起止
                </div>
                <div v-if="isShowQ" class="porItem" @click="toggleTrack">
                  <i
                    class="el-icon-success"
                    style="font-size: 20px"
                    v-if="isToggle"
                  ></i>
                  <i
                    class="el-icon-circle-close"
                    style="font-size: 20px"
                    v-else
                  ></i>
                  视角跟踪
                </div>
              </div>
              <div v-if="isShowQ" id="hoverPor2">
                <div>当前时间:{{ inDate }}</div>
                <div>停留时间:</div>
              </div>
            </el-form-item></el-col
          >
        </el-row>
      </el-form>
    </div>
    <div class="timeline">
      <div class="timeAn">
        <i
          class="el-icon-video-play"
          @click="timeLinePlay"
          v-if="isBf"
          style="font-size: 20px"
        ></i>
        <i
          class="el-icon-video-pause"
          @click="timeLinePlay"
          v-else
          style="font-size: 20px"
        ></i>
      </div>
      <div class="timeAn2">
        <MultiplierSelector
          v-if="isShowQ"
          @cb="handleMultiplierChange"
          :modelValue="selectedMultiplier"
        />
      </div>

      <TimeLine
        :markTime="time_range"
        v-if="isShowQ"
        :key="timeIndex"
        ref="time_line"
        @change="changeDate"
        :time-range="dateRange"
        :isAutoPlay="isAutoPlay"
      />
    </div>
  </div>
</template>

<script>
import dayjs from "dayjs";
import TimeLine from "@/components/TimeLine/timeline-canvas.vue";
import MultiplierSelector from "@/components/Preset/index";
// import { resData as animatedLineData } from "../codeApplication/data";
import { positionHistoryPositionFindPersonHistoryList } from "@/api/lanya_transfer";
import {
  showPresetPointsData,
  showPresetLinesData,
  // showAnimatedLineData,
} from "./data";
import { resData } from "./data1";
import {
  DynamicCanvasLayer,
  simplifyPath,
  parseTimeString,
  calculateSegmentAngle,
  createSvgIcon,
} from "./utils";

import { listLanya_device_card_sender_log_by_name_card_type } from "@/api/system/lanya_device_card_sender_log";

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

      // 地图相关
      map: null,
      polyline: null,
      points: [],
      markers: [],
      savedPointGroups: [],
      presetOverlays: {
        items: [],
        animationId: null,
        arrows: [],
        isTracking: false,
      },
    };
  },
  mounted() {
    this.loadSavedGroups();
    this.initMap();
    this.showAllSavedGroups();
  },
  beforeDestroy() {
    this.cleanupAnimation();
  },
  methods: {
    // 初始化地图
    initMap() {
      const BMap = window.BMap;
      this.map = new BMap.Map("container");
      this.map.centerAndZoom(new BMap.Point(124.87612, 46.64195), 15);
      this.map.setMinZoom(11);
      this.map.setMaxZoom(19);
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

    // 数据预处理
    preprocessLineData(rawData) {
      // 采样 - 每5个点取1个点
      const sampledData = [];
      for (let i = 0; i < rawData.length; i += 5) {
        sampledData.push(rawData[i]);
      }

      return simplifyPath(sampledData, 0.0001);
    },

    //预设点
    showPresetPoints() {
      const deepData = !this.isShowD
        ? JSON.parse(JSON.stringify(this.tableData))
        : [];

      this.clearPresetOverlays();

      if (deepData.length > 0 && !this.isShowD) {
        const allPoints = deepData.flat();
        console.log(allPoints, 123);
        this.map.setViewport(
          allPoints.map(
            (coord) => new BMap.Point(coord.longitude, coord.latitude)
          )
        );
      }
      setTimeout(() => {
        this.map.panBy(0, 0); // 轻微移动触发重绘
      }, 100);
      const pointsCanvas = new DynamicCanvasLayer(this.map, (ctx, canvas) => {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = "#FF5722";
        deepData.forEach((group) => {
          const pixel = this.map.pointToOverlayPixel(
            new BMap.Point(group.longitude, group.latitude)
          );
          ctx.beginPath();
          ctx.arc(pixel.x, pixel.y, 2, 0, Math.PI * 2);
          ctx.fill();
        });
      });

      this.presetOverlays.items.push({
        type: "canvas",
        instance: pointsCanvas,
      });

      this.isShowD = !this.isShowD;
      this.isShowX = false;
      this.isShowQ = false;
    },

    //预设线
    showPresetLines() {
      const deepData = !this.isShowX
        ? JSON.parse(JSON.stringify(this.tableData))
        : [];

      this.clearPresetOverlays();

      if (deepData.length > 0 && !this.isShowX) {
        this.map.setViewport(
          deepData.map(
            (coord) => new BMap.Point(coord.longitude, coord.latitude)
          )
        );
      }
      setTimeout(() => {
        this.map.panBy(0, 0); // 轻微移动触发重绘
      }, 100);

      const linesCanvas = new DynamicCanvasLayer(this.map, (ctx, canvas) => {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        if (deepData.length < 2) return;

        // 绘制连线（顺序连接所有点）
        ctx.beginPath();
        const firstPixel = this.map.pointToOverlayPixel(
          new BMap.Point(deepData[0].longitude, deepData[0].latitude)
        );
        ctx.moveTo(firstPixel.x, firstPixel.y);

        for (let i = 1; i < deepData.length; i++) {
          const pixel = this.map.pointToOverlayPixel(
            new BMap.Point(deepData[i].longitude, deepData[i].latitude)
          );
          ctx.lineTo(pixel.x, pixel.y);
        }

        ctx.strokeStyle = "#2196F3";
        ctx.lineWidth = 3;
        ctx.stroke();
      });

      this.presetOverlays.items.push({
        type: "canvas",
        instance: linesCanvas,
      });

      this.isShowX = !this.isShowX;
      this.isShowD = false;
      this.isShowQ = false;
    },

    // 显示动画轨迹 - 类似高德地图的路线效果（性能优化版本）
    showAnimatedLine() {
      this.selectedMultiplier = 10;

      if (this.isShowQ) {
        this.cleanupAnimation();
        this.isShowQ = false;
        this.isBf = false;
        this.$refs.time_line.updateSpeed();
        return;
      }

      this.clearPresetOverlays();

      // 使用预设的动画路线数据，转换为正确的格式
      // const rawData = showAnimatedLineData;
      let optimizedData = JSON.parse(JSON.stringify(this.tableData));

      // 数据预处理 - 如果数据量很大，进行采样
      if (optimizedData.length > 1000) {
        console.log(`原始数据量: ${optimizedData.length}，进行采样优化`);
        const sampleRate = Math.ceil(optimizedData.length / 1000); // 采样到1000个点
        optimizedData = optimizedData.filter(
          (_, index) => index % sampleRate === 0
        );
        console.log(`采样后数据量: ${optimizedData.length}`);
      }

      if (optimizedData.length === 0) return;
      // 设置视图范围
      const bPoints = optimizedData.map(
        (item) => new window.BMap.Point(item.longitude, item.latitude)
      );
      this.map.setViewport(bPoints, { padding: [50, 50, 50, 50] });

      // 设置时间范围
      this.time_range = [
        {
          beginTime: optimizedData[0].createTime,
          endTime: optimizedData[optimizedData.length - 1].createTime,
          bgColor: "green",
          text: "",
        },
      ];
      this.getTimeLineOne = optimizedData[0].createTime;

      // 1. 绘制静态路径线（类似高德地图的路线）
      this.renderRoutePath(optimizedData);

      // 2. 绘制路径点标记
      this.renderRoutePoints(optimizedData);

      // 3. 创建小人标记
      const characterMarker = this.createCharacterMarker(
        optimizedData[0].longitude,
        optimizedData[0].latitude
      );
      this.map.addOverlay(characterMarker);
      this.presetOverlays.character = characterMarker;

      // 4. 初始化方向箭头动画
      this.initDirectionArrows(optimizedData);

      // 5. 设置时间控制
      this.setupTimeControls(optimizedData, characterMarker);

      this.isShowQ = true;
      this.isShowX = false;
      this.isShowD = false;
    },

    // 渲染路线路径（类似高德地图的路线效果）- 性能优化版本
    renderRoutePath(data) {
      const BMap = window.BMap;

      // 创建路径点数组
      const pathPoints = data.map(
        (item) => new BMap.Point(item.longitude, item.latitude)
      );

      // 根据数据量决定渲染层数
      const shouldRenderShadow = data.length <= 2000;
      const shouldRenderBorder = data.length <= 3000;

      // 路线阴影 - 增强立体感（大数据量时跳过）
      if (shouldRenderShadow) {
        const shadowRoute = new BMap.Polyline(pathPoints, {
          strokeColor: "#000000",
          strokeWeight: 6,
          strokeOpacity: 0.15,
          strokeStyle: "solid",
        });
        this.map.addOverlay(shadowRoute);
        this.presetOverlays.items.push(shadowRoute);
      }

      // 路线边框 - 白色边框效果（大数据量时跳过）
      if (shouldRenderBorder) {
        const borderRoute = new BMap.Polyline(pathPoints, {
          strokeColor: "#ffffff",
          strokeWeight: 5,
          strokeOpacity: 0.4,
          strokeStyle: "solid",
        });
        this.map.addOverlay(borderRoute);
        this.presetOverlays.items.push(borderRoute);
      }

      // 主路线 - 绿色实线（类似高德地图的主路线）
      const mainRoute = new BMap.Polyline(pathPoints, {
        strokeColor: "#4CAF50",
        strokeWeight: 3,
        strokeOpacity: 0.9,
        strokeStyle: "solid",
      });
      this.map.addOverlay(mainRoute);
      this.presetOverlays.items.push(mainRoute);

      // 路线高亮 - 内部亮线（大数据量时跳过）
      if (data.length <= 1500) {
        const highlightRoute = new BMap.Polyline(pathPoints, {
          strokeColor: "#ffffff",
          strokeWeight: 1,
          strokeOpacity: 0.6,
          strokeStyle: "solid",
        });
        this.map.addOverlay(highlightRoute);
        this.presetOverlays.items.push(highlightRoute);
      }
    },

    // 渲染路径点标记 - 性能优化版本
    renderRoutePoints(data) {
      const BMap = window.BMap;

      // 数据采样 - 如果数据量很大，减少标记点数量
      const sampleInterval =
        data.length > 1000 ? 50 : data.length > 500 ? 25 : 10;

      data.forEach((item, index) => {
        const point = new BMap.Point(item.longitude, item.latitude);

        // 只在关键点显示标记（起点、终点、转折点）
        const isStart = index === 0;
        const isEnd = index === data.length - 1;
        const isKeyPoint = isStart || isEnd || index % sampleInterval === 0;

        if (isKeyPoint) {
          // 创建路径点图标
          const createPointIcon = (isStart, isEnd) => {
            let fillColor = "#3388ff";
            let size = 8;
            let strokeWidth = 1;

            if (isStart) {
              fillColor = "#4CAF50"; // 起点绿色
              size = 12;
              strokeWidth = 2;
            } else if (isEnd) {
              fillColor = "#F44336"; // 终点红色
              size = 12;
              strokeWidth = 2;
            }

            const svgStr = `
              <svg xmlns="http://www.w3.org/2000/svg" width="${size}" height="${size}" viewBox="0 0 ${size} ${size}">
                <circle cx="${size / 2}" cy="${size / 2}" r="${
              size / 2 - 1
            }" fill="${fillColor}" stroke="#ffffff" stroke-width="${strokeWidth}"/>
                <circle cx="${size / 2}" cy="${size / 2}" r="${
              size / 2 - 3
            }" fill="#ffffff" opacity="0.3"/>
              </svg>
            `;

            const icon = createSvgIcon(
              svgStr,
              { width: size, height: size },
              { width: size / 2, height: size / 2 }
            );
            return icon;
          };

          const icon = createPointIcon(isStart, isEnd);

          const marker = new BMap.Marker(point, { icon });
          this.map.addOverlay(marker);
          this.presetOverlays.items.push(marker);
        }
      });
    },

    // 初始化方向箭头动画 - 性能优化版本
    initDirectionArrows(data) {
      const BMap = window.BMap;
      const arrows = [];

      // 数据采样 - 如果数据量很大，进行采样
      const sampleInterval =
        data.length > 1000 ? 10 : data.length > 500 ? 5 : 2;

      // 在路径的关键转折点添加方向箭头
      for (let i = 0; i < data.length - 1; i += sampleInterval) {
        // 每隔一定距离或方向变化较大时添加箭头
        if (
          i % (sampleInterval * 2) === 0 ||
          this.isDirectionChanged(data, i, 20)
        ) {
          const start = new BMap.Point(data[i].longitude, data[i].latitude);
          const end = new BMap.Point(
            data[i + 1].longitude,
            data[i + 1].latitude
          );
          const angle = calculateSegmentAngle(start, end);

          const arrowMarker = this.createDirectionArrow(start, angle);
          this.map.addOverlay(arrowMarker);
          arrows.push({
            marker: arrowMarker,
            start,
            end,
            progress: Math.random(), // 随机起始位置，创造更自然的效果
            segmentIndex: i,
            speed: 0.002 + Math.random() * 0.003, // 降低速度，减少CPU负担
          });
        }
      }

      // 动画函数 - 性能优化版本
      const animateArrows = (timestamp) => {
        if (!this.presetOverlays.animationId) return;

        // 限制同时更新的箭头数量，避免过度渲染
        const maxArrowsPerFrame = 20;
        const arrowsToUpdate = arrows.slice(0, maxArrowsPerFrame);

        arrowsToUpdate.forEach((arrow) => {
          arrow.progress += arrow.speed; // 使用随机速度

          if (arrow.progress >= 1) {
            arrow.progress = 0;
            arrow.marker.setPosition(arrow.start);
          } else {
            const currentPos = new BMap.Point(
              arrow.start.lng +
                (arrow.end.lng - arrow.start.lng) * arrow.progress,
              arrow.start.lat +
                (arrow.end.lat - arrow.start.lat) * arrow.progress
            );
            arrow.marker.setPosition(currentPos);
          }
        });

        // 轮换更新箭头，确保所有箭头都能被更新
        arrows.push(...arrows.splice(0, maxArrowsPerFrame));

        this.presetOverlays.arrows = arrows;
        this.presetOverlays.animationId = requestAnimationFrame(animateArrows);
      };

      this.presetOverlays.animationId = requestAnimationFrame(animateArrows);
    },

    // 创建方向箭头 - 确保箭头不超过路线宽度
    createDirectionArrow(point, angle) {
      const BMap = window.BMap;
      const svgStr = `
        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="8" viewBox="0 0 24 24">
          <g transform="rotate(${angle},12,12)">
            <!-- 简洁的白色箭头，类似高德地图样式 -->
            <path d="M12 6L8 12l4 6 4-6z" fill="#ffffff" stroke="#ffffff" stroke-width="0.5"/>
          </g>
        </svg>
      `;

      const icon = createSvgIcon(
        svgStr,
        { width: 8, height: 8 },
        { width: 4, height: 4 }
      );
      return new BMap.Marker(point, { icon });
    },

    // 使用Canvas渲染静态路径
    renderStaticPathWithCanvas(data) {
      const pathCanvas = new DynamicCanvasLayer(this.map, (ctx, canvas) => {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        // 绘制路径线
        ctx.beginPath();
        const firstPoint = this.map.pointToOverlayPixel(
          new BMap.Point(data[0].longitude, data[0].latitude)
        );
        ctx.moveTo(firstPoint.x, firstPoint.y);

        for (let i = 1; i < data.length; i++) {
          const pixel = this.map.pointToOverlayPixel(
            new BMap.Point(data[i].longitude, data[i].latitude)
          );
          ctx.lineTo(pixel.x, pixel.y);
        }

        ctx.strokeStyle = "rgba(170, 170, 170, 0.5)";
        ctx.lineWidth = 0.1;
        ctx.stroke();
      });

      const dotsCanvas = new DynamicCanvasLayer(this.map, (ctx, canvas) => {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = "rgba(255, 0, 0, 0.1)";

        data.forEach((item) => {
          const pixel = this.map.pointToOverlayPixel(
            new BMap.Point(item.longitude, item.latitude)
          );
          ctx.beginPath();
          ctx.arc(pixel.x, pixel.y, 3, 0, Math.PI * 2);
          ctx.fill();
        });
      });

      this.presetOverlays.items.push(
        {
          type: "canvas",
          instance: pathCanvas,
        },
        {
          type: "canvas",
          instance: dotsCanvas,
        }
      );
    },

    // 创建小人标记
    createCharacterMarker(lng, lat, angle = 0) {
      const BMap = window.BMap;
      const svgStr = `
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
          <g transform="rotate(${angle},12,12)">
            <circle cx="12" cy="8" r="5" fill="#FFD700" stroke="#000" stroke-width="1"/>
            <path d="M12,13 L12,20" stroke="#000" stroke-width="2"/>
            <path d="M8,15 L16,15" stroke="#000" stroke-width="2"/>
            <path d="M12,20 L9,24 M12,20 L15,24" stroke="#000" stroke-width="2"/>
          </g>
        </svg>
      `;

      const icon = createSvgIcon(
        svgStr,
        { width: 24, height: 24 },
        { width: 12, height: 12 }
      );
      return new BMap.Marker(new BMap.Point(lng, lat), { icon });
    },

    // 初始化箭头动画
    initArrowAnimation(data) {
      const BMap = window.BMap;
      const arrows = [];

      // 只在路径转折处添加箭头
      for (let i = 0; i < data.length - 1; i++) {
        if (i % 10 === 0 || this.isDirectionChanged(data, i, 15)) {
          const start = new BMap.Point(data[i].longitude, data[i].latitude);
          const end = new BMap.Point(
            data[i + 1].longitude,
            data[i + 1].latitude
          );
          const angle = calculateSegmentAngle(start, end);

          const arrowMarker = this.createArrowMarker(start, angle);
          this.map.addOverlay(arrowMarker);
          arrows.push({
            marker: arrowMarker,
            start,
            end,
            progress: 0,
          });
        }
      }

      // 动画函数
      const animateArrows = (timestamp) => {
        if (!this.presetOverlays.animationId) return;

        arrows.forEach((arrow) => {
          arrow.progress += 0.005; // 固定增量，避免依赖时间戳

          if (arrow.progress >= 1) {
            arrow.progress = 0;
            arrow.marker.setPosition(arrow.start);
          } else {
            const currentPos = new BMap.Point(
              arrow.start.lng +
                (arrow.end.lng - arrow.start.lng) * arrow.progress,
              arrow.start.lat +
                (arrow.end.lat - arrow.start.lat) * arrow.progress
            );
            arrow.marker.setPosition(currentPos);
          }
        });

        this.presetOverlays.arrows = arrows;
        this.presetOverlays.animationId = requestAnimationFrame(animateArrows);
      };

      this.presetOverlays.arrows = arrows;
      this.presetOverlays.animationId = requestAnimationFrame(animateArrows);
    },

    // 创建箭头标记
    createArrowMarker(point, angle) {
      const BMap = window.BMap;
      const svgStr = `
        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="8" viewBox="0 0 24 24">
          <g transform="rotate(${angle},12,12)">
            <path d="M6,6 L18,12 L6,18 M8,6 L18,12 L8,18"
                  stroke="#FFD700" stroke-width="2.5" fill="none"/>
            <path d="M6,6 L18,12 L6,18 M8,6 L18,12 L8,18"
                  stroke="#FFF" stroke-width="1" fill="none"/>
          </g>
        </svg>
      `;

      const icon = createSvgIcon(
        svgStr,
        { width: 12, height: 12 },
        { width: 6, height: 6 }
      );
      return new BMap.Marker(point, { icon });
    },

    // 检查方向是否变化
    isDirectionChanged(data, index, threshold) {
      if (index < 1 || index >= data.length - 1) return false;

      const prev = data[index - 1];
      const curr = data[index];
      const next = data[index + 1];

      const angle1 = Math.atan2(
        curr.longitude - prev.longitude,
        curr.latitude - prev.latitude
      );
      const angle2 = Math.atan2(
        next.longitude - curr.longitude,
        next.latitude - curr.latitude
      );

      const diff = (Math.abs(angle1 - angle2) * 180) / Math.PI;
      return diff > threshold;
    },

    // 设置时间控制
    setupTimeControls(data, characterMarker) {
      const moveCharacterToTime = (targetTime) => {
        const targetTimestamp = parseTimeString(targetTime);
        let startIndex = 0;
        let endIndex = 0;

        for (let i = 0; i < data.length - 1; i++) {
          const startTime = parseTimeString(data[i].createTime);
          const endTime = parseTimeString(data[i + 1].createTime);

          if (targetTimestamp >= startTime && targetTimestamp <= endTime) {
            startIndex = i;
            endIndex = i + 1;
            break;
          }
        }

        // 边界检查
        if (targetTimestamp < parseTimeString(data[0].createTime)) {
          characterMarker.setPosition(
            new BMap.Point(data[0].longitude, data[0].latitude)
          );
          return;
        }

        if (
          targetTimestamp > parseTimeString(data[data.length - 1].createTime)
        ) {
          characterMarker.setPosition(
            new BMap.Point(
              data[data.length - 1].longitude,
              data[data.length - 1].latitude
            )
          );
          return;
        }
        // 3. 创建小人标记
        const createCharacterIcon = (angle = 0) => {
          const svgStr = `
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
            <g transform="rotate(${angle},12,12)">
              <!-- 头部 -->
              <circle cx="12" cy="8" r="5" fill="#FFD700" stroke="#000" stroke-width="1"/>
              <!-- 身体 -->
              <path d="M12,13 L12,20" stroke="#000" stroke-width="2"/>
              <!-- 手臂 -->
              <path d="M8,15 L16,15" stroke="#000" stroke-width="2"/>
              <!-- 腿部 -->
              <path d="M12,20 L9,24 M12,20 L15,24" stroke="#000" stroke-width="2"/>
            </g>
          </svg>
        `
            .trim()
            .replace(/\s+/g, " ");

          const encodedSvg = encodeURIComponent(svgStr)
            .replace(/'/g, "%27")
            .replace(/"/g, "%22");

          return new BMap.Icon(
            "data:image/svg+xml;charset=utf-8," + encodedSvg,
            new BMap.Size(24, 24),
            { anchor: new BMap.Size(12, 12) }
          );
        };
        // 计算位置和角度
        const startTime = parseTimeString(data[startIndex].createTime);
        const endTime = parseTimeString(data[endIndex].createTime);
        const progress = (targetTimestamp - startTime) / (endTime - startTime);

        const startPoint = new BMap.Point(
          data[startIndex].longitude,
          data[startIndex].latitude
        );
        const endPoint = new BMap.Point(
          data[endIndex].longitude,
          data[endIndex].latitude
        );
        const angle = calculateSegmentAngle(startPoint, endPoint);

        const currentPos = new BMap.Point(
          startPoint.lng + (endPoint.lng - startPoint.lng) * progress,
          startPoint.lat + (endPoint.lat - startPoint.lat) * progress
        );

        characterMarker.setPosition(currentPos);
        characterMarker.setIcon(
          createCharacterIcon(currentPos.lng, currentPos.lat, angle)
        );

        if (this.presetOverlays.isTracking) {
          this.map.panTo(currentPos, { noAnimation: true });
        }
      };

      this.presetOverlays.moveToTime = moveCharacterToTime;
      this.presetOverlays.toggleTracking = () => {
        this.presetOverlays.isTracking = !this.presetOverlays.isTracking;
      };
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

    // 时间轴控制
    timeLinePlay() {
      if (this.isBf) this.play();
      else this.stop();
      this.isBf = !this.isBf;
    },

    play() {
      this.$refs.time_line.play();
    },

    stop() {
      this.$refs.time_line.stop();
    },

    handleMultiplierChange(value) {
      console.log(value);
      const m = value == 10 ? 100 : value == 20 ? 50 : 10;
      this.$refs.time_line.play(m);
    },

    toggleTrack() {
      this.isToggle = !this.isToggle;
      this.presetOverlays.isTracking = this.isToggle;
    },

    changeDate(data) {
      this.inDate = data;
      if (this.presetOverlays.moveToTime) {
        this.presetOverlays.moveToTime(data);
      }
    },

    // 从本地存储加载保存的标记组
    loadSavedGroups() {
      const saved = localStorage.getItem("baiduMapSavedPoints");
      if (saved) {
        this.savedPointGroups = JSON.parse(saved);
      }
    },

    // 保存标记组到本地存储
    saveGroupsToLocal() {
      localStorage.setItem(
        "baiduMapSavedPoints",
        JSON.stringify(this.savedPointGroups)
      );
    },

    // 保存当前标记点
    savePoints() {
      if (this.points.length === 0) {
        alert("没有可保存的标记点！");
        return;
      }

      const groupId = Date.now().toString();
      const timestamp = new Date().toLocaleString();

      this.savedPointGroups.push({
        id: groupId,
        timestamp: timestamp,
        points: [...this.points],
      });

      this.saveGroupsToLocal();
      alert(`已保存当前 ${this.points.length} 个标记点`);
      this.clearCurrentPoints();
    },

    // 清空当前标记（不删除保存的组）
    clearCurrentPoints() {
      this.map.clearOverlays();
      this.points = [];
      this.markers = [];
      this.polyline = null;
      this.presetOverlays = [];

      // 重新加载预设坐标和已保存的标记组
      this.showAllSavedGroups();
    },

    // 显示所有保存的标记组
    showAllSavedGroups() {
      this.savedPointGroups.forEach((group, groupIndex) => {
        const color = this.getColorByIndex(groupIndex);

        group.points.forEach((point, pointIndex) => {
          const bPoint = new window.BMap.Point(point.lng, point.lat);
          this.addSavedMarker(bPoint, pointIndex + 1, color);
        });

        if (group.points.length >= 2) {
          const pathPoints = group.points.map(
            (p) => new window.BMap.Point(p.lng, p.lat)
          );
          if (group.points.length >= 3) {
            pathPoints.push(
              new window.BMap.Point(group.points[0].lng, group.points[0].lat)
            );
          }

          const groupPolyline = new window.BMap.Polyline(pathPoints, {
            strokeColor: color,
            strokeWeight: 3,
            strokeOpacity: 0.8,
            strokeStyle: "solid",
          });
          this.map.addOverlay(groupPolyline);
        }
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
    addSavedMarker(point, label, color) {
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
        this.updatePolyline();
      });
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
    remoteSearchUser(a, b, c) {
      console.log(this.dateRange);
      console.log(this.queryParams.status);
      listLanya_device_card_sender_log_by_name_card_type({ param: a }).then(
        (response) => {
          response.rows.forEach(
            (item) =>
              (item.label =
                item.realName + "-" + item.cardId + "-" + item.personTypeName)
          );
          console.log(response.rows);
          this.selectData = response.rows;
        }
      );
    },

    handleSelectChange(val) {
      positionHistoryPositionFindPersonHistoryList({
        personId: this.queryParams.status.personId,
        date: "",
        time: [],
        beginTime: this.dateRange[0],
        endTime: this.dateRange[1],
      }).then((res) => {
        if (res.code == 200) {
          this.tableData = res.data;
        }
      });
    },
    // 清除所有标记
    clearAll() {
      this.map.clearOverlays();
      this.points = [];
      this.markers = [];
      this.polyline = null;
      this.savedPointGroups = [];
      this.presetOverlays = [];
      this.saveGroupsToLocal();
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
::v-deep .el-button,
.queryBtn,
.el-button--default,
.el-button--medium {
  span {
    font-size: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #409eff;
  }
}
::v-deep .el-form-item__content .el-date-editor,
.el-range-editor,
.el-input__inner,
.el-date-editor--datetimerange,
.el-range-editor--medium {
  width: 100%;
}
</style>
