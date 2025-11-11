<template>
  <div class="BaseFrame font-pm-zd vci-pst-rlv">
    <div v-if="st" class="switch-title vci-flex-rml vci-bg-fill vci-pst-rlv">
      <div class="title-icon vci-bg-fill"></div>
      <div
        v-for="item in switchTitle"
        :key="item['value']"
        :class="{ active: item['value'] === value }"
        class="sn vci-flex-rml"
        @click="changeComFn(item)">
        <div class="sn-txt vci-fs-26 vci-pst-rlv">
          {{ item.label }}
        </div>
        <div class="ver vci-fs-16">/</div>
      </div>
      <transition mode="out-in" name="fade">
        <div
          v-show="moreBtn"
          class="icon-more vci-bg-fill vci-pst-m"
          @click="$emit('moreData')" />
      </transition>
      <div class="bg-img vci-bg-fill vci-pst-m" />
      <div class="rect-decoration vci-flex-rmb vci-pst-abs">
        <div class="rect rect-0"></div>
        <div class="rect rect-1"></div>
      </div>
    </div>
    <div v-else class="title vci-flex-rml vci-bg-fill vci-pst-rlv">
      <div class="title-icon vci-bg-fill"></div>
      <div class="zh vci-fs-26">
        {{ zh }}
      </div>
      <div class="ver vci-fs-26">/</div>
      <div class="en font-oppo-b vci-fs-16">
        {{ en }}
      </div>
      <div class="bg-img vci-bg-fill vci-pst-m" />
      <div class="rect-decoration vci-flex-rmb vci-pst-abs">
        <div class="rect rect-0"></div>
        <div class="rect rect-1"></div>
      </div>
      <transition mode="out-in" name="fade">
        <div
          v-show="moreBtn"
          class="icon-more vci-bg-fill vci-pst-m"
          @click="$emit('moreData')" />
      </transition>
    </div>
    <div class="cont-frame vci-pst-rlv">
      <slot></slot>
    </div>
    <!-- <div class="bottom-decoration vci-bg-fill vci-pst-c" /> -->
    <VdQuadrilateralLineFLow />
  </div>
</template>

<script>
import MixinModuleParams from "@/views/common/mixins/MixinModuleParams";
import VdQuadrilateralLineFLow from "@/views/common/components/VdQuadrilateralLineFLow.vue";

export default {
  name: "BaseFrame",
  components: { VdQuadrilateralLineFLow },
  mixins: [MixinModuleParams],
  model: {
    props: "value",
    event: "updateValue",
  },
  props: {
    value: {
      type: [String, Number],
      default: null,
    },
  },
  data() {
    return {};
  },
  computed: {
    moreBtn() {
      return this.value === "1";
    },
  },
  methods: {
    changeComFn(item) {
      this.$emit("updateValue", item.value);
    },
  },
};
</script>

<style lang="scss" scoped>
.BaseFrame {
  background: linear-gradient(to right, rgba(#243a5c, 0.6), rgba(#243b5c, 0.6));
  backdrop-filter: blur(6px);
  .title-icon {
    width: 33px;
    height: 33px;
    margin-left: 4px;
    margin-right: 20px;
    background-image: url("./img/icon.decoration.png");
  }
  .rect-decoration {
    right: 0;
    bottom: 0;
    width: 36px;
    .rect {
      height: 3px;
      border-radius: 3px;
      background-color: #00b5db;
      &-0 {
        width: 8px;
      }
      &-1 {
        width: 24px;
      }
    }
  }
  .bg-img {
    z-index: -1;
    left: -10px;
    width: 766px;
    height: 49px;
    background-image: url("./img/bg.title.png");
  }
  .icon-more {
    right: 7px;
    width: 29px;
    height: 19px;
    background-image: url("./img/icon.more.png");
    cursor: pointer;
  }
  .switch-title {
    height: 44px;
    .sn {
      cursor: pointer;
      .sn-txt {
        letter-spacing: 4px;
        color: #99b5c9;
        transition: color 0.3s, text-shadow 0.3s;
        &::before {
          content: " ";
          position: absolute;
          bottom: 0;
          left: 0;
          width: 100%;
          height: 3px;
          background: linear-gradient(
            to bottom,
            rgba(#99b5c9, 0.23),
            #99b5c9,
            rgba(#99b5c9, 0.23)
          );
          transition: opacity 0.3s;
        }
      }
      .ver {
        margin: 12px;
        color: #00deff;
      }
      &.active {
        .sn-txt {
          color: #fff;
          text-shadow: 0 0 2px rgba(#006bff, 0.3), 0 0 4px rgba(#006bff, 0.6),
            0 0 6px rgba(#006bff, 0.6), 0 0 8px rgba(#006bff, 1),
            0 0 12px rgba(#006bff, 1);
          &::before {
            opacity: 0;
          }
        }
      }
      & + .sn {
        .ver {
          display: none;
        }
      }
    }
  }
  .title {
    height: 44px;
    font-size: 1rem !important;
    .zh {
      letter-spacing: 4px;
      text-shadow: 0 0 2px rgba(#006bff, 0.3), 0 0 4px rgba(#006bff, 0.6),
        0 0 6px rgba(#006bff, 0.6), 0 0 8px rgba(#006bff, 1),
        0 0 12px rgba(#006bff, 1);
      font-size: 1.2rem !important;
    }
    .ver {
      margin: 0 16px;
      color: #0077ff;
      opacity: 0.3;
    }
    .en {
      letter-spacing: 2px;
      color: #04f2ef;
      opacity: 0.23;
      font-size: 1.2rem !important;
    }
  }
  .cont-frame {
    height: calc(100% - 44px);
  }
  .bottom-decoration {
    bottom: 0;
    width: 776px;
    height: 13px;
    background-image: url("./img/bottom.decoration.png");
  }
}
</style>
