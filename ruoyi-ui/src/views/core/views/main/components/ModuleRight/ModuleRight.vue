<template>
  <div class="ModuleRight vci-pst-abs vci-flex-ccbt">
    <!-- 物料统计 -->
    <MaterialStatistics
      class="vci-w-100"
      en="MATERIAL STATISTICS"
      zh="物料统计" />
    <!-- 高风险区域统计 -->
    <RegionalStatistics
      class="vci-w-100"
      en="STATISTICS ON HIGH-RISK AREAS"
      zh="高风险区域统计" />
  </div>
</template>

<script>
import MaterialStatistics from "./components/MaterialStatistics/MaterialStatistics.vue";
import RegionalStatistics from "./components/RegionalStatistics/RegionalStatistics.vue";

export default {
  name: "ModuleRight",
  components: { RegionalStatistics, MaterialStatistics },
  data() {
    return {};
  },
};
</script>

<style lang="scss" scoped>
.ModuleRight {
  top: 9vh;
  right: 10px;
  width: 25vw;
  height: 85vh;
}
</style>
