import { CubeTextureLoader, LinearSRGBColorSpace, NoColorSpace, SRGBColorSpace } from "three";
import { resourceRootAliyun } from "@vci/vci/config/api";
import MaterialPatch from "../../helper/MaterialPatch";

async function createEnvCubeTexture(qt) {
  const manager = qt.lm ? qt.lm.manager : null;
  const environmentMap = await new CubeTextureLoader(manager).loadAsync([
    `${resourceRootAliyun}/textures/sky-night/dark-s_px.jpg`, `${resourceRootAliyun}/textures/sky-night/dark-s_nx.jpg`,
    `${resourceRootAliyun}/textures/sky-night/dark-s_py.jpg`, `${resourceRootAliyun}/textures/sky-night/dark-s_ny.jpg`,
    `${resourceRootAliyun}/textures/sky-night/dark-s_pz.jpg`, `${resourceRootAliyun}/textures/sky-night/dark-s_nz.jpg`
  ]);
  environmentMap.colorSpace = SRGBColorSpace;
  qt.environmentMap = environmentMap;
  const scene = qt.scene;
  scene.background = environmentMap;
  scene.environment = environmentMap;
  qt.scene.backgroundBlurriness = 0;
  if (qt.debug && qt.gui) {
    qt.gui.guis.env && qt.gui.gui().removeFolder(qt.gui.guis.env);
    qt.gui.guis.env = qt.gui.gui().addFolder("环境贴图").close();
    qt.gui.guis.env.add({ env: 0 }, "env", 0, 1, 0.01).name("环境贴图强度").onChange(e => MaterialPatch.ModifyMtlProperties(scene, { env: e }));
    qt.gui.guis.env.add({ visibleBg: true }, "visibleBg").name("显示环境").onChange(e => e ? qt.scene.background = qt.environmentMap : qt.scene.background = null);
    qt.gui.guis.env.add(qt.scene, "backgroundBlurriness", 0, 1, 0.01).name("环境模糊度");
    qt.gui.guis.env.add(environmentMap, "colorSpace", {
      "NoColorSpace": NoColorSpace,
      "SRGBColorSpace": SRGBColorSpace,
      "LinearSRGBColorSpace": LinearSRGBColorSpace
    }).name("colorSpace").onChange(e => {
      environmentMap.colorSpace = e;
      environmentMap.needsUpdate = true;
    }).listen();
  }
}

export { createEnvCubeTexture };