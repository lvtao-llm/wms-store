<template>
  <div class="MapQa">
    <div
      ref="map"
      class="dm vci-size-100"
    ></div>
    <slot
      ref="loading"
      name="loading"
    ></slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2024/11/13 15:55

import QuickAMap from "./../../src/index";
import MixinMapQa from "./mixins/MixinMapQa";
import { EventsQa } from "./../../src/events/EventsQa";
import { createLoaderGltf } from "@vci/quick-three/src/creator/loaders/createLoaderGltf";
import { PluginEdit } from "@vci/quick-three/src/plugins/PluginEdit";
import { createEnvEXR } from "@vci/quick-three/src/creator/env/createEnvEXR";
import { PluginRenderer2D } from "@vci/quick-three/src/plugins/PluginRenderer2D";
import { PluginLoadingManager } from "@vci/quick-three/src/plugins/PluginLoadingManager";
import { PluginLoading } from "@vci/quick-three/src/plugins/PluginLoading";
import { Euler } from "three";
import { degToRad } from "three/src/math/MathUtils";

export default {
  name: "MapQa",
  mixins: [MixinMapQa],
  props: {
    urlExr: {
      type: String,
      default: null
    }
  },
  data() {
    return {};
  },
  async mounted() {
    await this.$nextTick();
    const center = [105.917707, 29.367904];
    const qa = new QuickAMap({
      el: this.$refs.map,
      map: {
        mapOption: {
          center,
          zoom: 13.5,
          pitch: 47,
          rotation: 0,
          zooms: [2, 26],
          mapStyle: "amap://styles/d90b3b8d32158446651e7c3cd123036b",
          viewMode: "3D"
        },
        loader: {
          key: "ca9a1da3fead25c2ad41768bce85a816",
          Loca: {
            version: "2.0.0"
          }
        }
      }
    });
    qa.addEventListener(EventsQa.AfterMapComplete, async e => {
      const { map } = e.detail;
      // 创建weg图层
      const layerGL = await qa.createLayerGL({ offsetOfLnglat: center });
      // 将qa|qt全域化
      this.setQa(qa, map, layerGL);
      const { qt } = layerGL;
      // qt|追加渲染器插件
      qt.use(PluginRenderer2D, PluginRenderer2D.namespace);
      qt.use(PluginLoadingManager, PluginLoadingManager.namespace);
      qt.use(PluginLoading, PluginLoading.namespace, this.$refs.loading);
      // qt|创建资源加载器
      qt.loaderGltf = createLoaderGltf({ qt });
      // qt|载入环境贴图
      this.urlExr && await createEnvEXR(qt, this.urlExr);
      qt.scene.background = null;
      qt.scene.environmentRotation = qt.scene.backgroundRotation = new Euler(degToRad(90), degToRad(0), degToRad(135));
      // qt|设置进入编辑模式后对地图的操作限制
      const _event_stop = e => e.stopPropagation();
      qt.addEventListener(PluginEdit.Events.DragStartEditThing, () => qt.el.addEventListener("mousemove", _event_stop));
      qt.addEventListener(PluginEdit.Events.DragEndEditThing, () => qt.el.removeEventListener("mousemove", _event_stop));
      qt.addEventListener(PluginEdit.Events.DraggingEditThing, e => {
        const thing = e.detail.transformThing;
        console.info("被编辑物体的最新经纬度: ", layerGL.coordsToLnglat(thing.position.slice(0, 2)));
      });
      qt.addEventListener(PluginEdit.Events.AfterInEdit, () => {
        qt._map_pitch = map.getPitch();
        qt._map_rotation = map.getRotation();
        map.setPitch(0, false, 1e3);
        map.setRotation(0, false, 1e3);
      });
      qt.addEventListener(PluginEdit.Events.AfterExitEdit, () => {
        map.setPitch(qt._map_pitch, false, 1e3);
        map.setRotation(qt._map_rotation, false, 1e3);
        delete qt._map_pitch;
        delete qt._map_rotation;
      });
      // 动画入场
      await layerGL.qt.animation.animationInAsync();
      // 触发地图准备就绪事件
      this.$emit("ready");
    });
  }
};
</script>

<style lang="scss" scoped>
.MapQa {
  position: relative;
  width: 100%;
  height: 100%;
  .dm {
    position: relative;
    z-index: 1;
  }
}
</style>