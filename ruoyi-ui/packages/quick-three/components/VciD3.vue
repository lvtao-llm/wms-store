<template>
  <div class="VciD3">
    <div ref="vd" class="vd-view"></div>
    <slot></slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/7/26 15:17
import QuickThree from "../src/index";
import { QtEvents } from "../src/events/QtEvents";

export default {
  name: "VciD3",
  props: {
    beforeInitQt: {
      type: Function,
      default: null,
    },
  },
  data() {
    return {};
  },
  mounted() {
    const optionQt = { el: this.$refs.vd };
    this.beforeInitQt && this.beforeInitQt(optionQt);
    this.vd = new QuickThree(optionQt);
    this.vd.addEventListener(QtEvents.AfterMount, (e) =>
      this.$emit("ready", e.target)
    );
  },

  beforeDestroy() {
    this.vd && this.vd.destroy();
  },
};
</script>

<style lang="scss" scoped>
.VciD3 {
  position: relative;
  width: 100%;
  height: 100%;
  .vd-view {
    position: relative;
    z-index: 6;
    width: 100%;
    height: 100%;
  }
}
</style>
