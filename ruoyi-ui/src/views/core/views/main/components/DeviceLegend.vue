<template>
  <div
    class="DeviceLegend vci-pst-abs vci-transition"
    :class="{ 'dl-drawing-mode': mapRiskArea.drawing }">
    <div
      v-for="option in options"
      :key="option.value"
      :class="{ active: value.includes(option.value) }"
      class="the-option vci-flex-rml vci-fs-18 font-oppo-b"
      @click="optionClick(option)">
      <div class="select-frame vci-pst-rlv">
        <div class="top vci-pst-cm"></div>
      </div>
      <div class="label">
        {{ option.label }}
      </div>
    </div>
  </div>
</template>

<script>
import MixinMapQt from "@vci/quick-three/components/MapD3/mixins/MixinMapQt";
import { mapWritableState } from "pinia";
import { storeCore } from "@/store/store.core";
import { cloneDeepByJSON } from "@vci/helper/src/object";

export default {
  name: "DeviceLegend",
  mixins: [MixinMapQt],
  data() {
    return {
      options: [
        {
          label: "车辆",
          value: "car",
        },
        {
          label: "人员",
          value: "worker",
        },
        {
          label: "摄像头",
          value: "camera",
        },
        {
          label: "传感器",
          value: "sensor",
        },
      ],
      value: [],
    };
  },
  computed: {
    ...mapWritableState(storeCore, ["mapRiskArea"]),
  },
  watch: {
    value: {
      deep: true,
      handler(value) {
        if (this.qt) {
          this.qt.thingPark.thingsCar.forEach((thing) =>
            value.includes("car") ? thing.fadeIn() : thing.fadeOut()
          );
          this.qt.thingPark.thingsWorker.forEach((thing) =>
            value.includes("worker") ? thing.fadeIn() : thing.fadeOut()
          );
          this.qt.thingPark.thingsCamera.forEach((thing) =>
            value.includes("camera") ? thing.fadeIn() : thing.fadeOut()
          );
          this.qt.thingPark.thingsSensor.forEach((thing) =>
            value.includes("sensor") ? thing.fadeIn() : thing.fadeOut()
          );
        }
      },
    },
    "mapRiskArea.drawing"(value) {
      if (value) {
        this.oldValue = cloneDeepByJSON(this.value);
        this.value = [];
      } else {
        this.value = this.oldValue;
        delete this.oldValue;
      }
    },
  },
  created() {
    this.value.push(...this.options.map((v) => v.value));
  },
  methods: {
    optionClick(option) {
      if (this.value.includes(option.value))
        this.value.splice(this.value.indexOf(option.value), 1);
      else this.value.push(option.value);
    },
  },
};
</script>

<style lang="scss" scoped>
.DeviceLegend {
  pointer-events: auto;
  top: 10vh;
  right: 26vw;
  width: 112px;
  padding: 20px 0 20px 14px;
  border-radius: 4px;
  background-color: rgba(#284865, 0.6);
  backdrop-filter: blur(6px);
  .the-option {
    cursor: pointer;
    color: #c7e7fd;
    .select-frame {
      width: 16px;
      height: 16px;
      border-radius: 4px;
      margin-right: 12px;
      border: 2px solid rgba(#8aa8c1, 0.6);
      .top {
        width: 7px;
        height: 7px;
        border-radius: 50%;
        border: 1px solid #fff;
        background-color: #56e9ff;
        box-shadow: 0 0 7px #0096ff;
        opacity: 0;
        transition: opacity 0.5s;
      }
    }
    & + .the-option {
      margin-top: 16px;
    }
    &.active {
      .top {
        opacity: 1;
      }
    }
  }
  &.dl-drawing-mode {
    pointer-events: none;
    opacity: 0;
  }
}
</style>
