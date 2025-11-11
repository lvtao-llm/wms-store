<template>
  <div
    :class="{ 'vs-visible': visible }"
    class="VphState vci-flex-rcm vci-transition"
  >
    <transition mode="out-in" name="fade">
      <VphError v-if="error" key="ve" :text="error" />
      <TextBounce v-else-if="tip" key="jt" :text="tip" />
    </transition>
  </div>
</template>

<script>
// power by visual-ceiling, at 2022/11/21 13:27
import VphError from "./VphError.vue";
import TextBounce from "../../../text/TextBounce.vue";

export default {
  name: "VphState",
  components: { VphError, TextBounce },
  props: {
    tip: {
      type: String,
      default: null,
    },
    error: {
      type: String,
      default: null,
    },
  },
  data() {
    return {};
  },
  computed: {
    visible() {
      return this.tip || this.error;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "../../../../../../../../node_modules/@vci/style/src/mixins/font";
@import "@vci/style";
.VphState {
  position: relative;
  width: 100%;
  height: 100%;
  opacity: 0;
  backdrop-filter: blur(0px);
  pointer-events: none;
  // @include vci-font-stroke();
  &.vs-visible {
    opacity: 1;
    backdrop-filter: blur(8px);
    pointer-events: auto;
  }
}
</style>
