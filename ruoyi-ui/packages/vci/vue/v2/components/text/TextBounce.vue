<template>
  <div class="TextBounce">
    <span
      v-for="(t, index) in textArray"
      :key="t + index"
      :class="{
        'tb-up': indexCurrentBounce === index,
        'tb-space': /\s/.test(t),
      }"
      class="tb"
    >
      {{ t }}
    </span>
  </div>
</template>

<script>
import MixinClearScheduleTasks from "../../mixins/MixinClearScheduledTasks";

export default {
  name: "TextBounce",
  mixins: [MixinClearScheduleTasks],
  props: {
    text: {
      type: String,
      default: "bounce",
    },
  },
  data() {
    return {
      indexCurrentBounce: -1,
      inter: -1,
    };
  },
  computed: {
    textArray() {
      return (this.text + "...").split("");
    },
  },
  watch: {
    text: {
      handler() {
        this.startBounce();
      },
      immediate: true,
    },
  },
  methods: {
    startBounce() {
      this.stopBounce();
      if (this.textArray.length > 2) {
        this.indexCurrentBounce = -1;
        this.inter = setInterval(() => {
          this.indexCurrentBounce++;
          if (this.indexCurrentBounce >= this.textArray.length)
            this.indexCurrentBounce = -1;
        }, 220);
      }
    },
    stopBounce() {
      clearInterval(this.inter);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style/src/var";
@import "@vci/style";
.TextBounce {
  white-space: nowrap;
  .tb {
    display: inline-block;
    transition: $transition;
    letter-spacing: 0.1em;
    &.tb-up {
      transform: translateY(-20%);
    }
    &.tb-space {
      padding: 0 2px;
    }
  }
}
</style>
