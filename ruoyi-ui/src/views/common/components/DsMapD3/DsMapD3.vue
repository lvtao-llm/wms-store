<template>
  <div class="DsMapD3 vci-size-100">
    <!-- 地图 -->
    <map-d3
      :center="[125.0421233, 46.5829954]"
      mode="3D"
      scope="china"
      @ready="ready"
    >
      <QtLoading name="loading" />
    </map-d3>
    <!-- 风险区域编辑框 -->
    <!-- <transition name="fade">
      <PopupEdit v-if="mapRiskArea.drawing" class="vci-pst-abs" />
    </transition> -->
  </div>
</template>

<script>
// power by visual-ceiling, at 2025/3/31 11:14
import MapD3 from "@vci/quick-three/components/MapD3/MapD3.vue";
import QtLoading from "@/common/components/qt/QtLoading.vue";
import { createLoaderGltf } from "@vci/quick-three/src/creator/loaders/createLoaderGltf";
import { resourceRootAliyun } from "@vci/vci/config/api";
import { Box3, TextureLoader, Vector3, VSMShadowMap } from "three";
import { createDirectionLight } from "@vci/quick-three/src/creator/lights/createDirectionLight";
import { createBloomEffect } from "@vci/quick-three/src/creator/effects/createBloomEffect";
import { EffectPass } from "postprocessing";
import { createEnvEXR } from "@vci/quick-three/src/creator/env/createEnvEXR";
import Ground from "@/views/common/components/DsMapD3/things/Ground";
import Park from "@/views/common/components/DsMapD3/things/Park";
import { cloneO3 } from "@vci/quick-three/src/helper/ThingHelper";
import { delay } from "@/common/helper";
import { PerspectiveModes } from "@/constants";
import { degToRad } from "three/src/math/MathUtils";
import { mapWritableState } from "pinia";
import { storeCore } from "@/store/store.core";
// import PopupEdit from "@/views/common/components/PopupEdit/PopupEdit.vue";

export default {
  name: "DsMapD3",
  components: { QtLoading, MapD3 },
  data() {
    return {};
  },
  computed: {
    ...mapWritableState(storeCore, ["mapPerspectiveMode", "mapRiskArea"]),
  },
  created() {
    this.ready();
  },
  methods: {
    async ready(qt) {
      this.qt = qt;
      // 设置默认视角
      qt.control.setLookAt(0, 2e3, 0, 0, 0, 0, false);
      // 设置地图移动范围
      qt.control.setBoundary(
        new Box3(new Vector3(-1e3, -1e3, -1e3), new Vector3(1e3, 1e3, 1e3))
      );
      qt.control.maxDistance = 2e3;
      qt.control.minDistance = 10;
      // 创建GLTF加载器
      qt.loaderGltf = createLoaderGltf({ qt });
      // 载入必要资源
      const loadingManager = qt.lm ? qt.lm.manager : undefined;
      const [gltfFactory, mapGround, mapGroundMask] = await Promise.all([
        qt.loaderGltf.loadAsync(
          `${resourceRootAliyun}/projects/dv-stash-3d-dq/models/factory/factory.min.glb`
        ),
        new TextureLoader(loadingManager).loadAsync(
          `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/grass.jpg`
        ),
        new TextureLoader(loadingManager).loadAsync(
          `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/mask.jpg`
        ),
        createEnvEXR(
          qt,
          `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/env.exr`
        ),
      ]);
      qt.assets.models = {
        factory: gltfFactory.scene,
      };
      qt.assets.textures = {
        mapGround,
        mapGroundMask,
      };
      // 隐藏环境贴图
      // qt.scene.background = null;
      // qt.scene.background = new Color("#062434");
      // 隐藏世界地图
      qt.thingWorld.o3Hide();
      // 创建地面
      new Ground({
        qt,
        position: [0, -0.01, 0],
        renderOrder: -10,
      });
      // 创建工厂
      qt.thingPark = new Park({
        qt,
        object: cloneO3(qt.assets.models.factory, true),
      }).sleep();
      // 创建光照
      this.createLights();
      // 创建后期处理|虚幻
      qt.effect.effectBloom = createBloomEffect({
        qt,
        gui: qt.gui.guis.postprocessing,
      });
      qt.effect.composer.addPass(
        new EffectPass(qt.camera, qt.effect.effectBloom)
      );
      qt.effect.effectBloom.inverted = false;
      qt.effect.effectBloom.ignoreBackground = true;
      // 创建地图功能
      this.createMapFn(qt);
      // 动画入场
      await qt.animation.animationInAsync();
      await delay(0.35e3);
      // 切换至主视角
      await qt.perspectiveReset();
      // 触发准备完成事件
      this.$emit("ready");
      qt.thingPark.mock();
    },
    // 创建光照
    createLights() {
      const { qt } = this;
      // 修改阴影类型
      qt.renderer.shadowMap.type = VSMShadowMap;
      // createAmbientLight({ qt, intensity: .2 });
      createDirectionLight({
        qt,
        name: "主光源",
        intensity: 6,
        position: [300, 300, -300],
        castShadow: true,
        shadow: {
          bias: -0.0001,
          farCamera: 1e3,
          sizeCamera: 1.2e3,
          sizeMap: 2 ** 12,
        },
      });
    },
    // 创建地图功能
    createMapFn(qt) {
      // 长按右键多角度观察仓库
      {
        const LpPerspective = {
          perspectives: [
            {
              camera: [
                403.41322499761066, 54.39649765318477, 328.49325297232076,
              ],
              target: [
                304.1193147143692, -5.987946524664025e-15, 217.6735978023493,
              ],
            },
            {
              camera: [
                -534.411634925127, 19.283296993204882, -421.0960278114357,
              ],
              target: [
                -507.04361800034303, -5.118764267784078e-16,
                -388.51883439022816,
              ],
            },
            {
              camera: [36.09447175159743, 71.06886813748373, 147.1876403653784],
              target: [
                -61.297762213598524, -6.350714812869869e-16,
                -15.209379246083296,
              ],
            },
          ],
          index: 0,
        };
        qt.el.addEventListener("pointerdown", (e) => {
          if (
            e.button === 2 &&
            this.mapPerspectiveMode !== PerspectiveModes.D2
          ) {
            clearTimeout(qt.inter.perspectiveOfLongPress);
            qt.inter.perspectiveOfLongPress = setTimeout(() => {
              if (!qt.events.enableMoveFn) return false;
              const targetPerspective =
                LpPerspective.perspectives[LpPerspective.index];
              qt.control.setLookAt(
                ...targetPerspective.camera,
                ...targetPerspective.target,
                true
              );
              LpPerspective.index++;
              LpPerspective.index >= LpPerspective.perspectives.length &&
                (LpPerspective.index = 0);
            }, 1e3);
          }
        });
        qt.el.addEventListener(
          "pointerup",
          (e) => e.button === 2 && clearTimeout(qt.inter.perspectiveOfLongPress)
        );
      }
      /**
       * 重置到默认视角
       * @param immediate
       * @returns {Promise<*>}
       */
      qt.perspectiveReset = async (immediate = true) =>
        await qt.control.setLookAt(
          266.09447175159743,
          131.06886813748373,
          147.1876403653784,
          -61.297762213598524,
          -6.350714812869869e-16,
          -15.209379246083296,
          immediate
        );
      /**
       * 切换视角模式
       * @param mode 2D|3D
       */
      qt.switchPerspectiveMode = async (mode = PerspectiveModes.D3) => {
        this.mapPerspectiveMode = mode;
        if (mode === PerspectiveModes.D2) {
          qt.control.maxPolarAngle = degToRad(0);
          await qt.control.setLookAt(
            -95.57579092254079,
            752.8660311816277,
            -77.47831985110676,
            -95.57579092254079,
            0,
            -77.47907271713794,
            true
          );
        } else if (mode === PerspectiveModes.D3) {
          qt.control.maxPolarAngle = degToRad(88);
          await qt.perspectiveReset();
        }
      };

      /**
       * 启动风险区域绘制
       */
      qt.bootRiskAreaDrawing = () => {
        this.mapRiskArea.drawing = true;
        qt.thingRiskArea.wakeUp();
        qt.switchPerspectiveMode(PerspectiveModes.D2);
      };

      /**
       * 关闭风险区域绘制
       */
      qt.shutdownRiskAreaDrawing = () => {
        this.mapRiskArea.drawing = false;
        qt.thingRiskArea.sleep();
      };
    },
  },
};
</script>

<style lang="scss" scoped>
.DsMapD3 {
  .PopupEdit {
    z-index: 2;
    left: 45vw;
    top: 118px;
  }
}
</style>
