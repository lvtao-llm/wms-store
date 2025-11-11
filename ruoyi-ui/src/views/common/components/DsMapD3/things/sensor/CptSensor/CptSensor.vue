<template>
  <div class="CptSensor vci-pst-rlv">
    <i class="cc-bg vci-bg-fill vci-pst-cm"></i>
    <div class="cc vci-size-100 vci-flex-rcm" @click="$emit('click')">
      <i class="cc-icon vci-bg-contain"></i>
    </div>
    <span class="cc-name vci-pst-c vci-fs-12" @click="$emit('click')">
      {{ name }}
    </span>
    <!-- 传感器弹框 -->
    <transition name="fade">
      <PopupSensor
        v-if="isFocused"
        class="vci-pst-abs"
        :title="name"
        :list="list"
        @close="$emit('click')"
      />
    </transition>
  </div>
</template>

<script>
// power by visual-ceiling, at 2025/4/8 16:08
import PopupSensor from "@/views/common/components/PopupSensor/PopupSensor.vue";

export default {
  name: "CptSensor",
  components: { PopupSensor },
  props: {
    name: {
      type: String,
      default: null,
    },
    list: {
      type: Array,
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
.CptSensor {
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
      height: 16px;
      background-image: url("img/icon.png");
    }
  }
  .cc-name {
    bottom: 100%;
    white-space: nowrap;
    margin-bottom: 6px;
    // @include vci-font-stroke();
  }
  .PopupSensor {
    left: 100%;
    top: -8px;
    margin-left: 12px;
  }
}
</style>
