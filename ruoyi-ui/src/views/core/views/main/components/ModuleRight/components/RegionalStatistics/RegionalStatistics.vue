<template>
  <base-frame class="RegionalStatistics" v-bind="$props">
    <div class="space-box"></div>
    <BaseTable
      :key="keyIndex"
      :head="head"
      :list="list"
      :swiper-opt="swiperOpt"
      line-bg-visible
      list-height="210px" />
  </base-frame>
</template>

<script>
import BaseFrame from "@/views/common/components/BaseFrame/BaseFrame.vue";
import MixinModuleParams from "@/views/common/mixins/MixinModuleParams";
import BaseTable from "@/views/common/components/BaseTable/BaseTable.vue";
import MixinModuleMethods from "@/views/common/mixins/MixinModuleMethods";
import { uuid } from "@vci/helper/src/string";

export default {
  name: "RegionalStatistics",
  components: { BaseTable, BaseFrame },
  mixins: [MixinModuleParams, MixinModuleMethods],
  data() {
    return {
      head: [
        {
          id: uuid(),
          value: "序号",
          width: "80px",
        },
        {
          id: uuid(),
          value: "区域名称",
          width: "180px",
        },
        {
          id: uuid(),
          value: "人员数量",
          width: "100px",
        },
        {
          id: uuid(),
          value: "车辆数量",
          width: "100px",
        },
      ],
      list: [],
      keyIndex: 0,
      swiperOpt: {
        direction: "vertical",
        slidesPerView: 7,
        slidesPerGroup: 7,
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

        if (data.msgType == "areaLog") {
          that.list = data.data.map((i, index) => {
            return [
              {
                width: "80px",
                value: index + 1,
                id: uuid(),
              },
              {
                id: uuid(),
                value: i.areaName,
                color: "#00d9ff",
                width: "200px",
              },
              {
                id: uuid(),
                value: i.personCount,
                color: "#ffa000",
                width: "80px",
              },
              {
                id: uuid(),
                value: i.vehicleCount,
                color: "#ffa000",
                width: "80px",
              },
            ];
          });
        }
      };
      this.ws.onclose = function () {
        that.text_content = that.text_content + "已经关闭连接!" + "\n";
      };
    },
    simulationData() {
      this.list = Array.from({ length: 12 }).map(() => [
        {
          id: uuid(),
          value: ["综合楼一楼楼梯间", "实验室一楼", "停车场二层", "办公楼一层"][
            Math.round(Math.random() * 3)
          ],
          width: "220px",
        },
        {
          id: uuid(),
          value: Math.round(Math.random() * 100),
          color: "#00d9ff",
          width: "220px",
        },
        {
          id: uuid(),
          value: Math.round(Math.random() * 100),
          color: "#ffa000",
          width: "220px",
        },
      ]);
    },
    getRealData() {},
  },
};
</script>

<style lang="scss" scoped>
.RegionalStatistics {
  height: 30vh;

  .space-box {
    height: 24px;
  }
  :deep(.BaseTable) {
    .table-item {
      &:nth-child(1) {
        text-align: left;
      }
      text-align: center;
    }
    .table-head {
      margin: 0 auto 4px;
    }
  }
}
</style>
