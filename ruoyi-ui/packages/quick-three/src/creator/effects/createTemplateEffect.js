import { mergeDeep } from "@vci/helper/src/object";
import { BlendFunction, GodRaysEffect } from "postprocessing";
import { uuid } from "@vci/helper/src/string";

function createTemplateEffect(option) {
  const { qt, name, gui, paramsEffect } = mergeDeep(
    {
      name: "TemplateEffect",
      qt: null,
      gui: null,
      o3Sun: null,
      paramsEffect: {
        opacity: 1,
        blendFunction: BlendFunction.NORMAL
      }
    },
    option
  );
  const effect = new GodRaysEffect(paramsEffect);
  qt.effect[effect.uuid = uuid()] = effect;
  if (qt.debug) {
    const menu = gui.addFolder(name);
    menu.close();
    const blendMode = effect.blendMode;
    menu.add(blendMode.opacity, "value", 0.0, 1.0, 0.01).name("opacity").listen();
    menu.add(blendMode, "blendFunction", BlendFunction).name("blend mode").listen();
  }
  return effect;
}
export { createTemplateEffect };
