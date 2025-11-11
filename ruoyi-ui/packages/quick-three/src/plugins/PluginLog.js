import { QtPlugin } from "./QtPlugin";
import { mergeDeep } from "@vci/helper/src/object";

class PluginLog extends QtPlugin {
  static namespace = "log";
  static syl = "color: #fff; font-style: Microsoft YaHei UI; background-color: #4caf50;padding: 2px;border-radius: 2px;";

  constructor(qt, option) {
    super(qt, mergeDeep({ name: null }, option));
  }

  format(message) {
    const { name } = this.option;
    return `%c[ QuickThree ${name ? "| " + name : ""} ]%c ${message}`;
  }

  info(message) {
    message = this.format(message);
    console.info(message, PluginLog.syl, ...arguments);
  }

  error(message) {
    message = this.format(message);
    console.error(message, PluginLog.syl, ...arguments);
  }

  warn(message) {
    message = this.format(message);
    console.warn(message, PluginLog.syl, ...arguments);
  }
}

export { PluginLog };
