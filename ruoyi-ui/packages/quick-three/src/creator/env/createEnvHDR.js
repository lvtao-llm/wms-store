import { RGBELoader } from "three/examples/jsm/loaders/RGBELoader";
import { EquirectangularReflectionMapping, PMREMGenerator } from "three";

async function createEnvHDR(qt, urlHdr, onlyLoadTexture = false, enablePmremGenerator = false) {
  !qt.lm && qt.log.warn("PluginLoadingManager未安装，资源载入进度");
  const manager = qt.lm ? qt.lm.manager : undefined;
  const textureEvn = await new RGBELoader(manager).loadAsync(urlHdr);
  textureEvn.mapping = EquirectangularReflectionMapping;
  if (!onlyLoadTexture) {
    qt.environmentMap = textureEvn;
    if (enablePmremGenerator) {
      const pmremGenerator = new PMREMGenerator(qt.renderer);
      pmremGenerator.compileEquirectangularShader();
      const webGLRenderTarget = pmremGenerator.fromScene(qt.scene);
      qt.scene.background = webGLRenderTarget.texture;
      qt.scene.environment = webGLRenderTarget.texture;
      qt.environmentMap = webGLRenderTarget.texture;
    } else {
      qt.scene.background = textureEvn;
      qt.scene.environment = textureEvn;
    }
    if (qt.debug && qt.gui) {
      qt.gui.guis.env && qt.gui.guis.env.destroy();
      qt.gui.guis.env = qt.gui.gui().addFolder("环境贴图").close();
      qt.gui.guis.env.add({ visibleBg: true }, "visibleBg").name("显示环境").onChange(e => e ? qt.scene.background = qt.environmentMap : qt.scene.background = null);
      qt.gui.guis.env.add(qt.scene, "backgroundBlurriness", 0, 1, 0.01).name("环境模糊度");
    }
  }
  return textureEvn;
}
export { createEnvHDR };
