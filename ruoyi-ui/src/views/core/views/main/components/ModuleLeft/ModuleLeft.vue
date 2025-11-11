<template>
  <div class="ModuleLeft vci-pst-abs vci-flex-ccbt">
    <!-- 车辆、人员统计 -->
    <VehiclePerson class="vci-w-100 m10" en="STATISTICS" zh="车辆、人员统计" />
    <!-- 今日人员报警 -->
    <PersonnelAlarm
      class="vci-w-100 m10"
      en="PERSONNEL ALARM"
      zh="今日人员报警"
    />
    <!-- 今日车辆报警 -->
    <CarAlarm class="vci-w-100 m10" en="VEHICLE ALARM" zh="今日车辆报警" />
  </div>
</template>

<script>
import VehiclePerson from "./components/VehiclePerson/VehiclePerson.vue";
import CarAlarm from "./components/CarAlarm/CarAlarm.vue";
import PersonnelAlarm from "./components/PersonnelAlarm/PersonnelAlarm.vue";

export default {
  name: "ModuleLeft",
  components: { PersonnelAlarm, CarAlarm, VehiclePerson },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
@import "@vci/style";
.ModuleLeft {
  top: 9vh;
  left: 10px;
  width: 29vw;
  height: 85vh;
}
.m10 {
  // margin-top: 5vh;
}
</style>
