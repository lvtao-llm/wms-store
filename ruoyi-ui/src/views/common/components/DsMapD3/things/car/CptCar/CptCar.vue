<template>
  <div
    class="CptCar vci-bg-fill"
    :class="{
      'c-active': isDisplayTrajectoryReal || isDisplayTrajectoryHistory,
    }"
  >
    <div class="cc-name vci-pst-c vci-fs-12 vci-fw-bolder">
      <span>{{ name }}</span>
    </div>
    <CptTrajectory
      v-if="!isHistoryMode"
      class="vci-pst-abs"
      :id-thing="idCar"
      :is-display-trajectory-real="isDisplayTrajectoryReal"
      :is-display-trajectory-history="isDisplayTrajectoryHistory"
      @display-trajectory-real="$emit('display-trajectory-real')"
      @cancel-trajectory-real="$emit('cancel-trajectory-real')"
      @display-trajectory-history="$emit('display-trajectory-history')"
      @cancel-trajectory-history="$emit('cancel-trajectory-history')"
    />
  </div>
</template>

<script>
// power by visual-ceiling, at 2025/4/3 11:15
import CptTrajectory from "@/views/common/components/DsMapD3/things/trajectory/CptTrajectory/CptTrajectory.vue";

export default {
  name: "CptCar",
  components: { CptTrajectory },
  props: {
    name: {
      type: String,
      default: null,
    },
    idCar: {
      type: String,
      default: null,
    },
    isDisplayTrajectoryReal: {
      type: Boolean,
      default: false,
    },
    isDisplayTrajectoryHistory: {
      type: Boolean,
      default: false,
    },
    isHistoryMode: {
      type: Boolean,
      default: false,
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
.CptCar {
  width: 34px;
  height: 52px;
  background-image: url("img/icon.png");
  .cc-name {
    bottom: 100%;
    white-space: nowrap;
    margin-bottom: 2px;
    // @include vci-font-stroke();
  }
  .CptTrajectory {
    left: 100%;
    top: 10%;
    transform-origin: 0 0;
    transform: scale(0);
    opacity: 0;
    transition: 0.2s ease-in-out;
  }
  &:hover .CptTrajectory,
  &.c-active .CptTrajectory {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
