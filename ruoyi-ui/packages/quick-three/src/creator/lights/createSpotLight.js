import { mergeDeep } from "@vci/helper/src/object";
import { CameraHelper, Color, SpotLight, SpotLightHelper } from "three";

function createSpotLight(option) {
  const {
    qt, name, parent,
    position, intensity, distance, angle, penumbra, decay,
    castShadow, color, enableHelper, shadowCamera
  } = mergeDeep({
    qt: null,
    name: null,
    parent: null,
    color: "#fff",
    position: [100, 100, 100],
    castShadow: true,
    enableHelper: false,
    intensity: 1,
    distance: 0,
    angle: Math.PI / 3,
    penumbra: 1,
    decay: 0.01,
    shadowCamera: {
      near: 1,
      far: 100,
      fov: 75
    }
  }, option);
  const spotLight = new SpotLight(color, intensity, distance, angle, penumbra, decay);
  spotLight.position.fromArray(position);
  spotLight.castShadow = castShadow;
  spotLight.shadow.mapSize.width = 2 ** 11;
  spotLight.shadow.mapSize.height = 2 ** 11;
  spotLight.shadow.camera.near = shadowCamera.near;
  spotLight.shadow.camera.far = shadowCamera.far;
  spotLight.shadow.camera.fov = shadowCamera.fov;
  if (qt) {
    const op = parent || qt.scene;
    const key = `spotLight@${spotLight.uuid}`;
    qt.lights.set(key, spotLight);
    op.add(spotLight);
    if (qt.debug) {
      const helper = {};
      if (enableHelper) {
        helper.spotLightHelper = new SpotLightHelper(spotLight, "#cbffa4");
        op.add(helper.spotLightHelper);
        helper.cameraHelper = new CameraHelper(spotLight.shadow.camera);
        op.add(helper.cameraHelper);
      }
      helper.update = () => {
        helper.spotLightHelper && helper.spotLightHelper.update();
        helper.cameraHelper && helper.cameraHelper.update();
      };
      if (qt.gui) {
        qt.gui.guis[key] = qt.gui.guis.light.addFolder(name || key);
        qt.gui.guis[key].addColor({ color: spotLight.color.getHex() }, "color").name("光照颜色").onChange(e => spotLight.color = new Color(e));
        qt.gui.guis[key].add(spotLight, "intensity").name("光强").step(0.1).listen();
        qt.gui.guis[key].add(spotLight, "angle").min(0.01).max(Math.PI / 2).step(0.001).onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(spotLight, "penumbra", 0, 1, 0.01).onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(spotLight, "decay").onChange(() => helper.update()).listen();
        // qt.gui.guis[key].add(spotLight, "distance").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(spotLight.position, "x").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(spotLight.position, "y").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(spotLight.position, "z").onChange(() => helper.update()).listen();
        if (castShadow) {
          qt.gui.guis[key].add(spotLight.shadow, "bias").name("shadow.bias").step(0.0001).listen();
          qt.gui.guis[key].add(spotLight.shadow, "normalBias").name("shadow.normalBias").step(0.0001).listen();
          qt.gui.guis[key].add(spotLight.shadow, "radius").name("shadow.radius").min(0).step(0.1).listen();
          qt.gui.guis[key].add(spotLight.shadow.camera, "near").name("shadowCamera.near").onChange(() => {
            spotLight.shadow.camera.updateProjectionMatrix();
            helper.update();
          }).listen();
          qt.gui.guis[key].add(spotLight.shadow.camera, "far").name("shadowCamera.far").onChange(() => {
            spotLight.shadow.camera.updateProjectionMatrix();
            helper.update();
          }).listen();
        }
        qt.gui.guis[key].close();
      }
    }
  }
  return spotLight;
}
export { createSpotLight };
