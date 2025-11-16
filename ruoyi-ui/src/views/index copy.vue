<template>
  <div class="map-container">
    <div id="container"></div>
    <!-- <div class="controls">
      <div class="controls-left">
        <el-form
          class="form"
          :model="queryParams"
          ref="queryRef"
          :inline="true"
        >
          <el-row>
            <el-col :span="8">
              <el-form-item>
                <el-select
                  popper-class="popperClass"
                  placeholder="请输入关键词搜索用户"
                  filterable
                  remote
                  reserve-keyword
                  :popper-append-to-body="false"
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
            <el-col :span="15">
              <el-form-item>
                <el-date-picker
                  value-format="yyyy-MM-dd HH:mm:ss"
                  v-model="dateRange"
                  popper-class="customDatePicker"
                  type="datetimerange"
                  range-separator="To"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                /> </el-form-item
            ></el-col>
          </el-row>
        </el-form>
      </div>
      <div class="controls-right">
        <el-form
          class="form"
          :model="queryParams"
          ref="queryRef"
          :inline="true"
        >
          <el-row>
            <el-col :span="4">
              <el-form-item label="全场人数">
                <el-select
                  placeholder="全场人数"
                  filterable
                  remote
                  reserve-keyword
                  :popper-append-to-body="false"
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
            <el-col :span="4">
              <el-form-item label="楼层选择">
                <el-select
                  placeholder="楼层选择"
                  filterable
                  remote
                  reserve-keyword
                  :popper-append-to-body="false"
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
            <el-col :span="4">
              <el-form-item label="人员类型">
                <el-select
                  placeholder="人员类型"
                  filterable
                  remote
                  reserve-keyword
                  :popper-append-to-body="false"
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
              <el-row>
                <el-col :span="3">
                  <div
                    :style="{ color: isJk ? 'rgb(30, 150, 224)' : '' }"
                    class="porItem"
                    @click="isJk = !isJk"
                  >
                    监控
                  </div></el-col
                >
                <el-col :span="3">
                  <div
                    :style="{ color: isDj ? 'rgb(30, 150, 224)' : '' }"
                    class="porItem"
                    @click="isDj = !isDj"
                  >
                    灯具
                  </div></el-col
                >
                <el-col :span="4">
                  <div
                    :style="{ color: isWl ? 'rgb(30, 150, 224)' : '' }"
                    class="porItem"
                    @click="isWl = !isWl"
                  >
                    电子围栏
                  </div></el-col
                >
                <el-col :span="5">
                  <div
                    :style="{ color: isZf ? 'rgb(30, 150, 224)' : '' }"
                    class="porItem"
                    @click="handleZf"
                  >
                    重大风险区域
                  </div></el-col
                >
                <el-col :span="4">
                  <div
                    :style="{ color: isJj ? 'rgb(30, 150, 224)' : '' }"
                    class="porItem"
                    @click="isJj = !isJj"
                  >
                    聚集报警
                  </div></el-col
                >
                <el-col :span="4">
                  <div
                    :style="{ color: isLy ? 'rgb(30, 150, 224)' : '' }"
                    class="porItem"
                    @click="isLy = !isLy"
                  >
                    楼宇
                  </div></el-col
                >
              </el-row>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div> -->
    <div class="dialog-box">
      <!-- 左上 -->
      <div
        :style="{ left: isMovedOutTf ? '0' : '-50vw' }"
        class="left-top-dialog"
      >
        <img
          @click="toggleDialog1"
          class=""
          src="../assets/images/展开.png"
          alt=""
        />

        <div class="content">
          <h5>人员类别统计</h5>
          <div class="type-box">
            <!-- <div class="type-item">
              <span class="title">承包商</span>
              <div class="num">
                {{
                  this.tableDataTl && this.tableDataTl.personTypeStatistics
                    ? this.tableDataTl.personTypeStatistics.find(
                        (i) => i.personType == "contractor"
                      ).count
                    : 0
                }}人
              </div>
            </div> -->
            <div class="type-item">
              <span class="title">正式员工</span>
              <div class="num">
                {{
                  this.tableDataTl && this.tableDataTl.personTypeStatistics
                    ? this.tableDataTl.personTypeStatistics.find(
                        (i) => i.personType == "staff"
                      ).count
                    : 0
                }}人
              </div>
            </div>
            <div class="type-item">
              <span class="title">临时作业</span>
              <div class="num">
                {{
                  this.tableDataTl && this.tableDataTl.personTypeStatistics
                    ? this.tableDataTl.personTypeStatistics.find(
                        (i) => i.personType == "visitor"
                      ).count
                    : 0
                }}人
              </div>
            </div>
          </div>

          <!-- <div class="echarts1"></div> -->
          <div class="table-wrapper">
            <el-table height="100%" :data="tableDataTl.data" class="dark-table">
              <el-table-column
                label="序号"
                width="60"
                type="index"
              ></el-table-column>
              <el-table-column
                width="50"
                prop="personTypeName"
                label="类型"
              ></el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="realName"
                label="姓名"
              ></el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="phone"
                label="联系方式"
              ></el-table-column>
            </el-table>
          </div>
        </div>
      </div>

      <div
        @click="toggleDialog1"
        v-show="!isMovedOutTf"
        class="left-top-dialog-btn"
      >
        <div class="title">人员统计类别</div>
        <img class="" src="../assets/images/展开.png" alt="" />
      </div>
      <!-- 右上 -->
      <div
        :style="{ right: isMovedOutTr ? '0' : '-50vw' }"
        class="right-top-dialog"
      >
        <img
          @click="toggleDialog2"
          class=""
          src="../assets/images/展开.png"
          alt=""
        />

        <div class="content">
          <h5>物料统计</h5>
          <div class="table-wrapper">
            <el-table
              :header-cell-style="{ 'text-align': 'center' }"
              :cell-style="{ 'text-align': 'center' }"
              height="100%"
              :data="tableDataTr"
              class="dark-table"
            >
              <el-table-column
                label="序号"
                width="60"
                type="index"
              ></el-table-column>
              <el-table-column
                prop="areaName"
                label="物料区名称"
              ></el-table-column>
              <el-table-column
                prop="materialType"
                label="物料类型"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                width="50"
                prop="stockIn"
                label="入库"
              ></el-table-column>
              <el-table-column
                width="50"
                prop="stockOut"
                label="出库"
              ></el-table-column>
              <el-table-column
                width="50"
                prop="stock"
                label="存库"
              ></el-table-column>
              <el-table-column width="80" prop="cz" label="详情">
                <template #default="{ row }">
                  <el-button type="text" size="mini">查看 </el-button></template
                ></el-table-column
              >
            </el-table>
          </div>
        </div>
      </div>
      <div
        @click="toggleDialog2"
        v-show="!isMovedOutTr"
        class="right-top-dialog-btn"
      >
        <img class="" src="../assets/images/展开.png" alt="" />
        <div class="title">物料统计</div>
      </div>
      <!-- 左下 -->
      <div
        :style="{ left: isMovedOutBf ? '0' : '-50vw' }"
        class="left-bottom-dialog"
      >
        <img
          @click="toggleDialog3"
          class=""
          src="../assets/images/展开.png"
          alt=""
        />

        <div class="content">
          <h5>车辆统计</h5>
          <!-- <div class="type-box">
            <div class="type-item">
              <span class="title">内部</span>
              <div class="num">
                {{
                  this.tableDataBl && this.tableDataBl.categoryStatistics
                    ? this.tableDataBl.categoryStatistics.find(
                        (i) => i.type == "internal"
                      ).count
                    : 0
                }}辆
              </div>
            </div>
            <div class="type-item">
              <span class="title">访客</span>
              <div class="num">
                {{
                  this.tableDataBl && this.tableDataBl.categoryStatistics
                    ? this.tableDataBl.categoryStatistics.find(
                        (i) => i.type == "visitor"
                      ).count
                    : 0
                }}辆
              </div>
            </div>
          </div> -->
          <div class="table-wrapper">
            <el-table height="100%" :data="tableDataBl.data" class="dark-table">
              <el-table-column
                label="序号"
                width="60"
                type="index"
              ></el-table-column>
              <el-table-column
                prop="personTypeName"
                label="类型"
              ></el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="realName"
                label="姓名"
              ></el-table-column>
              <el-table-column
                show-overflow-tooltip
                prop="phone"
                label="联系方式"
              ></el-table-column>
            </el-table>
          </div>
        </div>
      </div>

      <div
        @click="toggleDialog3"
        v-show="!isMovedOutBf"
        class="left-bottom-dialog-btn"
      >
        <div class="title">车辆统计</div>
        <img class="" src="../assets/images/展开.png" alt="" />
      </div>

      <!-- 右下 -->
      <div
        :style="{ right: isMovedOutBr ? '0' : '-50vw' }"
        class="right-bottom-dialog"
      >
        <img
          @click="toggleDialog4"
          class=""
          src="../assets/images/展开.png"
          alt=""
        />

        <div class="content">
          <h5>区域统计</h5>
          <div class="table-wrapper">
            <el-table
              :header-cell-style="{ 'text-align': 'center' }"
              :cell-style="{ 'text-align': 'center' }"
              height="100%"
              :data="tableDataTr"
              class="dark-table"
            >
              <el-table-column
                label="序号"
                width="60"
                type="index"
              ></el-table-column>
              <el-table-column
                prop="areaName"
                label="物料区名称"
              ></el-table-column>
              <el-table-column
                prop="materialType"
                label="物料类型"
                show-overflow-tooltip
              ></el-table-column>
              <el-table-column
                width="50"
                prop="stockIn"
                label="入库"
              ></el-table-column>
              <el-table-column
                width="50"
                prop="stockOut"
                label="出库"
              ></el-table-column>
              <el-table-column
                width="50"
                prop="stock"
                label="存库"
              ></el-table-column>
              <el-table-column width="80" prop="cz" label="详情">
                <template #default="{ row }">
                  <el-button type="text" size="mini">查看 </el-button></template
                ></el-table-column
              >
            </el-table>
          </div>
        </div>
      </div>
      <div
        @click="toggleDialog4"
        v-show="!isMovedOutBr"
        class="right-bottom-dialog-btn"
      >
        <img class="" src="../assets/images/展开.png" alt="" />
        <div class="title">区域统计</div>
      </div>
    </div>
  </div>
</template>

<script>
import PieChart from "./dashboard/PieChart";
import dayjs from "dayjs";
import * as echarts from "echarts";
import TimeLine from "@/components/TimeLine/timeline-canvas.vue";
import MultiplierSelector from "@/components/Preset/index";
// import { resData as animatedLineData } from "../codeApplication/data";
import { positionHistoryPositionFindPersonHistoryList } from "@/api/lanya_transfer";
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
  components: {
    PieChart,
  },
  data() {
    const start = new Date();
    start.setHours(0, 0, 0, 0); // 当天 00:00:00
    const end = new Date();
    return {
      isMovedOutTf: false,
      isMovedOutTr: false,
      isMovedOutBf: false,
      isMovedOutBr: false,
      isJk: false,
      isDj: false,
      isWl: false,
      isZf: false,
      isJj: false,
      isLy: false,
      // 响应式数据
      queryParams: {},
      selectData: [],
      tableDataTl: [],
      tableDataTr: [],
      tableDataBr: [],
      tableDataBl: [],
      page: {
        pageSize: 10,
        pageNum: 1,
        total: 0,
      },
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
      pieChartData: [
        { name: "承包商", value: 1 },
        { name: "正式员工", value: 12 },
        { name: "临时作业", value: 2 },
      ],
      pieChartInstance: null,

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
              console.log(data);
              that.tableDataTl = data;
            },
          ],
          [
            () => data.msgType === "currentVehicleLocation", //车辆
            () => {
              that.tableDataBl = data;
              console.log(data);
            },
          ],
          [
            () => data.msgType === "materialLog", //物料
            () => {
              that.tableDataTr = data.data;
              console.log(data);
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

<style scoped lang="scss">
.dialog-box {
  .left-top-dialog {
    position: absolute;
    top: 10%;
    left: 0;
    width: 25vw;
    height: 35vh;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    display: flex;
    font-size: 1rem;
    /* 关键：添加过渡动画，让 left 改变时平滑移动 */
    transition: left 0.5s ease-in-out;
    .content {
      text-align: center;
      width: 100%;
      .type-box {
        display: flex;
        justify-content: center;
        align-items: center;
        .type-item {
          margin: 0px 1% 0 1%;
          .title {
            font-size: 13px;
          }
          .num {
            margin-top: 10px;
            width: 3vw;
            height: 3vh;
            border: 1px solid white;
            border-radius: 10px;
          }
        }
      }
      .echarts1 {
        width: 90%; // 或固定像素，比如 200px
        height: 20vh; // 必须有明确高度，否则图表无法显示
        margin-top: 10px;
      }
      .table-wrapper {
        width: 95%;
        height: 20vh; // 固定高度，确保表格可以滚动
        margin-top: 10px;
        overflow: hidden;
      }
    }

    img {
      width: 10px;
      height: 50px;
      position: absolute;
      right: 0;
      top: 45%;
    }
  }
  .left-top-dialog-btn {
    position: absolute;
    top: 18vh;
    display: flex;
    justify-content: center;
    align-items: center;
    .title {
      width: 50px;
      // height: 60px;
      padding: 15px 13px;
      border-radius: 28px;
      border: 1px solid white;
      background-color: rgb(68, 66, 66);
      color: white;
      // font-size: 18px;
      // font-weight: 700;
    }
    img {
      width: 10px;
      height: 50px;
    }
  }
  .right-top-dialog {
    position: absolute;
    top: 10%;
    right: 0;
    width: 30vw;
    height: 35vh;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    display: flex;
    font-size: 18px;
    /* 关键：添加过渡动画，让 left 改变时平滑移动 */
    transition: left 0.5s ease-in-out;
    img {
      width: 10px;
      height: 50px;
      position: absolute;
      left: 0;
      top: 45%;
    }
    .content {
      text-align: center;
      width: 98%;
      padding-left: 5%;
      .table-wrapper {
        width: 100%;
        height: 25vh; // 固定高度，确保表格可以滚动
        margin-top: 10px;
        overflow: hidden;
      }
    }
  }
  .right-top-dialog-btn {
    position: absolute;
    right: 0;
    top: 18%;
    display: flex;
    justify-content: center;
    align-items: center;
    .title {
      width: 45px;
      // height: 60px;
      padding: 15px 13px;
      border-radius: 28px;
      border: 1px solid white;
      background-color: rgb(68, 66, 66);
      color: white;
      // font-size: 18px;
      // font-weight: 700;
    }
    img {
      width: 10px;
      height: 50px;
    }
  }

  .left-bottom-dialog {
    position: absolute;
    top: 55%;
    left: 0;
    width: 25vw;
    height: 35vh;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    display: flex;
    font-size: 1rem;
    /* 关键：添加过渡动画，让 left 改变时平滑移动 */
    transition: left 0.5s ease-in-out;
    .content {
      text-align: center;
      width: 100%;
      .type-box {
        display: flex;
        justify-content: center;
        align-items: center;
        .type-item {
          margin: 0px 1% 0 1%;
          .title {
            font-size: 13px;
          }
          .num {
            margin-top: 10px;
            width: 3vw;
            height: 3vh;
            border: 1px solid white;
            border-radius: 10px;
          }
        }
      }
      .table-wrapper {
        width: 95%;
        height: 20vh; // 固定高度，确保表格可以滚动
        margin-top: 10px;
        overflow: hidden;
      }
    }

    img {
      width: 10px;
      height: 50px;
      position: absolute;
      right: 0;
      top: 45%;
    }
  }
  .left-bottom-dialog-btn {
    position: absolute;
    top: 58%;
    display: flex;
    justify-content: center;
    align-items: center;
    .title {
      width: 45px;
      // height: 60px;
      padding: 15px 13px;
      border-radius: 28px;
      border: 1px solid white;
      background-color: rgb(68, 66, 66);
      color: white;
      // font-size: 18px;
      // font-weight: 700;
    }
    img {
      width: 10px;
      height: 50px;
    }
  }

  .right-bottom-dialog {
    position: absolute;
    top: 55%;
    right: 0;
    width: 30vw;
    height: 35vh;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    display: flex;
    font-size: 18px;
    /* 关键：添加过渡动画，让 left 改变时平滑移动 */
    transition: left 0.5s ease-in-out;
    img {
      width: 10px;
      height: 50px;
      position: absolute;
      left: 0;
      top: 45%;
    }
    .content {
      text-align: center;
      width: 98%;
      padding-left: 5%;
      .table-wrapper {
        width: 100%;
        height: 25vh; // 固定高度，确保表格可以滚动
        margin-top: 10px;
        overflow: hidden;
      }
    }
  }
  .right-bottom-dialog-btn {
    position: absolute;
    right: 0;
    top: 58%;
    display: flex;
    justify-content: center;
    align-items: center;
    .title {
      width: 45px;
      // height: 60px;
      padding: 15px 13px;
      border-radius: 28px;
      border: 1px solid white;
      background-color: rgb(68, 66, 66);
      color: rgb(255, 255, 255);
      // font-size: 18px;
      // font-weight: 700;
    }
    img {
      width: 10px;
      height: 50px;
    }
  }

  // button {
  //   margin-top: 20px;
  //   padding: 10px 20px;
  //   font-size: 16px;
  //   cursor: pointer;
  // }
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

.width25 {
  width: 25%;
}

.width33 {
  width: 30%;
}

.controls {
  position: absolute;
  top: 20px;
  z-index: 1000;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  width: 80vw;
  height: 50px;

  .controls-left {
    flex: 2;
    background-color: black;
    border-radius: 30px;
    height: 50px;
    .form {
      width: 100%;
      margin: 9px 0 0 9px;
    }
    ::v-deep.searchSelect .el-input__inner {
      width: 100%;
      border-radius: 30px;
      // background-color: #5ba5ef;
      background-color: black;
    }
    ::v-deep .el-form-item__content .el-date-editor,
    .el-range-editor,
    .el-input__inner,
    .el-date-editor--datetimerange,
    .el-range-editor--medium {
      width: 100%;
    }
  }

  .controls-right {
    flex: 5;
    background-color: black;
    border-radius: 30px;
    height: 50px;
    .form {
      width: 100%;
      margin: 9px 0 0 9px;
    }
    ::v-deep.searchSelect .el-input__inner {
      width: 100%;
      border-radius: 30px;
      // background-color: #5ba5ef;
      background-color: black;
    }
    ::v-deep .el-form-item__content {
      width: 50%;
    }
    .porItem {
      font-size: 11px;
      vertical-align: middle;
      color: #fff;
      text-align: left;
      // padding-left: 20px;
      // height: 40px;
      line-height: 40px;
      // border: 1px solid #409eff;
      cursor: pointer;
      // border-radius: 10px;
      i {
        vertical-align: middle; /* 图标垂直居中 */
        margin-right: 5px; /* 图标右侧间距 */
      }
    }
  }
}

::v-deep .el-form-item__label {
  color: white;
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
::v-deep .el-input__inner {
  color: white;
}

// 深色表格样式
::v-deep .dark-table {
  background-color: transparent !important;
  color: #fff !important;

  // 表格主体
  .el-table__body-wrapper {
    background-color: rgba(0, 0, 0, 0.3) !important;
    &::-webkit-scrollbar {
      width: 6px;
      height: 6px;
    }
    &::-webkit-scrollbar-thumb {
      background-color: rgba(255, 255, 255, 0.3);
      border-radius: 3px;
      &:hover {
        background-color: rgba(255, 255, 255, 0.5);
      }
    }
    &::-webkit-scrollbar-track {
      background-color: rgba(0, 0, 0, 0.2);
    }
  }

  // 表头
  .el-table__header-wrapper {
    background-color: rgba(0, 0, 0, 0.5) !important;

    th {
      background-color: rgba(0, 0, 0, 0.5) !important;
      border-bottom: 1px solid rgba(255, 255, 255, 0.2) !important;
      color: #fff !important;

      .cell {
        color: #fff !important;
        font-weight: 500;
      }
    }
  }

  // 表格行
  .el-table__body {
    tr {
      background-color: transparent !important;

      td {
        background-color: transparent !important;
        border-bottom: 1px solid rgba(255, 255, 255, 0.1) !important;
        color: #fff !important;

        .cell {
          color: #fff !important;
        }
      }

      &:hover {
        td {
          background-color: rgba(255, 255, 255, 0.1) !important;
        }
      }
    }

    // 斑马纹样式（如果需要）
    tr.el-table__row--striped {
      td {
        background-color: rgba(255, 255, 255, 0.05) !important;
      }

      &:hover {
        td {
          background-color: rgba(255, 255, 255, 0.15) !important;
        }
      }
    }
  }

  // 边框
  &::before {
    background-color: rgba(255, 255, 255, 0.1) !important;
  }

  // 空数据提示
  .el-table__empty-block {
    background-color: transparent !important;
    color: #fff !important;

    .el-table__empty-text {
      color: rgba(255, 255, 255, 0.6) !important;
    }
  }
}
</style>
