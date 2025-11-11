<template>
  <div class="TheCore vci-bg-fill">
    <!-- 地图 -->
    <DsMapD3
      class="vci-pst-abs vci-left-0 vci-top-0"
      @ready="enableRenderRoute = true" />
    <!-- 路由 -->
    <transition mode="out-in" name="fade">
      <RouterView v-if="enableRenderRoute" class="tc-router" />
    </transition>
  </div>
</template>

<script>
import DsMapD3 from "@/views/common/components/DsMapD3/DsMapD3.vue";

export default {
  name: "TheCore",
  components: { DsMapD3 },
  data() {
    return {
      enableRenderRoute: false,
    };
  },
};
</script>

<style lang="scss" scoped>
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
