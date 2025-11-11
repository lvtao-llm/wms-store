import { EquirectangularReflectionMapping } from "three";
import { HDRJPGLoader } from "@monogrid/gainmap-js";

async function createEnvHDRJPG(qt, urlJpeg, onlyLoadTexture = false) {
  !qt.lm && qt.log.warn("PluginLoadingManager未安装，资源载入进度");
  const manager = qt.lm ? qt.lm.manager : undefined;
  const textureEvn = await new Promise(resolve => {
    manager.itemStart(urlJpeg);
    const loaderHj = new HDRJPGLoader(qt.renderer).load(urlJpeg, () => {
      const texture = loaderHj.renderTarget.texture;
      texture.mapping = EquirectangularReflectionMapping;
      texture.needsUpdate = true;
      loaderHj.dispose();
      manager.itemEnd(urlJpeg);
      resolve(texture);
    });
  });
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
export { createEnvHDRJPG };
