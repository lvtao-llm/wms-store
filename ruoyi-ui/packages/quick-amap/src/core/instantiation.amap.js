import { load } from "@amap/amap-jsapi-loader";
import { cloneDeep, mergeDeep } from "@vci/helper/src/object";
import { clearScheduledTasks } from "@vci/helper/src/browser";
import { debug } from "../helper/env";
import { log } from "../helper/log";
import { addCss } from "@vci/helper/src/element";
// 地图缓存
const Map = {
  AMap: null,
  Loca: null
};
/**
 * 高德地图实例化
 */
export default class InstantiationAMap {
  constructor(option) {
    this.option = mergeDeep({
      el: null,
      debug,
      // 配置说明 https://lbs.amap.com/api/jsapi-v2/documentation#map
      mapOption: {},
      // 配置说明 https://lbs.amap.com/api/jsapi-v2/guide/abc/load
      loader: {
        key: "d1c52120f93c51dcce52437c00d8683a",
        version: "2.0",
        plugins: []
      },
      enableTransparentMap: false,
      on: {
        afterInitMap: null,
        afterMapComplete: null
      }
    }, option);
    this.init().catch(e => console.error(log(e)));
  }

  async init() {
    this.initElement();
    this.initProperties();
    // 载入地图类
    if (!Map.AMap) {
      Map.AMap = await load(this.option.loader);
      if (global.Loca) Map.Loca = global.Loca;
    }
    // 实例化地图
    await this.initMap();
  }

  initElement() {
    this.el = this.option.el;
    if (!this.el) throw new Error(log("option.el为空，请传入HTML元素"));
  }

  initProperties() {
    this.inter = {};
  }

  async initMap() {
    // 实例化地图
    this.map = new Map.AMap.Map(this.el, cloneDeep(this.option.mapOption));
    this.map.instantiation = this;
    this.option.on["afterInitMap"] && this.option.on["afterInitMap"](this.map, Map.AMap, Map.Loca);
    // 隐藏地图
    addCss(this.el, { opacity: 0 });
    // 隐藏地图Logo
    this.hideMapLogo();
    // 实例化Loca，如果存在
    if (Map.Loca) {
      this.loca = new Map.Loca.Container({ map: this.map });
      this.loca.animate.start();
    }
    // 事件注入
    this.map.on("moveend", this.printMapProps.bind(this));
    this.map.on("zoomend", this.printMapProps.bind(this));
    this.map.on("dragend", this.printMapProps.bind(this));
    this.map.on("click", e => console.info("当前点击位置经纬度: ", e["lnglat"].toArray().toString()));
    this.map.on("complete", this.onMapCompleteHandle.bind(this));
    Object.keys(this.option.on).forEach(key => {
      this.map.on(key, e => {
        const events = Array.isArray(this.option.on[key]) ? this.option.on[key] : [this.option.on[key]];
        events.forEach(ev => ev(e, this.map));
      });
    });
  }

  printMapProps() {
    if (this.option.debug) {
      const map = this.map;
      console.info(log(`地图中心点: ${map.getCenter().toArray().toString()}`));
      console.info(log(`地图缩放等级: ${map.getZoom()}`));
      console.info(log(`倾斜角度: ${map.getPitch()}`));
      console.info(log(`旋转角度: ${map.getRotation()}`));
    }
  }

  async onMapCompleteHandle() {
    const option = this.option;
    const { enableTransparentMap } = this.option;
    // 透明化地图
    enableTransparentMap && await this.startUsingTransparent();
    // 显示地图
    await this.fadeIn("1s", ".5s");
    option.on["afterMapComplete"] && option.on["afterMapComplete"](this.map, this.loca);
  }

  async fadeIn(duration = "1s", delay = "0s") {
    await this.fadeTo(1, duration, delay);
  }

  async fadeOut(duration = "1s", delay = "0s") {
    await this.fadeTo(0, duration, delay);
  }

  fadeTo(opacity, duration = "1s", delay = "0s") {
    return new Promise(resolve => {
      addCss(this.el, {
        opacity,
        transition: `opacity ${duration}`,
        transitionDelay: delay
      });
      const delayMilliSecond = delay.includes("s") ? Number(delay.replace("s", "")) * 1e3 : Number(delay.replace("ms", ""));
      clearTimeout(this.inter.fade);
      this.inter.fade = setTimeout(resolve, delayMilliSecond);
    });
  }

  getAMap() {
    return Map.AMap;
  }

  getLoca() {
    return Map.Loca;
  }

  hideMapLogo() {
    this.el.querySelector(".amap-copyright").style.opacity = "0";
    this.el.querySelector(".amap-logo").style.opacity = "0";
  }

  // 启用地图透明化
  startUsingTransparent() {
    this.map.getLayers().forEach(vl => !["AMap.VectorLayer"].includes(vl["CLASS_NAME"]) && vl.hide());
    return new Promise(resolve => {
      clearTimeout(this.inter.transparentMap);
      this.inter.transparentMap = setTimeout(() => {
        const container = this.map.getContainer();
        container.style.backgroundColor = "transparent";
        container.style.backgroundImage = "none";
        resolve();
      }, 200);
    });
  }

  // 停用地图透明化
  stopUsingTransparent() {
    this.map.getLayers().forEach(vl => !["AMap.VectorLayer"].includes(vl["CLASS_NAME"]) && vl.show());
  }

  destroy() {
    clearScheduledTasks(this.inter);
    // 销毁loca
    if (this.loca) {
      this.loca.animate.stop();
      this.loca.destroy();
    }
    // 销毁地图
    if (this.map) {
      this.map.clearMap();
      this.map.destroy();
    }
    if (debug) console.info(log("map is destroyed"), this);
  }
}
