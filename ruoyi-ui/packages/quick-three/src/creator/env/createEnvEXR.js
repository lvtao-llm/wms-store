import { EXRLoader } from "three/examples/jsm/loaders/EXRLoader";
import { EquirectangularReflectionMapping } from "three";

async function createEnvEXR(qt, urlExr, onlyLoadTexture = false) {
  !qt.lm && qt.log.warn("PluginLoadingManager未安装，资源载入进度");
  const manager = qt.lm ? qt.lm.manager : undefined;
  const textureEvn = await new EXRLoader(manager).loadAsync(urlExr);
  textureEvn.mapping = EquirectangularReflectionMapping;
  if (!onlyLoadTexture) {
    qt.environmentMap = textureEvn;
    qt.scene.background = textureEvn;
    qt.scene.environment = textureEvn;
    if (qt.debug && qt.gui) {
      qt.gui.guis.env && qt.gui.guis.env.destroy();
      qt.gui.guis.env = qt.gui.gui().addFolder("环境贴图").close();
      qt.gui.guis.env.add({ visibleBg: true }, "visibleBg").name("显示环境").onChange(e => e ? qt.scene.background = qt.environmentMap : qt.scene.background = null);
      qt.gui.guis.env.add(qt.scene, "backgroundBlurriness", 0, 1, 0.01).name("环境模糊度");
    }
  }
  return textureEvn;
}
export { createEnvEXR };
