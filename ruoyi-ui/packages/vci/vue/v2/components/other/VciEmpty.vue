<template>
  <div class="VciEmpty vci-w-100">
    <slot>
      <span class="ve-text">{{ text }}</span>
    </slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/4/13 15:36
export default {
  name: "VciEmpty",
  props: {
    text: {
      type: [String, Number],
      default: "暂无"
    }
  },
  data() {
    return {};
  }
};
</script>

<style lang="scss" scoped>
.VciEmpty {
  .ve-text {
    color: #fff;
    opacity: 0.6;
  }
}
</style>