<template>
  <div
    :class="cls"
    :style="syl"
    class="VciCell vci-flex"
  >
    <slot v-bind="{ value, item }">
      <span
        :title="value"
        class="vci-to-e"
      >
        {{ value }}
      </span>
    </slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2024/9/29 15:24
import { toVh, toVw } from "../../../../other/adapt";
import { isEmpty } from "@vci/helper/src/other";

export default {
  name: "VciCell",
  inheritAttrs: false,
  props: {
    value: {
      type: [String, Number],
      default: "-"
    },
    item: {
      type: Object,
      default: null
    },
    width: {
      type: Number,
      default: null
    },
    height: {
      type: Number,
      default: null
    },
    justifyContent: {
      type: String,
      default: "center"
    },
    alignItems: {
      type: String,
      default: "center"
    },
    isOrderNumber: {
      type: Boolean,
      default: false
    },
    isHeader: {
      type: Boolean,
      default: false
    },
    isLastColumn: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {};
  },
  computed: {
    syl() {
      return {
        width: isEmpty(this.width) ? "" : toVw(this.width),
        minHeight: isEmpty(this.height) ? "" : toVh(this.height),
        justifyContent: this.justifyContent,
        alignItems: this.alignItems
      };
    },
    cls() {
      return [
        isEmpty(this.width) ? "vci-flex-1-0" : "vci-flex-0-0",
        {
          "vc-last-column": this.isLastColumn
        }
      ];
    }
  }
};
</script>

<style lang="scss" scoped>
.VciCell {
}
</style>