<template>
  <div class="VciChart"></div>
</template>

<script>
import Chart from "./Chart";
import { isEmpty } from "@vci/helper/src/other";

export default {
  name: "VciChart",
  props: {
    chart: {
      type: Function,
      default: null,
    },
    chartOption: {
      type: Object,
      default: null,
    },
    chartData: {
      type: [Array, Object, Number],
      default: null,
    },
    renderOption: {
      type: Object,
      default: null,
    },
    customRender: {
      type: Function,
      default: null,
    },
    onAfterRender: {
      type: Function,
      default: null,
    },
    enableWatchElement: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
  watch: {
    chartData: {
      immediate: true,
      deep: true,
      handler: "render",
    },
  },
  mounted() {
    this.enableWatchElement && this.watchElement();
  },
  methods: {
    render() {
      this.$nextTick(() => {
        if (!this.ci) {
          if (!this.chart) {
            console.info("请传入图表类");
            return false;
          }
          this.ci = new this.chart({
            el: this.$el,
            ...(this.chartOption || {}),
          });
          if (!(this.ci instanceof Chart)) {
            console.warn(`图表类不是继承于"@vci/vue/v2/components/chart"`);
            delete this.ci;
            return false;
          }
          this.$once("hook:beforeDestroy", () => this.ci && this.ci.destroy());
        }
        if (this.customRender)
          this.customRender && this.customRender(this.ci, this.chartData);
        else
          !isEmpty(this.chartData) &&
            this.ci.render(this.chartData, this.renderOption);
        this.onAfterRender && this.onAfterRender(this.ci);
      });
    },
    watchElement() {
      const observer = new MutationObserver(() => this.ci && this.ci.resize());
      observer.observe(this.$el, { childList: true, subtree: true });
      this.$on("hook:beforeDestroy", () => observer.disconnect());
    },
  },
};
</script>

<style lang="scss" scoped>
.VciChart {
  width: 100%;
  height: 100%;
}
</style>
