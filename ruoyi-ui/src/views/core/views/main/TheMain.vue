<template>
  <div class="TheMain">
    <!-- 头部模块 -->
    <TheHeader />
    <!-- 左边模块 -->
    <ModuleLeft />
    <!-- 右边模块 -->
    <ModuleRight />
    <!-- 实景风格/光影线条切换按钮 -->
    <RealLightSwitch />
    <!-- 设备类型图例 -->
    <DeviceLegend />
    <!-- 模型模式切换/编辑按钮 -->
    <ModelOperation />
    <!-- 历史数据 -->
    <!-- <HistoryTrajectory /> -->
    <!-- 装饰箭头 -->
    <!-- 装饰箭头 -->
    <effect-shadow-tracing
      class="decoration-arrow decoration-arrow-left vci-bg-fill vci-pst-m" />
    <effect-shadow-tracing
      class="decoration-arrow decoration-arrow-right vci-bg-fill vci-pst-m" />
    <!-- 边框装饰 -->
    <div class="decoration-border vci-pst-cm vci-bg-fill"></div>
    <!-- 遮罩 -->
    <div class="mark vci-pst-abs vci-bg-fill vci-w-100 vci-h-100" />
  </div>
</template>

<script>
import TheHeader from "@/views/core/views/main/components/TheHeader/TheHeader.vue";
import ModuleLeft from "@/views/core/views/main/components/ModuleLeft/ModuleLeft.vue";
import ModuleRight from "@/views/core/views/main/components/ModuleRight/ModuleRight.vue";
import RealLightSwitch from "@/views/core/views/main/components/RealLightSwitch.vue";
import DeviceLegend from "@/views/core/views/main/components/DeviceLegend.vue";
import ModelOperation from "@/views/core/views/main/components/ModelOperation/ModelOperation.vue";
// import HistoryTrajectory from "@/views/common/components/HistoryTrajectory/HistoryTrajectory.vue";
import EffectShadowTracing from "@vci/vci/vue/v2/components/effect/EffectShadowTracing.vue";
// import { vf } from "@vci/vci/helper/v-fetch";

export default {
  name: "TheMain",
  components: {
    EffectShadowTracing,
    // HistoryTrajectory,
    ModelOperation,
    DeviceLegend,
    RealLightSwitch,
    ModuleRight,
    ModuleLeft,
    TheHeader,
  },
  created() {},
  data() {
    return {};
  },
  methods: {
    exit() {
      if (this.ws) {
        this.ws.close();
        this.ws = null;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.TheMain {
  padding-top: 1vh;
  pointer-events: none;
  .ModuleLeft,
  .ModuleRight {
    pointer-events: auto;
  }
  .decoration-border {
    z-index: -1;
    // width: 3811px;
    // height: 1070px;
    width: 150vw;
    height: 108vh;
    background-image: url("./img/bg.png");
  }
  :deep(.decoration-arrow) {
    position: absolute;
    z-index: -1;
    width: 70px;
    height: 784px;
    .est {
      background-image: url("./img/decoration.img.png");
      &.est-amt {
        animation: amtEst 1.8s infinite both linear;
        @keyframes amtEst {
          from {
            opacity: 0;
            transform: translateX(0) scale(1);
          }
          50% {
            opacity: 0.5;
            transform: translateX(50%) scale(1.1);
          }
          to {
            opacity: 0;
            transform: translateX(100%) scale(1.2);
          }
        }
      }
    }
  }
  .decoration-arrow {
    &-left {
      left: 920px;
      transform: translateY(-50%) rotate(180deg);
    }
    &-right {
      right: 920px;
    }
  }
  .mark {
    z-index: -2;
    top: 0;
    left: 0;
    background-image: url("./img/bg.mark.png");
  }
}
</style>
