import { QtPlugin } from "./QtPlugin";
import { Cache, LoadingManager } from "three";
import { QtEvents } from "../events/QtEvents";
import { mergeDeep } from "@vci/helper/src/object";

class PluginLoadingManager extends QtPlugin {
  static namespace = "lm";
  static Events = {
    LoadedBases: QtEvents.LoadedBases = "LoadedBases",
    AfterLoaded: QtEvents.AfterLoaded = "AfterLoaded"
  };

  constructor(qt, option) {
    super(qt, mergeDeep({
      // 是否支持资源缓存
      enableCache: false
    }, option));
  }

  init() {
    super.init();
    const { qt } = this;
    !qt.loading && qt.log.warn("PluginLoadingManager建议配合PluginLoading插件一起使用");
    Object.defineProperty(this, "enableCache", {
      set(v) {
        Cache.enabled = v;
      },
      get() {
        return Cache.enabled;
      }
    });
    this.enableCache = this.option.enableCache;
    this.isLoaded = false;
    this.manager = new LoadingManager(
      () => {
        clearTimeout(qt.inter.loaded);
        qt.inter.loaded = setTimeout(() => {
          qt.debug && qt.log.info(`本次加载已完成`);
          clearTimeout(qt.inter.loading);
          qt.loading && qt.loading.hide();
          qt.dispatchEvent(QtEvents.AfterLoaded);
          if (!this.isLoaded) {
            this.isLoaded = true;
            qt.dispatchEvent(QtEvents.LoadedBases);
          }
        }, 100);
      },
      (url, loaded, total) => {
        clearTimeout(qt.inter.loaded);
        qt.loading && qt.loading.process(loaded * 100 / total);
        qt.debug && qt.log.info(`资源加载进度: ${loaded}/${total}`);
      },
      e => qt.debug && qt.log.error("loading error", e)
    );
    this.manager.onStart = () => {
      clearTimeout(qt.inter.loaded);
      clearTimeout(qt.inter.loading);
      !this.isLoaded && (qt.inter.loading = setTimeout(() => qt.loading && qt.loading.show(), 100));
    };
  }

  getCache() {
    return Cache;
  }
}

export { PluginLoadingManager };
