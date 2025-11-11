<template>
  <div
    :style="syl"
    class="VciNotification vci-fs-16"
  >
    <div class="vn-head vci-flex-rmb">
      <strong class="vn-title">{{ title }}</strong>
      <VciClose @click="$emit('close')" />
    </div>
    <div class="vn-body">
      <p>{{ message }}</p>
    </div>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/7/17 15:56
import VciClose from "./VciClose.vue";
import { hexToRgba, rgbaToHex } from "@vci/helper/src/color";

export default {
  name: "VciNotification",
  components: { VciClose },
  props: {
    title: {
      type: String,
      default: "提示"
    },
    message: {
      type: String,
      default: "来自勤奋的vci"
    },
    theme: {
      type: String,
      default: "#000"
    }
  },
  data() {
    return {};
  },
  computed: {
    syl() {
      const theme = /^rgba\(/.test(this.theme) ? rgbaToHex(this.theme) : this.theme;
      return {
        borderColor: hexToRgba(theme, .8),
        backgroundColor: hexToRgba(theme, .6),
        boxShadow: `0 0 8px ${hexToRgba(theme, .6)}, 0 0 16px ${hexToRgba(theme, .2)})`
      };
    }
  }
};
</script>

<style lang="scss" scoped>
.VciNotification {
  width: 320px;
  padding: 10px 12px;
  border: 1px solid rgba(#000, .6);
  border-radius: 4px;
  background-color: rgba(#000, .4);
  backdrop-filter: blur(4px);
  box-shadow: 0 0 8px rgba(#000, .6), 0 0 16px rgba(#000, .2);
  .vn-head {
    align-items: flex-start;
    .VciClose {
      flex-shrink: 0;
      margin-left: 4px;
    }
  }
  .vn-body {
    padding-top: 12px;
    word-break: break-all;
  }
}
</style>