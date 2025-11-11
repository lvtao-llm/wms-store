import Stats from "three/examples/jsm/libs/stats.module";
import { addCss, getElementBody, removeCss } from "@vci/helper/src/element";
import { QtPlugin } from "./QtPlugin";

class PluginPerformance extends QtPlugin {
  static namespace = "performance";

  init() {
    super.init();
    if (this.qt.debug) {
      this.stats = new Stats();
      removeCss(this.stats.dom, ["top", "left"]);
      addCss(this.stats.dom, {
        right: "12px",
        bottom: "12px"
      });
      getElementBody().appendChild(this.stats.dom);
      this.addEventListener(QtPlugin.Events.BeforeRaf, () => this.stats.begin());
      this.addEventListener(QtPlugin.Events.AfterRaf, () => this.stats.end());
    }
  }

  destroy() {
    this.qt.debug && getElementBody().removeChild(this.stats.dom);
  }
}

export { PluginPerformance };
