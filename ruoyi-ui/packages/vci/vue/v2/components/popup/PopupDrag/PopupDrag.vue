<template>
  <div
    v-if="visible"
    :class="[{ 'pd-no-transition': isDragging }, vmClass]"
    :style="[vmStyle, { transform: `translate(${offsetX}px, ${offsetY}px)` }]"
    class="PopupDrag"
    @click="toppingPopup(true)"
  >
    <div ref="view" class="pd-view">
      <i class="pd-drag" @mousedown.self="dragHandle"></i>
      <slot v-bind="slotProps"></slot>
    </div>
  </div>
</template>

<script>
import {
  getElementBody,
  getElementMaxZIndex,
  getElementPropertyOfNumber,
} from "@vci/helper/src/element";
import { isEmpty } from "@vci/helper/src/other";
import MixinClearScheduleTasks from "../../../mixins/MixinClearScheduledTasks";

export default {
  name: "PopupDrag",
  mixins: [MixinClearScheduleTasks],
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    zIndex: {
      type: [String, Number],
      default: null,
    },
    left: {
      type: [String, Number],
      default: 0,
    },
    top: {
      type: [String, Number],
      default: 0,
    },
    toBody: {
      type: Boolean,
      default: false,
    },
    animation: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      vmClass: "pd-hide",
      vmStyle: {
        zIndex: 8,
      },
      isDragging: false,
      offsetX: 0,
      offsetY: 0,
      inter: -1,
    };
  },
  computed: {
    slotProps() {
      return {
        close: this.close,
      };
    },
  },
  watch: {
    visible: {
      immediate: true,
      handler(value, oldValue) {
        clearTimeout(this.inter);
        if (this.visible) {
          this.$emit("beforeOpen", this);
          if (!isEmpty(this.zIndex)) this.vmStyle.zIndex = this.zIndex;
          else this.toppingPopup();
          this.inter = setTimeout(() => {
            this.vmClass = "pd-show pd-show-" + this.animation;
            this.inter = setTimeout(() => this.$emit("afterOpen", this), 350);
          }, 50);
        } else {
          !isEmpty(oldValue) && this.close();
        }
      },
    },
    zIndex: {
      immediate: true,
      handler(zIndex) {
        if (isEmpty(zIndex)) this.vmStyle.zIndex = getElementMaxZIndex();
        else this.vmStyle.zIndex = zIndex;
      },
    },
    left: {
      immediate: true,
      handler(left) {
        this.vmStyle.left = left;
        this.offsetX = this.offsetY = 0;
      },
    },
    top: {
      immediate: true,
      handler(top) {
        this.vmStyle.top = top;
        this.offsetX = this.offsetY = 0;
      },
    },
  },
  mounted() {
    this.$nextTick(this.init.bind(this));
  },
  beforeDestroy() {
    if (this._elParent) this._elParent.appendChild(this.$el);
  },
  methods: {
    init() {
      if (this.toBody) this.appendToBody();
    },
    toppingPopup() {
      this.vmStyle.zIndex = getElementMaxZIndex();
    },
    appendToBody() {
      this._elParent = this.$el.parentNode;
      getElementBody().appendChild(this.$el);
    },
    close() {
      this.$emit("beforeClose", this);
      clearTimeout(this.inter);
      this.vmClass = "pd-hide pd-hide-" + this.animation;
      this.inter = setTimeout(() => {
        this.$emit("close", this);
      }, 350);
    },
    dragHandle(e) {
      this.isDragging = true;
      this.toppingPopup();
      const start = {
        x: e.clientX,
        y: e.clientY,
      };
      const fnMove = (em) => {
        const move = {
          x: em.clientX - start.x,
          y: em.clientY - start.y,
        };
        start.x += move.x;
        start.y += move.y;
        this.offsetX += move.x;
        this.offsetY += move.y;
        const left = getElementPropertyOfNumber(this.$el, "left");
        const top = getElementPropertyOfNumber(this.$el, "top");
        if (left + this.offsetX + this.$el.clientWidth > window.innerWidth)
          this.offsetX = window.innerWidth - this.$el.clientWidth - left;
        else if (left + this.offsetX < 0) this.offsetX = -left;
        if (top + this.offsetY + this.$el.clientHeight > window.innerHeight)
          this.offsetY = window.innerHeight - this.$el.clientHeight - top;
        else if (top + this.offsetY < 0) this.offsetY = -top;
      };
      const fnUp = () => {
        this.isDragging = false;
        window.removeEventListener("mousemove", fnMove);
        window.removeEventListener("mouseup", fnUp);
      };
      window.addEventListener("mousemove", fnMove);
      window.addEventListener("mouseup", fnUp);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style/src/var";
@import "@vci/style";
.PopupDrag {
  position: fixed;
  left: 0;
  top: 0;
  z-index: 8;
  transition: $transition;
  .pd-view {
    position: relative;
    min-width: 100px;
    min-height: 100px;
    transition: $transition-slow;
    backdrop-filter: blur(0);
    opacity: 0;
    .pd-drag {
      position: absolute;
      left: 0;
      top: 0;
      z-index: 2;
      width: 100%;
      height: 32px;
      cursor: all-scroll;
      user-select: none;
    }
  }
  &.pd-no-transition {
    transition: none;
  }
  &.pd-show {
    .pd-view {
      opacity: 1;
      backdrop-filter: blur(2px);
    }
  }
  &.pd-hide {
  }
}
</style>
