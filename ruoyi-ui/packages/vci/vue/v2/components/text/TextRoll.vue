<template>
  <div
    class="TextRoll"
    @mouseenter="stop"
    @mouseleave="roll"
  >
    <div
      :class="{'vci-transition' : !enableRoll}"
      :style="{ transform: `translateX(${offset}px)` }"
      class="tr-view vci-flex-rml"
    >
      <span
        ref="text"
        :title="text"
        class="tr-text"
      >
        {{ text }}
      </span>
      <span
        v-if="enableRoll"
        :title="text"
        class="tr-text-fill"
      >
        {{ text }}
      </span>
    </div>
  </div>
</template>
<script>
import MixinClearScheduleTasks from "../../mixins/MixinClearScheduledTasks";
import { addCss, getElementPropertyOfNumber } from "@vci/helper/src/element";

export default {
  name: "TextRoll",
  mixins: [MixinClearScheduleTasks],
  props: {
    text: {
      default: "文字滚动",
      type: [String, Number, Boolean]
    },
    speed: {
      type: Number,
      default: 0.6
    },
    forceRoll: {
      type: Boolean,
      default: false
    },
    debug: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      offset: 0,
      enableRoll: false,
      inter: {
        resize: -1,
        roll: -1
      }
    };
  },
  watch: {
    text: {
      handler: "roll",
      immediate: true
    }
  },
  mounted() {
    window.addEventListener("resize", this.roll);
    this.$once("hook:beforeDestroy", () => window.removeEventListener("resize", this.roll));
  },
  methods: {
    roll() {
      this.stop();
      this.$nextTick(() => {
        const el = this.$el;
        const elText = this.$refs.text;
        if (this.forceRoll) {
          const widthMax = getElementPropertyOfNumber(el, "maxWidth");
          (isNaN(widthMax) || elText.clientWidth < widthMax) && addCss(el, { width: elText.clientWidth - 1 + "px" });
        }
        this.debug && console.info(elText.clientWidth, el.clientWidth);
        this.enableRoll = elText ? elText.clientWidth > el.clientWidth : false;
        if (this.enableRoll) {
          this.inter.roll = setInterval(() => {
            this.offset -= this.speed;
            if (this.offset <= -elText.clientWidth) this.offset = 0;
          }, 1e3 / 60);
        } else this.offset = 0;
      });
    },
    stop() {
      clearInterval(this.inter.roll);
    }
  }
};
</script>
<style lang="scss" scoped>
.TextRoll {
  overflow: hidden;
  .tr-view {
    flex-wrap: nowrap;
    .tr-text {
      white-space: nowrap;
      padding-right: 8px;
    }
    .tr-text-fill {
      white-space: nowrap;
    }
  }
}
</style>
