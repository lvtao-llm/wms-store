import { QtPlugin } from "./QtPlugin";
import { addCss } from "@vci/helper/src/element";
import { CSS3DRenderer } from "three/examples/jsm/renderers/CSS3DRenderer";

class PluginRenderer3D extends QtPlugin {
  static namespace = "rendererC3";

  init() {
    super.init();
    const { qt } = this;
    const renderer = this.renderer = new CSS3DRenderer();
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

export { PluginRenderer3D };
