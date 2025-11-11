<template>
  <div class="CptCamera vci-pst-rlv">
    <i class="cc-bg vci-bg-fill vci-pst-cm"></i>
    <div class="cc vci-size-100 vci-flex-rcm" @click="$emit('click')">
      <i class="cc-icon vci-bg-contain"></i>
    </div>
    <span class="cc-name vci-pst-c vci-fs-12" @click="$emit('click')">
      {{ name }}
    </span>
    <!-- 弹框|监控画面 -->
    <transition name="fade">
      <PopupCamera
        v-if="isFocused"
        :title="name"
        class="vci-pst-abs"
        @close="$emit('click')"
      />
    </transition>
  </div>
</template>

<script>
// power by visual-ceiling, at 2025/4/8 16:08
import PopupCamera from "@/views/common/components/PopupCamera/PopupCamera.vue";

export default {
  name: "CptCamera",
  components: { PopupCamera },
  props: {
    name: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      isFocused: false,
    };
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style/src/mixins/font";
@import "@vci/style";
.CptCamera {
  width: 24px;
  height: 24px;
  .cc-bg {
    width: 98px;
    height: 98px;
    background-image: url("img/bg.png");
    pointer-events: none;
  }
  .cc {
    position: relative;
    z-index: 2;
    .cc-icon {
      width: 14px;
      height: 14px;
      background-image: url("img/icon.png");
    }
  }
  .cc-name {
    bottom: 100%;
    white-space: nowrap;
    margin-bottom: 6px;
    // @include vci-font-stroke();
  }
  .PopupCamera {
    left: 100%;
    bottom: 0;
  }
}
</style>
