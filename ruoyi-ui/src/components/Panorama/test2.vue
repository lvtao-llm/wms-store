<template>
  <div id="app">
    <el-dialog
      :visible.sync="isShow"
      title="预览"
      width="80vw"
      append-to-body
      @close="closeDia"
    >
      <div ref="sceneContainer" class="scene-container"></div>
    </el-dialog>
  </div>
</template>

<script>
import * as THREE from "three";

export default {
  name: "PopupCamera",
  components: {},
  props: {
    title: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      // DOM 引用
      sceneContainer: null, // 实际在 mounted 中通过 this.$refs.sceneContainer 获取

      // 状态信息
      status: "初始化中...",
      sceneObjectCount: 0,
      photoInfoText: "未加载",
      cameraPosition: "未设置",
      currentSceneId: 0,

      // 弹窗状态
      showPopup: false,
      currentPopup: null,
      popupPosition: { x: 0, y: 0 },
      isShow: false,

      // 鼠标控制状态
      isMouseDown: false,
      mouseX: 0,
      mouseY: 0,

      // 事件处理函数引用
      handleMouseDown: null,
      handleMouseMove: null,
      handleMouseUp: null,
      handleWheel: null,
      // 场景数据
      scenes: [
        {
          id: 0,
          name: "筒仓外部全景",
          description: "111",
          panorama: "/scenes/1.jpg",
          thumbnail: "/thumbnails/1.jpg",
          resolution: "5647x1280",
          date: "2024-01-15",
        },
        {
          id: 1,
          name: "筒仓内部入口",
          description: "222",
          panorama: "/scenes/2.jpg",
          thumbnail: "/thumbnails/2.jpg",
          resolution: "5647x1280",
          date: "2024-01-15",
        },
        {
          id: 2,
          name: "筒仓内部中心",
          description: "333",
          panorama: "/scenes/3.jpg",
          thumbnail: "/thumbnails/3.jpg",
          resolution: "5647x1280",
          date: "2024-01-15",
        },
      ],

      // 热点数据
      hotspots: [
        {
          id: 1,
          name: "按钮1",
          worldPosition: { x: 8, y: 5, z: 10 },
          targetSceneId: 1,
          currentSceneId: 0,
        },
        {
          id: 2,
          name: "按钮2",
          worldPosition: { x: -10, y: 2, z: 4 },
          targetSceneId: 2,
          currentSceneId: 0,
        },
        {
          id: 3,
          name: "返回外部",
          worldPosition: { x: 12, y: 1, z: 0 },
          targetSceneId: 0,
          currentSceneId: 1,
        },
        {
          id: 4,
          name: "返回外部",
          worldPosition: { x: -10, y: 0, z: 0 },
          targetSceneId: 0,
          currentSceneId: 2,
        },
      ],

      // Three.js 相关对象
      scene: null,
      camera: null,
      renderer: null,
      currentPanorama: null,
      animationId: null,
      rotationX: 0,
      rotationY: 0,
      baseUrl: process.env.VUE_APP_BASE_API,
      imgSrc: "",
      testCube: null, // 保存测试立方体引用以便后续移除
    };
  },
  mounted() {
    this.sceneContainer = this.$refs.sceneContainer;
    // this.initScene();
  },
  methods: {
    openDia(src) {
      // 先设置图片源并显示对话框
      this.imgSrc = this.baseUrl + src;
      this.isShow = true;

      // 等待对话框完全渲染后再初始化场景
      this.$nextTick(() => {
        setTimeout(() => {
          this.sceneContainer = this.$refs.sceneContainer;
          this.initScene();
        }, 100); // 添加明确的延迟，确保对话框渲染完成
      });
    },
    closeDia() {
      // 隐藏对话框
      this.isShow = false;

      // 取消动画循环
      if (this.animationId) {
        cancelAnimationFrame(this.animationId);
        this.animationId = null;
      }

      // 移除事件监听器
      window.removeEventListener("resize", this.onWindowResize);
      if (this.sceneContainer) {
        // 移除鼠标事件监听器
        this.sceneContainer.removeEventListener(
          "mousedown",
          this.handleMouseDown
        );
        this.sceneContainer.removeEventListener(
          "mousemove",
          this.handleMouseMove
        );
        this.sceneContainer.removeEventListener("mouseup", this.handleMouseUp);
        this.sceneContainer.removeEventListener("wheel", this.handleWheel);
      }

      // 清理Three.js资源
      if (this.renderer) {
        // 移除渲染器DOM元素
        if (this.renderer.domElement && this.sceneContainer) {
          this.sceneContainer.removeChild(this.renderer.domElement);
        }
        this.renderer.dispose();
        this.renderer = null;
      }

      // 清理场景对象
      if (this.scene) {
        // 移除所有子对象
        while (this.scene.children.length > 0) {
          const object = this.scene.children[0];
          // 递归处理对象和材质
          if (object instanceof THREE.Mesh) {
            if (object.geometry) {
              object.geometry.dispose();
            }
            if (object.material) {
              if (object.material.map) {
                object.material.map.dispose();
              }
              object.material.dispose();
            }
          }
          this.scene.remove(object);
        }
        this.scene = null;
      }

      // 清理相机和其他引用
      this.camera = null;
      this.currentPanorama = null;

      console.log("场景资源已完全清理");
    },
    worldToScreen(worldPos) {
      if (!this.camera || !this.renderer) return { x: 0, y: 0, visible: false };

      const vector = new THREE.Vector3(worldPos.x, worldPos.y, worldPos.z);
      vector.project(this.camera);

      const visible = vector.z < 1 && vector.z > -1;

      const x = (vector.x * 0.5 + 0.5) * window.innerWidth;
      const y = (vector.y * -0.5 + 0.5) * window.innerHeight;

      return { x, y, visible };
    },

    // getHotspotStyle(hotspot) {
    //   if (hotspot.currentSceneId !== this.currentSceneId) {
    //     return { display: "none" };
    //   }

    //   const screenPos = this.worldToScreen(hotspot.worldPosition);

    //   if (!screenPos.visible) {
    //     return { display: "none" };
    //   }

    //   return {
    //     left: `${screenPos.x}px`,
    //     top: `${screenPos.y}px`,
    //     transform: "translate(-50%, -50%)",
    //     display: "block",
    //   };
    // },

    getCurrentSceneName() {
      return this.scenes[this.currentSceneId]?.name || "未知场景";
    },

    switchToScene(sceneId) {
      if (sceneId !== this.currentSceneId) {
        this.currentSceneId = sceneId;
        this.loadScene(sceneId);
      }
    },

    showHotspotPopup(hotspot) {
      this.currentPopup = hotspot;
      const screenPos = this.worldToScreen(hotspot.worldPosition);
      this.popupPosition = { x: screenPos.x, y: screenPos.y };
      this.showPopup = true;
    },

    closePopup() {
      this.showPopup = false;
      this.currentPopup = null;
    },

    // confirmSwitch() {
    //   if (this.currentPopup) {
    //     this.switchToScene(this.currentPopup.targetSceneId);
    //     this.closePopup();
    //   }
    // },

    getPopupStyle() {
      return {
        left: `${this.popupPosition.x + 50}px`,
        top: `${this.popupPosition.y - 100}px`,
        transform: "translate(-50%, -50%)",
      };
    },

    getPopupDescription() {
      if (!this.currentPopup) return "";
      const targetScene = this.scenes.find(
        (s) => s.id === this.currentPopup.targetSceneId
      );
      return targetScene ? targetScene.description : "即将切换到新场景";
    },

    initScene() {
      try {
        console.log("开始初始化Three.js场景");
        this.status = "创建场景...";

        // 确保场景容器存在
        if (!this.sceneContainer) {
          console.error("场景容器不存在");
          this.status = "初始化失败: 场景容器不存在";
          return;
        }

        this.scene = new THREE.Scene();
        this.scene.background = new THREE.Color(0x000000);

        // 使用场景容器的实际尺寸
        const containerWidth =
          this.sceneContainer.clientWidth || window.innerWidth;
        const containerHeight =
          this.sceneContainer.clientHeight || window.innerHeight;

        this.camera = new THREE.PerspectiveCamera(
          75, // 使用更大的视场角确保能够看到更大范围
          containerWidth / containerHeight,
          0.1,
          1000
        );
        this.camera.position.set(0, 0, 5);
        this.camera.lookAt(0, 0, 0);

        this.renderer = new THREE.WebGLRenderer({ antialias: true });
        this.renderer.setSize(containerWidth, containerHeight);
        this.renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

        this.sceneContainer.appendChild(this.renderer.domElement);

        const ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
        this.scene.add(ambientLight);

        const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
        directionalLight.position.set(10, 10, 5);
        this.scene.add(directionalLight);

        // 移除测试立方体，避免遮挡全景图
        // this.createTestCube();

        this.loadScene();

        this.addMouseControls();

        this.animate();

        window.addEventListener("resize", this.onWindowResize);

        this.status = "初始化完成";
        console.log("场景初始化完成");
      } catch (error) {
        console.log(error);
        console.error("初始化失败:", error);
        this.status = "初始化失败: " + error.message;
      }
    },
    loadScene(sceneId) {
      try {
        this.status = "加载场景中...";
        console.log("开始加载场景:", sceneId);

        // 添加图片路径有效性检查
        if (!this.imgSrc) {
          console.error("图片路径为空");
          this.status = "图片路径无效";
          this.createDefaultScene();
          return;
        }

        const textureLoader = new THREE.TextureLoader();

        textureLoader.load(
          this.imgSrc,
          (texture) => {
            console.log("照片加载成功:", texture.image);
            this.photoInfoText = `已加载: ${texture.image.width}x${texture.image.height}`;

            // 如果场景中有测试立方体，移除它
            if (this.testCube) {
              this.scene.remove(this.testCube);
              this.testCube.geometry.dispose();
              this.testCube.material.dispose();
              this.testCube = null;
            }

            const aspectRatio = texture.image.width / texture.image.height;
            console.log("照片宽高比:", aspectRatio);

            // 使用可拖动全景展示方式
            this.createPanorama(texture, aspectRatio);

            this.status = "场景加载完成";
          },
          (progress) => {
            console.log("加载进度:", progress);
            this.status = `加载中... ${Math.round(
              (progress.loaded / progress.total) * 100
            )}%`;
          },
          (error) => {
            console.error("照片加载失败:", error);
            this.photoInfoText = "加载失败: " + error.message;
            this.status = "照片加载失败";
            this.createDefaultScene();
          }
        );
      } catch (error) {
        console.error("加载场景出错:", error);
        this.status = "加载出错: " + error.message;
        this.createDefaultScene();
      }
    },
    createTestCube() {
      const geometry = new THREE.BoxGeometry(2, 2, 2);
      const material = new THREE.MeshPhongMaterial({ color: 0x00ff00 });
      const cube = new THREE.Mesh(geometry, material);
      cube.position.set(0, 0, 0);
      this.scene.add(cube);

      // 保存测试立方体的引用，以便后续可以移除
      this.testCube = cube;

      this.sceneObjectCount = this.scene.children.length;
      console.log("添加测试立方体，场景对象数量:", this.sceneObjectCount);
    },
    createPanorama(texture, aspectRatio) {
      console.log("创建可拖动全景照片，宽高比:", aspectRatio);

      if (this.currentPanorama) {
        this.scene.remove(this.currentPanorama);
      }

      // 创建一个非常长的矩形平面作为全景图的载体
      // 宽度设置得很大，以便容纳全景内容
      const width = 300; // 大幅增加宽度，用于水平拖动查看
      const height = 50; // 根据实际需要调整高度

      // 创建平面几何体
      const geometry = new THREE.PlaneGeometry(width, height);

      // 创建材质，设置纹理
      const material = new THREE.MeshBasicMaterial({
        map: texture,
        side: THREE.DoubleSide,
      });

      // 创建平面网格
      const plane = new THREE.Mesh(geometry, material);

      // 设置平面的位置，使中心点在原点
      plane.position.set(0, 0, 0);

      this.scene.add(plane);
      this.currentPanorama = plane;

      // 重置旋转
      this.rotationX = 0;
      this.rotationY = 0;

      // 设置相机位置
      // 将相机放置在平面前适当距离，高度与平面中心对齐
      const cameraDistance = 50; // 相机与平面的距离
      this.camera.position.set(0, 0, cameraDistance);
      this.camera.lookAt(0, 0, 0); // 相机看向平面中心

      // 存储全景图的宽度，用于拖动限制
      this.panoramaWidth = width;

      this.sceneObjectCount = this.scene.children.length;
      this.cameraPosition = `(${this.camera.position.x.toFixed(
        1
      )}, ${this.camera.position.y.toFixed(
        1
      )}, ${this.camera.position.z.toFixed(1)})`;

      console.log("球体全景照片创建完成，相机位置:", this.camera.position);
      console.log("相机距离球心距离:", this.camera.position.length());
      console.log("球体半径:", radius);
    },
    createPlanePhoto(texture, aspectRatio) {
      console.log("创建平面照片，宽高比:", aspectRatio);

      if (this.currentPanorama) {
        this.scene.remove(this.currentPanorama);
      }

      const screenAspect = window.innerWidth / window.innerHeight;
      let width, height;

      if (screenAspect > aspectRatio) {
        height = 20;
        width = height * aspectRatio;
      } else {
        width = 20;
        height = width / aspectRatio;
      }

      const geometry = new THREE.PlaneGeometry(width, height);
      const material = new THREE.MeshBasicMaterial({ map: texture });
      const plane = new THREE.Mesh(geometry, material);

      this.scene.add(plane);
      this.currentPanorama = plane;

      const initialDistance = Math.max(width, height) / 2 + 1;
      this.camera.position.set(0, 0, initialDistance);
      this.camera.lookAt(0, 0, 0);

      this.rotationX = 0;
      this.rotationY = 0;

      this.sceneObjectCount = this.scene.children.length;
      this.cameraPosition = `(${this.camera.position.x.toFixed(
        1
      )}, ${this.camera.position.y.toFixed(
        1
      )}, ${this.camera.position.z.toFixed(1)})`;

      console.log("平面照片创建完成，相机位置:", this.camera.position);
    },
    addMouseControls() {
      // 创建命名的事件处理函数并保存到组件实例
      this.handleMouseDown = (event) => {
        this.isMouseDown = true;
        this.mouseX = event.clientX;
        this.sceneContainer.style.cursor = "grabbing";
      };

      this.handleMouseMove = (event) => {
        if (!this.isMouseDown) return;

        const deltaX = event.clientX - this.mouseX;

        // 根据鼠标移动调整相机的水平位置
        // 这里我们直接平移相机，而不是旋转
        const moveSpeed = 0.3; // 调整移动速度
        const newCameraX = this.camera.position.x - deltaX * moveSpeed;

        // 设置相机移动的范围限制，避免移动到全景图范围外
        const halfPanoramaWidth = this.panoramaWidth / 2;
        const maxX = halfPanoramaWidth - 30; // 留出一些边距
        const minX = -halfPanoramaWidth + 30;

        // 限制相机位置在有效范围内
        const clampedX = Math.max(minX, Math.min(maxX, newCameraX));

        // 更新相机位置
        this.camera.position.set(clampedX, 0, this.camera.position.z);
        this.camera.lookAt(clampedX, 0, 0); // 相机始终看向当前X位置的正前方

        this.cameraPosition = `(${this.camera.position.x.toFixed(
          1
        )}, ${this.camera.position.y.toFixed(
          1
        )}, ${this.camera.position.z.toFixed(1)})`;

        this.mouseX = event.clientX;
        this.mouseY = event.clientY;
      };

      this.handleMouseUp = () => {
        this.isMouseDown = false;
        this.sceneContainer.style.cursor = "grab";
      };

      // 禁用滚轮缩放，保持固定的观察距离以确保完整展示
      this.handleWheel = (event) => {
        event.preventDefault();
      };

      // 添加事件监听器，使用保存的处理函数引用
      this.sceneContainer.addEventListener("mousedown", this.handleMouseDown);
      this.sceneContainer.addEventListener("mousemove", this.handleMouseMove);
      this.sceneContainer.addEventListener("mouseup", this.handleMouseUp);
      this.sceneContainer.addEventListener("wheel", this.handleWheel);

      this.sceneContainer.style.cursor = "grab";
    },
    animate() {
      this.animationId = requestAnimationFrame(this.animate);

      if (this.scene && this.camera && this.renderer) {
        this.renderer.render(this.scene, this.camera);

        // 强制更新热点位置（响应式触发）
        this.hotspots = [...this.hotspots];
      }
    },
    createDefaultScene() {
      console.log("创建默认场景");

      // 创建一个简单的默认平面作为 fallback
      if (this.currentPanorama) {
        this.scene.remove(this.currentPanorama);
      }

      const geometry = new THREE.PlaneGeometry(10, 10);
      const material = new THREE.MeshBasicMaterial({
        color: 0x333333,
        side: THREE.DoubleSide,
      });
      const plane = new THREE.Mesh(geometry, material);

      this.scene.add(plane);
      this.currentPanorama = plane;

      this.camera.position.set(0, 0, 15);
      this.camera.lookAt(0, 0, 0);

      console.log("默认场景创建完成");
    },

    onWindowResize() {
      if (!this.camera || !this.renderer || !this.sceneContainer) return;

      // 使用场景容器的实际尺寸而不是window尺寸
      const containerWidth =
        this.sceneContainer.clientWidth || window.innerWidth;
      const containerHeight =
        this.sceneContainer.clientHeight || window.innerHeight;

      this.camera.aspect = containerWidth / containerHeight;
      this.camera.updateProjectionMatrix();
      this.renderer.setSize(containerWidth, containerHeight);
    },
  },

  beforeDestroy() {
    // Vue 2 对应 onUnmounted
    if (this.animationId) {
      cancelAnimationFrame(this.animationId);
    }
    window.removeEventListener("resize", this.onWindowResize);
    if (this.renderer) {
      this.renderer.dispose();
    }
  },
};
</script>

<style scoped>
#app {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

.scene-container {
  width: 100%;
  height: 100%;
}

/* 热点按钮样式 */
.hotspot-button {
  position: absolute;
  z-index: 1000;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.hotspot-button:hover {
  transform: translate(-50%, -50%) scale(1.2);
}

.hotspot-icon {
  width: 40px;
  height: 40px;
  background: rgba(0, 212, 255, 0.9);
  border: 3px solid white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  position: relative;
  animation: pulse 2s infinite;
}

/* 闪动光圈效果 */
.hotspot-icon::before {
  content: "";
  position: absolute;
  top: -10px;
  left: -10px;
  right: -10px;
  bottom: -10px;
  border: 2px solid rgba(0, 212, 255, 0.6);
  border-radius: 50%;
  animation: ripple 2s infinite;
}

.hotspot-icon::after {
  content: "";
  position: absolute;
  top: -20px;
  left: -20px;
  right: -20px;
  bottom: -20px;
  border: 1px solid rgba(0, 212, 255, 0.3);
  border-radius: 50%;
  animation: ripple 2s infinite 0.5s;
}

/* 脉冲动画 */
@keyframes pulse {
  0% {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3), 0 0 0 0 rgba(0, 212, 255, 0.7);
  }
  50% {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3), 0 0 0 10px rgba(0, 212, 255, 0);
  }
  100% {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3), 0 0 0 0 rgba(0, 212, 255, 0);
  }
}

/* 波纹扩散动画 */
@keyframes ripple {
  0% {
    transform: scale(0.8);
    opacity: 1;
  }
  100% {
    transform: scale(2);
    opacity: 0;
  }
}

.hotspot-label {
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  white-space: nowrap;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.debug-panel {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 300px;
  background: rgba(0, 0, 0, 0.8);
  border-radius: 10px;
  padding: 20px;
  color: white;
  font-family: monospace;
}

.debug-panel h3 {
  margin: 0 0 15px 0;
  color: #00ff00;
  text-align: center;
}

.debug-item {
  margin-bottom: 10px;
  padding: 8px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 5px;
  font-size: 12px;
  color: #00ff00;
}

/* 场景选择面板 */
.scene-panel {
  position: absolute;
  top: 20px;
  left: 20px;
  width: 280px;
  background: rgba(0, 0, 0, 0.8);
  border-radius: 10px;
  padding: 20px;
  color: white;
  max-height: calc(100vh - 40px);
  overflow-y: auto;
}

.scene-panel h3 {
  margin: 0 0 15px 0;
  color: #00d4ff;
  text-align: center;
}

.scene-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.scene-item {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.scene-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.scene-item.active {
  border-color: #00d4ff;
  background: rgba(0, 212, 255, 0.2);
}

.scene-thumbnail {
  width: 100%;
  height: 80px;
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 10px;
}

.scene-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.scene-info h4 {
  margin: 0 0 5px 0;
  font-size: 14px;
  color: #fff;
}

.scene-info p {
  margin: 0;
  font-size: 11px;
  color: #ccc;
  line-height: 1.3;
}

/* 热点弹窗样式 */
.hotspot-popup {
  position: absolute;
  z-index: 2000;
  background: rgba(0, 0, 0, 0.95);
  border: 2px solid #00d4ff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 212, 255, 0.3);
  backdrop-filter: blur(10px);
  min-width: 280px;
  max-width: 350px;
  animation: popupFadeIn 0.3s ease-out;
}

.popup-content {
  padding: 0;
  color: white;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid rgba(0, 212, 255, 0.3);
  background: rgba(0, 212, 255, 0.1);
  border-radius: 10px 10px 0 0;
}

.popup-header h3 {
  margin: 0;
  color: #00d4ff;
  font-size: 16px;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  color: #00d4ff;
  font-size: 24px;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(0, 212, 255, 0.2);
  transform: scale(1.1);
}

.popup-body {
  padding: 20px;
}

.popup-body p {
  margin: 0 0 20px 0;
  color: #ccc;
  line-height: 1.5;
  font-size: 14px;
}

.popup-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.action-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  min-width: 80px;
}

.action-btn.primary {
  background: linear-gradient(135deg, #00d4ff, #0099cc);
  color: white;
  box-shadow: 0 2px 8px rgba(0, 212, 255, 0.3);
}

.action-btn.primary:hover {
  background: linear-gradient(135deg, #00b8e6, #0088bb);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 212, 255, 0.4);
}

.action-btn.secondary {
  background: rgba(255, 255, 255, 0.1);
  color: #ccc;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.action-btn.secondary:hover {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

/* 弹窗动画 */
@keyframes popupFadeIn {
  0% {
    opacity: 0;
    transform: translate(-50%, -50%) scale(0.8);
  }
  100% {
    opacity: 1;
    transform: translate(-50%, -50%) scale(1);
  }
}
</style>
