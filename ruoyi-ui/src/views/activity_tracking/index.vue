<template>
  <div class="TheCore vci-bg-fill">
    <DsMapD3
      :key="indexKey"
      class="vci-pst-abs vci-left-0 vci-top-0"
      @ready="enableRenderRoute = true"
    />
    <!-- <div style="height: 100vh">
      <TheMain v-if="enableRenderRoute"></TheMain>
    </div> -->
    <transition mode="out-in" name="fade">
      <!-- <TheMain v-if="enableRenderRoute" class="tc-router" /> -->
    </transition>
  </div>
</template>

<script>
import TheMain from "../core/views/main/TheMain.vue";
import DsMapD3 from "@/views/common/components/DsMapD3/DsMapD3.vue";
export default {
  components: { DsMapD3, TheMain },
  data() {
    return {
      enableRenderRoute: false,
      indexKey: 0,
    };
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "@vci/style";
.activity-tracking-container {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
}

.ds-map-d3-container {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
}
.TheCore {
  background: linear-gradient(to top, #050507, #2d3845, #507294, #0e243b);
  //background: #062434;
  .tc-router {
    position: absolute;
    z-index: 10;
    width: 100%;
    height: 100%;
  }
  .DsMapD3 {
    z-index: 1;
  }
}
</style>
