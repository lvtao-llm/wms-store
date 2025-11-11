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
  mounted() {
    // 只在mounted中调用ready，避免重复初始化
  },
  methods: {
    async ready(qt) {
      try {
        // 添加防重复初始化检查
        if (this._mapInitialized) {
          return;
        }
        this._mapInitialized = true;
        
        this.qt = qt;
        
        // 初始化配置 - 使用requestAnimationFrame避免阻塞主线程
        await new Promise(resolve => requestAnimationFrame(() => {
          // 设置默认视角
          qt.control.setLookAt(0, 2e3, 0, 0, 0, 0, false);
          // 设置地图移动范围
          qt.control.setBoundary(
            new Box3(new Vector3(-1e3, -1e3, -1e3), new Vector3(1e3, 1e3, 1e3))
          );
          qt.control.maxDistance = 2e3;
          qt.control.minDistance = 10;
          
          // 优化渲染器性能
          qt.renderer.setPixelRatio(Math.min(window.devicePixelRatio, 1.5)); // 限制像素比
          qt.renderer.autoClear = true;
          
          resolve();
        }));
        
        // 创建GLTF加载器
        qt.loaderGltf = createLoaderGltf({ qt });
        
        // 载入必要资源 - 添加超时控制
        const loadingManager = qt.lm ? qt.lm.manager : undefined;
        
        // 设置资源加载超时
        const withTimeout = (promise, timeout = 10000) => {
          return Promise.race([
            promise,
            new Promise((_, reject) => setTimeout(() => reject(new Error('资源加载超时')), timeout))
          ]);
        };
        
        try {
          // 分批加载资源，而不是一次性加载全部
          const [gltfFactory] = await withTimeout(Promise.all([
            qt.loaderGltf.loadAsync(
              `${resourceRootAliyun}/projects/dv-stash-3d-dq/models/factory/factory.min.glb`
            )
          ]));
          
          // 存储模型资源
          qt.assets.models = {
            factory: gltfFactory.scene,
          };
          
          // 在下一个动画帧加载纹理资源
          await new Promise(resolve => requestAnimationFrame(async () => {
            const [mapGround, mapGroundMask] = await withTimeout(Promise.all([
              new TextureLoader(loadingManager).loadAsync(
                `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/grass.jpg`
              ),
              new TextureLoader(loadingManager).loadAsync(
                `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/mask.jpg`
              ),
            ]));
            
            qt.assets.textures = {
              mapGround,
              mapGroundMask,
            };
            
            resolve();
          }));
          
          // 可选地加载环境贴图，放在最后加载
          try {
            await withTimeout(createEnvEXR(
              qt,
              `${resourceRootAliyun}/projects/dv-stash-3d-dq/textures/env.exr`
            ));
          } catch (error) {
            console.warn("环境贴图加载失败，将继续使用默认设置", error);
          }
        } catch (error) {
          console.error("资源加载失败", error);
          // 资源加载失败时仍然尝试继续初始化基础场景
        }
        
        // 分步骤创建场景组件
        await new Promise(resolve => requestAnimationFrame(() => {
          // 隐藏世界地图
          if (qt.thingWorld) qt.thingWorld.o3Hide();
          
          // 创建地面
          try {
            new Ground({
              qt,
              position: [0, -0.01, 0],
              renderOrder: -10,
            });
          } catch (error) {
            console.warn("创建地面时出错", error);
          }
          
          resolve();
        }));
        
        // 延迟创建工厂模型
        if (qt.assets.models?.factory) {
          await new Promise(resolve => requestAnimationFrame(() => {
            try {
              qt.thingPark = new Park({
                qt,
                object: cloneO3(qt.assets.models.factory, true),
              }).sleep();
            } catch (error) {
              console.warn("创建工厂模型时出错", error);
            }
            resolve();
          }));
        }
        
        // 创建光照（已优化）
        this.createLights();
        
        // 延迟创建后期处理效果
        await new Promise(resolve => setTimeout(resolve, 300)); // 给场景一些初始化时间
        
        // 有条件地创建后期处理效果
        try {
          // 仅在必要时启用后期处理
          const isLowEndDevice = window.innerWidth < 1024 || navigator.hardwareConcurrency < 4;
          if (!isLowEndDevice && qt.effect) {
            qt.effect.effectBloom = createBloomEffect({
              qt,
              gui: qt.gui?.guis?.postprocessing || null,
            });
            qt.effect.composer.addPass(
              new EffectPass(qt.camera, qt.effect.effectBloom)
            );
            qt.effect.effectBloom.inverted = false;
            qt.effect.effectBloom.ignoreBackground = true;
          }
        } catch (error) {
          console.warn("创建后期处理效果时出错", error);
        }
        
        // 创建地图功能
        this.createMapFn(qt);
        
        // 简化动画入场
        try {
          if (qt.animation?.animationInAsync) {
            await qt.animation.animationInAsync();
          }
          // 减少延迟时间
          await delay(100);
          
          // 切换至主视角
          if (qt.perspectiveReset) {
            await qt.perspectiveReset();
          }
        } catch (error) {
          console.warn("动画入场或视角切换时出错", error);
        }
        
        // 触发准备完成事件 - 尽早通知父组件
        this.$emit("ready");
        
        // 延迟执行mock操作
        setTimeout(() => {
          try {
            if (qt.thingPark?.mock) {
              qt.thingPark.mock();
            }
          } catch (error) {
            console.warn("执行mock操作时出错", error);
          }
        }, 500);
        
      } catch (error) {
        console.error("地图初始化失败", error);
        // 即使出错也尝试通知父组件
        this.$emit("ready");
      }
    },
    // 创建光照 - 优化阴影性能
    createLights() {
      const { qt } = this;
      try {
        // 使用性能更好的PCFSoftShadowMap替代VSMShadowMap
        qt.renderer.shadowMap.type = 2; // THREE.PCFSoftShadowMap
        qt.renderer.shadowMap.autoUpdate = false; // 减少阴影更新频率
        
        // 降低光源强度和阴影分辨率以提高性能
        createDirectionLight({
          qt,
          name: "主光源",
          intensity: 4, // 降低强度
          position: [300, 300, -300],
          castShadow: true,
          shadow: {
            bias: -0.0001,
            farCamera: 800, // 减小阴影相机范围
            sizeCamera: 1000, // 减小阴影相机大小
            sizeMap: 2 ** 10, // 降低阴影贴图分辨率
          },
        });
      } catch (error) {
        console.warn("创建光照时出错", error);
      }
    },
    // 创建地图功能 - 优化版
    createMapFn(qt) {
      try {
        // 初始化交互对象，确保存在
        if (!qt.inter) {
          qt.inter = {};
        }
        if (!qt.events) {
          qt.events = { enableMoveFn: true };
        }
        
        // 长按右键多角度观察仓库 - 添加防抖和错误处理
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
            // 添加防抖标记
            isChanging: false
          };
          
          // 清理现有事件监听器，避免重复绑定
          if (this._pointerDownListener) {
            qt.el.removeEventListener('pointerdown', this._pointerDownListener);
          }
          if (this._pointerUpListener) {
            qt.el.removeEventListener('pointerup', this._pointerUpListener);
          }
          
          // 优化事件处理函数
          this._pointerDownListener = (e) => {
            if (
              e.button === 2 &&
              this.mapPerspectiveMode !== PerspectiveModes.D2
            ) {
              try {
                // 清除之前的定时器
                if (qt.inter.perspectiveOfLongPress) {
                  clearTimeout(qt.inter.perspectiveOfLongPress);
                }
                
                // 设置新的定时器，使用更合理的时间间隔
                qt.inter.perspectiveOfLongPress = setTimeout(() => {
                  try {
                    // 防止快速连续切换导致的性能问题
                    if (LpPerspective.isChanging || !qt.events.enableMoveFn) {
                      return;
                    }
                    
                    LpPerspective.isChanging = true;
                    const targetPerspective =
                      LpPerspective.perspectives[LpPerspective.index];
                    
                    // 使用requestAnimationFrame优化视角切换
                    requestAnimationFrame(async () => {
                      try {
                        await qt.control.setLookAt(
                          ...targetPerspective.camera,
                          ...targetPerspective.target,
                          true
                        );
                        // 更新索引
                        LpPerspective.index = (LpPerspective.index + 1) % LpPerspective.perspectives.length;
                      } catch (error) {
                        console.warn("视角切换失败", error);
                      } finally {
                        // 延迟重置状态，防止快速切换
                        setTimeout(() => {
                          LpPerspective.isChanging = false;
                        }, 300);
                      }
                    });
                  } catch (error) {
                    console.warn("执行视角切换时出错", error);
                    LpPerspective.isChanging = false;
                  }
                }, 800); // 稍微降低长按时间
              } catch (error) {
                console.warn("指针按下事件处理出错", error);
              }
            }
          };
          
          this._pointerUpListener = (e) => {
            if (e.button === 2 && qt.inter.perspectiveOfLongPress) {
              clearTimeout(qt.inter.perspectiveOfLongPress);
            }
          };
          
          // 添加事件监听器
          qt.el.addEventListener("pointerdown", this._pointerDownListener);
          qt.el.addEventListener("pointerup", this._pointerUpListener);
        }
        
        /**
         * 重置到默认视角
         * @param immediate
         * @returns {Promise<*>}
         */
        qt.perspectiveReset = async (immediate = true) => {
          try {
            // 添加防抖动
            if (this._isResetting) {
              return;
            }
            
            this._isResetting = true;
            
            // 使用requestAnimationFrame优化性能
            return await new Promise(async (resolve, reject) => {
              requestAnimationFrame(async () => {
                try {
                  await qt.control.setLookAt(
                    266.09447175159743,
                    131.06886813748373,
                    147.1876403653784,
                    -61.297762213598524,
                    -6.350714812869869e-16,
                    -15.209379246083296,
                    immediate
                  );
                  resolve();
                } catch (error) {
                  console.warn("重置视角失败", error);
                  reject(error);
                } finally {
                  this._isResetting = false;
                }
              });
            });
          } catch (error) {
            console.error("执行视角重置时出错", error);
            this._isResetting = false;
            throw error;
          }
        };
        
        /**
         * 切换视角模式
         * @param mode 2D|3D
         */
        qt.switchPerspectiveMode = async (mode = PerspectiveModes.D3) => {
          try {
            // 防重复切换
            if (this._isSwitchingMode || this.mapPerspectiveMode === mode) {
              return;
            }
            
            this._isSwitchingMode = true;
            
            // 使用防抖优化
            this.mapPerspectiveMode = mode;
            
            await new Promise(resolve => requestAnimationFrame(async () => {
              try {
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
              } catch (error) {
                console.warn("切换视角模式失败", error);
              } finally {
                this._isSwitchingMode = false;
                resolve();
              }
            }));
          } catch (error) {
            console.error("执行视角模式切换时出错", error);
            this._isSwitchingMode = false;
          }
        };

        /**
         * 启动风险区域绘制
         */
        qt.bootRiskAreaDrawing = () => {
          try {
            this.mapRiskArea.drawing = true;
            if (qt.thingRiskArea?.wakeUp) {
              qt.thingRiskArea.wakeUp();
            }
            // 延迟切换视角，避免同时操作导致的性能问题
            setTimeout(() => {
              qt.switchPerspectiveMode(PerspectiveModes.D2);
            }, 100);
          } catch (error) {
            console.error("启动风险区域绘制失败", error);
          }
        };

        /**
         * 关闭风险区域绘制
         */
        qt.shutdownRiskAreaDrawing = () => {
          try {
            this.mapRiskArea.drawing = false;
            if (qt.thingRiskArea?.sleep) {
              qt.thingRiskArea.sleep();
            }
          } catch (error) {
            console.error("关闭风险区域绘制失败", error);
          }
        };
      } catch (error) {
        console.error("创建地图功能时出错", error);
      }
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
