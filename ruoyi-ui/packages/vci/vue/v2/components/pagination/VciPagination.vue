<template>
  <div
    v-if="total"
    class="VciPagination vci-flex-rcm vci-fs-14"
  >
    <div
      v-if="page"
      class="vp-control vci-flex-rml"
    >
      <div
        class="vp-page-as-ctr"
        @click="subPage"
      >
        <slot
          name="page-sub"
          v-bind="{ page }"
        >
          <div
            :class="{'vp-disabled': page <= 1}"
            class="vp-page-sub vci-flex-rcm vci-transition-quick"
          >
            <i class="vp-arrow vci-bg-contain"></i>
          </div>
        </slot>
      </div>
      <div class="vp-pages vci-flex-rcm">
        <div
          v-for="(p,i) in pagesArr"
          :key="i"
          class="vp-page"
          @click="switchPage(p)"
        >
          <slot
            name="page"
            v-bind="{ p, page }"
          >
            <div
              v-if="typeof p === 'number'"
              :class="{ 'vp-current': p === page }"
              class="vp vci-flex-rcm vci-transition-quick"
            >
              <span>{{ p }}</span>
            </div>
            <div
              v-else
              class="vp vci-flex-rcm"
            >
              <span>{{ p.symbolMore }}</span>
            </div>
          </slot>
        </div>
      </div>
      <div
        class="vp-page-as-ctr"
        @click="addPage"
      >
        <slot
          name="page-sub"
          v-bind="{ page }"
        >
          <div
            :class="{'vp-disabled': page >= pages}"
            class="vp-page-add vci-flex-rcm vci-transition-quick"
          >
            <i class="vp-arrow vci-bg-contain"></i>
          </div>
        </slot>
      </div>
    </div>
    <div class="vp-desc vci-flex-rml">
      <slot
        v-if="visibleStatistics"
        name="desc-statistics"
      >
        <div class="vp-statistics vci-flex-rml">
          <span>共</span>
          <span class="vp-total">{{ total }}</span>
          <span>条</span>
        </div>
      </slot>
    </div>
  </div>
</template>

<script>
// power by visual-ceiling, at 2024/9/29 10:40
import { array } from "@vci/helper/src/array";

export default {
  name: "VciPagination",
  model: {
    prop: "page",
    event: "change"
  },
  props: {
    // 页码
    page: {
      type: Number,
      default: 1
    },
    // 每页数量
    size: {
      type: Number,
      default: 10
    },
    // 总页数
    total: {
      type: Number,
      default: 0
    },
    // 总共显示的页码个数
    pagerCount: {
      type: Number,
      default: 4
    },
    // 是否展示统计信息
    visibleStatistics: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {};
  },
  computed: {
    pages() {
      return Math.ceil(this.total / this.size);
    },
    pagesArr() {
      const pagerCountNoEdge = this.pagerCount - 2;
      const pagerCountNoEdgeHalf = Math.floor(pagerCountNoEdge / 2);
      if (this.pages <= this.pagerCount) return array(this.pages).map((v, i) => i + 1);
      else {
        const arr = [];
        arr.push(1);
        if ((this.page - pagerCountNoEdgeHalf) > 2) {
          arr.push({ location: "left", symbolMore: "···" });
          if ((this.page + pagerCountNoEdgeHalf) < (this.pages - 1)) {
            for (let i = this.page - pagerCountNoEdgeHalf; i <= (this.page + pagerCountNoEdgeHalf); i++) {
              arr.push(i);
            }
            arr.push({ location: "right", symbolMore: "···" });
            arr.push(this.pages);
          } else {
            for (let i = (this.pages - pagerCountNoEdge); i <= this.pages; i++) {
              arr.push(i);
            }
          }
        } else {
          arr.push(...array(pagerCountNoEdge).map((v, i) => i + 2));
          arr.push({ location: "right", symbolMore: "···" });
          arr.push(this.pages);
        }
        return arr;
      }
    }
  },
  methods: {
    addPage() {
      const page = this.page + 1;
      this.$emit("change", page > this.pages ? this.page : page);
    },
    subPage() {
      const page = this.page - 1;
      this.$emit("change", page < 1 ? 1 : page);
    },
    switchPage(page) {
      if (typeof page !== "number") {
        const pageCountNoEdge = Math.floor((this.pagerCount - 2) / 2) * 2 + 1;
        if (page.location === "right") this.$emit("change", Math.min(this.page + pageCountNoEdge, this.pages));
        else this.$emit("change", Math.max(1, this.page - pageCountNoEdge));
      } else this.$emit("change", page);
    }
  }
};
</script>

<style lang="scss" scoped>
.VciPagination {
  flex-wrap: wrap;
  .vp-arrow {
    display: block;
    width: 7px;
    height: 12hpx;
    background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAMCAMAAACZS1ciAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAABIUExURQAAAP///////////////////////////////////////////////////////////////////////////////////////////+d4eLAAAAAXdFJOUwD8RCT+LiE9QULaLDE8+jBAB+YY4x6N6putLgAAAEBJREFUCNctjFsOgCAQAxdFF/GB4mPuf1O0sT8zTZqa586+LOCSGVbJAJOkwC45K+Jxif2mwRhJL+4g+P/zqFkDfJICnIRotgQAAAAASUVORK5CYII=");
  }
  .vp-control {
    .vp-page-sub, .vp-page-add {
      width: 24px;
      height: 24hpx;
      cursor: pointer;
    }
    .vp-page-sub {
      .vp-arrow {
        transform: rotateY(180deg);
      }
    }
    .vp-pages {
      .vp-page {
        cursor: pointer;
        .vp {
          width: 24px;
          height: 24hpx;
          border-radius: 2px;
          &.vp-current {
            background-color: rgba(255, 255, 255, 0.1);
          }
        }
      }
    }
    .vp-disabled {
      opacity: .6;
      cursor: not-allowed;
    }
  }
  .vp-desc {
    .vp-statistics .vp-total {
      padding: 0 4px;
      color: #00f7fb;
    }
  }
}
</style>