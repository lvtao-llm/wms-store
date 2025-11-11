import { QtPlugin } from "./QtPlugin";
import { GUI } from "three/examples/jsm/libs/lil-gui.module.min";
import { mergeDeep } from "@vci/helper/src/object";

class PluginGui extends QtPlugin {
  static namespace = "gui";
  static location = QtPlugin.Location.AfterInitProperties;

  constructor(qt, option) {
    super(qt, mergeDeep({ name: null, nameGui: null }, option));
  }

  init() {
    super.init();
    const { qt } = this;
    const { name } = qt.option;
    this.guis = {};
    if (qt.debug) {
      this.gui(name);
      this.guis.scene = this.gui().addFolder("场景").close();
      this.guis.camera = this.gui().addFolder("相机").close();
      this.guis.renderer = this.gui().addFolder("渲染器").close();
      this.guis.control = this.gui().addFolder("控制器").close();
      this.guis.light = this.gui().addFolder("光源").close();
      this.guis.postprocessing = this.gui().addFolder("后期处理").close();
      this.guis.fns = this.gui().addFolder("功能").close();
    }
  }

  // 辅助调试工具
  gui(name) {
    if (!this._gui) {
      this._gui = new GUI({
        width: 400,
        title: name
      });
    }
    return this._gui;
  }

  destroy() {
    super.destroy();
    if (this._gui) {
      this._gui.destroy();
      this.guis = {};
    }
  }
}

export { PluginGui };
