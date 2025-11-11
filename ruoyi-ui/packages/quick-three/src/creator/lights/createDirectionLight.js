import { mergeDeep } from "@vci/helper/src/object";
import { CameraHelper, Color, DirectionalLight, DirectionalLightHelper, Object3D } from "three";
import { ShadowMapViewer } from "three/examples/jsm/utils/ShadowMapViewer";
import { QtEvents } from "../../events/QtEvents";
import { Easing, remove, Tween } from "@tweenjs/tween.js";

// 光照}方向光
function createDirectionLight(option) {
  const {
    qt,
    name,
    parent,
    position,
    target,
    intensity,
    castShadow,
    color,
    enableHelper,
    enableHelperViewer,
    shadow,
    extData
  } = mergeDeep({
    qt: null,
    name: null,
    parent: null,
    position: [100, 100, 100],
    target: [0, 0, 0],
    color: "#fff",
    intensity: 1,
    enableHelper: false,
    enableHelperViewer: false,
    castShadow: false,
    shadow: {
      radius: 1,
      bias: 0,
      normalBias: 0,
      nearCamera: 0.5,
      farCamera: 360,
      sizeCamera: 100,
      sizeMap: 1024
    },
    extData: {}
  }, option);
  const directionalLight = new DirectionalLight(color, intensity);
  directionalLight.position.fromArray(position);
  directionalLight.castShadow = castShadow;
  directionalLight.shadow.camera.top = shadow.sizeCamera;
  directionalLight.shadow.camera.right = shadow.sizeCamera;
  directionalLight.shadow.camera.left = -shadow.sizeCamera;
  directionalLight.shadow.camera.bottom = -shadow.sizeCamera;
  directionalLight.shadow.camera.near = shadow.nearCamera;
  directionalLight.shadow.camera.far = shadow.farCamera;
  directionalLight.shadow.radius = shadow.radius;
  directionalLight.shadow.bias = shadow.bias;
  directionalLight.shadow.normalBias = shadow.normalBias;
  directionalLight.shadow.mapSize.set(shadow.sizeMap, shadow.sizeMap);
  directionalLight.target = new Object3D();
  directionalLight.target.position.fromArray(target);
  directionalLight.target.updateWorldMatrix();
  directionalLight.shadow.updateMatrices(directionalLight);
  directionalLight._intensity = directionalLight.intensity;
  directionalLight.extData = extData;
  if (qt) {
    const key = `directionalLight@${directionalLight.uuid}`;
    qt.lights.set(key, directionalLight);
    const op = parent || qt.scene;
    op.add(directionalLight);
    if (enableHelperViewer) {
      const lightShadowMapViewer = new ShadowMapViewer(directionalLight);
      lightShadowMapViewer.position.x = 10;
      lightShadowMapViewer.position.y = 10;
      lightShadowMapViewer.size.width = window.innerWidth / 4;
      lightShadowMapViewer.size.height = window.innerHeight / 4;
      lightShadowMapViewer.update();
      qt.addEventListener(QtEvents.AfterAnimateFrame, e => lightShadowMapViewer.render(e.target.renderer));
    }
    if (qt.debug) {
      const helper = {};
      if (enableHelper) {
        helper.directionalLightHelper = new DirectionalLightHelper(directionalLight, shadow.sizeCamera, "#58ffb2");
        op.add(helper.directionalLightHelper);
        if (castShadow) {
          helper.cameraHelper = new CameraHelper(directionalLight.shadow.camera);
          op.add(helper.cameraHelper);
        }
      }
      helper.update = () => {
        directionalLight.shadow.updateMatrices(directionalLight);
        helper.directionalLightHelper && helper.directionalLightHelper.update();
        helper.cameraHelper && helper.cameraHelper.update();
      };
      if (qt.gui) {
        qt.gui.guis[key] = qt.gui.guis.light.addFolder(name || key);
        qt.gui.guis[key].addColor({ color: directionalLight.color.getHex() }, "color").name("光照颜色").onChange(e => directionalLight.color = new Color(e));
        qt.gui.guis[key].add(directionalLight, "intensity").name("光强").step(0.1).listen();
        qt.gui.guis[key].add(directionalLight.position, "x").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(directionalLight.position, "y").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(directionalLight.position, "z").onChange(() => helper.update()).listen();
        if (castShadow) {
          qt.gui.guis[key].add(directionalLight.shadow, "bias").name("shadow.bias").step(0.0001).listen();
          qt.gui.guis[key].add(directionalLight.shadow, "normalBias").name("shadow.normalBias").step(0.0001).listen();
          qt.gui.guis[key].add(directionalLight.shadow, "radius").name("shadow.radius").min(0).step(0.1).listen();
          qt.gui.guis[key].add(directionalLight.shadow.camera, "near").name("shadowCamera.near").onChange(() => {
            directionalLight.shadow.camera.updateProjectionMatrix();
            helper.update();
          }).listen();
          qt.gui.guis[key].add(directionalLight.shadow.camera, "far").name("shadowCamera.far").onChange(() => {
            directionalLight.shadow.camera.updateProjectionMatrix();
            helper.update();
          }).listen();
        }
        qt.gui.guis[key].close();
      }
    }
    directionalLight.modifyIntensity = (intensity, twOption) => {
      const { duration, easing } = mergeDeep(
        {
          duration: .8e3,
          easing: Easing.Cubic.InOut
        },
        twOption
      );
      qt.tw[key] && remove(qt.tw[key]);
      return new Promise(resolve => {
        qt.tw[key] = new Tween(directionalLight)
          .to({ intensity })
          .duration(duration)
          .easing(easing)
          .onComplete(resolve)
          .start();
      });
    };
    directionalLight.intensityReset = async () => await directionalLight.modifyIntensity(directionalLight._intensity);
  }
  return directionalLight;
}
export { createDirectionLight };
