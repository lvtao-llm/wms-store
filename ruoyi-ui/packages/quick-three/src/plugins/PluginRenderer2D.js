import { QtPlugin } from "./QtPlugin";
import { CSS2DRenderer } from "three/examples/jsm/renderers/CSS2DRenderer";
import { addCss } from "@vci/helper/src/element";

class PluginRenderer2D extends QtPlugin {
  static namespace = "rendererC2";

  init() {
    super.init();
    const { qt } = this;
    const renderer = this.renderer = new CSS2DRenderer();
    renderer.setSize(qt.elWidth, qt.elHeight);
    addCss(renderer.domElement, {
      position: "absolute",
      left: 0,
      top: 0,
      zIndex: 3,
      pointerEvents: qt.renderer ? "none" : "auto"
    });
    !qt.renderer && (qt.renderer = this.renderer);
    qt.el.appendChild(renderer.domElement);
    this.addEventListener(QtPlugin.Events.Render, () => this.renderer.render(qt.scene, qt.camera));
    this.addEventListener(QtPlugin.Events.Resize, e => this.renderer.setSize(e.detail.width, e.detail.height));
  }

  destroy() {
    super.destroy();
    this.renderer.domElement.parentNode && this.renderer.domElement.parentNode.removeChild(this.renderer.domElement);
  }
}

export { PluginRenderer2D };
