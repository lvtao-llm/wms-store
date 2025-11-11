import { QtPlugin } from "./QtPlugin";
import { mergeDeep } from "@vci/helper/src/object";

class PluginLoading extends QtPlugin {
  static namespace = "loading";

  constructor(qt, option) {
    super(qt, mergeDeep({
      // 加载状态实例  实例必须提供 show|hide|process 三个功能函数
      show: () => qt.debug && console.info("开始加载"),
      hide: () => qt.debug && console.info("结束加载"),
      process: percent => qt.debug && console.info(`进度: ${percent}`)
    }, option));
  }

  init() {
    super.init();
    const { show, hide, process } = this.option;
    this.show = show;
    this.hide = hide;
    this.process = process;
  }
}

export { PluginLoading };
