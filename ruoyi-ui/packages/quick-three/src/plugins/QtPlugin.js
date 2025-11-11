import EventDispatcher from "@vci/helper/src/EventDispatcher";
import { mergeDeep } from "@vci/helper/src/object";

class QtPlugin extends EventDispatcher {
  static isQtPlugin = true;
  static Events = {
    BeforeRaf: "BeforeRaf",
    Render: "Render",
    AfterRaf: "AfterRaf",
    Resize: "Resize"
  };
  static Location = {
    AfterInitProperties: "AfterInitProperties",
    AfterInit: "AfterInit"
  };
  static location = QtPlugin.Location.AfterInit;

  constructor(qt, option) {
    super();
    this.option = mergeDeep({ namespace: null }, option);
    this.qt = qt;
    this.init();
  }

  init() {
    const { namespace } = this.option;
    this.qt[namespace] = this;
    this.order = 8;
  }

  destroy() {
    super.destroy();
  }
}

export {
  QtPlugin
};
