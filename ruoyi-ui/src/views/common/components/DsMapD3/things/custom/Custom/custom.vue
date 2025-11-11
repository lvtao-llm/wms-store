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
      <PopupCustom
        class="fixedBox"
        v-if="isFocused"
        :title="name"
        @close="$emit('click')"
      />
    </transition>
  </div>
</template>

<script>
// power by visual-ceiling, at 2025/4/8 16:08
import PopupCustom from "@/views/common/components/CustomDia/PopupCustom.vue";

export default {
  name: "CustomDia",
  components: { PopupCustom },
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
.CptCamera {
  width: 24px;
  height: 24px;
  transform: none;
  .fixedBox {
    transform: none;
    width: 80vw;
    height: 80vh;
    position: fixed;
    z-index: 9999999;
    top: -15vh;
    left: -18vw;
  }
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
    // left: 100%;
    // bottom: 0;
  }
}
</style>
