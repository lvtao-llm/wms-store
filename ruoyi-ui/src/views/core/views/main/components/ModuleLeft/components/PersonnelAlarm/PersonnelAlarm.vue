<template>
  <base-frame class="PersonnelAlarm" v-bind="$props">
    <div class="space-box"></div>
    <BaseBarList :list="list" :swiper-opt="swiperOpt" />
  </base-frame>
</template>

<script>
import BaseFrame from "@/views/common/components/BaseFrame/BaseFrame.vue";
import MixinModuleParams from "@/views/common/mixins/MixinModuleParams";
import BaseBarList from "@/views/common/components/BaseBarList.vue";
import MixinModuleMethods from "@/views/common/mixins/MixinModuleMethods";
import { uuid } from "@vci/helper/src/string";

export default {
  name: "PersonnelAlarm",
  components: { BaseBarList, BaseFrame },
  mixins: [MixinModuleParams, MixinModuleMethods],
  data() {
    return {
      list: [],
      swiperOpt: {
        direction: "vertical",
        slidesPerView: 5,
        autoplay: { delay: 5e3 },
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
        if (data.msgType == "personAlarm") {
          that.list = data.rules;
          if (that.list.length) {
            that.list.forEach((i) => {
              i.id = uuid();
              i.name = i.alarmRuleName;
              i.value = i.count;
              i.percent = i.percentage;
            });
            that.list = that.list.map((i) => ({ ...i, left: 0 }));
            const sum = eval(that.list.map((i) => i.value).join("+"));
            that.list.forEach(
              (i) => (i.percent = Number(((i.value / sum) * 100).toFixed(2)))
            );
            for (let i = 0; i < that.list.length; i++) {
              const item = that.list[i];
              let mark = i - 1;
              while (mark >= 0) {
                item["left"] += that.list[mark]["percent"];
                mark--;
              }
            }
          }
        }
      };
      this.ws.onclose = function () {
        that.text_content = that.text_content + "已经关闭连接!" + "\n";
      };
    },
    simulationData() {
      this.list = [
        {
          id: uuid(),
          name: "未授权进入",
          value: Math.round(Math.random() * 100),
          percent: 0,
        },
        {
          id: uuid(),
          name: "长时间停留",
          value: Math.round(Math.random() * 100),
          percent: 0,
        },
        {
          id: uuid(),
          name: "超时工作",
          value: Math.round(Math.random() * 100),
          percent: 0,
        },
        {
          id: uuid(),
          name: "多人聚集",
          value: Math.round(Math.random() * 100),
          percent: 0,
        },
        {
          id: uuid(),
          name: "行动轨迹异常",
          value: Math.round(Math.random() * 100),
          percent: 0,
        },
      ].map((i) => ({ ...i, left: 0 }));
      const sum = eval(this.list.map((i) => i.value).join("+"));
      this.list.forEach(
        (i) => (i.percent = Number(((i.value / sum) * 100).toFixed(2)))
      );
      for (let i = 0; i < this.list.length; i++) {
        const item = this.list[i];
        let mark = i - 1;
        while (mark >= 0) {
          item["left"] += this.list[mark]["percent"];
          mark--;
        }
      }
    },
    getRealData() {},
  },
};
</script>

<style lang="scss" scoped>
.PersonnelAlarm {
  height: 252px;
  .space-box {
    height: 14px;
  }
  .BaseBarList {
    height: 170px;
  }
}
</style>
