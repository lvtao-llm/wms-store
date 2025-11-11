import { QtPlugin } from "./QtPlugin";
import { CubeCamera, LinearMipmapLinearFilter, SRGBColorSpace, WebGLCubeRenderTarget } from "three";

export default class PluginCubeCamera extends QtPlugin {
  init() {
    super.init();
    this.order = 0;
    const cubeRenderTarget = new WebGLCubeRenderTarget(256, {
      colorSpace: SRGBColorSpace,
      generateMipmaps: true,
      minFilter: LinearMipmapLinearFilter
    });
    const cubeCamera = new CubeCamera(0.001, 10e4, cubeRenderTarget);
    this.qt.cubeCamera = cubeCamera;
    this.qt.cubeRenderTarget = cubeRenderTarget;
    this.addEventListener(QtPlugin.Events.Render, () => cubeCamera.update(this.qt.renderer, this.qt.scene));
  }
}
