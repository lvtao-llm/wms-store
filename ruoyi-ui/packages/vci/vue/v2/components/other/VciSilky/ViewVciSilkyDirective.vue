<template>
  <div
    v-silky.enable-error-tip="state"
    class="ViewVciSilkyDirective"
  >
    <div
      class="view vci-flex-rcm"
      style="height: 100%;"
    >
      <span>加载完成展示内容</span>
    </div>
    <div ref="error">
      自定义错误提示
    </div>
    <VciLoading
      ref="loading"
      text="自定义加载动画"
    />
  </div>
</template>

<script>
import { DirectiveSilky } from "../VpSilky/directive";
import MixinClearScheduledTasks from "../../../mixins/MixinClearScheduledTasks";
import VciLoading from "../VciLoading.vue";
import { delay } from "@/common/helper";
// 注入指令
DirectiveSilky();
export default {
  name: "ViewVciSilkyDirective",
  components: { VciLoading },
  mixins: [MixinClearScheduledTasks],
  data() {
    return {
      state: 1
    };
  },
  async mounted() {
    await delay(2e3);
    this.state = 2;
    await delay(2e3);
    this.state = 1;
    await delay(2e3);
    this.state = "错了，错了";
    await delay(2e3);
    this.state = 1;
    await delay(2e3);
    this.state = 2;
  }
};
</script>

<style lang="scss" scoped>
.ViewVciSilkyDirective {
  position: relative;
  width: 100%;
  height: 100%;
}
</style>