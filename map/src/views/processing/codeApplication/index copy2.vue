<template>
  <div class="map-container">
    <div id="container"></div>
    <div class="controls">
      <el-form class="form" :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item class="width25">
          <el-select
            :teleported="false"
            popper-class="popperClass"
            :popper-append-to-body="false"
            class="searchSelect"
            v-model="queryParams.status"
            clearable
            style="">
            <el-option
              v-for="dict in selectData"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item class="width33">
          <el-date-picker
            value-format="YYYY-MM-DD HH:mm:ss"
            :teleported="false"
            v-model="dateRange"
            popper-class="customDatePicker"
            type="datetimerange"
            range-separator="To"
            start-placeholder="开始时间"
            end-placeholder="结束时间" />
        </el-form-item>
        <el-form-item id="porBox">
          <span style="color: #fff; cursor: pointer">
            <el-icon style="vertical-align: top" :size="20" class="searchIcon"
              ><Setting
            /></el-icon>
            历史轨迹播放设置</span
          >

          <div id="hoverPor">
            <div class="porItem" @click="savePoints">
              <el-icon :size="20"><UploadFilled /></el-icon> 保存当前标记点
            </div>
            <div class="porItem" @click="clearAll">
              <el-icon :size="20"><DeleteFilled /></el-icon> 清除全部标记
            </div>
            <div class="porItem" @click="showPresetPoints">
              <el-icon :size="20" v-if="isShowD"><SuccessFilled /></el-icon>
              <el-icon :size="20" v-else><CircleCloseFilled /></el-icon>
              加载预设点
            </div>
            <div class="porItem" @click="showPresetLines">
              <el-icon :size="20" v-if="isShowX"><SuccessFilled /></el-icon>
              <el-icon :size="20" v-else><CircleCloseFilled /></el-icon>
              加载预设线
            </div>
            <div class="porItem" @click="showAnimatedLine">
              <el-icon :size="20" v-if="isShowQ"><SuccessFilled /></el-icon>
              <el-icon :size="20" v-else><CircleCloseFilled /></el-icon> 起止
            </div>
            <div v-if="isShowQ" class="porItem" @click="toggleTrack">
              <el-icon :size="20" v-if="isToggle"><SuccessFilled /></el-icon>
              <el-icon :size="20" v-else><CircleCloseFilled /></el-icon>
              视角跟踪
            </div>
          </div>
          <div v-if="isShowQ" id="hoverPor2">
            <div>当前时间:{{ inDate }}</div>
            <div>停留时间:</div>
          </div>
        </el-form-item>
      </el-form>
    </div>
    <div class="timeline">
      <div class="timeAn">
        <el-icon @click="timeLinePlay" v-if="isBf"><VideoPlay /></el-icon>
        <el-icon @click="timeLinePlay" v-else><VideoPause /></el-icon>
      </div>
      <div class="timeAn2">
        <MultiplierSelector
          v-if="isShowQ"
          @cb="handleMultiplierChange"
          v-model="selectedMultiplier" />
      </div>

      <TimeLine
        :markTime="time_range"
        v-if="isShowQ"
        :key="timeIndex"
        ref="time_line"
        @change="changeDate"
        :time-range="dateRange"
        :isAutoPlay="isAutoPlay" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import TimeLine from "@/components/TimeLine/timeline-canvas.vue";
import MultiplierSelector from "@/components/Preset/index";
import { resData as animatedLineData } from "../codeApplication/data";
import {
  showPresetPointsData,
  showPresetLinesData,
  showAnimatedLineData,
} from "./data";
import {
  simplifyPath,
  parseTimeString,
  calculateSegmentAngle,
  createSvgIcon,
} from "./utils";

// 响应式数据
const queryParams = ref({});
const selectData = ref([
  { label: "data1", value: "1" },
  { label: "data2", value: "2" },
  { label: "data3", value: "3" },
]);
const dateRange = ref([]);
const selectedMultiplier = ref(10);
const isAutoPlay = ref(true);
const time_line = ref(null);
const time_range = ref(["2023-01-02 00:00:00", "2023-01-02 23:59:59"]);
const timeIndex = ref(0);
const getTimeLineOne = ref("");
const isShowD = ref(false);
const isShowX = ref(false);
const isBf = ref(false);
const isShowQ = ref(false);
const isToggle = ref(false);
const inDate = ref("");

// 地图相关
const map = ref(null);
const polyline = ref(null);
const points = ref([]);
const markers = ref([]);
const savedPointGroups = ref([]);
const presetOverlays = ref({
  items: [],
  animationId: null,
  arrows: [],
  isTracking: false,
});

// 生命周期
onMounted(() => {
  loadSavedGroups();
  initMap();
  showAllSavedGroups();
});

onBeforeUnmount(() => {
  cleanupAnimation();
});

// 初始化地图
const initMap = () => {
  const BMap = window.BMap;
  map.value = new BMap.Map("container");
  map.value.centerAndZoom(new BMap.Point(124.87612, 46.64195), 15);
  map.value.setMinZoom(11);
  map.value.setMaxZoom(19);
  map.value.enableScrollWheelZoom(true);

  // 添加控件
  map.value.addControl(
    new BMap.NavigationControl({
      anchor: window.BMAP_ANCHOR_TOP_RIGHT,
      type: window.BMAP_NAVIGATION_CONTROL_SMALL,
    })
  );
  map.value.addControl(new BMap.ScaleControl());
  map.value.addControl(new BMap.OverviewMapControl());

  // 右键添加标记
  map.value.addEventListener("rightclick", (e) => {
    addNewMarker(e.point);
    updatePolyline();
  });
};

// 数据预处理
const preprocessLineData = (rawData) => {
  // 采样 - 每5个点取1个点
  const sampledData = [];
  for (let i = 0; i < rawData.length; i += 5) {
    sampledData.push(rawData[i]);
  }

  // 简化路径 - 容差值0.0001大约相当于10米左右
  return simplifyPath(sampledData, 0.0001);
};

// 显示预设点
const showPresetPoints = () => {
  const deepData = !isShowD.value
    ? JSON.parse(JSON.stringify(showPresetPointsData))
    : [];

  clearPresetOverlays();
  const BMap = window.BMap;

  if (deepData.length > 0 && !isShowD.value) {
    const allPoints = deepData.flat();
    map.value.setViewport(
      allPoints.map((coord) => new BMap.Point(coord[0], coord[1]))
    );
  }

  // 使用Canvas渲染替代大量DOM标记
  const canvasOverlay = createPointsCanvasOverlay(deepData);
  map.value.addOverlay(canvasOverlay);
  presetOverlays.value.items.push(canvasOverlay);

  isShowD.value = !isShowD.value;
  isShowX.value = false;
  isShowQ.value = false;
};

// 创建点集的Canvas覆盖物
const createPointsCanvasOverlay = (pointGroups) => {
  const BMap = window.BMap;

  return new BMap.CanvasLayer({
    update: (ctx, canvas) => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.fillStyle = "#FF5722";

      pointGroups.forEach((group) => {
        group.forEach((coord) => {
          const pixel = map.value.pointToOverlayPixel(
            new BMap.Point(coord[0], coord[1])
          );
          ctx.beginPath();
          ctx.arc(pixel.x, pixel.y, 4, 0, Math.PI * 2);
          ctx.fill();
        });
      });
    },
  });
};

// 显示预设线
const showPresetLines = () => {
  const deepData = !isShowX.value
    ? JSON.parse(JSON.stringify(showPresetLinesData))
    : [];

  clearPresetOverlays();
  const BMap = window.BMap;

  if (deepData.length > 0 && !isShowX.value) {
    const allPoints = deepData.flat();
    map.value.setViewport(
      allPoints.map((coord) => new BMap.Point(coord[0], coord[1]))
    );
  }

  // 使用单个Canvas渲染所有线和点
  const canvasOverlay = createLinesCanvasOverlay(deepData);
  map.value.addOverlay(canvasOverlay);
  presetOverlays.value.items.push(canvasOverlay);

  isShowX.value = !isShowX.value;
  isShowD.value = false;
  isShowQ.value = false;
};

// 创建线集的Canvas覆盖物
const createLinesCanvasOverlay = (lineGroups) => {
  const BMap = window.BMap;

  return new BMap.CanvasLayer({
    update: (ctx, canvas) => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      lineGroups.forEach((group) => {
        if (group.length < 2) return;

        // 绘制连线
        ctx.beginPath();
        const firstPixel = map.value.pointToOverlayPixel(
          new BMap.Point(group[0][0], group[0][1])
        );
        ctx.moveTo(firstPixel.x, firstPixel.y);

        for (let i = 1; i < group.length; i++) {
          const pixel = map.value.pointToOverlayPixel(
            new BMap.Point(group[i][0], group[i][1])
          );
          ctx.lineTo(pixel.x, pixel.y);
        }

        ctx.strokeStyle = "#2196F3";
        ctx.lineWidth = 3;
        ctx.stroke();

        // 绘制点
        ctx.fillStyle = "#FF5722";
        group.forEach((coord) => {
          const pixel = map.value.pointToOverlayPixel(
            new BMap.Point(coord[0], coord[1])
          );
          ctx.beginPath();
          ctx.arc(pixel.x, pixel.y, 4, 0, Math.PI * 2);
          ctx.fill();
        });
      });
    },
  });
};

// 显示动画轨迹
const showAnimatedLine = () => {
  selectedMultiplier.value = 10;

  if (isShowQ.value) {
    cleanupAnimation();
    isShowQ.value = false;
    isBf.value = false;
    time_line.value?.updateSpeed();
    return;
  }

  clearPresetOverlays();
  const optimizedData = preprocessLineData(animatedLineData.data);

  if (optimizedData.length === 0) return;

  // 设置视图范围
  const bPoints = optimizedData.map(
    (item) => new window.BMap.Point(item.longitude, item.latitude)
  );
  map.value.setViewport(bPoints, { padding: [50, 50, 50, 50] });

  // 设置时间范围
  time_range.value = [
    {
      beginTime: optimizedData[0].createTime,
      endTime: optimizedData[optimizedData.length - 1].createTime,
      bgColor: "green",
      text: "",
    },
  ];
  getTimeLineOne.value = optimizedData[0].createTime;

  // 使用Canvas渲染静态路径
  renderStaticPathWithCanvas(optimizedData);

  // 创建小人标记
  const characterMarker = createCharacterMarker(
    optimizedData[0].longitude,
    optimizedData[0].latitude
  );
  map.value.addOverlay(characterMarker);
  presetOverlays.value.character = characterMarker;

  // 初始化箭头动画
  initArrowAnimation(optimizedData);

  // 设置时间控制
  setupTimeControls(optimizedData, characterMarker);

  isShowQ.value = true;
  isShowX.value = false;
  isShowD.value = false;
};

// 使用Canvas渲染静态路径
const renderStaticPathWithCanvas = (data) => {
  const BMap = window.BMap;

  // 路径线Canvas
  const pathCanvas = new BMap.CanvasLayer({
    update: function () {
      // 正确获取画布和上下文
      const canvas = this.canvas;
      const ctx = canvas.getContext("2d");
      if (!ctx) return;

      // 清空画布
      ctx.clearRect(0, 0, canvas.width, canvas.height);

      // 绘制路径线
      ctx.beginPath();
      const firstPixel = map.value.pointToOverlayPixel(
        new BMap.Point(data[0].longitude, data[0].latitude)
      );
      ctx.moveTo(firstPixel.x, firstPixel.y);

      for (let i = 1; i < data.length; i++) {
        const pixel = map.value.pointToOverlayPixel(
          new BMap.Point(data[i].longitude, data[i].latitude)
        );
        ctx.lineTo(pixel.x, pixel.y);
      }

      ctx.strokeStyle = "rgba(170, 170, 170, 0.5)";
      ctx.lineWidth = 2;
      ctx.stroke();
    },
    // 初始化画布尺寸（可选但推荐）
    initialize: function (canvas) {
      this.canvas = canvas;
      canvas.width = map.value.getSize().width;
      canvas.height = map.value.getSize().height;
    },
  });

  // 红点Canvas（同理修复）
  const dotsCanvas = new BMap.CanvasLayer({
    update: function () {
      const canvas = this.canvas;
      const ctx = canvas.getContext("2d");
      if (!ctx) return;

      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.fillStyle = "#FF0000";

      data.forEach((item) => {
        const pixel = map.value.pointToOverlayPixel(
          new BMap.Point(item.longitude, item.latitude)
        );
        ctx.beginPath();
        ctx.arc(pixel.x, pixel.y, 3, 0, Math.PI * 2);
        ctx.fill();
      });
    },
    initialize: function (canvas) {
      this.canvas = canvas;
    },
  });

  map.value.addOverlay(pathCanvas);
  map.value.addOverlay(dotsCanvas);
  presetOverlays.value.items.push(pathCanvas, dotsCanvas);
};

// 创建小人标记
const createCharacterMarker = (lng, lat, angle = 0) => {
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
};

// 初始化箭头动画
const initArrowAnimation = (data) => {
  const BMap = window.BMap;
  const arrows = [];

  // 只在路径转折处添加箭头
  for (let i = 0; i < data.length - 1; i++) {
    if (i % 10 === 0 || isDirectionChanged(data, i, 15)) {
      const start = new BMap.Point(data[i].longitude, data[i].latitude);
      const end = new BMap.Point(data[i + 1].longitude, data[i + 1].latitude);
      const angle = calculateSegmentAngle(start, end);

      const arrowMarker = createArrowMarker(start, angle);
      map.value.addOverlay(arrowMarker);
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
    if (!presetOverlays.value.animationId) return;

    arrows.forEach((arrow) => {
      arrow.progress += 0.005; // 固定增量，避免依赖时间戳

      if (arrow.progress >= 1) {
        arrow.progress = 0;
        arrow.marker.setPosition(arrow.start);
      } else {
        const currentPos = new BMap.Point(
          arrow.start.lng + (arrow.end.lng - arrow.start.lng) * arrow.progress,
          arrow.start.lat + (arrow.end.lat - arrow.start.lat) * arrow.progress
        );
        arrow.marker.setPosition(currentPos);
      }
    });

    presetOverlays.value.animationId = requestAnimationFrame(animateArrows);
  };

  presetOverlays.value.arrows = arrows;
  presetOverlays.value.animationId = requestAnimationFrame(animateArrows);
};

// 创建箭头标记
const createArrowMarker = (point, angle) => {
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
};

// 检查方向是否变化
const isDirectionChanged = (data, index, threshold) => {
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
};

// 设置时间控制
const setupTimeControls = (data, characterMarker) => {
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

    if (targetTimestamp > parseTimeString(data[data.length - 1].createTime)) {
      characterMarker.setPosition(
        new BMap.Point(
          data[data.length - 1].longitude,
          data[data.length - 1].latitude
        )
      );
      return;
    }

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

    if (presetOverlays.value.isTracking) {
      map.value.panTo(currentPos, { noAnimation: true });
    }
  };

  presetOverlays.value.moveToTime = moveCharacterToTime;
  presetOverlays.value.toggleTracking = () => {
    presetOverlays.value.isTracking = !presetOverlays.value.isTracking;
  };
};

// 清理动画
const cleanupAnimation = () => {
  if (presetOverlays.value.animationId) {
    cancelAnimationFrame(presetOverlays.value.animationId);
    presetOverlays.value.animationId = null;
  }

  // 清除箭头
  if (presetOverlays.value.arrows) {
    presetOverlays.value.arrows.forEach((arrow) => {
      try {
        map.value.removeOverlay(arrow.marker);
      } catch (e) {
        console.warn("移除箭头标记时出错:", e);
      }
    });
    presetOverlays.value.arrows = [];
  }

  // 清除其他覆盖物
  clearPresetOverlays();
};

// 清除预设覆盖物
const clearPresetOverlays = () => {
  if (!map.value) return;

  // 保留非预设的覆盖物（如用户手动添加的点）
  const toRemove = [];
  const toKeep = [];

  map.value.getOverlays().forEach((overlay) => {
    if (
      presetOverlays.value.items.includes(overlay) ||
      (presetOverlays.value.character &&
        overlay === presetOverlays.value.character)
    ) {
      toRemove.push(overlay);
    } else {
      toKeep.push(overlay);
    }
  });

  toRemove.forEach((overlay) => {
    map.value.removeOverlay(overlay);
  });

  presetOverlays.value.items = [];
  delete presetOverlays.value.character;
};

// 其他原有方法保持不变...
// (savePoints, clearCurrentPoints, showAllSavedGroups, addNewMarker, updatePolyline, clearAll等)

// 时间轴控制
const timeLinePlay = () => {
  if (isBf.value) play();
  else stop();
  isBf.value = !isBf.value;
};

const play = () => {
  time_line.value?.play();
};

const stop = () => {
  time_line.value?.stop();
};

const handleMultiplierChange = (value) => {
  const m = value == 10 ? 100 : value == 20 ? 50 : 10;
  time_line.value?.play(m);
};

const toggleTrack = () => {
  isToggle.value = !isToggle.value;
  presetOverlays.value.isTracking = isToggle.value;
};

const changeDate = (data) => {
  inDate.value = data;
  if (presetOverlays.value.moveToTime) {
    presetOverlays.value.moveToTime(data);
  }
};

// 从本地存储加载保存的标记组
const loadSavedGroups = () => {
  const saved = localStorage.getItem("baiduMapSavedPoints");
  if (saved) {
    savedPointGroups.value = JSON.parse(saved);
  }
};

// 保存标记组到本地存储
const saveGroupsToLocal = () => {
  localStorage.setItem(
    "baiduMapSavedPoints",
    JSON.stringify(savedPointGroups.value)
  );
};

// 保存当前标记点
const savePoints = () => {
  if (points.value.length === 0) {
    alert("没有可保存的标记点！");
    return;
  }

  const groupId = Date.now().toString();
  const timestamp = new Date().toLocaleString();

  savedPointGroups.value.push({
    id: groupId,
    timestamp: timestamp,
    points: [...points.value],
  });

  saveGroupsToLocal();
  alert(`已保存当前 ${points.value.length} 个标记点`);
  clearCurrentPoints();
};

// 清空当前标记（不删除保存的组）
const clearCurrentPoints = () => {
  map.value.clearOverlays();
  points.value = [];
  markers.value = [];
  polyline.value = null;
  presetOverlays.value = [];

  // 重新加载预设坐标和已保存的标记组
  showAllSavedGroups();
};

// 显示所有保存的标记组
const showAllSavedGroups = () => {
  savedPointGroups.value.forEach((group, groupIndex) => {
    const color = getColorByIndex(groupIndex);

    group.points.forEach((point, pointIndex) => {
      const bPoint = new window.BMap.Point(point.lng, point.lat);
      addSavedMarker(bPoint, pointIndex + 1, color);
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
      map.value.addOverlay(groupPolyline);
    }
  });
};

// 为不同组生成不同颜色
const getColorByIndex = (index) => {
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
};

// 添加已保存的标记
const addSavedMarker = (point, label, color) => {
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
  map.value.addOverlay(marker);
};

// 添加新标记（用户右键添加）
const addNewMarker = (point) => {
  const BMap = window.BMap;
  const marker = new BMap.Marker(point, { enableDragging: true });

  const labelNumber = points.value.length + 1;
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

  points.value.push({ lat: point.lat, lng: point.lng });
  marker.setLabel(label);
  map.value.addOverlay(marker);
  markers.value.push(marker);

  marker.addEventListener("dragend", (e) => {
    const index = markers.value.indexOf(marker);
    if (index !== -1) {
      points.value[index] = { lat: e.point.lat, lng: e.point.lng };
    }
    updatePolyline();
  });
};

// 更新连线（当前编辑组的连线）
const updatePolyline = () => {
  if (polyline.value) {
    map.value.removeOverlay(polyline.value);
  }

  if (points.value.length < 2) return;

  const pathPoints = points.value.map(
    (p) => new window.BMap.Point(p.lng, p.lat)
  );

  if (points.value.length >= 3) {
    pathPoints.push(
      new window.BMap.Point(points.value[0].lng, points.value[0].lat)
    );
  }

  polyline.value = new window.BMap.Polyline(pathPoints, {
    strokeColor: "#3388ff",
    strokeWeight: 3,
    strokeOpacity: 0.8,
    strokeStyle: "solid",
  });

  map.value.addOverlay(polyline.value);
};

// 清除所有标记（包括保存的组）
const clearAll = () => {
  map.value.clearOverlays();
  points.value = [];
  markers.value = [];
  polyline.value = null;
  savedPointGroups.value = [];
  presetOverlays.value = [];
  saveGroupsToLocal();
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
  // display: none;
}
#porBox {
  width: 23%;
  position: relative;
  #hoverPor {
    position: absolute;
    left: 0px;
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
.searchIcon {
  color: #fff;
  width: 25px;
  height: 25px;
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
.searchSelect {
  width: 200px;
  border-radius: 30px;
}
:deep() {
  .controls .el-select__wrapper,
  .el-tooltip__trigger,
  .el-tooltip__trigger {
    border-radius: 30px !important;
    background-color: black;
  }
}
:deep() {
  .el-popover {
    background: #000022;
    border-color: #000022;
    border-radius: 10px;
    opacity: 0.92;
    color: #fff;
  }
  .el-popper.is-light {
    background: black;
    border: 1px solid #273f70;
  }
  .controls .el-select-dropdown__item.hover {
    background: transparent;
    border: none;
    color: #04faa0;
  }
  .controls .el-select-dropdown__item {
    background: transparent;
    border: none;
    color: #fff !important;
  }

  .el-popper.is-light .el-popper__arrow::before {
    border: 1px solid #fff;
    background: black;
    right: 0;
  }
  .controls .el-select__selected-item,
  .controls .el-select__placeholder {
    color: #fff !important;
    font-weight: 400;
  }
  .el-range-input {
    color: #fff !important;
    &::placeholder {
      color: #fff;
    }
  }
}
</style>
