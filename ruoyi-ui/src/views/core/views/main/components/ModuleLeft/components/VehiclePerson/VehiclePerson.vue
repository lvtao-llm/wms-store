<template>
  <base-frame class="VehiclePerson" v-bind="$props">
    <div class="person-frame vci-flex-rml">
      <div class="chart-frame vci-pst-rlv">
        <VciChart :chart="chart" :chart-data="personData" class="vci-pst-cm1" />
        <div class="bg-circle vci-pst-cm vci-bg-fill"></div>
        <div class="name vci-pst-cm font-oppo-r">人员统计</div>
      </div>
      <div class="legend-frame font-oppo-r vci-fs-16 vci-flex-rmb">
        <div
          v-for="(legend, index) in personData"
          :key="index"
          class="the-legend">
          <div class="name-percent vci-flex-rmb">
            <div class="icon-name vci-flex-rml">
              <div :style="{ backgroundColor: legend['color'] }" class="icon" />
              <div class="name">
                {{ legend["personTypeName"] }}
              </div>
            </div>
            <div class="percent font-din vci-fs-22">{{ legend["ratio"] }}%</div>
          </div>
          <div class="value-unit vci-flex-rml vci-bg-fill">
            <div
              :style="{ backgroundColor: legend['color'] }"
              class="icon"></div>
            <div
              :style="{ color: legend['color'] }"
              class="value vci-fs-22 font-din">
              {{ legend["count"] }}
            </div>
            <div class="unit">人</div>
          </div>
        </div>
      </div>
    </div>
    <div class="car-frame vci-flex-rml">
      <div class="chart-frame vci-pst-rlv">
        <VciChart :chart="chart" :chart-data="carData" class="vci-pst-cm1" />
        <div class="bg-circle vci-pst-cm vci-bg-fill"></div>
        <div class="name vci-pst-cm font-oppo-r">车辆统计</div>
      </div>
      <div class="legend-frame font-oppo-r vci-fs-16 vci-flex-rmb">
        <div class="the-legend">
          <!-- <div class="name-percent vci-flex-rmb">
            <div class="icon-name vci-flex-rml">
              <div :style="{ backgroundColor: legend['color'] }" class="icon" />
              <div class="name">
                {{ legend["label"] }}
              </div>
            </div>
            <div class="percent font-din vci-fs-22">{{ legend["ratio"] }}%</div>
          </div> -->
          <div class="value-unit vci-flex-rml vci-bg-fill">
            <div :style="{ backgroundColor: '#0096ff' }" class="icon"></div>
            <div :style="{ color: '#0096ff' }" class="value vci-fs-22 font-din">
              {{ carNum }}
            </div>
            <div class="unit">辆</div>
          </div>
        </div>
      </div>
    </div>
  </base-frame>
</template>

<script>
import MixinModuleParams from "@/views/common/mixins/MixinModuleParams";
import BaseFrame from "@/views/common/components/BaseFrame/BaseFrame.vue";
import MixinModuleMethods from "@/views/common/mixins/MixinModuleMethods";
import VciChart from "@vci/vci/vue/v2/components/chart/VciChart.vue";
import VehiclePerson from "./js/VehiclePerson";
import { uuid } from "@vci/helper/src/string";

export default {
  name: "VehiclePerson",
  components: { VciChart, BaseFrame },
  mixins: [MixinModuleParams, MixinModuleMethods],
  data() {
    return {
      chart: VehiclePerson,
      carData: [],
      carNum: 0,
      personData: [],
      personColor: ["#0096ff", "#00d9ff", "#ffa000"],
      carColor: ["#0096ff", "#00d9ff"],
    };
  },
  created() {
    this.onLoad();
  },
  methods: {
    onLoad() {
      const wsuri =
        "ws://112.98.110.101:10030/system/lanya-transfer/ws/" + uuid();
      this.ws = new WebSocket(wsuri);
      const that = this;
      this.ws.onopen = function () {
        that.text_content = that.text_content + "已经打开连接!" + "\n";
      };
      this.ws.onmessage = function (event) {
        that.text_content = event.data + "\n";
        const data = JSON.parse(event.data);
        const map = [
          [
            () => data.msgType === "currentPersonLocation", //人员
            () => {
              that.personData = data.personTypeStatistics.filter(
                (i) => i.personType !== "contractor"
              );
              if (that.personData.length) {
                that.personData.forEach((i) => {
                  if (i.personType == "staff") {
                    i.personTypeName = "正式员工";
                  } else if (i.personType == "visitor") {
                    i.personTypeName = "临时作业";
                  }
                  i.color =
                    that.personColor[
                      Math.floor(Math.random() * that.personColor.length)
                    ];
                });
              }
            },
          ],
          [
            () => data.msgType === "currentVehicleLocation", //车辆
            () => {
              that.carData = data.categoryStatistics;
              if (that.personData.length) {
                this.carNum = 0;
                that.carData.forEach((i) => {
                  this.carNum += i.count;
                });
              }
            },
          ],
        ];
        const target = map.find((m) => m[0]());
        if (target) target[1]();
      };
      this.ws.onclose = function () {
        that.text_content = that.text_content + "已经关闭连接!" + "\n";
      };
    },
    simulationData() {
      this.carData = [
        {
          id: uuid(),
          personTypeName: "货车数量",
          value: Math.round(Math.random() * 100),
          percent: 0,
          unit: "辆",
          color: "#0096ff",
        },
        {
          id: uuid(),
          personTypeName: "社会车辆",
          value: Math.round(Math.random() * 100),
          percent: 0,
          unit: "辆",
          color: "#00d9ff",
        },
        {
          id: uuid(),
          personTypeName: "内部车辆",
          value: Math.round(Math.random() * 100),
          percent: 0,
          unit: "辆",
          color: "#ffa000",
        },
      ];
      this.personData = [
        {
          id: uuid(),
          name: "作业人员",
          value: Math.round(Math.random() * 100),
          percent: 0,
          unit: "人",
          color: "#0096ff",
        },
        {
          id: uuid(),
          name: "内部人员",
          value: Math.round(Math.random() * 100),
          percent: 0,
          unit: "人",
          color: "#00d9ff",
        },
      ];
      // const carSum = eval(this.carData.map((i) => i.value).join("+"));
      // const personSum = eval(this.personData.map((i) => i.value).join("+"));
      // this.carData.forEach(
      //   (it) => (it.percent = ((it.value / carSum) * 100).toFixed(2))
      // );
      // this.personData.forEach(
      //   (it) => (it.percent = ((it.value / personSum) * 100).toFixed(2))
      // );
    },
    getRealData() {},
  },
};
</script>

<style lang="scss" scoped>
.VehiclePerson {
  height: 25vh;
  font-size: 1rem !important;
  padding-right: 10px;

  .chart-frame {
    width: 2vw;
    height: 3vh;
    position: relative;
    .vci-pst-cm1 {
      position: absolute;
      width: 8vw;
      height: 8vh;
      top: -2.2vh;
      left: -3.2vw;
    }
    .bg-circle {
      width: 100%;
      height: 100%;
      background-image: url("./img/bg.circle.png");
      animation: rotate 24s infinite linear;
      @keyframes rotate {
        from {
          transform: translate(-50%, -50%) rotate(0deg);
        }
        to {
          transform: translate(-50%, -50%) rotate(360deg);
        }
      }
    }
    .name {
      width: 1.8vw;
      margin-right: 3vw;
      font-size: 0.9rem !important;
    }
  }
  .legend-frame {
    width: 50%;
    margin-left: 16px;
    font-size: 0.7rem !important;
    .the-legend {
      width: 100%;
      color: #c7e7fd;
      .name-percent {
        font-size: 0.7rem !important;
        .icon-name {
          font-size: 0.7rem !important;
          margin-left: 0.8vw;
          .icon {
            width: 5px;
            height: 5px;
            margin-right: 12px;
            border-radius: 50%;
          }
          .name {
          }
          .percent {
          }
        }
      }
      .value-unit {
        width: 7vw;
        height: 39px;
        margin-top: 4px;
        margin-left: 16px;
        font-size: 0.7rem !important;
        background-image: url("./img/bg.legend.1.png");
        .icon {
          width: 5px;
          height: 5px;
          margin-right: 12px;
          border-radius: 50%;
        }
        .value {
          margin-right: 10px;
        }
      }
    }
  }
  .car-frame {
    padding-top: 14px;
    padding-left: 30px;
    .the-legend {
      width: 100%;
      color: #c7e7fd;
      .name-percent {
        font-size: 0.7rem !important;
        .icon-name {
          font-size: 0.7rem !important;
          margin-left: 0.8vw;
          .icon {
            width: 5px;
            height: 5px;
            margin-right: 12px;
            border-radius: 50%;
          }
          .name {
          }
          .percent {
          }
        }
      }
      .value-unit {
        width: 23vw;
        height: 39px;
        margin-top: 4px;
        margin-left: 16px;
        font-size: 0.7rem !important;
        background-image: url("./img/bg.legend.1.png");
        .icon {
          width: 5px;
          height: 5px;
          margin-right: 12px;
          border-radius: 50%;
        }
        .value {
          margin-right: 10px;
        }
      }
    }
  }
  .person-frame {
    padding-left: 30px;
    padding-top: 12px;
    .legend-frame {
      .the-legend {
        width: 100%;
        .icon-name {
          margin-left: 0.8vw;
        }
        .value-unit {
          width: 11vw;
          background-image: url("./img/bg.legend.2.png");
        }
      }
    }
  }
}
</style>
