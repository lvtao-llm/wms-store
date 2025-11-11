import { mergeDeep } from "@vci/helper/src/object";
import { BlendFunction, GodRaysEffect, KernelSize } from "postprocessing";
import { uuid } from "@vci/helper/src/string";
import { createPointLight } from "../../creator/lights/createPointLight";
import { Mesh, MeshBasicMaterial, SphereGeometry } from "three";
import Thing from "../../core/Thing";

function createO3Sun(option) {
  const { qt, size, color, position, optionLight } = mergeDeep({
    qt: null,
    size: 1,
    position: [0, 0, 0],
    color: "#ffddaa",
    optionLight: {
      position: [0, 0, 0]
    }
  }, option);
  optionLight.color = color;
  const light = createPointLight({ qt, ...optionLight });
  const geoSun = new SphereGeometry(size);
  const mtlSun = new MeshBasicMaterial({
    color,
    transparent: true,
    fog: false
  });
  const o3Sun = new Mesh(geoSun, mtlSun);
  // o3Sun.matrixAutoUpdate = false;
  o3Sun.frustumCulled = false;
  o3Sun.add(light);
  o3Sun.light = light;
  new Thing({
    qt,
    object: o3Sun,
    position
  });
  return o3Sun;
}
function createGodRaysEffect(option) {
  const { qt, name, gui, paramsEffect, optionSun } = mergeDeep(
    {
      name: "GodRaysEffect",
      qt: null,
      gui: null,
      o3Sun: null,
      paramsEffect: {
        opacity: 1,
        blendFunction: BlendFunction.ADD,
        height: 480,
        kernelSize: KernelSize.SMALL,
        density: 0.96,
        decay: 0.92,
        weight: 0.3,
        exposure: 0.54,
        samples: 60,
        clampMax: 1.0
      },
      optionSun: {}
    },
    option
  );
  const o3Sun = createO3Sun({ qt, ...optionSun });
  const effect = new GodRaysEffect(qt.camera, o3Sun, paramsEffect);
  qt.effect[effect.uuid = uuid()] = effect;
  if (qt.debug) {
    const menu = gui.addFolder(name);
    menu.close();
    const blendMode = effect.blendMode;
    menu.add(blendMode.opacity, "value", 0.0, 1.0, 0.01).name("opacity").listen();
    menu.add(blendMode, "blendFunction", BlendFunction).name("blend mode").listen();
    const sun = o3Sun;
    const light = o3Sun.light;
    const uniforms = effect.godRaysMaterial.uniforms;
    const params = {
      "resolution": effect.resolution.height,
      "blurriness": effect.blurPass.kernelSize + 1,
      "density": uniforms.density.value,
      "decay": uniforms.decay.value,
      "weight": uniforms.weight.value,
      "exposure": uniforms.exposure.value,
      "clampMax": uniforms.clampMax.value,
      "samples": effect.godRaysMaterial.samples,
      "color": sun.material.color.getHex()
    };
    menu
      .add(params, "resolution", [240, 360, 480, 720, 1080])
      .onChange(value => effect.resolution.height = Number(value));
    menu
      .add(params, "blurriness", KernelSize.VERY_SMALL, KernelSize.HUGE + 1, 1)
      .onChange(value => {
        effect.blur = (value > 0);
        effect.blurPass.kernelSize = value - 1;
      });
    menu.add(params, "density", 0.0, 1.0, 0.01).onChange(value => uniforms.density.value = value);
    menu.add(params, "decay", 0.0, 1.0, 0.01).onChange(value => uniforms.decay.value = value);
    menu.add(params, "weight", 0.0, 1.0, 0.01).onChange(value => uniforms.weight.value = value);
    menu.add(params, "exposure", 0.0, 1.0, 0.01).onChange(value => uniforms.exposure.value = value);
    menu.add(params, "clampMax", 0.0, 1.0, 0.01).onChange(value => uniforms.clampMax.value = value);
    menu.add(effect, "samples", 15, 200, 1);
    menu.addColor(params, "color").onChange(value => {
      sun.material.color.setHex(value);
      light.color.setHex(value);
    });
  }
  return effect;
}
export { createGodRaysEffect };
