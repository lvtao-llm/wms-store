<template>
  <div class="PopupEdit vci-bg-fill">
    <div class="title font-pm-zd vci-fs-28">
      编辑区域
    </div>
    <div class="area-name vci-flex-rmb">
      <div class="name vci-ta-c">
        区域名称
      </div>
      <el-input
        v-model="mapRiskArea.info.name"
        placeholder="请填入区域名称"
      />
    </div>
    <div class="area-type vci-flex-rmb">
      <div class="name vci-ta-c">
        区域类型
      </div>
      <el-select v-model="mapRiskArea.info.idType">
        <el-option
          v-for="v in mapRiskArea.types"
          :key="v.value"
          :label="v.name"
          :value="v.value"
        ></el-option>
      </el-select>
    </div>
    <div class="area-color vci-flex-rmb">
      <div class="name vci-ta-c">
        区域颜色
      </div>
      <div class="vci-flex-rml">
        <span
          class="vci-fs-14"
          style="width: 112px;padding-right: 12px;"
        >
          {{ mapRiskArea.info.color }}
        </span>
        <el-color-picker
          v-model="mapRiskArea.info.color"
          size="small"
          color-format="hex"
          @active-change="changeColor"
        />
      </div>
    </div>
    <div class="pe-tips vci-flex vci-fs-12">
      <span>鼠标左键点选绘制区域</span>
    </div>
    <div class="btn-frame vci-flex-rmb vci-ta-c">
      <div
        class="btn btn-sure"
        @click="saveRad"
      >
        确定
      </div>
      <div
        class="btn btn-cancel"
        @click="cancelRad"
      >
        取消
      </div>
    </div>
  </div>
</template>

<script>
import MixinMapQt from "@vci/quick-three/components/MapD3/mixins/MixinMapQt";
import { mapActions, mapWritableState } from "pinia";
import { storeCore } from "@/store/store.core";
import { isEmpty } from "@vci/helper/src/other";
import { PluginNotification } from "@vci/vci/vue/v2/components/other/VpNotification";
import { PerspectiveModes, TypesRiskArea } from "@/constants";
import { rgbToHex } from "@vci/helper/src/color";

export default {
  name: "PopupEdit",
  mixins: [MixinMapQt],
  computed: {
    ...mapWritableState(storeCore, ["mapRiskArea"])
  },
  watch: {
    "mapRiskArea.info.idType": {
      immediate: true,
      handler(idType) {
        this.mapRiskArea.info.color = TypesRiskArea.find(v => v.value === idType).color;
      }
    },
    "mapRiskArea.info.color": {
      handler(color) {
        const thingArea = this.qt.thingRiskArea.getThingArea();
        thingArea
          .update({ color })
          .updateShapeTexture()
          .updateMarkerInfo(this.mapRiskArea.info.idType)
        ;
      }
    },
    "mapRiskArea.info.name": {
      handler(name) {
        const thingArea = this.qt.thingRiskArea.getThingArea();
        thingArea
          .update({ name })
          .updateText(name)
          .updateMarkerInfo()
        ;
      }
    }
  },
  created() {
    if (!this.mapRiskArea.isUpdate) {
      const thingArea = this.qt.thingRiskArea.createThingArea();
      thingArea
        .update({ color: this.mapRiskArea.info.color })
        .updateShapeTexture();
    } else {
      const thingArea = this.qt.thingRiskArea.getThingArea();
      thingArea._info = {
        name: thingArea.extData.name,
        idType: thingArea.extData.idType,
        color: thingArea.extData.color
      };
    }
  },
  methods: {
    ...mapActions(storeCore, ["resetMapRiskAreaInfo"]),
    async saveRad() {
      if (isEmpty(this.mapRiskArea.info.name)) {
        PluginNotification.error("请输入区域名");
        return Promise.resolve();
      }
      if (isEmpty(this.mapRiskArea.info.idType)) {
        PluginNotification.error("请选择区域类型");
        return Promise.resolve();
      }
      const { qt } = this;
      const saveSuccess = await qt.thingRiskArea.saveThingArea({
        id: qt.thingRiskArea.getThingArea().id,
        name: this.mapRiskArea.info.name,
        idType: this.mapRiskArea.info.idType,
        color: this.mapRiskArea.info.color
      });
      if (saveSuccess) {
        this.mapRiskArea.isUpdate && qt.switchPerspectiveMode(PerspectiveModes.D3);
        this.resetMapRiskAreaInfo();
        qt.shutdownRiskAreaDrawing();
      }
    },
    cancelRad() {
      const { qt } = this;
      if (!this.mapRiskArea.isUpdate) qt.thingRiskArea.deprecatedThingArea();
      else {
        const thingArea = qt.thingRiskArea.getThingArea();
        const info = thingArea._info;
        delete thingArea._info;
        thingArea
          .update({ ...info })
          .updateText()
          .updateMarkerInfo()
          .updateShapeTexture()
        ;
      }
      this.resetMapRiskAreaInfo();
      qt.shutdownRiskAreaDrawing();
      qt.switchPerspectiveMode(PerspectiveModes.D3);
    },
    changeColor(colorRgb) {
      this.mapRiskArea.info.color = rgbToHex(colorRgb);
    }
  }
};
</script>


<style lang="scss" scoped>
.PopupEdit {
  width: 430px;
  height: 289px;
  padding-top: 52px;
  padding-left: 50px;
  background-image: url("./img/bg.png");
  box-sizing: border-box;
  .title {
    margin-left: 40px;
    margin-bottom: 6px;
    color: #dbf3ff;
    letter-spacing: 2px;
    transform: skewX(-15deg);
    text-shadow: 0 2px 2px #01133b,
    0 2px 2px #01133b,
    0 2px 2px #01133b,
    0 2px 2px #01133b;
  }
  :deep(.el-input) {
    width: 160px;
    height: 30px;
    .el-input__inner {
      width: 100%;
      height: 30px;
      line-height: 30px;
      background-color: transparent;
      border: none;
      color: #dbf3ff;
    }
    .el-input__icon {
      line-height: 30px;
    }
  }
  .name {
    width: 112px;
    height: 30px;
    background-color: rgba(#7abfff, .25);
    line-height: 30px;
    border-radius: 2px;
  }
  .area-name {
    margin-top: 2px;
  }
  .area-type {
    margin-top: 2px;
  }
  .area-name, .area-type, .area-color {
    width: 353px;
    height: 40px;
    border-bottom: 1px solid rgba(#77a1cf, .52);
  }
  .pe-tips {
    justify-content: flex-end;
    padding-right: 24px;
    opacity: .6;
    padding-bottom: 6px;
  }
  .btn-frame {
    width: 156px;
    margin: 2px auto 0;
    .btn {
      width: 73px;
      height: 30px;
      border-radius: 2px;
      background-color: rgba(#7acfff, .25);
      line-height: 30px;
      cursor: pointer;
      transition: background-color .3s;
      &:hover {
        background-color: #7acfff;
      }
    }
  }
}
</style>
