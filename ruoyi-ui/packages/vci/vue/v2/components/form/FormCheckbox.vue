<template>
  <div class="FormCheckbox">
    <input
      :checked="checked"
      :name="name"
      type="checkbox"
      @input="$emit('change', $event.target.checked)"
    />
    <i class="fc-icon vci-transition-quick vci-bg-fill"></i>
  </div>
</template>

<script>
// power by visual-ceiling, at 2022/9/27 15:27
export default {
  name: "FormCheckbox",
  model: {
    prop: "checked",
    event: "change"
  },
  props: {
    name: {
      type: String,
      default: "fc"
    },
    checked: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {};
  }
};
</script>

<style lang="scss" scoped>
.FormCheckbox {
  position: relative;
  width: 20px;
  height: 20px;
  .fc-icon {
    position: relative;
    z-index: 1;
    display: block;
    width: 100%;
    height: 100%;
    opacity: 0.4;
    background-color: #fff;
  }
  input {
    position: absolute;
    left: 0;
    top: 0;
    z-index: 6;
    width: 100%;
    height: 100%;
    opacity: 0;
    cursor: pointer;
    &:checked ~ .fc-icon {
      opacity: 1 !important;
    }
  }
}
</style>