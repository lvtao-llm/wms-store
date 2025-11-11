<template>
  <div class="VciDrawerEqualProportion">
    <VciDrawer
      :style="syl"
      enable-adapt
      v-bind="$attrs"
      v-on="$listeners"
    />
    <slot v-bind="{ syl, scale }"></slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/6/19 16:14
import VciDrawer from "./VciDrawer.vue";

export default {
  name: "VciDrawerEqualProportion",
  components: { VciDrawer },
  props: {
    width: {
      type: Number,
      default: 200
    },
    height: {
      type: Number,
      default: 200
    }
  },
  data() {
    return {
      scale: 1
    };
  },
  computed: {
    syl() {
      return {
        width: `${this.width}px`,
        height: `${this.height}px`,
        transform: `scale(${this.scale},${this.scale}) rotateZ(0deg)`
      };
    }
  },
  mounted() {
    this.computeProportion();
    const resize = () => {
      clearTimeout(this.inter);
      this.inter = setTimeout(() => this.computeProportion(), 100);
    };
    window.addEventListener("resize", resize);
    this.$once("hook:beforeDestroy", () => window.removeEventListener("resize", resize));
  },
  methods: {
    computeProportion() {
      const el = this.$el;
      const width = el.clientWidth;
      const height = el.clientHeight;
      const proportion = width / height;
      const proportionOrigin = this.width / this.height;
      if (proportion < proportionOrigin) this.scale = width / this.width;
      else this.scale = height / this.height;
    }
  }
};
</script>

<style lang="scss" scoped>
.VciDrawerEqualProportion {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  //border: 1px solid #fff;
  .VciDrawer {
    position: relative;
    z-index: 2;
    width: 100%;
    height: 100%;
    flex-shrink: 0;
    //border: 1px solid #f0f;
    transition: transform .35s ease-in-out;
  }
}
</style>