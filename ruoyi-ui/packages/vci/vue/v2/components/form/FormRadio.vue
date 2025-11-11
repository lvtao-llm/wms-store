<template>
  <div class="FormRadio">
    <input
      :checked="value === label"
      :name="name"
      :value="value"
      type="radio"
      @input="$emit('change', label)"
    />
    <i class="fc-icon vci-transition-quick vci-bg-fill"></i>
  </div>
</template>

<script>
// power by visual-ceiling, at 2022/9/27 15:27
export default {
  name: "FormRadio",
  model: {
    prop: "value",
    event: "change"
  },
  props: {
    name: {
      type: String,
      default: "fr"
    },
    value: {
      type: String,
      default: "fr"
    },
    label: {
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
.FormRadio {
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
      opacity: 1;
    }
  }
}
</style>