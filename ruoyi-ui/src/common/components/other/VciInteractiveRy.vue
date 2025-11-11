<template>
  <div
    :style="{ width: width ? `${width}px` : null, perspective }"
    class="VciInteractiveRy"
    @mouseenter="handleEnter"
    @mouseleave="handleLeave"
    @mousemove="handleMove"
  >
    <div
      :class="{'vci-transition': enableTransition}"
      :style="syl"
      class="vir-view"
    >
      <slot></slot>
    </div>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/6/7 17:49
import { getElementOffset } from "@vci/helper/src/element";

export default {
  name: "VciInteractiveRy",
  props: {
    transformOrigin: {
      type: String,
      default: "50% 50%"
    },
    perspective: {
      type: String,
      default: "1000px"
    },
    hr: {
      type: [String, Number],
      default: 0.55
    },
    angleMax: {
      type: [String, Number],
      default: 15
    }
  },
  data() {
    return {
      horizontal: null,
      enableTransition: false,
      width: null,
      inter: {}
    };
  },
  computed: {
    syl() {
      return {
        transform: `rotateZ(0deg) rotateY(${(this.horizontal - 0.5) * (Number(this.angleMax) * 2)}deg)`,
        transformOrigin: this.transformOrigin
      };
    }
  },
  mounted() {
    this.adapt();
  },
  methods: {
    adapt() {
      const handle = () => {
        this.width = null;
        this.resetHorizontal();
        clearTimeout(this.inter.resize);
        this.inter.resize = setTimeout(() => this.width = this.$el.clientWidth, 200);
      };
      handle();
      window.addEventListener("resize", handle);
      this.$once("hook:beforeDestroy", () => window.removeEventListener("resize", handle));
    },
    handleEnter(e) {
      this.enableTransition = true;
      this.handleMe(e);
      clearTimeout(this.inter.enter);
      this.inter.enter = setTimeout(() => this.enableTransition = false, 350);
    },
    handleMove(e) {
      !this.enableTransition && this.handleMe(e);
    },
    handleLeave() {
      clearTimeout(this.inter.enter);
      this.enableTransition = true;
      this.resetHorizontal();
    },
    handleMe(e) {
      const { left } = getElementOffset(this.$el);
      this.horizontal = (e.clientX - left) / this.width;
    },
    resetHorizontal() {
      this.horizontal = Number(this.hr);
    }
  }
};
</script>

<style lang="scss" scoped>
.VciInteractiveRy {
}
</style>