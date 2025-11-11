<template>
  <div class="BaseTable">
    <div class="table-head vci-flex-rmb vci-pst-rlv font-oppo-b vci-fs-18">
      <div
        v-for="item in head"
        :key="item.id"
        :style="{ width: `${item['width']}` }"
        class="table-item">
        {{ item["value"] }}
      </div>
      <div class="circle-decoration vci-pst-abs"></div>
      <div class="circle-decoration vci-pst-abs"></div>
    </div>
    <div class="ListSwiper">
      <list-swiper
        :list="list"
        :style="{ height: listHeight }"
        :swiper-opt="swiperOpt">
        <div class="swiper-wrapper">
          <div v-for="slide in list" :key="slide.id" class="swiper-slide">
            <div class="in-str vci-flex-rmb font-oppo-m">
              <div
                v-for="item in slide"
                :key="item.id"
                :style="{
                  width: `${item['width']}`,
                  color: `${item['color']}`,
                }"
                :title="item['value']"
                class="table-item vci-to-e">
                {{ item["value"] }}
              </div>
              <div
                v-if="lineBgVisible"
                class="bg-line vci-bg-fill vci-pst-abs" />
              <div v-else class="bg-color vci-pst-abs"></div>
            </div>
          </div>
        </div>
      </list-swiper>
    </div>
  </div>
</template>

<script>
import ListSwiper from "@/views/common/components/ListSwiper.vue";

export default {
  name: "BaseTable",
  components: { ListSwiper },
  props: {
    head: {
      type: Array,
      default: () => [],
    },
    list: {
      type: Array,
      default: () => [],
    },
    swiperOpt: {
      type: Object,
      default: () => ({}),
    },
    listHeight: {
      type: String,
      default: "180px",
    },
    lineBgVisible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
.BaseTable {
  height: 100%;
  .table-item {
    &:nth-child(1) {
      margin-left: 30px;
    }
  }
  .table-head {
    width: 100%;
    height: 30px;
    margin: 0 auto 8px;
    background-color: rgba(#7abfff, 0.17);
    color: #c7e7fd;
    .circle-decoration {
      top: 0;
      right: 0;
      width: 4px;
      height: 4px;
      border-radius: 50%;
      background-color: #00d2ff;
      transform: translateY(-50%);
      & + .circle-decoration {
        top: auto;
        bottom: 0;
        transform: translateY(50%);
      }
    }
  }
  .ListSwiper {
    width: 100%;
    height: 70%;
    color: #9bb9cd;
    overflow-y: hidden;
    // .swiper-wrapper {
    //   height: 50%;
    // }
    element::-webkit-scrollbar {
      display: none;
    }
    .in-str {
      width: 100%;
      height: 28px;
      .bg-line {
        z-index: -1;
        right: 0;
        width: 705px;
        height: 28px;
        background-image: url("./img/bg.line.png");
      }
      .bg-color {
        z-index: -1;
        width: 100%;
        height: 28px;
        background-color: rgba(#74a7d1, 0.18);
      }
    }
  }
}
</style>
