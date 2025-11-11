<template>
  <div class="ChartTooltip vci-fs-14">
    <strong class="ct-title vci-fs-16">{{ title }}</strong>
    <div
      v-if="list"
      class="ct-list"
    >
      <div
        v-for="(item, i) in listOp"
        :key="i"
        class="ci-item vci-flex-rml"
      >
        <span class="ci-name">{{ item.name }}</span>
        <NumberAnimation
          :enable-background="false"
          :numbers="item.value"
          :sp-three="true"
          class="ci-value vci-fw-bolder"
          height="16"
          width="8"
        />
        <strong class="ci-unit">{{ item.unit }}</strong>
      </div>
    </div>
  </div>
</template>

<script>


import NumberAnimation from "../text/NumberAnimation/NumberAnimation.vue";

export default {
  name: "ChartTooltip",
  components: { NumberAnimation },
  props: {
    title: {
      type: String,
      default: ""
    },
    list: {
      type: Array,
      default: null
    }
  },
  data() {
    return {};
  },
  computed: {
    listOp() {
      return this.list;
    }
  }
};
</script>

<style lang="scss" scoped>
.ChartTooltip {
  font-family: "Microsoft YaHei UI Light", sans-serif;
  padding: 8 hpx 24px;
  background-color: rgba(0, 0, 0, 0.4);
  border-radius: 2px;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(2px);
  color: #fff;
  line-height: 1;
  .ct-title {
    display: block;
    margin-bottom: 8px;
  }
  .ct-list {
    .ci-item {
      margin: 4 hpx 0;
      .ci-name {
        margin-right: 8px;
      }
      .ci-value {
        margin-right: 4px;
      }
    }
  }
}
</style>
