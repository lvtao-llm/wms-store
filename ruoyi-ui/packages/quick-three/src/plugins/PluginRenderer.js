import { QtPlugin } from "./QtPlugin";
import { addCss } from "@vci/helper/src/element";
import { mergeDeep } from "@vci/helper/src/object";
import {
  BasicShadowMap,
  LinearSRGBColorSpace,
  PCFShadowMap,
  PCFSoftShadowMap,
  SRGBColorSpace,
  VSMShadowMap,
  WebGLRenderer
} from "three";

class PluginRenderer extends QtPlugin {
  static namespace = "rendererGl";

  constructor(qt, option) {
    super(qt, mergeDeep({
      params: {
        powerPreference: "high-performance",
        antialias: false,
        stencil: false,
        depth: false,
        alpha: true,
        // 超大模型，巨大的比例差异处理
        logarithmicDepthBuffer: true
      },
      enableShadow: false,
      shadowMapType: PCFSoftShadowMap,
      releaseWebGLContextOnDestroy: true
    }, option));
  }

  init() {
    super.init();
    const { qt } = this;
    const { params, enableShadow, shadowMapType } = this.option;
    this.renderer = qt.renderer = this.createRenderer(qt, params);
    if (!params.context) {
      qt.el.appendChild(this.renderer.domElement);
      addCss(this.renderer.domElement, {
        position: "relative",
        zIndex: 1
      });
    }
    qt.renderer.debug.checkShaderErrors = qt.debug;
    qt.renderer.info.autoReset = false;
    qt.renderer.shadowMap.type = shadowMapType;
    qt.renderer.shadowMap.enabled = enableShadow;
    this.addEventListener(QtPlugin.Events.Render, () => {
      if (!qt.effect) {
        this.renderer.render(this.qt.scene, this.qt.camera);
        this.renderer.info.reset();
      }
    });
    this.addEventListener(QtPlugin.Events.Resize, e => this.renderer.setSize(e.detail.width, e.detail.height));
  }

  createRenderer(qt, params) {
    const renderer = new WebGLRenderer(params);
    renderer.setPixelRatio(qt.dpr);
    renderer.setSize(qt.elWidth, qt.elHeight, true);
    renderer.domElement.removeAttribute("data-engine");
    if (qt.debug && qt.gui) {
      const gui = qt.gui.guis.renderer;
      gui
        .add(renderer.shadowMap, "enabled")
        .name("是否启用阴影贴图")
        .onChange(() => {
          renderer.shadowMap.type = PCFSoftShadowMap;
          qt.helper.updateSceneMaterials();
        })
        .listen();
      gui
        .add(renderer.shadowMap, "type", {
          "BasicShadowMap": BasicShadowMap,
          "PCFShadowMap": PCFShadowMap,
          "PCFSoftShadowMap": PCFSoftShadowMap,
          "VSMShadowMap": VSMShadowMap
        })
        .onChange(() => qt.helper.updateSceneMaterials()).name("阴影类型")
        .listen();
      gui
        .add(renderer, "outputColorSpace", {
          "SRGBColorSpace": SRGBColorSpace,
          "LinearSRGBColorSpace": LinearSRGBColorSpace
        })
        .name("颜色空间")
        .onChange(e => {
          renderer.outputColorSpace = e;
          qt.helper.updateSceneMaterials();
        })
        .listen();
    }
    return renderer;
  }

  destroy() {
    super.destroy();
    const renderer = this.renderer;
    renderer.dispose();
    // #正确释放WebGL上下文
    if (this.option.releaseWebGLContextOnDestroy) {
      renderer.getContext().getExtension("WEBGL_lose_context").loseContext();
      renderer.domElement.parentNode && renderer.domElement.parentNode.removeChild(renderer.domElement);
    }
    console.info(renderer.info);
  }
}

export { PluginRenderer };
