<template>
  <base-frame class="MaterialStatistics" v-bind="$props">
    <div class="table-head vci-flex-rmb vci-ta-c font-oppo-b">
      <div v-for="item in head" :key="item" class="table-item">
        {{ item }}
      </div>
    </div>
    <list-swiper :list="list" :swiper-opt="swiperOpt">
      <div class="swiper-wrapper vci-ta-c font-oppo-m">
        <div v-for="(slide, index) in list" :key="index" class="swiper-slide">
          <div class="in-ctr vci-flex-rmb">
            <div class="table-item">
              {{ index + 1 }}
            </div>
            <div class="table-item">
              {{ slide["areaName"] }}
            </div>
            <div class="table-item">
              {{ slide["materialType"] }}
            </div>
            <div class="table-item">
              {{ slide["stockIn"] }}
            </div>
            <div class="table-item">
              {{ slide["stockOut"] }}
            </div>
            <div class="table-item">
              {{ slide["stock"] }}
            </div>
          </div>
        </div>
      </div>
    </list-swiper>
    <!-- <VciChart :chart="chart" :chart-data="chartData" /> -->
  </base-frame>
</template>

<script>
import BaseFrame from "@/views/common/components/BaseFrame/BaseFrame.vue";
import MixinModuleParams from "@/views/common/mixins/MixinModuleParams";
import ListSwiper from "@/views/common/components/ListSwiper.vue";
import MixinModuleMethods from "@/views/common/mixins/MixinModuleMethods";
import { uuid } from "@vci/helper/src/string";
// import VciChart from "@vci/vci/vue/v2/components/chart/VciChart.vue";
import MaterialStatistics from "./js/MaterialStatistics";
import moment from "moment";

export default {
  name: "MaterialStatistics",
  components: { ListSwiper, BaseFrame },
  mixins: [MixinModuleParams, MixinModuleMethods],
  data() {
    return {
      head: ["序号", "物料区名称", "物料类型", "入库", "出库", "存库"],
      list: [],
      chart: MaterialStatistics,
      chartData: {},
      swiperOpt: {
        direction: "vertical",
        slidesPerView: 5,
        slidesPerGroup: 5,
        autoplay: { delay: 5e3, disableOnInteraction: false },
      },
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

        if (data.msgType == "materialLog") {
          that.list = data.data;
        }
      };
      this.ws.onclose = function () {
        that.text_content = that.text_content + "已经关闭连接!" + "\n";
      };
    },
    simulationData() {
      const axis = Array.from({ length: 6 })
        .map((_, i) => moment().subtract(i, "day").format("YYYY-MM-DD"))
        .reverse();
      this.chartData = {
        series: ["入库统计", "出库统计"].map((_, i) => ({
          name: _,
          color: ["#00d9ff", "#0096ff"][i],
          data: axis.map((i) => ({
            name: i,
            value: Math.round(Math.random() * 1e2),
          })),
        })),
        axis,
      };
    },
    getRealData() {},
  },
};
</script>

<style lang="scss" scoped>
.MaterialStatistics {
  height: 43vh;
  margin-bottom: 2vh;
  :deep(.cont-frame) {
    padding-top: 20px;
    padding-left: 30px;
  }
  .table-item {
    width: 100%;
    height: 30px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .table-head {
    width: 100%;
    color: #c7e7fd;
    line-height: 30px;
    .table-item {
      background-color: rgba(#74a7d1, 0.26);
    }
  }
  .ListSwiper {
    width: 100%;
    height: 30vh;
    margin-top: 3px;
    margin-left: 0;
    .table-item {
      height: 27px;
      line-height: 27px;
      background-color: rgba(#74a7d1, 0.14);
      color: #9bb9cd;

      &:last-child {
        color: #00deff;
      }
    }
    .swiper-slide,
    .swiper-slide-active {
      height: 30px !important;
    }
    .swiper-wrapper {
      // overflow: hidden;
      // height: 30vh;
    }
  }

  .VciChart {
    height: 270px;
  }
}
</style>
