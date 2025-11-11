<template>
  <div class="VciDrawer"></div>
</template>

<script>
// power by visual-ceiling, at 2023/6/13 14:06
import Drawer from "@vci/quick-drawer";

export default {
  name: "VciDrawer",
  props: {
    enableAdapt: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {};
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.$nextTick(() => {
        const drawer = this.drawer = new Drawer({
          width: this.$el.clientWidth,
          height: this.$el.clientHeight
        });
        drawer.mount(this.$el);
        this.$once("hook:beforeDestroy", () => drawer.destroy());
        this.enableAdapt && drawer.addEventListener("resize", () => drawer.setSize(this.$el.clientWidth, this.$el.clientHeight));
        this.$emit("ready", drawer);
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.VciDrawer {
  position: relative;
  width: 100%;
  height: 100%;
}
</style>