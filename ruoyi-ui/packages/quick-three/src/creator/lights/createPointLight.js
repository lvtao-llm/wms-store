import { mergeDeep } from "@vci/helper/src/object";
import { CameraHelper, Color, PointLight, PointLightHelper } from "three";

function createPointLight(option) {
  const {
    qt,
    name,
    parent,
    position,
    intensity,
    distance,
    decay,
    castShadow,
    color,
    enableHelper,
    shadow
  } = mergeDeep({
    qt: null,
    name: null,
    parent: null,
    position: [100, 100, 100],
    color: "#fff",
    intensity: 1,
    distance: 0, // 0代表无限远
    decay: 2,  // 在物理正确渲染的上下文中，不应更改默认值。
    castShadow: false,
    enableHelper: false,
    shadow: {
      radius: 1,
      bias: 0,
      normalBias: 0,
      farCamera: 360,
      sizeCamera: 100,
      sizeMap: 1024
    }
  }, option);
  const pointLight = new PointLight(color, intensity, distance, decay);
  pointLight.position.fromArray(position);
  pointLight.castShadow = castShadow;
  pointLight.shadow.camera.top = shadow.sizeCamera;
  pointLight.shadow.camera.right = shadow.sizeCamera;
  pointLight.shadow.camera.left = -shadow.sizeCamera;
  pointLight.shadow.camera.bottom = -shadow.sizeCamera;
  pointLight.shadow.camera.near = 0.1;
  pointLight.shadow.camera.far = shadow.farCamera;
  pointLight.shadow.radius = shadow.radius;
  pointLight.shadow.bias = shadow.bias;
  pointLight.shadow.normalBias = shadow.normalBias;
  pointLight.shadow.mapSize.set(shadow.sizeMap, shadow.sizeMap);
  pointLight.lookAt(0, 0, 0);
  pointLight.shadow.updateMatrices(pointLight);
  if (qt) {
    const op = parent || qt.scene;
    const key = `pointLight@${pointLight.uuid}`;
    qt.lights.set(key, pointLight);
    op.add(pointLight);
    if (qt.debug) {
      const helper = {};
      if (enableHelper) {
        helper.pointLightHelper = new PointLightHelper(pointLight, 1, "#50cbb2");
        op.add(helper.pointLightHelper);
        if (castShadow) {
          helper.cameraHelper = new CameraHelper(pointLight.shadow.camera);
          op.add(helper.cameraHelper);
        }
      }
      helper.update = () => {
        helper.pointLightHelper && helper.pointLightHelper.update();
        helper.cameraHelper && helper.cameraHelper.update();
      };
      if (qt.gui) {
        qt.gui.guis[key] = qt.gui.guis.light.addFolder(name || key);
        qt.gui.guis[key].addColor({ color: pointLight.color.getHex() }, "color").name("光照颜色").onChange(e => pointLight.color = new Color(e));
        qt.gui.guis[key].add(pointLight, "intensity").name("光强").step(0.1).listen();
        qt.gui.guis[key].add(pointLight, "power").name("功率").step(0.1).listen();
        qt.gui.guis[key].add(pointLight, "distance").name("最大距离").step(0.1).listen();
        qt.gui.guis[key].add(pointLight, "decay").name("衰减").step(0.1).listen();
        qt.gui.guis[key].add(pointLight.position, "x").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(pointLight.position, "y").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(pointLight.position, "z").onChange(() => helper.update()).listen();
        if (castShadow) {
          qt.gui.guis[key].add(pointLight.shadow, "bias").name("shadow.bias").step(0.0001).listen();
          qt.gui.guis[key].add(pointLight.shadow, "normalBias").name("shadow.normalBias").step(0.0001).listen();
          qt.gui.guis[key].add(pointLight.shadow, "radius").name("shadow.radius").min(0).step(0.1).listen();
          qt.gui.guis[key].add(pointLight.shadow.camera, "near").name("shadowCamera.near").onChange(() => {
            pointLight.shadow.camera.updateProjectionMatrix();
            helper.update();
          }).listen();
          qt.gui.guis[key].add(pointLight.shadow.camera, "far").name("shadowCamera.far").onChange(() => {
            pointLight.shadow.camera.updateProjectionMatrix();
            helper.update();
          }).listen();
        }
        qt.gui.guis[key].close();
      }
    }
  }
  return pointLight;
}
export { createPointLight };
