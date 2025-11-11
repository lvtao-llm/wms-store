<template>
  <transition name="fade">
    <div v-if="!mapRiskArea.drawing" class="ModelOperation vci-pst-abs">
      <div class="mode-type vci-flex-rmb vci-fs-18">
        <div
          v-for="m in modes"
          :key="m"
          :class="{ 'tm-current': mapPerspectiveMode === m }"
          class="the-mode vci-bg-fill vci-ta-c"
          @click="switchMode(m)"
        >
          <span class="label">{{ m }}</span>
        </div>
      </div>
      <div
        v-if="!mapRiskArea.drawing"
        class="icon-edit vci-bg-fill vci-pst-rlv"
        @click="qt.bootRiskAreaDrawing()"
      >
        <div class="icon vci-bg-fill vci-pst-cm"></div>
      </div>
    </div>
  </transition>
</template>

<script>
import MixinMapQt from "@vci/quick-three/components/MapD3/mixins/MixinMapQt";
import { PerspectiveModes } from "@/constants";
import { mapWritableState } from "pinia";
import { storeCore } from "@/store/store.core";

export default {
  name: "ModelOperation",
  mixins: [MixinMapQt],
  data() {
    return {
      modes: [PerspectiveModes.D2, PerspectiveModes.D3],
      currentMode: null,
    };
  },
  computed: {
    ...mapWritableState(storeCore, ["mapPerspectiveMode", "mapRiskArea"]),
  },
  methods: {
    switchMode(mode) {
      mode !== this.mapPerspectiveMode && this.qt.switchPerspectiveMode(mode);
    },
  },
};
</script>

<style lang="scss" scoped>
@import "@vci/style";
.ModelOperation {
  pointer-events: auto;
  top: 10vh;
  left: 30vw;
  .mode-type {
    width: 160px;
    .the-mode {
      width: 72px;
      height: 35px;
      background-image: url("./img/bg.1.png");
      line-height: 35px;
      cursor: pointer;
      transition: background-image 0.5s;
      .label {
        font-family: "oppo.b", sans-serif;
        transition: font-family 0.5s;
      }
      &.tm-current {
        background-image: url("./img/bg.2.png");
        .label {
          font-family: "oppo.r", sans-serif;
        }
      }
    }
  }
  .icon-edit {
    width: 48px;
    height: 48px;
    margin-top: 30px;
    background-image: url("./img/bg.edit.1.png");
    transition: background-image 0.5s;
    cursor: pointer;
    .icon {
      width: 23px;
      height: 23px;
      background-image: url("./img/icon.edit.png");
    }
    &.active {
      background-image: url("./img/bg.edit.2.png");
    }
    &:hover {
      background-image: url("./img/bg.edit.2.png");
    }
  }
}
</style>
