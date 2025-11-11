import { BlendFunction, ToneMappingEffect, ToneMappingMode } from "postprocessing";
import { mergeDeep } from "@vci/helper/src/object";
import { uuid } from "@vci/helper/src/string";

function createToneMappingEffect(option) {
  const { qt, name, gui, paramsEffect } = mergeDeep(
    {
      name: "ToneMappingEffect",
      qt: null,
      gui: null,
      paramsEffect: {
        opacity: 1,
        mode: ToneMappingMode.ACES_FILMIC,
        resolution: 256,
        whitePoint: 16.0,
        middleGrey: 0.6,
        minLuminance: 0.01,
        averageLuminance: 0.01,
        adaptationRate: 1.0
      }
    },
    option
  );
  const effect = new ToneMappingEffect(paramsEffect);
  effect.blendMode.opacity.value = paramsEffect.opacity;
  qt.effect[effect.uuid = uuid()] = effect;
  if (qt.debug) {
    const menu = gui.addFolder(name);
    menu.close();
    const renderer = qt.effect.composer.getRenderer();
    const blendMode = effect.blendMode;
    const adaptiveLuminancePass = effect.adaptiveLuminancePass;
    const adaptiveLuminanceMaterial = adaptiveLuminancePass.fullscreenMaterial;
    const params = {
      "mode": effect.mode,
      "exposure": renderer.toneMappingExposure,
      "resolution": effect.resolution,
      "white point": effect.whitePoint,
      "middle grey": effect.middleGrey,
      "average lum": effect.averageLuminance,
      "min lum": adaptiveLuminanceMaterial.minLuminance,
      "adaptation rate": adaptiveLuminanceMaterial.adaptationRate
    };
    menu.add(blendMode.opacity, "value", 0.0, 1.0, 0.01).name("opacity").listen();
    menu.add(blendMode, "blendFunction", BlendFunction).name("blend mode").listen();
    menu.add(params, "mode", ToneMappingMode).onChange(value => effect.mode = Number(value));
    menu.add(renderer, "toneMappingExposure", 0.0, 2.0, 0.001).listen();
    const folderRm = menu.addFolder("Reinhard (Modified)");
    folderRm.add(params, "white point", 2.0, 32.0, 0.01).onChange(value => effect.whitePoint = value);
    folderRm.add(params, "middle grey", 0.0, 1.0, 0.0001).onChange(value => effect.middleGrey = value);
    folderRm.add(params, "average lum", 0.0001, 1.0, 0.0001).onChange(value => effect.averageLuminance = value);
    const folderRa = menu.addFolder("Reinhard (Adaptive)");
    folderRa.add(params, "resolution", [64, 128, 256, 512]).onChange(value => effect.resolution = Number(value));
    folderRa.add(params, "adaptation rate", 0.001, 3.0, 0.001).onChange(value => adaptiveLuminanceMaterial.adaptationRate = value);
    folderRa.add(params, "min lum", 0.001, 1.0, 0.001).onChange(value => adaptiveLuminanceMaterial.uniforms.minLuminance.value = value);
  }
  return effect;
}
export { createToneMappingEffect };
