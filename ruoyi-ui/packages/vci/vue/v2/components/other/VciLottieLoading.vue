<template>
  <div class="VciLottieLoading">
    <div
      ref="vll"
      class="vll"
    ></div>
    <slot></slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/7/26 17:48
import { resourceRootAliyun } from "@vci/vci/config/api";
import Lottie from "lottie-web";

export default {
  name: "VciLottieLoading",
  props: {
    path: {
      type: String,
      default: `${resourceRootAliyun}/lotties/animation_lkjixqot.json`
    },
    speed: {
      type: [Number, String],
      default: 1
    }
  },
  data() {
    return {};
  },
  watch: {
    path: {
      immediate: true,
      async handler(path) {
        await this.$nextTick();
        this.lottie && this.lottie.destroy();
        this.lottie = Lottie.loadAnimation({
          container: this.$refs.vll,
          renderer: "svg",
          loop: true,
          autoplay: true,
          path
        });
        this.lottie.setSpeed(this.speed);
      }
    },
    speed() {
      this.lottie && this.lottie.setSpeed(this.speed);
    }
  }
};
</script>

<style lang="scss" scoped>
.VciLottieLoading {
  position: relative;
  width: 188px;
  height: 188px;
  .vll {
    position: relative;
    height: 100%;
    z-index: 1;
  }
}
</style>
