<template>
  <div class="RealLightSwitch vci-pst-abs vci-flex-rmb vci-ta-c">
    <div
      v-for="option in options"
      :key="option.value"
      :class="{ active: value === option.value }"
      class="switch-item vci-pst-rlv vci-fs-18 font-oppo-b"
      @click="switchClick(option)">
      <div v-for="i in 4" :key="i" class="rect vci-pst-abs" />
      <div class="label">
        {{ option.label }}
      </div>
    </div>
  </div>
</template>

<script>
import MixinModuleMethods from "@/views/common/mixins/MixinModuleMethods";

export default {
  name: "RealLightSwitch",
  mixins: [MixinModuleMethods],
  data() {
    return {
      options: [],
      value: "real",
    };
  },
  methods: {
    simulationData() {
      this.options = [
        {
          label: "实景风格",
          value: "real",
        },
        {
          label: "光影线条",
          value: "wireframe",
        },
      ];
    },
    switchClick(option) {
      this.value = option.value;
    },
    getRealData() {},
  },
};
</script>

<style lang="scss" scoped>
.RealLightSwitch {
  top: 5vh;
  right: 64px;
  width: 193px;
  pointer-events: auto;
  .switch-item {
    width: 91px;
    height: 29px;
    line-height: 29px;
    background-color: rgba(#29485b, 0.22);
    cursor: pointer;
    color: #c7e7fd;
    .rect {
      width: 2px;
      height: 2px;
      background-color: #fff;
      box-shadow: 0 0 1px #fff, 0 0 2px #fff, 0 0 3px #fff, 0 0 4px #fff,
        0 0 5px #fff, 0 0 6px #fff, 0 0 7px #fff;
      opacity: 0;
      transition: opacity 0.5s;
      &:nth-child(1) {
        top: 0;
        left: 0;
        transform: translate(-50%, -50%);
      }
      &:nth-child(2) {
        top: 0;
        right: 0;
        transform: translate(50%, -50%);
      }
      &:nth-child(3) {
        bottom: 0;
        right: 0;
        transform: translate(50%, 50%);
      }
      &:nth-child(4) {
        bottom: 0;
        left: 0;
        transform: translate(-50%, 50%);
      }
    }
    .label {
      transition: text-shadow 0.5s;
    }
    &.active {
      .rect {
        opacity: 1;
      }
      .label {
        text-shadow: 0 0 1px #00d2ff, 0 0 2px #00d2ff, 0 0 3px #00d2ff,
          0 0 4px #00d2ff, 0 0 5px #00d2ff, 0 0 6px #00d2ff;
      }
    }
  }
}
</style>
