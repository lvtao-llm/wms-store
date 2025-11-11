<template>
  <div class="HistoryTrajectory vci-pst-abs">
    <div class="name-date vci-pst-c">
      <SelectSearch />
      <DateRange />
      <SearchIcon />
    </div>
    <!-- 进度条 -->
    <transition name="fade">
      <VideoProgressBar v-if="mapHistorySearch.isDisplayTrajectoryHistory" />
    </transition>
    <!-- 退出按钮 -->
    <transition name="fade">
      <ExitBtn
        v-if="mapHistorySearch.isDisplayTrajectoryHistory"
        @click.native="qt.thingPark.clearTrajectoryHistory()" />
    </transition>
  </div>
</template>

<script>
import ExitBtn from "@/views/common/components/ExitBtn/ExitBtn.vue";
import SelectSearch from "@/views/common/components/SelectSearch/SelectSearch.vue";
import DateRange from "@/views/common/components/DateRange/DateRange.vue";
import SearchIcon from "@/views/common/components/SearchIcon/SearchIcon.vue";
import { mapState } from "pinia";
import { storeCore } from "@/store/store.core";
import MixinMapQt from "@vci/quick-three/components/MapD3/mixins/MixinMapQt";
import VideoProgressBar from "@/views/common/components/VideoProgressBar/VideoProgressBar.vue";

export default {
  name: "HistoryTrajectory",
  components: {
    VideoProgressBar,
    SearchIcon,
    DateRange,
    SelectSearch,
    ExitBtn,
  },
  mixins: [MixinMapQt],
  data() {
    return {};
  },
  computed: {
    ...mapState(storeCore, ["mapHistorySearch"]),
  },
};
</script>

<style lang="scss" scoped>
.HistoryTrajectory {
  top: -6vh;
  left: 10vw;
  width: 100%;
  height: 100%;
  pointer-events: none;
  .name-date {
    top: 150px;
    width: 740px;
    display: flex;
  }
}
</style>
