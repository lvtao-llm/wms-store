import { mergeDeep } from "@vci/helper/src/object";
import { RectAreaLightUniformsLib } from "three/examples/jsm/lights/RectAreaLightUniformsLib";
import { Color, RectAreaLight, Vector3 } from "three";
import { RectAreaLightHelper } from "three/examples/jsm/helpers/RectAreaLightHelper";

RectAreaLightUniformsLib.init();
function createRectAreaLight(option) {
  const { qt, name, parent, color, intensity, width, height, position, lookAt, enableHelper } = mergeDeep({
    qt: null,
    name: null,
    parent: null,
    width: 20,
    height: 4,
    position: [0, 0, 0],
    lookAt: [0, 0, 0],
    color: "#fff",
    intensity: 1,
    enableHelper: false
  }, option);
  const ve3LookAt = new Vector3(...lookAt);
  const rectAreaLight = new RectAreaLight(color, intensity, width, height);
  rectAreaLight.position.fromArray(position);
  rectAreaLight.lookAt(ve3LookAt);
  if (qt) {
    const op = parent || qt.scene;
    const key = `rectAreaLight@${rectAreaLight.uuid}`;
    qt.lights.set(key, rectAreaLight);
    op.add(rectAreaLight);
    if (qt.debug) {
      const helper = {};
      if (enableHelper) {
        helper.rectAreaLightHelper = new RectAreaLightHelper(rectAreaLight);
        op.add(helper.rectAreaLightHelper);
      }
      helper.update = () => {
        rectAreaLight.lookAt(ve3LookAt);
        rectAreaLight.updateMatrix();
        helper.rectAreaLightHelper && helper.rectAreaLightHelper.updateMatrixWorld();
      };
      if (qt.gui) {
        qt.gui.guis[key] = qt.gui.guis.light.addFolder(name || key);
        qt.gui.guis[key].addColor({ color: rectAreaLight.color.getHex() }, "color").name("光照颜色").onChange(e => rectAreaLight.color = new Color(e));
        qt.gui.guis[key].add(rectAreaLight, "intensity").name("光强").step(0.1).listen();
        qt.gui.guis[key].add(rectAreaLight.position, "x").name("position@x").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(rectAreaLight.position, "y").name("position@y").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(rectAreaLight.position, "z").name("position@z").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(ve3LookAt, "x").name("lookAt@x").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(ve3LookAt, "y").name("lookAt@y").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(ve3LookAt, "z").name("lookAt@z").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(rectAreaLight, "width").onChange(() => helper.update()).listen();
        qt.gui.guis[key].add(rectAreaLight, "height").onChange(() => helper.update()).listen();
        qt.gui.guis[key].close();
      }
    }
  }
  return rectAreaLight;
}
function createRectAreaSurroundLight(option) {
  option = mergeDeep({
    qt: null,
    parent: null,
    position: [0, 0, 0],
    width: 20,
    height: 4
  }, option);
  const { width, height, position } = option;
  const heightHalf = height / 2;
  const widthHalf = width / 2;
  const lookAt = [0, heightHalf, 0].map((v, i) => v + position[i]);
  createRectAreaLight({
    ...option,
    position: [-widthHalf, heightHalf, 0].map((v, i) => v + position[i]),
    lookAt
  });
  createRectAreaLight({
    ...option,
    position: [widthHalf, heightHalf, 0].map((v, i) => v + position[i]),
    lookAt
  });
  createRectAreaLight({
    ...option,
    position: [0, heightHalf, widthHalf].map((v, i) => v + position[i]),
    lookAt
  });
  createRectAreaLight({
    ...option,
    position: [0, heightHalf, -widthHalf].map((v, i) => v + position[i]),
    lookAt
  });
}
export { createRectAreaLight, createRectAreaSurroundLight };
