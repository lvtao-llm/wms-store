<template>
  <vci-scroll>
    <div class="app">
      <transition mode="out-in" name="fade">
        <RouterView class="app-router" />
      </transition>
    </div>
  </vci-scroll>
</template>

<script>
import VciScroll from "./components/other/VciScroll.vue";
export default {
  components: {
    VciScroll,
  },
};
</script>
<style lang="scss">
.app {
  position: relative;
  z-index: 1;
  height: 100%;
  overflow: hidden;
  width: 100%;
  .app-router {
    position: relative;
    z-index: 1;
    height: 100%;
  }
}
</style>
