<template>
  <div :class="{ 'vl-mask': enableMask }" class="VciLoading vci-flex-ccm">
    <svg class="circular" viewBox="25 25 50 50">
      <circle
        :style="{ stroke: color }"
        class="path"
        cx="50"
        cy="50"
        fill="none"
        r="20"
      ></circle>
    </svg>
    <TextBounce
      v-if="text"
      :style="{ color: textColor }"
      :text="text"
      class="text"
    />
  </div>
</template>

<script>
import TextBounce from "../text/TextBounce.vue";

export default {
  name: "VciLoading",
  components: { TextBounce },
  props: {
    color: {
      default: "#fff",
      type: String,
    },
    text: {
      default: "加载中",
      type: String,
    },
    textColor: {
      default: "#fff",
      type: String,
    },
    enableMask: {
      type: Boolean,
      default: false,
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style/src/var";
@import "@vci/style";
.VciLoading {
  padding: 12px 24px;
  border-radius: 8px;
  min-width: 120px;
  transition: $transition-slow;
  &.vl-mask {
    background-color: rgba(0, 0, 0, 0.2);
  }
  .circular {
    height: 42px;
    width: 42px;
    animation: loading-rotate 2s linear infinite;
    transition: $transition-slow;
    .path {
      animation: loading-dash 1.5s ease-in-out infinite;
      stroke-dasharray: 90, 150;
      stroke-dashoffset: 0;
      stroke-width: 3;
      stroke: #5185e9;
      stroke-linecap: round;
    }
    @keyframes loading-rotate {
      to {
        transform: rotate(1turn);
      }
    }
    @keyframes loading-dash {
      0% {
        stroke-dasharray: 1, 200;
        stroke-dashoffset: 0;
      }
      50% {
        stroke-dasharray: 90, 150;
        stroke-dashoffset: -40px;
      }
      to {
        stroke-dasharray: 90, 150;
        stroke-dashoffset: -120px;
      }
    }
  }
  .text {
    margin-top: 12px;
    transition: $transition-slow;
    text-shadow: 1px 0 1px #333, -1px 0 1px #333, 0 -1px 1px #333,
      0 1px 1px #333;
  }
}
</style>
