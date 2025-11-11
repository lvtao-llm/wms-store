<template>
  <div class="CptTrajectory">
    <button
      v-if="!isDisplayTrajectoryReal && !isDisplayTrajectoryHistory"
      class="ct-btn vci-bg-fill vci-transition-quick"
      @click="$emit('display-trajectory-real')"
    >
      <span>实时轨迹</span>
    </button>
    <button
      v-if="!isDisplayTrajectoryReal && !isDisplayTrajectoryHistory"
      class="ct-btn vci-bg-fill vci-transition-quick"
      @click="$emit('display-trajectory-history')"
    >
      <span>历史轨迹</span>
    </button>
    <button
      v-if="isDisplayTrajectoryReal || isDisplayTrajectoryHistory"
      class="ct-btn vci-bg-fill vci-transition-quick"
      @click="cancelTrajectory"
    >
      <span>关闭轨迹</span>
    </button>
  </div>
</template>

<script>
// power by visual-ceiling, at 2025/4/3 15:42

export default {
  name: "CptTrajectory",
  props: {
    idThing: {
      type: String,
      default: null
    },
    isDisplayTrajectoryReal: {
      type: Boolean,
      default: false
    },
    isDisplayTrajectoryHistory: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {};
  },
  methods: {
    async cancelTrajectory() {
      this.isDisplayTrajectoryReal && this.$emit("cancel-trajectory-real");
      this.isDisplayTrajectoryHistory && this.$emit("cancel-trajectory-history");
    }
  }
};
</script>

<style lang="scss" scoped>
.CptTrajectory {
  .ct-btn {
    width: 104px;
    height: 38px;
    padding-bottom: 4px;
    margin-top: -6px;
    background-image: url("img/bg.png");
    &:hover {
      background-image: url("img/bg.active.png");
      text-shadow: 0 0 1px #0be9ff, 0 0 2px #0be9ff;
    }
  }
}
</style>