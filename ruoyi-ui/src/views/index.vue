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
            style=""
          >
            <el-option
              v-for="dict in selectData"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
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
            end-placeholder="结束时间"
          />
        </el-form-item>
        <el-form-item id="porBox">
          <div id="hoverPor">
            <span style="color: #fff; cursor: pointer">
              <i
                class="el-icon-setting"
                style="vertical-align: top; margin-right: 5px; font-size: 20px"
              ></i>
              历史轨迹播放设置
            </span>

            <div class="porItem" @click="savePoints">
              <i
                class="el-icon-upload"
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
              保存当前标记点
            </div>

            <div class="porItem" @click="clearAll">
              <i
                class="el-icon-delete"
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
              清除全部标记
            </div>

            <div class="porItem" @click="showPresetPoints">
              <i
                class="el-icon-circle-check"
                v-if="isShowD"
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
              <i
                class="el-icon-circle-close"
                v-else
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
              加载预设点
            </div>

            <div class="porItem" @click="showPresetLines">
              <i
                class="el-icon-circle-check"
                v-if="isShowX"
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
              <i
                class="el-icon-circle-close"
                v-else
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
              加载预设线
            </div>

            <div class="porItem" @click="showAnimatedLine">
              <i
                class="el-icon-circle-check"
                v-if="isShowQ"
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
              <i
                class="el-icon-circle-close"
                v-else
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
              起止
            </div>

            <div v-if="isShowQ" class="porItem" @click="toggleTrack">
              <i
                class="el-icon-circle-check"
                v-if="isToggle"
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
              <i
                class="el-icon-circle-close"
                v-else
                style="
                  vertical-align: middle;
                  margin-right: 5px;
                  font-size: 20px;
                "
              ></i>
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
import TimeLine from "@/components/TimeLine/timeline-canvas.vue";
import MultiplierSelector from "@/components/Preset/index";
import { showPresetPointsData, showPresetLinesData } from "./data";
import { resData } from "./data1";
import {
  DynamicCanvasLayer,
  simplifyPath,
  parseTimeString,
  calculateSegmentAngle,
  createSvgIcon,
} from "./utils";

export default {
  name: "BaiduMapWithControls",
  components: {
    TimeLine,
    MultiplierSelector,
  },
  data() {
    return {
      queryParams: {},
      selectData: [
        { label: "data1", value: "1" },
        { label: "data2", value: "2" },
        { label: "data3", value: "3" },
      ],
      dateRange: [],
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
        character: null,
        moveToTime: null,
        toggleTracking: null,
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
    loadSavedGroups() {
      const saved = localStorage.getItem("baiduMapSavedPoints");
      if (saved) {
        this.savedPointGroups = JSON.parse(saved);
      }
    },
    saveGroupsToLocal() {
      localStorage.setItem(
        "baiduMapSavedPoints",
        JSON.stringify(this.savedPointGroups)
      );
    },
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
    clearCurrentPoints() {
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
        character: null,
        moveToTime: null,
        toggleTracking: null,
      };
      this.showAllSavedGroups();
    },
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
        character: null,
        moveToTime: null,
        toggleTracking: null,
      };
      this.saveGroupsToLocal();
    },
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
    showPresetPoints() {
      const deepData = !this.isShowD
        ? JSON.parse(JSON.stringify(showPresetPointsData))
        : [];

      this.clearPresetOverlays();

      if (deepData.length > 0 && !this.isShowD) {
        const allPoints = deepData.flat();
        this.map.setViewport(
          allPoints.map((coord) => new window.BMap.Point(coord[0], coord[1]))
        );
      }
      setTimeout(() => {
        this.map.panBy(0, 0);
      }, 100);

      const pointsCanvas = new DynamicCanvasLayer(this.map, (ctx, canvas) => {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = "#FF5722";

        deepData.forEach((group) => {
          group.forEach((coord) => {
            const pixel = this.map.pointToOverlayPixel(
              new window.BMap.Point(coord[0], coord[1])
            );
            ctx.beginPath();
            ctx.arc(pixel.x, pixel.y, 4, 0, Math.PI * 2);
            ctx.fill();
          });
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
    showPresetLines() {
      const deepData = !this.isShowX
        ? JSON.parse(JSON.stringify(showPresetLinesData))
        : [];

      this.clearPresetOverlays();

      if (deepData.length > 0 && !this.isShowX) {
        const allPoints = deepData.flat();
        this.map.setViewport(
          allPoints.map((coord) => new window.BMap.Point(coord[0], coord[1]))
        );
      }
      setTimeout(() => {
        this.map.panBy(0, 0);
      }, 100);

      const linesCanvas = new DynamicCanvasLayer(this.map, (ctx, canvas) => {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        deepData.forEach((group) => {
          if (group.length < 2) return;

          ctx.beginPath();
          const firstPixel = this.map.pointToOverlayPixel(
            new window.BMap.Point(group[0][0], group[0][1])
          );
          ctx.moveTo(firstPixel.x, firstPixel.y);

          for (let i = 1; i < group.length; i++) {
            const pixel = this.map.pointToOverlayPixel(
              new window.BMap.Point(group[i][0], group[i][1])
            );
            ctx.lineTo(pixel.x, pixel.y);
          }

          ctx.strokeStyle = "#2196F3";
          ctx.lineWidth = 3;
          ctx.stroke();

          ctx.fillStyle = "#FF5722";
          group.forEach((coord) => {
            const pixel = this.map.pointToOverlayPixel(
              new window.BMap.Point(coord[0], coord[1])
            );
            ctx.beginPath();
            ctx.arc(pixel.x, pixel.y, 4, 0, Math.PI * 2);
            ctx.fill();
          });
        });
      });

      this.presetOverlays.items.push({
        type: "canvas",
        instance: linesCanvas,
      });

      this.isShowX = !this.isShowX;
      this.isShowD = false;
      this.isShowQ = false;
    },
    showAnimatedLine() {
      this.selectedMultiplier = 10;

      if (this.isShowQ) {
        this.cleanupAnimation();
        this.isShowQ = false;
        this.isBf = false;
        this.time_line?.updateSpeed();
        return;
      }

      this.clearPresetOverlays();

      let optimizedData = resData.data;

      if (optimizedData.length > 1000) {
        console.log(`原始数据量: ${optimizedData.length}，进行采样优化`);
        const sampleRate = Math.ceil(optimizedData.length / 1000);
        optimizedData = optimizedData.filter(
          (_, index) => index % sampleRate === 0
        );
        console.log(`采样后数据量: ${optimizedData.length}`);
      }

      if (optimizedData.length === 0) return;
      const bPoints = optimizedData.map(
        (item) => new window.BMap.Point(item.longitude, item.latitude)
      );
      this.map.setViewport(bPoints, { padding: [50, 50, 50, 50] });

      time_range.value = [
        {
          beginTime: optimizedData[0].createTime,
          endTime: optimizedData[optimizedData.length - 1].createTime,
          bgColor: "green",
          text: "",
        },
      ];
      this.getTimeLineOne = optimizedData[0].createTime;

      this.renderRoutePath(optimizedData);
      this.renderRoutePoints(optimizedData);
      const characterMarker = this.createCharacterMarker(
        optimizedData[0].longitude,
        optimizedData[0].latitude
      );
      this.map.addOverlay(characterMarker);
      this.presetOverlays.character = characterMarker;

      this.initDirectionArrows(optimizedData);
      this.setupTimeControls(optimizedData, characterMarker);

      this.isShowQ = true;
      this.isShowX = false;
      this.isShowD = false;
    },
    renderRoutePath(data) {
      const BMap = window.BMap;
      const pathPoints = data.map(
        (item) => new BMap.Point(item.longitude, item.latitude)
      );

      const shouldRenderShadow = data.length <= 2000;
      const shouldRenderBorder = data.length <= 3000;

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

      const mainRoute = new BMap.Polyline(pathPoints, {
        strokeColor: "#4CAF50",
        strokeWeight: 3,
        strokeOpacity: 0.9,
        strokeStyle: "solid",
      });
      this.map.addOverlay(mainRoute);
      this.presetOverlays.items.push(mainRoute);

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
    renderRoutePoints(data) {
      const BMap = window.BMap;
      const sampleInterval =
        data.length > 1000 ? 50 : data.length > 500 ? 25 : 10;

      data.forEach((item, index) => {
        const point = new BMap.Point(item.longitude, item.latitude);
        const isStart = index === 0;
        const isEnd = index === data.length - 1;
        const isKeyPoint = isStart || isEnd || index % sampleInterval === 0;

        if (isKeyPoint) {
          const createPointIcon = (isStart, isEnd) => {
            let fillColor = "#3388ff";
            let size = 8;
            let strokeWidth = 1;

            if (isStart) {
              fillColor = "#4CAF50";
              size = 12;
              strokeWidth = 2;
            } else if (isEnd) {
              fillColor = "#F44336";
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
    initDirectionArrows(data) {
      const BMap = window.BMap;
      const arrows = [];

      const sampleInterval =
        data.length > 1000 ? 10 : data.length > 500 ? 5 : 2;

      for (let i = 0; i < data.length - 1; i += sampleInterval) {
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
            progress: Math.random(),
            segmentIndex: i,
            speed: 0.002 + Math.random() * 0.003,
          });
        }
      }

      const animateArrows = (timestamp) => {
        if (!this.presetOverlays.animationId) return;

        const maxArrowsPerFrame = 20;
        const arrowsToUpdate = arrows.slice(0, maxArrowsPerFrame);

        arrowsToUpdate.forEach((arrow) => {
          arrow.progress += arrow.speed;

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

        arrows.push(...arrows.splice(0, maxArrowsPerFrame));

        this.presetOverlays.arrows = arrows;
        this.presetOverlays.animationId = requestAnimationFrame(animateArrows);
      };

      this.presetOverlays.arrows = arrows;
      this.presetOverlays.animationId = requestAnimationFrame(animateArrows);
    },
    createDirectionArrow(point, angle) {
      const BMap = window.BMap;
      const svgStr = `
        <svg xmlns="http://www.w3.org/2000/svg" width="8" height="8" viewBox="0 0 24 24">
          <g transform="rotate(${angle},12,12)">
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

        if (targetTimestamp < parseTimeString(data[0].createTime)) {
          characterMarker.setPosition(
            new window.BMap.Point(data[0].longitude, data[0].latitude)
          );
          return;
        }

        if (
          targetTimestamp > parseTimeString(data[data.length - 1].createTime)
        ) {
          characterMarker.setPosition(
            new window.BMap.Point(
              data[data.length - 1].longitude,
              data[data.length - 1].latitude
            )
          );
          return;
        }

        const startTime = parseTimeString(data[startIndex].createTime);
        const endTime = parseTimeString(data[endIndex].createTime);
        const progress = (targetTimestamp - startTime) / (endTime - startTime);

        const startPoint = new window.BMap.Point(
          data[startIndex].longitude,
          data[startIndex].latitude
        );
        const endPoint = new window.BMap.Point(
          data[endIndex].longitude,
          data[endIndex].latitude
        );
        const angle = calculateSegmentAngle(startPoint, endPoint);

        const currentPos = new window.BMap.Point(
          startPoint.lng + (endPoint.lng - startPoint.lng) * progress,
          startPoint.lat + (endPoint.lat - startPoint.lat) * progress
        );

        characterMarker.setPosition(currentPos);
        characterMarker.setIcon(
          this.createCharacterIcon(currentPos.lng, currentPos.lat, angle)
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
    createCharacterIcon(angle = 0) {
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

      const encodedSvg = encodeURIComponent(svgStr)
        .replace(/'/g, "%27")
        .replace(/"/g, "%22");

      return new BMap.Icon(
        "data:image/svg+xml;charset=utf-8," + encodedSvg,
        new BMap.Size(24, 24),
        { anchor: new BMap.Size(12, 12) }
      );
    },
    cleanupAnimation() {
      if (this.presetOverlays.animationId) {
        cancelAnimationFrame(this.presetOverlays.animationId);
        this.presetOverlays.animationId = null;
      }

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

      this.clearPresetOverlays();
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
    timeLinePlay() {
      if (this.isBf) this.play();
      else this.stop();
      this.isBf = !this.isBf;
    },
    play() {
      this.time_line?.play();
    },
    stop() {
      this.time_line?.stop();
    },
    handleMultiplierChange(value) {
      const m = value == 10 ? 100 : value == 20 ? 50 : 10;
      this.time_line?.play(m);
    },
  },
};
</script>

<style scoped>
.simple-multiplier select {
  border: 1px solid #ddd;
  border-radius: 4px;
  background: black;
  font-size: 14px;
  cursor: pointer;
  outline: none;
  color: #fff;
}

.simple-multiplier select:hover {
  border-color: #aaa;
}

.simple-multiplier select:focus {
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.1);
}
</style>
