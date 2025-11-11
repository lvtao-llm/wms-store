<template>
  <div class="ListAnimation">
    <div
      v-for="(item, index) in listTransformed"
      :key="'animation+' + index"
      :class="[arrow, { 'la-active': item.active }]"
      :style="{
        transitionDuration: `${animation.duration}ms`,
        transitionTimingFunction: animation.transitionTimingFunction,
        ...(item.data.style || {}),
      }"
      class="la"
    >
      <slot
        v-bind="{
          item: item.data,
          index,
          listTransformed,
        }"
      >
        v-bind数据为数组元素{item, index, listTransformed}
      </slot>
    </div>
  </div>
</template>

<script>
import MixinClearScheduleTasks from "../../mixins/MixinClearScheduledTasks";
import { mergeDeep } from "@vci/helper/src/object";

export default {
  name: "ListAnimation",
  mixins: [MixinClearScheduleTasks],
  props: {
    // 列表数据
    list: {
      type: Array,
      default: null,
    },
    // 是否仅第一次有动画
    justAnimationOneTime: {
      type: Boolean,
      default: false,
    },
    // 运动方向
    arrow: {
      type: String,
      default: "toRight",
      validator(value) {
        return ["toRight", "toLeft", "toTop", "toBottom"].some(
          (a) => a === value
        );
      },
    },
    // 反向动画播放顺序
    reverseAnimationOrder: {
      type: Boolean,
      default: false,
    },
    // 列表动画配置
    animationOptions: {
      default: null,
      type: Object,
    },
  },
  data() {
    return {
      animated: false,
      animation: {
        // 动画个数
        amount: 20,
        // 列表元素动画间隔时间
        timeSplit: 100,
        // 当前正在执行动画的元素
        current: 0,
        // 动画执行时间
        duration: 500,
        // 动画函数
        transitionTimingFunction: "cubic-bezier(0.35, 0.12, 0.02, 0.99)",
      },
      listTransformed: [],
      inter: {
        animation: -1,
      },
    };
  },
  watch: {
    list: {
      immediate: true,
      handler: "analysis",
    },
  },
  methods: {
    analysis() {
      this.stopAnimation();
      if (this.list && this.list.length) {
        const listTransformed = [];
        this.animation = mergeDeep(this.animation, this.animationOptions);
        this.list.forEach((v, i) =>
          listTransformed.push({
            active:
              this.justAnimationOneTime && this.animated
                ? true
                : i >= this.animation.amount,
            data: v,
          })
        );
        this.listTransformed = listTransformed;
        this.bootAnimation();
        this.animated = true;
      } else this.listTransformed = [];
    },
    bootAnimation() {
      const max = Math.min(this.animation.amount, this.listTransformed.length);
      const reverseAnimationOrder = this.reverseAnimationOrder;
      this.inter.animation = setInterval(() => {
        if (this.animation.current >= max) {
          clearInterval(this.inter.animation);
          setTimeout(() => this.$emit("animationEnd"), this.animation.duration);
          return false;
        }
        if (!reverseAnimationOrder)
          this.listTransformed[this.animation.current].active = true;
        else
          this.listTransformed[max - 1 - this.animation.current].active = true;
        this.animation.current++;
      }, this.animation.timeSplit);
    },
    stopAnimation() {
      clearInterval(this.inter.animation);
      this.animation.current = 0;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style/src/var";
@import "@vci/style";
.ListAnimation {
  width: 100%;
  height: 100%;
  .la {
    opacity: 0;
    transition: $transition;
    &.toLeft {
      transform: translateX(50%);
    }
    &.toRight {
      transform: translateX(-50%);
    }
    &.toTop {
      transform: translateY(50%);
    }
    &.toBottom {
      transform: translateY(-50%);
    }
    &.la-active {
      opacity: 1;
      transform: translate(0, 0) !important;
    }
  }
}
</style>
