<template>
  <div
    v-if="!mapRiskArea.isUpdate"
    class="CptMarkerRaOperation vci-flex-c"
    @mousemove="$emit('hover-on')"
    @mouseout="$emit('hover-off')"
  >
    <button
      class="cmr-btn vci-bg-fill vci-transition-quick"
      @click.stop="$emit('edit')"
    >
      <span>编辑区域</span>
    </button>
    <button
      class="cmr-btn vci-bg-fill vci-transition-quick"
      @click.stop="$emit('delete')"
    >
      <span>删除区域</span>
    </button>
  </div>
</template>

<script>
// power by visual-ceiling, at 2025/4/3 15:42

import { mapState } from "pinia";
import { storeCore } from "@/store/store.core";

export default {
  name: "CptMarkerRaOperation",
  props: {
    idThing: {
      type: String,
      default: null
    }
  },
  data() {
    return {};
  },
  computed: {
    ...mapState(storeCore, ["mapRiskArea"])
  }
};
</script>

<style lang="scss" scoped>
.CptMarkerRaOperation {
  .cmr-btn {
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