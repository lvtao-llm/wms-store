<template>
  <button
    type="button"
    class="SearchIcon vci-bg-fill vci-tc-alpha"
    @click="searchHistoryTrajectory()"
  ></button>
</template>

<script>
import { mapActions } from "pinia";
import { storeCore } from "@/store/store.core";

export default {
  name: "SearchIcon",
  data() {
    return {};
  },
  methods: {
    ...mapActions(storeCore, ["searchHistoryTrajectory"])
  }
};
</script>


<style lang="scss" scoped>
.SearchIcon {
  width: 37px;
  height: 35px;
  background-image: url("./img/icon.png");
  pointer-events: auto;
}
</style>
