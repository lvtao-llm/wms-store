<template>
  <div class="MaterialStatisticsTooltip font-oppo-b vci-bg-fill">
    <div class="title">
      {{ title }}
    </div>
    <div
      v-for="item in extData"
      :key="item.seriesName"
      class="the-line"
    >
      <span class="name">{{ item["seriesName"] }}</span>
      <span class="value">{{ item["value"] }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "MaterialStatisticsTooltip",
  data() {
    return {
      extData: []
    };
  },
  computed: {
    title() {
      return this.extData.length ? this.extData[0].name : "";
    }
  }
};
</script>


<style lang="scss" scoped>
.MaterialStatisticsTooltip {
  width: 135px;
  height: 67px;
  background-image: url("./img/bg.tooltip.png");
  color: #c7e7fd;
  .title {
    margin-left: 16px;
    color: #a4d0fa;
  }
  .the-line {
    margin-left: 16px;
    .name {
      margin-right: 12px;
    }
  }
}
</style>
