import { QtPlugin } from "./QtPlugin";
import {
  EffectComposer,
  EffectPass,
  OverrideMaterialManager,
  RenderPass,
  SMAAEffect,
  SMAAPreset
} from "postprocessing";
import { createOutlineEffect } from "../creator/effects/createOutlineEffect";
import { createToneMappingEffect } from "../creator/effects/createToneMappingEffect";

class PluginEffectComposer extends QtPlugin {
  static namespace = "effect";

  init() {
    super.init();
    const { qt } = this;
    OverrideMaterialManager.workaroundEnabled = true;
    const composer = this.composer = new EffectComposer(
      qt.renderer,
      { multisampling: Math.min(4, qt.renderer.capabilities.maxSamples) }
    );
    composer.addPass(new RenderPass(qt.scene, qt.camera));
    const effectSMAA = new SMAAEffect({ preset: SMAAPreset.HIGH });
    const effectOutline = this.effectOutline = createOutlineEffect({
      qt,
      gui: qt.gui.guis.postprocessing,
      paramsEffect: { blurriness: 1 }
    });
    const effectToneMapping = this.effectToneMapping = createToneMappingEffect({ qt, gui: qt.gui.guis.postprocessing });
    composer.addPass(new EffectPass(
      qt.camera,
      effectSMAA,
      effectOutline,
      effectToneMapping
    ));
    this.addEventListener(QtPlugin.Events.Render, e => {
      this.composer.render(e.detail.delta);
      qt.renderer.info.reset();
    });
    this.addEventListener(QtPlugin.Events.Resize, e => this.composer.setSize(e.detail.width, e.detail.height));
  }
}

export { PluginEffectComposer };
