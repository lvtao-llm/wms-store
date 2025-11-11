<template>
  <div
    :class="{'fs-yes': value}"
    class="FormSwitch vci-transition"
    @click="$emit('input', !value)"
  >
    <i
      class="fs-point vci-transition-quick"
    ></i>
  </div>
</template>

<script>
// power by visual-ceiling, at 2022/6/11 18:05
export default {
  name: "FormSwitch",
  props: {
    value: {
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
.FormSwitch {
  width: 48px;
  height: 20px;
  border-radius: 16px;
  border: 1px solid #a4b6cc;
  cursor: pointer;
  background: rgba(#fff, 0.01);
  .fs-point {
    display: block;
    width: 12px;
    height: 12px;
    box-shadow: 0 0 4px #333;
    border-radius: 100%;
    transform: translate(3px, 3px);
    background-color: #fff;
  }
  &.fs-yes {
    background: #576b9d;
    .fs-point {
      transform: translate(48 - 12 - 4 + px, 3px);
    }
  }
}
</style>