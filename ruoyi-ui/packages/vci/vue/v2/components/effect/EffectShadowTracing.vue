<template>
  <div
    :style="{ transformStyle, perspective }"
    class="EffectShadowTracing"
  >
    <i
      v-for="(v, i) in Number(amount)"
      :key="i"
      :class="mode"
      :style="genEstSyl(i)"
      class="est est-amt"
    ></i>
    <div class="est-view">
      <slot></slot>
    </div>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/6/12 16:10
export default {
  name: "EffectShadowTracing",
  props: {
    urlGraphic: {
      type: String,
      default: null
    },
    amount: {
      type: [Number, String],
      default: 4
    },
    mode: {
      type: String,
      default: "vci-bg-fill"
    },
    fixFirstEst: {
      type: Boolean,
      default: false
    },
    delayDurationStep: {
      type: [Number, String],
      default: 600
    },
    enableAlphaTracing: {
      type: Boolean,
      default: false
    },
    zIndexEst: {
      type: Number,
      default: 3
    },
    transformStyle: {
      type: String,
      default: "preserve-3d"
    },
    perspective: {
      type: String,
      default: "1000px"
    }
  },
  data() {
    return {};
  },
  methods: {
    genEstSyl(index = 0) {
      return {
        animationDelay: `${(this.fixFirstEst ? (index - 1) : index) * Number(this.delayDurationStep)}ms`,
        animationName: this.fixFirstEst && index === 0 ? "none" : null,
        opacity: this.enableAlphaTracing ? 1 - index / Number(this.amount) : 1,
        zIndex: this.zIndexEst
      };
    }
  }
};
</script>

<style lang="scss" scoped>
.EffectShadowTracing {
  position: relative;
  width: 100%;
  height: 100%;
  .est {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    &.est-amt {
      animation: amtEst 1.8s infinite both linear;
      @keyframes amtEst {
        from {
          opacity: 0;
          transform: scale(1);
        }
        50% {
          opacity: 0.5;
          transform: scale(1.3);
        }
        to {
          opacity: 0;
          transform: scale(1.6);
        }
      }
    }
  }
  .est-view {
    z-index: 2;
    width: 100%;
    height: 100%;
  }
}
</style>