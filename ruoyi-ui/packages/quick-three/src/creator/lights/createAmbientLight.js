import { mergeDeep } from "@vci/helper/src/object";
import { AmbientLight, Color } from "three";

function createAmbientLight(option) {
  const { qt, name, intensity, color } = mergeDeep({
    qt: null,
    name: null,
    color: "#fff",
    intensity: 1
  }, option);
  const ambientLight = new AmbientLight(color, intensity);
  if (qt) {
    const key = `ambientLight@${ambientLight.uuid}`;
    qt.lights.set(key, ambientLight);
    qt.scene.add(ambientLight);
    if (qt.debug && qt.gui) {
      qt.gui.guis[key] = qt.gui.guis.light.addFolder(name || key);
      qt.gui.guis[key].addColor({ color: ambientLight.color.getHex() }, "color").name("光照颜色").onChange(e => ambientLight.color = new Color(e));
      qt.gui.guis[key].add(ambientLight, "intensity").name("光强").step(0.1).listen();
      qt.gui.guis[key].close();
    }
  }
  return ambientLight;
}
export { createAmbientLight };
