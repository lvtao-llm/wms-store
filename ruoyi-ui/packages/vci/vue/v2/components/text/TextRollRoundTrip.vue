<template>
  <div
    ref="trr"
    class="TextRollRoundTrip vci-flex-rml"
  >
    <div
      ref="roll"
      :style="{ transform: `translateX(${offset}px)` }"
      class="trr"
    >
      <span>{{ text }}</span>
    </div>
  </div>
</template>

<script>
// power by visual-ceiling, at 2023/12/20 15:37
import MixinClearScheduledTasks from "../../mixins/MixinClearScheduledTasks";

export default {
  name: "TextRollRoundTrip",
  mixins: [MixinClearScheduledTasks],
  props: {
    text: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      offset: 0,
      arrow: 1
    };
  },
  watch: {
    text: {
      immediate: true,
      handler: "roll"
    }
  },
  mounted() {
    window.addEventListener("resize", this.roll);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.roll);
  },
  methods: {
    roll() {
      this.$nextTick(() => {
        const elEtr = this.$refs.trr;
        const elRoll = this.$refs.roll;
        const distance = elEtr.clientWidth - elRoll.clientWidth;
        const speed = .6;
        clearInterval(this.inter.roll);
        if (distance > 0) this.inter.roll = setInterval(
          () => {
            if (this.arrow > 0) {
              if (this.offset >= distance) this.arrow = -1;
            } else {
              if (this.offset <= 0) this.arrow = 1;
            }
            this.offset += speed * this.arrow;
          },
          1e3 / 60
        );
        else this.inter.roll = setInterval(
          () => {
            this.offset -= speed * this.arrow;
            if (this.arrow > 0) {
              if (this.offset <= distance) this.arrow = -1;
            } else {
              if (this.offset >= 0) this.arrow = 1;
            }
          },
          1e3 / 60
        );
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.TextRollRoundTrip {
  width: 888px;
  overflow: hidden;
  .trr {
    white-space: nowrap;
  }
}
</style>
