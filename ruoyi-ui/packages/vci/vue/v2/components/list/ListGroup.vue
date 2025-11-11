<template>
  <div
    class="ListGroup vci-size-100 vci-pst-rlv"
    @wheel="wheel"
  >
    <div
      :style="{ transform: `translateX(-${page * 100}%)` }"
      class="lg-ctr vci-flex-rml vci-h-100"
    >
      <div
        v-for="(lg, ig) in listGrp"
        :key="ig"
        class="lg-grp vci-size-100"
      >
        <transition name="fade">
          <list-animation
            v-if="ig === page"
            v-slot="props"
            :arrow="arrow"
            :list="lg"
            :reverse-animation-order="reverseAnimationOrder"
          >
            <slot v-bind="props">
              <span>v-bind数据为数组元素{item, index, listTransformed}</span>
            </slot>
          </list-animation>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
// power by visual-ceiling, at 2024/9/12 14:08
import ListAnimation from "./ListAnimation.vue";
import { grpArray } from "@vci/helper/src/array";
import MixinAutoplay from "../../mixins/MixinAutoplay";

export default {
  name: "ListGroup",
  components: { ListAnimation },
  mixins: [MixinAutoplay],
  props: {
    list: {
      type: Array,
      default: () => []
    },
    sizeGrp: {
      type: [Number, String],
      default: 6
    },
    timeSplitAutoplay: {
      type: Number,
      default: 5e3
    }
  },
  data() {
    return {
      page: 0,
      arrow: "toLeft"
    };
  },
  computed: {
    listGrp() {
      return grpArray(this.list, Number(this.sizeGrp));
    },
    reverseAnimationOrder() {
      return this.arrow === "toRight";
    }
  },
  watch: {
    list: {
      immediate: true,
      handler() {
        this.$nextTick(() => {
          if (!this.autoplay.el) this.initAutoPlay(this.$el, this.page, this.listGrp.length);
          else this.updateAutoPlay(this.page, this.listGrp.length, true, this.timeSplitAutoplay);
          this.emitPagination();
        });
      }
    }
  },
  methods: {
    onAutoPlay(index) {
      this.arrow = this.page < index ? "toLeft" : "toRight";
      this.page = index;
      this.emitPagination();
    },
    addPage() {
      this.arrow = "toLeft";
      this.page++;
      this.page > this.listGrp.length - 1 && (this.page--);
      this.updateAutoPlayIndexAndInDe(this.page, 1);
      this.emitPagination();
    },
    subPage() {
      this.arrow = "toRight";
      this.page--;
      this.page < 0 && (this.page++);
      this.updateAutoPlayIndexAndInDe(this.page, -1);
      this.emitPagination();
    },
    jumpPage(page) {
      page = Math.min(Math.max(page, 0), this.listGrp.length - 1);
      this.arrow = this.page < page ? "toLeft" : "toRight";
      this.page = page;
      this.updateAutoPlayIndexAndInDe(this.page, 1);
      this.emitPagination();
    },
    wheel(e) {
      clearTimeout(this.interWheel);
      this.interWheel = setTimeout(() => e["wheelDelta"] > 0 ? this.subPage() : this.addPage(), 50);
    },
    emitPagination() {
      this.$emit("pagination", {
        page: this.page,
        pages: this.listGrp.length
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.ListGroup {
  overflow: hidden;
  .lg-ctr {
    flex-wrap: nowrap;
    transition: .5s ease-in-out;
    .lg-grp {
      flex-shrink: 0;
      width: 100%;
    }
  }
}
</style>