<template>
  <transition name="fade">
    <div
      v-if="loading.visible"
      class="QtLoading vci-flex-rcm"
    >
      <vci-lottie-loading speed="2">
        <span
          class="vci-pst-cm vci-fs-24"
          style="z-index: 2;letter-spacing: .1em"
        >{{ loading.process }}%</span>
      </vci-lottie-loading>
    </div>
  </transition>
</template>

<script>
// power by visual-ceiling, at 2023/8/17 9:57
import VciLottieLoading from "@vci/vci/vue/v2/components/other/VciLottieLoading.vue";

export default {
  name: "QtLoading",
  components: { VciLottieLoading },
  data() {
    return {
      loading: {
        visible: false,
        process: 0
      }
    };
  },
  methods: {
    show() {
      this.loading.process = 0;
      this.loading.visible = true;
    },
    hide() { this.loading.visible = false; },
    process(percent) {this.loading.process = percent.toFixed(1); }
  }
};
</script>

<style lang="scss" scoped>
.QtLoading {
  position: absolute;
  left: 0;
  top: 0;
  z-index: 8;
  width: 100%;
  height: 100%;
  background-color: rgba(#000, 0.5);
  backdrop-filter: blur(8px);
}
</style>
