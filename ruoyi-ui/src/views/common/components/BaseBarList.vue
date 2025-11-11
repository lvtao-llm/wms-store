<template>
  <list-swiper :list="list" :swiper-opt="swiperOpt" class="BaseBarList">
    <div class="swiper-wrapper">
      <div v-for="slide in list" :key="slide['id']" class="swiper-slide">
        <div class="in-str vci-flex-rml">
          <div class="icon-frame vci-pst-rlv">
            <div class="top-circle vci-pst-cm"></div>
          </div>
          <div class="name vci-fs-14 font-oppo-b">
            {{ slide["name"] }}
          </div>
          <div class="bar-frame vci-pst-rlv">
            <div
              :style="{
                left: `${slide['left']}%`,
                width: `${slide['percent']}%`,
              }"
              class="bar vci-pst-m"></div>
          </div>
          <div class="percent font-din vci-fs-18 vci-to-e">
            {{ slide["percent"] }}
            <span class="unit vci-fs-14 font-oppo-r">%</span>
          </div>
          <div class="value font-din vci-fs-18 vci-to-e">
            {{ slide["value"] }}
            <span class="unit vci-fs-14 font-oppo-r">人</span>
          </div>
        </div>
      </div>
    </div>
  </list-swiper>
</template>

<script>
import ListSwiper from "@/views/common/components/ListSwiper.vue";

export default {
  name: "BaseBarList",
  components: { ListSwiper },
  props: {
    list: {
      type: Array,
      default: () => [],
    },
    swiperOpt: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
.BaseBarList {
  width: 100%;
  .swiper-slide {
    .in-str {
      height: 32px;
      background-color: rgba(#7abfff, 0.05);
      color: #c7e7fd;
      .icon-frame {
        width: 16px;
        height: 16px;
        margin-left: 14px;
        border-radius: 50%;
        border: 1px solid transparent;
        box-sizing: border-box;
        .top-circle {
          width: 8px;
          height: 8px;
          border-radius: 50%;
          border: 2px solid transparent;
        }
      }
      .name {
        width: 92px;
        margin-left: 4px;
      }
      .bar-frame {
        width: 60%;
        height: 7px;
        margin-right: 6px;
        box-sizing: border-box;
        border: 2px solid #5380ae;
        .bar {
          width: 0;
          height: 7px;
          box-sizing: border-box;
          border: 2px solid #7abfff;
          background: linear-gradient(to right, #0097bc, #00fef0);
          transition: width 0.3s, left 0.5s;
        }
      }
      .percent {
        width: 46px;
      }
      .value {
        margin-left: 12px;
      }
      .unit {
        opacity: 0.54;
      }
    }
    &:nth-child(2n-1) {
      .icon-frame {
        border-color: #05d0e4;
        background: linear-gradient(to right, rgba(#02e0f4, 0.6), transparent);
        .top-circle {
          border-color: #05d0e4;
        }
      }
    }
    &:nth-child(2n) {
      .icon-frame {
        border-color: #dead0a;
        background: linear-gradient(to right, rgba(#dead0a, 0.6), transparent);
        .top-circle {
          border-color: #dead0a;
        }
      }
    }
  }
}
</style>
