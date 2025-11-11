import { mergeDeep } from "@vci/helper/src/object";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
import { KTX2Loader } from "three/examples/jsm/loaders/KTX2Loader";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader";
import { DefaultLoadingManager, WebGLRenderer } from "three";
import { resourceRootAliyun } from "../../constants";

function createLoaderGltf(option) {
  const { qt, decoderPath, ktx2DecoderPath } = mergeDeep(
    {
      qt: {},
      decoderPath: `${resourceRootAliyun}/libs/three@167.1/draco/gltf/`,
      ktx2DecoderPath: `${resourceRootAliyun}/libs/three@167.1/basis/`
    },
    option
  );
  !qt.lm && (qt.log && qt.log.warn("PluginLoadingManager未安装，资源载入进度"));
  const manager = qt.lm ? qt.lm.manager : DefaultLoadingManager;
  const loader = new GLTFLoader(manager);
  const loaderKtx2 = new KTX2Loader(manager);
  loaderKtx2.setTranscoderPath(ktx2DecoderPath);
  const renderer = new WebGLRenderer();
  loaderKtx2.detectSupport(renderer);
  renderer.dispose();
  renderer.getContext().getExtension("WEBGL_lose_context").loseContext();
  loader.setKTX2Loader(loaderKtx2);
  const loaderDraco = new DRACOLoader(manager);
  loaderDraco.setDecoderPath(decoderPath);
  loader.setDRACOLoader(loaderDraco);
  return loader;
}
export { createLoaderGltf };
