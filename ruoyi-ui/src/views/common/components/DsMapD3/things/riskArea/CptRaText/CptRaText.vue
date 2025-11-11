<template>
  <div class="CptRaText vci-fs-16">
    <span>{{ name }}</span>
  </div>
</template>

<script>
// power by visual-ceiling, at 2025/4/11 17:18
export default {
  name: "CptRaText",
  props: {
    name: {
      type: String,
      default: null,
    },
  },
  data() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style/src/mixins/font";
@import "@vci/style";
.CptRaText {
  // @include vci-font-stroke();
}
</style>
