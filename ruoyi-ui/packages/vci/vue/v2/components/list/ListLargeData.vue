<template>
  <div class="ListLargeData">
    <vci-scroll
      ref="scroll"
      @scroll="scroll"
    >
      <i
        :style="{ height: `${fillTopHeight}px` }"
        class="lld-pre"
      ></i>
      <div
        ref="list"
        class="lld-list"
      >
        <div
          v-for="(v, i) in validList"
          :key="i"
          class="vl-item"
        >
          <slot v-bind="{ item: v, index: i }"></slot>
        </div>
      </div>
      <i
        :style="{ height: `${fillBottomHeight}px` }"
        class="lld-suf"
      ></i>
    </vci-scroll>
  </div>
</template>

<script>
// power by visual-ceiling, at 2022/11/2 15:51
import VciScroll from "@vci/vci/vue/v2/components/other/VciScroll.vue";

export default {
  name: "ListLargeData",
  components: { VciScroll },
  props: {
    list: {
      type: Array,
      default: () => []
    },
    validRows: {
      type: Number,
      default: 32
    }
  },
  data() {
    return {
      top: 0,
      topAmount: 0,
      validMaxAmount: 0,
      fillTopHeight: 0,
      fillBottomHeight: 0,
      validList: [],
      row: {
        height: 20
      }
    };
  },
  watch: {
    list: {
      immediate: true,
      handler: "analysis"
    }
  },
  methods: {
    analysis() {
      this.validList = this.list.filter((v, i) => i >= 0 && i < this.validRows);
      this.$nextTick(() => {
        // const { elViewHeight, elContentHeight } = this.$refs.scroll.getStructure();
        this.scroll({ top: 0 });
      });
    },
    scroll(e) {
      this.top = e.top;
      this.topAmount = Math.floor(this.top / this.row.height);
      this.validMaxAmount = this.topAmount + this.validRows;
      this.validList = this.list.filter((v, i) => i >= this.topAmount && i < this.validMaxAmount);
      this.fillTopHeight = this.topAmount * this.row.height;
      this.fillBottomHeight = this.list.filter((v, i) => i > this.validMaxAmount).length * this.row.height;
      this.$nextTick(() => this.$refs.scroll.scrollTo(this.top));
    }
  }
};
</script>

<style lang="scss" scoped>
.ListLargeData {
  width: 100%;
  height: 100%;
  .lld-pre, .lld-suf {
    display: block;
    width: 100%;
  }
}
</style>