<template>
  <div
    ref="swiper"
    class="ListSwiper swiper-container"
  >
    <slot></slot>
    <!--    <div-->
    <!--      ref="scroll-bar"-->
    <!--      class="swiper-scrollbar"-->
    <!--    />-->
  </div>
</template>

<script>
import Swiper from "swiper";

export default {
  name: "ListSwiper",
  props: {
    swiperOpt: {
      type: Object,
      default: () => ({
        direction: "vertical",
        slidesPerView: 6,
        slidesPerGroup: 6,
        autoplay: { delay: 5e3, disableOnInteraction: false }
      })
    },
    list: {
      type: Array,
      default: null
    },
    resetSwiper: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      swiper: null
    };
  },
  watch: {
    list: {
      immediate: true,
      handler() {
        this.$nextTick(this.swiperHandle.bind(this));
      }
    },
    swiperOpt: {
      immediate: true,
      handler() {
        if (this.swiper) this.swiper.update();
      }
    }
  },
  methods: {
    swiperHandle() {
      if (this.resetSwiper) {
        if (this.swiper) {
          this.swiper.destroy();
          this.swiper = null;
        }
        this.initSwiper();
      } else if (!this.resetSwiper && !this.swiper) this.initSwiper();
      else if (!this.resetSwiper && this.swiper) this.updateSwiper();
      this.$once("hook:beforeDestroy", () => this.swiper.destroy());
    },
    initSwiper() {
      this.swiper = new Swiper(this.$refs.swiper, ({
        // scrollbar: {
        //   el: this.$refs["scroll-bar"],
        //   draggable: true,
        //   snapOnRelease: false,
        //   hide: false
        // },
        mousewheel: true,
        ...this.swiperOpt
      }));
    },
    updateSwiper() {
      this.swiper.update(true);
    }
  }
};
</script>

<style lang="scss" scoped>
.ListSwiper {
  height: 100%;
}
.swiper-scrollbar {
  opacity: 0;
}
</style>
