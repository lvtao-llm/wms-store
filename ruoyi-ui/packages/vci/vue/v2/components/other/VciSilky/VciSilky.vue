<template>
  <div class="VciSilky vci-size-100 vci-pst-rlv">
    <transition
      appear
      name="fade"
    >
      <div
        v-if="visibleLoading"
        ref="loading"
        class="vs-loading vci-flex-rcm vci-pst-abs vci-left-0 vci-top-0 vci-size-100"
      >
        <slot name="loading">
          <VciLoading v-bind="propsLoading" />
        </slot>
      </div>
    </transition>
    <transition
      appear
      name="fade"
    >
      <div
        v-if="visibleError"
        ref="error"
        class="vs-error-tip vci-flex-rcm vci-pst-abs vci-left-0 vci-top-0 vci-size-100"
      >
        <slot name="error">
          <strong>{{ errorText }}</strong>
        </slot>
      </div>
    </transition>
    <transition
      appear
      name="fade"
    >
      <div
        v-if="visibleContent"
        ref="view"
        class="vs-content vci-pst-rlv vci-size-100"
      >
        <slot></slot>
      </div>
    </transition>
  </div>
</template>

<script>

import VciLoading from "../VciLoading.vue";
import { isEmpty } from "@vci/helper/src/other";

export default {
  name: "VciSilky",
  components: { VciLoading },
  props: {
    state: {
      type: [String, Number],
      default: 1
    },
    loadingAnyway: {
      type: Boolean,
      default: false
    },
    propsLoading: {
      type: Object,
      default: null
    },
    enableErrorTip: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      vciState: 1,
      errorText: null,
      completed: false
    };
  },
  computed: {
    visibleLoading() {
      return this.vciState.toString() === "1" && (this.completed ? this.loadingAnyway : true);
    },
    visibleError() {
      return !isEmpty(this.errorText) && this.enableErrorTip;
    },
    visibleContent() {
      return this.completed && !this.visibleError;
    }
  },
  watch: {
    state: {
      immediate: true,
      handler(state) {
        this.vciState = state;
        if (isNaN(Number(state))) this.errorText = state || "模块异常";
        if (state.toString() === "2") this.errorText = null;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.VciSilky {
  .vs-loading {
    z-index: 3;
    backdrop-filter: blur(2px);
  }
  .vs-error-tip {
    z-index: 2;
    color: #fd7878;
  }
  .vs-content {z-index: 1;}
}
</style>