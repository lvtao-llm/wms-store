import { mergeDeep } from "@vci/helper/src/object";
import { BlendFunction, KernelSize, OutlineEffect } from "postprocessing";
import { uuid } from "@vci/helper/src/string";

function createOutlineEffect(option) {
  const { qt, name, gui, paramsEffect } = mergeDeep(
    {
      name: "OutlineEffect",
      qt: null,
      gui: null,
      paramsEffect: {
        opacity: 1,
        blendFunction: BlendFunction.ADD,
        edgeStrength: 2.5,
        pulseSpeed: 0.0,
        blurriness: 0,
        visibleEdgeColor: 0xffffff,
        hiddenEdgeColor: 0x999999,
        height: 480,
        blur: false,
        xRay: true
      }
    },
    option
  );
  const effect = new OutlineEffect(qt.scene, qt.camera, paramsEffect);
  effect.blendMode.opacity.value = paramsEffect.opacity;
  paramsEffect.blurriness = Math.round(paramsEffect.blurriness);
  effect.blurPass.enabled = paramsEffect.blurriness > 0;
  effect.blurPass.blurMaterial.kernelSize = paramsEffect.blurriness - 1;
  qt.effect[effect.uuid = uuid()] = effect;
  if (qt.debug) {
    const menu = gui.addFolder(name);
    menu.close();
    const renderer = qt.effect.composer.getRenderer();
    paramsEffect.multisampling = Math.min(4, renderer.capabilities.maxSamples);
    const blendMode = effect.blendMode;
    const uniforms = effect.uniforms;
    const params = {
      "resolution": effect.height,
      "blurriness": 0,
      "use pattern": false,
      "pattern scale": 60.0,
      "pulse speed": effect.pulseSpeed,
      "edge strength": effect.edgeStrength,
      "visible edge": paramsEffect.visibleEdgeColor,
      "hidden edge": paramsEffect.hiddenEdgeColor,
      "x-ray": true
    };
    menu.add(blendMode.opacity, "value", 0.0, 1.0, 0.01).name("opacity").listen();
    menu.add(blendMode, "blendFunction", BlendFunction).name("blend mode").listen();
    menu.add(params, "resolution", [240, 360, 480, 720, 1080]).onChange(value => effect.resolution.height = Number(value));
    menu.add(effect, "multisampling", [0, 2, 4]);
    menu
      .add(params, "blurriness", KernelSize.VERY_SMALL, KernelSize.HUGE + 1, 1)
      .onChange(value => {
        effect.blurPass.enabled = (value > 0);
        effect.blurPass.blurMaterial.kernelSize = value - 1;
      });
    // menu
    //   .add(params, "use pattern")
    //   .onChange(value => {
    //   if (value) {
    //     effect.patternTexture = assets.get("pattern-color");
    //     uniforms.get("patternScale").value = params["pattern scale"];
    //   } else {
    //     effect.patternTexture = null;
    //   }
    // });
    // menu.add(params, "pattern scale", 20.0, 100.0, 0.1).onChange((value) => {
    //   uniforms.get("patternScale").value = value;
    // });
    menu.add(params, "edge strength", 0.0, 10.0, 0.01).onChange(value => uniforms.get("edgeStrength").value = value);
    menu.add(params, "pulse speed", 0.0, 2.0, 0.01).onChange(value => effect.pulseSpeed = value);
    menu.addColor(params, "visible edge").onChange(value => effect.visibleEdgeColor.setHex(value).convertSRGBToLinear());
    menu.addColor(params, "hidden edge").onChange(value => effect.hiddenEdgeColor.setHex(value).convertSRGBToLinear());
    menu.add(effect, "xRay");
  }
  return effect;
}
export { createOutlineEffect };
