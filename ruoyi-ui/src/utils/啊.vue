<template>
  <div class="MapD3">
    <VciD3 :before-init-qt="beforeInitQt" class="vci-size-100" @ready="ready" />
    <slot></slot>
  </div>
</template>

<script>
// power by visual-ceiling, at 2024/7/8 上午11:29
import VciD3 from "./../VciD3.vue";
import { mergeDeep } from "@vci/helper/src/object";
import { getChunkPlugins } from "./../../src/plugins/chunk";
import {
  FileLoader,
  Mesh,
  MeshBasicMaterial,
  PlaneGeometry,
  SRGBColorSpace,
  TextureLoader,
  Vector2,
} from "three";
import { degToRad } from "three/src/math/MathUtils";
import { createLoaderGltf } from "./../../src/creator/loaders/createLoaderGltf";
import { PluginRenderer2D } from "./../../src/plugins/PluginRenderer2D";
import { PluginRenderer3D } from "./../../src/plugins/PluginRenderer3D";
import { PluginThings } from "./../../src/plugins/PluginThings";
import World from "./things/World";
import MixinMapQt from "./mixins/MixinMapQt";
import { ThingEvents } from "./../../src/events/ThingEvents";
import { QtEvents } from "./../../src/events/QtEvents";
import Thing from "./../../src/core/Thing";
import CameraControls from "camera-controls";
import { PluginNotification } from "@vci/vci/vue/v2/components/other/VpNotification";
import { resourceRootAliyun } from "@vci/vci/config/api";

export default {
  name: "MapD3",
  components: { VciD3 },
  mixins: [MixinMapQt],
  props: {
    debug: {
      type: Boolean,
      default: false,
    },
    mode: {
      type: String,
      default: "2D",
    },
    autoAnimationIn: {
      type: Boolean,
      default: false,
    },
    // china|world
    scope: {
      type: String,
      default: "china",
    },
    center: {
      type: Array,
      default: () => [107.88219877049586, 30.048056747015057],
    },
    mapUrl: {
      type: String,
      default: null,
    },
    mapPosition: {
      type: Array,
      default: null,
    },
    size: {
      type: Array,
      default: () => [1000e4, 1000e4],
    },
  },
  data() {
    return {};
  },
  methods: {
    beforeInitQt(option) {
      const loading = this.$children.find((vm) => vm.$attrs.name === "loading");
      mergeDeep(option, {
        debug: this.debug,
        plugins: getChunkPlugins(loading).concat([
          PluginRenderer2D,
          PluginRenderer3D,
          PluginThings,
        ]),
      });
    },
    async ready(qt) {
      this.setQt(qt);
      // 启用双击事件
      qt.events.enableEventDblClick = true;
      // 调整相机及控制器
      qt.camera.near = 0.01;
      qt.camera.far = 1e8;
      qt.camera.updateProjectionMatrix();
      qt.control.maxDistance = 0.4e8;
      qt.control.maxPolarAngle = degToRad(88);
      qt.control.setLookAt(0, 1e3, 0, 0, 0, 0, false);
      // 载入资源
      const loadingManager = qt.lm ? qt.lm.manager : undefined;
      // 启用资源缓存
      qt.lm && (qt.lm.enableCache = true);
      qt.loadingManager = loadingManager;
      qt.loaderGltf = createLoaderGltf({ qt });

      // 空的 GeoJSON 数据作为后备
      const emptyGeoJSON = {
        type: "FeatureCollection",
        features: [],
      };

      // 辅助函数：加载并解析 GeoJSON
      const loadGeoJSON = async (url) => {
        try {
          const fileLoader = new FileLoader(loadingManager);
          // FileLoader 默认加载文本文件
          const data = await fileLoader.loadAsync(url);
          console.log(data);

          // 处理返回的数据
          let jsonString = data;
          if (typeof data !== "string") {
            // 如果是 ArrayBuffer，转换为字符串
            if (data instanceof ArrayBuffer) {
              jsonString = new TextDecoder("utf-8").decode(data);
            } else {
              jsonString = String(data);
            }
          }

          // 检查是否是 HTML 错误页面
          const trimmed = jsonString.trim();
          if (
            trimmed.startsWith("<!DOCTYPE") ||
            trimmed.startsWith("<html") ||
            trimmed.startsWith("<!")
          ) {
            console.warn(
              `GeoJSON file not found or returned HTML: ${url}, using empty GeoJSON`
            );
            return emptyGeoJSON;
          }

          // 尝试解析 JSON
          try {
            return JSON.parse(jsonString);
          } catch (e) {
            console.warn(`Failed to parse GeoJSON from ${url}:`, e);
            return emptyGeoJSON;
          }
        } catch (error) {
          // 捕获网络错误或加载失败
          console.warn(
            `Failed to load GeoJSON from ${url}:`,
            error.message || error
          );
          return emptyGeoJSON;
        }
      };

      // 加载 GeoJSON 文件
      const [dataGeoWorld, dataGeoChina] = await Promise.all([
        loadGeoJSON(`${resourceRootAliyun}/geo/world.geojson`),
        loadGeoJSON(`${resourceRootAliyun}/geo/china.geojson`),
      ]);

      // 调整环境贴图
      qt.scene.background = null;
      // 构建资源体系
      qt.assets = {
        models: {},
        geos: {
          world: dataGeoWorld,
          china: dataGeoChina,
        },
        textures: {},
      };
      // 创建世界
      qt.thingWorld = new World({
        qt,
        enableDisplayWorld: this.debug && this.scope !== "china",
        enableDisplayChina: this.debug,
      });
      qt.thingWorld.loadingManager = loadingManager;
      qt.thingWorld.addEventListener(ThingEvents.Click, (e) => {
        const { detail } = e;
        const vec3Point = detail.targetRay.point;
        const lngLatWGS84 = qt.thingWorld.coordsToLngLat([
          vec3Point.x,
          vec3Point.z,
        ]);
        console.info(`当前鼠标拾取的经纬度(WGS84): `, lngLatWGS84.join(","));
      });
      // 添加大地坐标拾取功能
      window.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
          if (qt.thingWorld) {
            if (!qt.thingWorld.enableEvent) {
              qt.thingWorld.wakeUp();
            } else {
              qt.thingWorld.sleep();
            }
            PluginNotification.tip(
              `已${qt.thingWorld.enableEvent ? "开启" : "关闭"}鼠标拾取大地坐标`
            );
          }
        }
      });
      // 补丁: PluginEdit在大范围世界地图进行模型编辑时不能移动模型
      qt.addEventListener(QtEvents.AfterInEdit, () => {
        const oldGeo = qt.editor.controlTransform._plane.geometry;
        qt.editor.controlTransform._plane.geometry = new PlaneGeometry(
          100e8,
          100e8,
          2,
          2
        );
        oldGeo.dispose();
      });
      // 调整色调
      // qt.effect.effectToneMapping.mode = ToneMappingMode.LINEAR;
      // 重置加载状态
      qt.lm.isLoaded = false;
      // 设置中心点
      qt.thingWorld.setOffsetFromLngLat(this.center);
      // 设置地图为2D
      if (this.mode === "2D")
        qt.control.minPolarAngle = qt.control.maxPolarAngle = 0;
      // 设置鼠标左键平移、右键旋转
      qt.control.mouseButtons.left = CameraControls.ACTION.TRUCK;
      qt.control.mouseButtons.right =
        this.mode === "2D"
          ? CameraControls.ACTION.NONE
          : CameraControls.ACTION.ROTATE;
      // 载入地图资源
      if (this.mapUrl) {
        const [map] = await Promise.all([
          new TextureLoader(qt.thingWorld.loadingManager).loadAsync(
            this.mapUrl
          ),
        ]);
        // 创建图片地图
        this.createGraphicMap(map);
      }
      // 动画入场
      this.autoAnimationIn && (await qt.animation.animationInAsync());
      // 标记地图准备完成
      this.$emit("ready", qt);
      // 设置地图缩放层级
      // await qt.control.setLookAt(
      //   0, 1e7, 0,
      //   0, 0, 0,
      //   true
      // );
    },
    // 创建图片地图
    createGraphicMap(map) {
      const { qt } = this;
      // // 基准图配置
      // const size = new Vector2(1374, 1010)
      // size.multiplyScalar(5.01e3)
      const size = new Vector2().fromArray(this.size);
      map.colorSpace = SRGBColorSpace;
      const geo = new PlaneGeometry(size.x, size.y, 9, 9);
      geo.rotateX(degToRad(-90));
      geo.translate(0, this.debug ? 5 : 0, 0);
      const mtl = new MeshBasicMaterial({
        transparent: true,
        opacity: this.debug ? 0.2 : 1,
        map,
      });
      const o3 = new Mesh(geo, mtl);
      qt.thingGraphicMap = new Thing({
        qt,
        enableEvent: false,
        position: this.mapPosition
          ? qt.thingWorld.lngLatToCoords(this.mapPosition, 0)
          : [0, 0, 0],
        object: o3,
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.MapD3 {
  position: relative;
  width: 100%;
  height: 100%;
  .VciD3 {
    z-index: 1;
  }
}
</style>
