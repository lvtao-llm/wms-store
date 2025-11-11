<template>
  <div class="VciScroll" @mouseenter="enter" @mouseleave="leave">
    <div
      ref="scrollView"
      :style="styleScrollView"
      class="vs-view"
      @scroll="scrollHandle()"
    >
      <div ref="scrollContent" class="vv-body">
        <slot></slot>
      </div>
    </div>
    <div
      v-if="!isMobile"
      ref="slotVertical"
      :style="[{ opacity: slotBar.vertical.visible ? 1 : 0 }, styleBarSLot]"
      class="vs-bar vertical"
    >
      <i
        :class="{ 'no-transition': !slotBar.vertical.enableTransition }"
        :style="[
          {
            transform: `translateY(${slotBar.vertical.top}px)`,
            height: `${slotBar.vertical.height}px`,
          },
          styleBarVertical,
        ]"
        class="vb"
        @mousedown="controlBar"
      ></i>
    </div>
    <div
      v-if="!isMobile"
      ref="slotHorizontal"
      :style="[
        { opacity: slotBar.horizontal.visible && visibleHorizontalBar ? 1 : 0 },
        styleBarSLot,
      ]"
      class="vs-bar horizontal"
    >
      <i
        :class="{ 'no-transition': !slotBar.horizontal.enableTransition }"
        :style="[
          {
            transform: `translateX(${slotBar.horizontal.left}px)`,
            width: `${slotBar.horizontal.width}px`,
          },
          styleBarHorizontal,
        ]"
        class="vb"
        @mousedown="controlBar($event, true)"
      ></i>
    </div>
  </div>
</template>

<script>
import { hexToRgba } from "@vci/helper/src/color";
import { client } from "@vci/helper/src/browser";

const isMobile = client().isMobile;
function computeScrollbarSize() {
  // 创建一个隐藏的div元素，并将其添加到页面中
  const el = document.createElement("div");
  el.style.visibility = "hidden";
  el.style.width = "100px";
  el.style.overflow = "scroll";
  document.body.appendChild(el);
  // 计算滚动条的宽度
  const sizeScrollbar = el.offsetWidth - el.clientWidth;
  // 删除创建的div元素
  document.body.removeChild(el);
  return sizeScrollbar;
}
export default {
  name: "VciScroll",
  props: {
    visibleHorizontalBar: {
      type: Boolean,
      default: true,
    },
    theme: {
      type: String,
      default: "#fff",
    },
    themeEnd: {
      type: String,
      default: "#fff",
    },
  },
  data() {
    return {
      isMobile,
      scrollBar: {
        slotSize: computeScrollbarSize(),
        scrollTop: 0,
      },
      slotBar: {
        vertical: {
          visible: false,
          top: 0,
          height: 0,
          enableTransition: true,
        },
        horizontal: {
          visible: false,
          left: 0,
          width: 0,
          enableTransition: true,
        },
      },
    };
  },
  computed: {
    styleScrollView() {
      return {
        width: `calc(100% + ${isMobile ? 0 : this.scrollBar.slotSize}px)`,
        height: `calc(100% + ${isMobile ? 0 : this.scrollBar.slotSize}px)`,
      };
    },
    scrollTop() {
      return this.scrollBar.scrollTop;
    },
    styleBarVertical() {
      return {
        background: `linear-gradient(180deg, ${hexToRgba(this.theme, 0.2)}, ${
          this.themeEnd ? hexToRgba(this.themeEnd, 0.2) : "transparent"
        })`,
      };
    },
    styleBarHorizontal() {
      return {
        background: `linear-gradient(90deg, ${hexToRgba(this.theme, 0.5)}, ${
          this.themeEnd ? hexToRgba(this.themeEnd, 0.5) : "transparent"
        })`,
      };
    },
    styleBarSLot() {
      return {
        backgroundColor: hexToRgba(this.theme, 0.1),
      };
    },
  },
  mounted() {
    this.$nextTick(() => !this["_isDestroyed"] && this.buildMutationObserver());
  },
  methods: {
    // 内容滚动处理
    scrollHandle(enableAutoHideScrollBar = false) {
      const {
        elSlotVerticalHeight,
        elViewHeight,
        elContentHeight,
        elViewScrollTop,
        elContentWidth,
        elViewWidth,
        elSlotHorizontalWidth,
        elViewScrollLeft,
      } = this.getStructure();
      this.scrollBar.scrollTop = elViewScrollTop;
      this.$emit("scroll", { top: elViewScrollTop, left: elViewScrollLeft });
      if (elContentHeight - elViewScrollTop - elViewHeight < 20)
        this.$emit("reachBottom");
      if (isMobile) return false;
      // 判断是否展示滚动条 - 纵向
      this.slotBar.vertical.visible = elContentHeight > elViewHeight;
      // 计算滚动条尺寸 - 纵向
      if (this.slotBar.vertical.visible) {
        this.slotBar.vertical.height =
          (elSlotVerticalHeight * elViewHeight) / elContentHeight;
        this.slotBar.vertical.top =
          elViewScrollTop * (elSlotVerticalHeight / elContentHeight);
      }
      // 判断是否展示滚动条 - 横向
      this.slotBar.horizontal.visible = elContentWidth > elViewWidth;
      // 计算滚动条尺寸 - 横向
      if (this.slotBar.horizontal.visible) {
        this.slotBar.horizontal.width =
          (elSlotHorizontalWidth * elViewWidth) / elContentWidth;
        this.slotBar.horizontal.left =
          elViewScrollLeft * (elSlotHorizontalWidth / elContentWidth);
      }
      enableAutoHideScrollBar && this.leave();
    },
    // 纵向滚动条控制
    controlBar(e, isHorizontal = false) {
      const {
        elView,
        elContentHeight,
        elSlotVerticalHeight,
        elContentWidth,
        elSlotHorizontalWidth,
      } = this.getStructure();
      if (isHorizontal) this.slotBar.horizontal.enableTransition = false;
      else this.slotBar.vertical.enableTransition = false;
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
        if (isHorizontal)
          elView.scrollLeft +=
            move.x * (elContentWidth / elSlotHorizontalWidth);
        else
          elView.scrollTop += move.y * (elContentHeight / elSlotVerticalHeight);
      };
      const fnUp = () => {
        window.removeEventListener("mousemove", fnMove);
        window.removeEventListener("mouseup", fnUp);
        if (isHorizontal) this.slotBar.horizontal.enableTransition = true;
        else this.slotBar.vertical.enableTransition = true;
      };
      window.addEventListener("mousemove", fnMove);
      window.addEventListener("mouseup", fnUp);
    },
    // 获取架构基础元素
    getStructure() {
      const elView = this.$refs.scrollView;
      const elContent = this.$refs.scrollContent;
      // 纵向滚动条
      const elSlotVertical = this.$refs.slotVertical;
      const elViewHeight = elView.clientHeight;
      const elContentHeight = elContent.clientHeight;
      const elSlotVerticalHeight = isMobile
        ? null
        : elSlotVertical.clientHeight;
      const elViewScrollTop = elView.scrollTop;
      // 横向滚动条
      const elSlotHorizontal = this.$refs.slotHorizontal;
      const elViewWidth = elView.clientWidth;
      const elContentWidth = elContent.scrollWidth;
      const elSlotHorizontalWidth = isMobile
        ? null
        : elSlotHorizontal.clientWidth;
      const elViewScrollLeft = elView.scrollLeft;
      return {
        elView,
        elContent,
        elSlotVertical,
        elViewHeight,
        elContentHeight,
        elSlotVerticalHeight,
        elViewScrollTop,
        elSlotHorizontal,
        elViewWidth,
        elContentWidth,
        elSlotHorizontalWidth,
        elViewScrollLeft,
      };
    },
    scrollTo(target = 0) {
      const { elView, elViewHeight, elContentHeight } = this.getStructure();
      elView.scrollTop = Math.min(
        Math.max(target, 0),
        elContentHeight - elViewHeight
      );
      return this;
    },
    scrollLeft(target = 0) {
      const { elView, elViewWidth, elContentWidth } = this.getStructure();
      elView.scrollLeft = Math.min(
        Math.max(target, 0),
        elContentWidth - elViewWidth
      );
      return this;
    },
    // 是否存在滚动条
    hasScrollBar() {
      const { elViewHeight, elContentHeight, elViewWidth, elContentWidth } =
        this.getStructure();
      return new Promise((resolve) => {
        this.$nextTick(() =>
          resolve(
            elContentHeight > elViewHeight || elContentWidth > elViewWidth
          )
        );
      });
    },
    // 更新滚动条
    updateScrollBar(enableAutoHideScrollBar = false) {
      this.scrollBar.slotSize = computeScrollbarSize();
      this.$nextTick(this.scrollHandle.bind(this, enableAutoHideScrollBar));
    },
    // 监听子节点变动来更新滚动条
    buildMutationObserver() {
      // 初始化更新一次滚动条
      this.updateScrollBar(true);
      // 添加子节点监听
      this.observer = new MutationObserver(this.updateScrollBar.bind(this));
      this.observer.observe(this.$refs.scrollView, {
        childList: true,
        subtree: true,
      });
      this.$once("hook:beforeDestroy", () => this.observer.disconnect());
      // 窗口变化监听
      this.resize();
    },
    // 窗口变化监听
    resize() {
      const resize = () => {
        clearTimeout(this._resize);
        this._resize = setTimeout(this.updateScrollBar.bind(this, true), 350);
      };
      this._resize = -1;
      window.addEventListener("resize", resize);
      this.$once("hook:beforeDestroy", () =>
        window.removeEventListener("resize", resize)
      );
    },
    // 鼠标移入
    enter() {
      clearTimeout(this.interVisibleScrollBar);
      this.updateScrollBar(true);
    },
    // 鼠标移出
    leave() {
      clearTimeout(this.interVisibleScrollBar);
      this.interVisibleScrollBar = setTimeout(() => {
        this.slotBar.vertical.visible = false;
        this.slotBar.horizontal.visible = false;
      }, 1e3);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style/src/var";
@import "@vci/style";
.VciScroll {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  .vs-view {
    position: relative;
    z-index: 1;
    width: 100%;
    height: 100%;
    overflow: scroll;
    .vv-body {
      width: 100%;
      height: 100%;
      position: relative;
      width: auto;
    }
  }
  .vs-bar {
    position: absolute;
    z-index: 2;
    transition: $transition-slow;
    .vb {
      display: block;
      cursor: pointer;
      transition: $transition-quick;
      border-radius: 4px;
      user-select: none;
      &.no-transition {
        transition: none;
      }
    }
    &.vertical {
      right: 4px;
      top: 8px;
      width: 6px;
      height: calc(100% - 16px);
      overflow: hidden;
      border-radius: 2px;
      .vb {
        width: 100%;
      }
    }
    &.horizontal {
      left: 8px;
      bottom: 4px;
      height: 6px;
      width: calc(100% - 16px);
      overflow: hidden;
      border-radius: 2px;
      .vb {
        height: 100%;
        &.no-transition {
          transition: none;
        }
      }
    }
  }
}
</style>
