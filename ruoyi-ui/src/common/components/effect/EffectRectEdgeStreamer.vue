<template>
  <div
    :class="{'ers-debug': isDebug, 'ers-double': !isSingle}"
    :style="sylGlobal"
    class="EffectRectEdgeStreamer"
  >
    <div
      :style="{ height: `${toVw(height)}`}"
      class="ers-edge ers-top"
    >
      <div
        :style="[syl, { height: `${toVw(height)}` }]"
        class="ers-flow"
      >
        <i
          :style="sylErs"
          class="ers vci-bg-fill"
        ></i>
      </div>
    </div>
    <div
      :style="{ width: `${toVw(height)}`}"
      class="ers-edge ers-right"
    >
      <div
        :style="[syl, {'animation-delay': `${halfOfDuration}ms`}]"
        class="ers-flow"
      >
        <i
          :style="[sylErs, { 'transform-origin': `100% ${toVw(height / 2)}`, right: toVw(height / 2), bottom: toVw(-height / 2)}]"
          class="ers vci-bg-fill"
        ></i>
      </div>
    </div>
    <div
      :style="{ height: `${toVw(height)}`}"
      class="ers-edge ers-bottom"
    >
      <div
        :style="[syl, {'animation-delay': `${halfOfDuration * 2}ms`}]"
        class="ers-flow"
      >
        <i
          :style="sylErs"
          class="ers vci-bg-fill"
        ></i>
      </div>
    </div>
    <div
      :style="{ width: `${toVw(height)}`}"
      class="ers-edge ers-left"
    >
      <div
        :style="[syl, {'animation-delay': `${halfOfDuration * 3}ms`}]"
        class="ers-flow"
      >
        <i
          :style="[sylErs, { 'transform-origin': `100% ${toVw(height / 2)}`, right: toVw(height / 2), bottom: toVw(-height / 2)}]"
          class="ers vci-bg-fill"
        ></i>
      </div>
    </div>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/6/30 16:48
import { toVw } from "@vci/vci/other/adapt";

export default {
  name: "EffectRectEdgeStreamer",
  props: {
    duration: {
      type: [Number, String],
      default: 2000
    },
    isDebug: {
      type: Boolean,
      default: false
    },
    isSingle: {
      type: Boolean,
      default: false
    },
    width: {
      type: [Number, String],
      default: 100
    },
    height: {
      type: [Number, String],
      default: 20
    }
  },
  data() {
    return {};
  },
  computed: {
    sylGlobal() {
      return {
        // left: toVw(-this.height / 2),
        // top: toVw(-this.height / 2),
        // width: `calc(100% + ${toVw(this.height)})`,
        // height: `calc(100% + ${toVw(this.height)})`
      };
    },
    syl() {
      return {
        animationDuration: `${this.duration}ms`
      };
    },
    sylErs() {
      return {
        width: toVw(this.width),
        height: toVw(this.height)
      };
    },
    halfOfDuration() {
      return Number(this.duration) / (this.isSingle ? 4 : 2);
    }
  },
  methods: { toVw }
};
</script>

<style lang="scss" scoped>
$thickness: 12;
.EffectRectEdgeStreamer {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  .ers-edge {
    position: absolute;
    overflow: hidden;
    .ers-flow {
      width: 100%;
      height: 100%;
      //animation: none !important;
      .ers {
        position: absolute;
        display: block;
        width: 100%;
        height: 100%;
        background-image: linear-gradient(90deg, transparent, #fff);
      }
    }
  }
  .ers-top {
    left: 0;
    top: 0;
    width: 100%;
    height: $thickness + px;
    transform: translateY(-50%);
    .ers-flow {
      transform: translateX(-100%);
      animation: amtErsHorizontal infinite both linear;
      .ers {
        right: 0;
        top: 0;
      }
    }
  }
  .ers-right {
    right: 0;
    top: 0;
    width: $thickness + px;
    height: 100%;
    transform: translateX(50%);
    .ers-flow {
      transform: translateY(-100%);
      animation: amtErsVertical infinite both linear;
      .ers {
        right: $thickness / 2 + px;
        bottom: -$thickness / 2 + px;
        transform-origin: 100% $thickness / 2 + px;
        transform: rotateZ(90deg);
      }
    }
  }
  .ers-bottom {
    left: 0;
    bottom: 0;
    width: 100%;
    height: $thickness + px;
    transform: translateY(50%) rotateY(180deg);
    .ers-flow {
      transform: translateX(-100%);
      animation: amtErsHorizontal infinite both linear;
      .ers {
        left: 0;
        top: 0;
      }
    }
  }
  .ers-left {
    left: 0;
    top: 0;
    transform: translateX(-50%) rotateX(180deg);
    width: $thickness + px;
    height: 100%;
    .ers-flow {
      transform: translateY(-100%);
      animation: amtErsVertical infinite both linear;
      .ers {
        right: $thickness / 2 + px;
        bottom: -$thickness / 2 + px;
        transform-origin: 100% $thickness / 2 + px;
        transform: rotateZ(90deg);
      }
    }
  }
  &.ers-debug {
    .ers-edge {
      overflow: visible;
      .ers-flow {
        border: 3px solid #fff;
        .ers {
          border: 3px solid #1984ff;
        }
      }
    }
  }
  &.ers-double {
    .ers-top .ers-flow {
      animation-name: amtErsHorizontalDouble;
    }
    .ers-right .ers-flow {
      animation-name: amtErsVerticalDouble;
    }
    .ers-bottom .ers-flow {
      animation-name: amtErsHorizontalDouble;
    }
    .ers-left .ers-flow {
      animation-name: amtErsVerticalDouble;
    }
  }
}
@keyframes amtErsHorizontal {
  from {
    opacity: 1;
    transform: translateX(-100%);
  }
  50% {
    opacity: 1;
  }
  to {
    opacity: 1;
    transform: translateX(300%);
  }
}
@keyframes amtErsVertical {
  from {
    opacity: 1;
    transform: translateY(-100%);
  }
  50% {
    opacity: 1;
  }
  to {
    opacity: 1;
    transform: translateY(300%);
  }
}
@keyframes amtErsHorizontalDouble {
  from {
    opacity: 1;
    transform: translateX(-100%);
  }
  50% {
    opacity: 1;
  }
  to {
    opacity: 1;
    transform: translateX(100%);
  }
}
@keyframes amtErsVerticalDouble {
  from {
    opacity: 1;
    transform: translateY(-100%);
  }
  50% {
    opacity: 1;
  }
  to {
    opacity: 1;
    transform: translateY(100%);
  }
}
</style>