<template>
  <div
    v-if="visible"
    :class="[{ 'pq-body': toBody }, vmClass]"
    :style="vmStyle"
    class="PopupQuick vci-flex-rcm"
  >
    <i
      v-if="enableMask"
      :style="maskStyle"
      class="pq-mask"
      @click="closeByMask"
    ></i>
    <div :style="{ overflow }" class="pq-slot">
      <slot v-bind="slotProps"></slot>
    </div>
  </div>
</template>

<script>
import MixinClearScheduleTasks from "../../../mixins/MixinClearScheduledTasks";
import {
  addCss,
  getElementBody,
  getElementMaxZIndex,
  getElementProperty,
} from "@vci/helper/src/element";
import { isEmpty } from "@vci/helper/src/other";

export default {
  name: "PopupQuick",
  mixins: [MixinClearScheduleTasks],
  inheritAttrs: false,
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    toBody: {
      type: Boolean,
      default: true,
    },
    maskStyle: {
      type: Object,
      default: null,
    },
    enableMask: {
      type: Boolean,
      default: true,
    },
    enableMaskClose: {
      type: Boolean,
      default: false,
    },
    enableLockScroll: {
      type: Boolean,
      default: false,
    },
    zIndex: {
      type: [String, Number],
      default: null,
    },
    animation: {
      type: String,
      default: "fade-down",
    },
    overflow: {
      type: String,
      default: "visible",
    },
  },
  data() {
    return {
      vmClass: null,
      vmStyle: {
        zIndex: 8,
      },
      inter: -1,
    };
  },
  computed: {
    slotProps() {
      return {
        close: this.close,
      };
    },
  },
  watch: {
    visible: {
      immediate: true,
      handler(value, oldValue) {
        if (value) {
          this.$emit("beforeOpen", this);
          if (this.zIndex !== null) this.vmStyle.zIndex = this.zIndex;
          else this.vmStyle.zIndex = getElementMaxZIndex();
          clearTimeout(this.inter);
          this.inter = setTimeout(() => {
            this.vmClass = "pq-show pq-show-" + this.animation;
            this.inter = setTimeout(() => this.$emit("afterOpen", this), 350);
          }, 50);
        } else !isEmpty(oldValue) && this.close();
      },
    },
    enableLockScroll: {
      immediate: true,
      handler: "lockScrollHandle",
    },
    animation: {
      immediate: true,
      handler(animation) {
        this.vmClass = "pq-hide pq-hide-" + animation;
      },
    },
  },
  methods: {
    lockScrollHandle() {
      const body = getElementBody();
      if (this.enableLockScroll) {
        this._oldScroll = getElementProperty(body, "overflow");
        addCss(body, { overflow: "hidden" });
      } else {
        if (this._oldScroll) addCss(body, { overflow: this._oldScroll });
      }
    },
    closeByMask() {
      this.enableMaskClose && this.close();
    },
    close() {
      if (!this.visible) return false;
      this.$emit("beforeClose", this);
      this.vmClass = "pq-hide pq-hide-" + this.animation;
      clearTimeout(this.inter);
      this.inter = setTimeout(() => this.$emit("afterClose", this), 350);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style/src/var";
@import "@vci/style";
.PopupQuick {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  .pq-mask {
    position: absolute;
    left: 0;
    top: 0;
    z-index: 1;
    display: block;
    width: 100%;
    height: 100%;
    opacity: 0;
    transition: $transition;
    background-color: rgba(0, 0, 0, 0.05);
    backdrop-filter: blur(2px);
  }
  .pq-slot {
    position: relative;
    z-index: 2;
    transition: $transition-slow;
    opacity: 0;
    max-height: calc(100% - 32px);
    max-width: calc(100% - 32px);
  }
  &.pq-body {
    position: fixed;
  }
  &.pq-show {
    .pq-mask {
      opacity: 1;
    }
    .pq-slot {
      opacity: 1;
    }
  }
  &.pq-hide {
    .pq-mask {
      transition: $transition-slow;
    }
    .pq-slot {
      transition: $transition;
    }
  }
  &.pq-show-fade-down {
    .pq-slot {
      transform: translateY(0);
    }
  }
  &.pq-hide-fade-down {
    .pq-slot {
      transform: translateY(-32px);
    }
  }
  &.pq-show-fade-up {
    .pq-slot {
      transform: translateY(0);
    }
  }
  &.pq-hide-fade-up {
    .pq-slot {
      transform: translateY(32px);
    }
  }
  &.pq-show-fade-zoom-out {
    .pq-slot {
      transform: scale(1);
    }
  }
  &.pq-hide-zoom-out {
    .pq-slot {
      transform: scale(0);
    }
  }
  &.pq-show-fade-zoom-in {
    .pq-slot {
      transform: scale(1);
    }
  }
  &.pq-hide-zoom-in {
    .pq-slot {
      transform: scale(2);
    }
  }
}
</style>
