<template>
  <div class="VphError">
    <span class="ve-text vci-fs-18">{{ text }}</span>
  </div>
</template>

<script>
// power by visual-ceiling, at 2022/11/21 13:25
export default {
  name: "VphError",
  props: {
    text: {
      type: String,
      default: ""
    }
  },
  data() {
    return {};
  }
};
</script>

<style lang="scss" scoped>
.VphError {
  color: #ff7373;
}
</style>