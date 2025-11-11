<template>
  <div
    :style="{ width, height, background: displayBackground ? 'null' : 'none' }"
    class="NumberSingle"
  >
    <ul :class="active ? `number-${value}` : ''" class="ns-list">
      <li
        v-for="num in numbers"
        :key="`number-${num}`"
        :style="{ width, height }"
        class="ns"
      >
        {{ num }}
      </li>
    </ul>
  </div>
</template>

<script>
import MixinClearScheduleTasks from "../../../mixins/MixinClearScheduledTasks";
import { toVw } from "../../../../../other/adapt";

export default {
  name: "NumberSingle",
  mixins: [MixinClearScheduleTasks],
  props: {
    value: {
      default: 0,
      type: [Number, String],
    },
    // 动画延迟 单位: 100ms
    delay: {
      default: 0,
      type: Number,
    },
    width: {
      default: toVw(35),
      type: [String, Number],
    },
    height: {
      default: toVw(64),
      type: [String, Number],
    },
    displayBackground: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      numbers: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
      active: false,
      inter: {
        animation: -1,
      },
    };
  },
  watch: {
    value: {
      handler: "animationHandle",
      immediate: true,
    },
  },
  methods: {
    animationHandle() {
      clearTimeout(this.inter.animation);
      this.inter.animation = setTimeout(
        () => (this.active = true),
        this.index * 100
      );
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style/src/var";
@import "@vci/style";
.NumberSingle {
  width: 35px;
  height: 64px;
  background-repeat: no-repeat;
  background-position: center;
  background-size: 100% 100%;
  background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAA4CAYAAAB3yEEBAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFQTRGODQxMTI4NzUxMUVBQTBBNkMyQzZGNDVDRTQyOSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFQTRGODQxMjI4NzUxMUVBQTBBNkMyQzZGNDVDRTQyOSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkVBNEY4NDBGMjg3NTExRUFBMEE2QzJDNkY0NUNFNDI5IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkVBNEY4NDEwMjg3NTExRUFBMEE2QzJDNkY0NUNFNDI5Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+V6NwsgAABoRJREFUeNqcWUuOHVUM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);
  overflow: hidden;
  .ns-list {
    transition: $transition-slow;
    transition-duration: 1s;
    transform: translateY(10%);
    opacity: 0;
    .ns {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 35px;
      height: 64px;
    }
    @for $idx from 0 to 10 {
      &.number-#{$idx} {
        opacity: 1;
        transform: translateY(-#{10 * $idx + "%"});
      }
    }
  }
}
</style>
