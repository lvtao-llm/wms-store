<template>
  <transition name="fade">
    <div
      v-if="htt && htt.duration > 0"
      class="VideoProgressBar vci-pst-c vci-bg-fill"
    >
      <div class="progress-inside vci-flex-rcm vci-pst-cm">
        <i
          :class="icon"
          class="iconfont play"
          @click="playOrPause()"
        />
        <span class="time-start">{{ startedAt }}</span>
        <vue-slider
          v-model="current"
          :max="Math.round(htt.duration)"
          v-bind="{ dotSize: 16, min: 0, interval: 0.1 }"
          @change="onSeek"
        >
          <template #tooltip="{focus}">
            <div
              :class="focus"
              class="mark vci-bg-fill"
            ></div>
          </template>
        </vue-slider>
        <span class="time-end">{{ endAt }}</span>
        <div
          v-if="htt && htt.speeds"
          class="speed-time vci-flex-rml"
        >
          <div
            v-for="(s,i) in htt.speeds"
            :key="i"
            :class="{'st-current': s === speed}"
            class="the-speed"
            @click.stop="switchSpeed(s, i)"
          >
            {{ s }}X
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import VueSlider from "vue-slider-component";
import "vue-slider-component/theme/antd.css";
import moment from "moment";
import MixinMapQt from "@vci/quick-three/components/MapD3/mixins/MixinMapQt";
import MixinClearScheduledTasks from "@vci/vci/vue/v2/mixins/MixinClearScheduledTasks";

export default {
  name: "VideoProgressBar",
  components: { VueSlider },
  mixins: [MixinClearScheduledTasks, MixinMapQt],
  data() {
    return {
      current: 0,
      speed: 1,
      icon: "icon-bofang_",
      htt: null
    };
  },
  computed: {
    startedAt() {
      if (this.htt && this.htt.startedAt) return moment(this.htt.startedAt + this.current * 1e3).format("YYYY-MM-DD HH:mm:ss");
      else return "";
    },
    endAt() {
      if (this.htt && this.htt.startedAt) return moment(this.htt.startedAt + this.htt.duration * 1e3).format("YYYY-MM-DD HH:mm:ss");
      else return "";
    }
  },
  watch: {
    htt: {
      deep: true,
      handler(htt) {
        htt && (this.current = htt.current);
      }
    }
  },
  created() {
    this.setHtt({
      startedAt: 0,
      current: 0,
      duration: 0,
      indexSpeed: 0,
      speeds: [1, 2, 4],
      paused: false
    });
    this.qt.thingPark.thingTrajectoryHistory.addEventListener("playing", e => {
      this.htt.startedAt = e.detail.startedAt;
      this.htt.current = e.detail.current;
      this.htt.duration = e.detail.duration;
    });
  },
  methods: {
    // 播放|暂停
    playOrPause() {
      // 播放轨迹
      if (this.icon === "icon-bofang") {
        this.icon = "icon-bofang_";
        this.switchSpeed(this.htt.speeds[this.htt.indexSpeed], this.htt.indexSpeed);
        this.qt.thingPark.thingTrajectoryHistory.action.paused = false;
        this.htt.paused = false;
      }
      // 暂停轨迹
      else {
        this.icon = "icon-bofang";
        this.qt.thingPark.thingTrajectoryHistory.action.halt(1);
        this.htt.paused = true;
      }
    },
    // 拖动进度
    onSeek() {
      this.qt.thingPark.thingTrajectoryHistory.action.time = this.current;
    },
    // 倍率调整
    switchSpeed(speed, indexSpeed) {
      this.speed = speed;
      this.htt.indexSpeed = indexSpeed;
      this.qt.thingPark.thingTrajectoryHistory.action.setEffectiveTimeScale(this.htt.speeds[indexSpeed]);
    },
    // 设置进度状态
    setHtt(htt) {
      this.htt = htt;
    },
    deleteHtt() {
      this.current = 0;
      this.speed = 1;
      this.icon = "icon-bofang_";
      this.htt = null;
    }
  }
};
</script>


<style lang="scss" scoped>
.VideoProgressBar {
  bottom: 32px;
  width: 970px;
  height: 99px;
  background-image: url("./img/bg.png");
  pointer-events: auto;
  .mark {
    width: 16px;
    height: 16px;
    background-image: url("./img/mark.png");
    cursor: pointer;
  }
  .progress-inside {
    width: 77%;
    .play {
      flex-shrink: 0;
      width: 32px;
      font-size: 32px;
      cursor: pointer;
      transition: all .3s;
    }
    .time-start, .time-end {
      display: block;
      width: 200px;
      margin: 0 6px;
      font-size: 16px;
      font-weight: bold;
      text-align: center;
    }
    .vue-slider {
      flex-shrink: 1;
      flex-grow: 1;
      width: 100% !important;
      height: 10hpx !important;
      margin: 0 16px !important;
    }
  }
  .speed-time {
    .the-speed {
      margin-left: 6px;
      padding: 5hpx 8px;
      border: 1px solid rgba(#fff, .5);
      border-radius: 5px;
      background-color: rgba(#0f213b, .4);
      font-size: 15px;
      font-weight: bold;
      cursor: pointer;
      transition: all .3s;
      & + .the-speed {
        margin-left: 4px;
      }
      &.st-current {
        background-color: rgba(#0f213b, 1);
      }
    }
  }
  :deep(.vue-slider-rail) {
    background-color: rgba(255, 255, 255, 0.38);
  }
  :deep(.vue-slider-process) {
    background: linear-gradient(to right, #7d9bca, #83e2fd);
  }
}
</style>
