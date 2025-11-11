<template>
  <div
    :class="cls"
    :style="syl"
    class="QtPopup"
    @mousemove="$parent.topping && $parent.topping()"
  >
    <slot></slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/10/11 11:26

import MixinQlProps from "./mixins/MixinQpProps";

export default {
  name: "QtPopup",
  mixins: [MixinQlProps],
  inject: ["qf"],
  inheritAttrs: false,
  data() {
    return {
      cls: "fade-in"
    };
  },
  computed: {
    syl() {
      const left = typeof this.left === "number" ? `${this.left}px` : this.left;
      const top = typeof this.top === "number" ? `${this.top}px` : this.top;
      return {
        transform: `translate(${left},${top})`,
        zIndex: this.zIndex
      };
    }
  },
  created() {
    this.qf.fadeIn = this.fadeIn.bind(this);
    this.qf.fadeOut = this.fadeOut.bind(this);
  },
  methods: {
    fadeIn() {
      this.cls = "fade-in";
      return new Promise(resolve => this.$el.addEventListener("animationend", () => resolve()));
    },
    fadeOut() {
      this.cls = "fade-out";
      return new Promise(resolve => this.$el.addEventListener("animationend", () => resolve()));
    }
  }
};
</script>

<style lang="scss" scoped>
.QtPopup {
  position: absolute;
  left: 0;
  top: 0;
  opacity: 0;
  pointer-events: none;
  &.fade-in {
    animation: amtQlIn .2s 1 both ease-in-out;
  }
  &.fade-out {
    animation: amtQlOut .2s 1 both ease-in-out;
  }
  @keyframes amtQlIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
  @keyframes amtQlOut {
    from {
      opacity: 1;
    }
    to {
      opacity: 0;
    }
  }
}
</style>
