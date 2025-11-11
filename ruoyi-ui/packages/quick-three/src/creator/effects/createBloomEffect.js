import { mergeDeep } from "@vci/helper/src/object";
import { BlendFunction, SelectiveBloomEffect } from "postprocessing";
import { uuid } from "@vci/helper/src/string";
import { remove, Tween } from "@tweenjs/tween.js";

// 特效|虚幻
function createBloomEffect(option) {
  const { qt, name, gui, paramsEffect } = mergeDeep(
    {
      name: "BloomEffect",
      qt: null,
      gui: null,
      paramsEffect: {
        opacity: 1,
        blendFunction: BlendFunction.ADD,
        mipmapBlur: true,
        luminanceThreshold: 0,
        luminanceSmoothing: 0.2,
        intensity: 1,
        radius: .85
      }
    },
    option
  );
  const effect = new SelectiveBloomEffect(qt.scene, qt.camera, paramsEffect);
  effect.blendMode.opacity.value = paramsEffect.opacity;
  effect.inverted = true;
  effect._intensity = paramsEffect.intensity;
  qt.effect[effect.uuid = uuid()] = effect;
  if (qt.debug) {
    const menu = gui.addFolder(name);
    menu.close();
    const blendMode = effect.blendMode;
    const params = {
      "intensity": effect.intensity,
      "radius": effect.mipmapBlurPass.radius,
      "luminance": {
        "filter": effect.luminancePass.enabled,
        "threshold": effect.luminanceMaterial.threshold,
        "smoothing": effect.luminanceMaterial.smoothing
      },
      "selection": {
        "inverted": effect.inverted,
        "ignore bg": effect.ignoreBackground
      }
    };
    menu.add(blendMode.opacity, "value", 0.0, 1.0, 0.01).name("opacity").listen();
    menu.add(blendMode, "blendFunction", BlendFunction).name("blend mode").listen();
    menu.add(params, "intensity", 0.0, 10.0, 0.01).onChange(value => effect.intensity = Number(value));
    menu.add(params, "radius", 0.0, 1.0, 0.001).onChange(value => effect.mipmapBlurPass.radius = Number(value));
    const folderLuminance = menu.addFolder("Luminance");
    folderLuminance.add(params.luminance, "filter").onChange(value => effect.luminancePass.enabled = value);
    folderLuminance.add(params.luminance, "threshold", 0.0, 1.0, 0.001).onChange(value => effect.luminanceMaterial.threshold = Number(value));
    folderLuminance.add(params.luminance, "smoothing", 0.0, 1.0, 0.001).onChange(value => effect.luminanceMaterial.smoothing = Number(value));
    const folderSelection = menu.addFolder("Selection");
    folderSelection.add(params.selection, "inverted").onChange(value => effect.inverted = value);
    folderSelection.add(params.selection, "ignore bg").onChange(value => effect.ignoreBackground = value);
  }
  // 调整强度
  effect.modifyIntensity = intensity => {
    qt.tw[`${effect.uuid}@intensity`] && remove(qt.tw[`${effect.uuid}@intensity`]);
    qt.tw[`${effect.uuid}@intensity`] = new Tween(effect)
      .to({ intensity })
      .start();
  };
  // 重置强度
  effect.resetIntensity = () => effect.modifyIntensity(effect._intensity);
  return effect;
}
export { createBloomEffect };
