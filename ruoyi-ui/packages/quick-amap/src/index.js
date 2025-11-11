import { mergeDeep } from "@vci/helper/src/object";
import { clearScheduledTasks } from "@vci/helper/src/browser";
import { remove, update } from "@tweenjs/tween.js";
import { log } from "./helper/log";
import InstantiationAMap from "./core/instantiation.amap";
import Stats from "stats.js";
import { addCss, removeCss } from "@vci/helper/src/element";
import { debug } from "./helper/env";
import { QuickThreeCreator } from "./creator/qt.creator";
import { Clock } from "./core/Clock";
import EventDispatcher from "@vci/helper/src/EventDispatcher";
import { EventsQa } from "./events/EventsQa";

export default class QuickAMap extends EventDispatcher {
  constructor(option) {
    super();
    this.option = mergeDeep({
      el: null,
      debug,
      // 是否开启TWEEN.update
      enableUpdateTween: true,
      // 是否开启性能工具
      enablePerformanceHelper: debug,
      // 地图配置 默认配置请参考./core/instantiation.amap.js
      map: {
        mapOption: {},
        loader: {},
        perspective: {},
        on: {}
      },
      // 通知插件
      notify: {
        success: msg => console.info(msg),
        error: msg => console.error(msg)
      }
    }, option);
    this.init();
  }

  init() {
    this.initElement();
    this.initProperties();
    this.initAMap();
    this.initOther();
    this.debug && (window.qa = this);
  }

  initProperties() {
    this.state = {};
    this.tw = {};
    this.inter = {};
    this.frame = -1;
    this.things = [];
    this.layerGLs = [];
    this.dpr = window.devicePixelRatio || 1;
    this.debug = this.option.debug;
    this.notify = this.option.notify;
  }

  initElement() {
    this.el = this.option.el;
    if (!this.el) throw new Error(log("option.el为空，请传入HTML元素"));
  }

  initAMap() {
    const mapEvents = this.option.map.on;
    // 地图加载完成
    const eventAc = mapEvents.afterMapComplete;
    mapEvents.afterMapComplete = (map, loca) => {
      this.map = map;
      this.loca = loca;
      this.mapInstantiation = map.instantiation;
      // 启动动画帧
      this.animate();
      eventAc && eventAc(this);
      this.dispatchEvent(EventsQa.AfterMapComplete, { map, loca });
    };
    // 实例化地图
    new InstantiationAMap({
      ...this.option.map,
      el: this.option.el,
      on: mapEvents,
      debug: this.debug
    });
  }

  initOther() {
    // 性能测试工具
    this.option.enablePerformanceHelper && this.bootPerformanceHelper();
    // 时钟
    this.clock = new Clock();
    this.delta = this.clock.getDelta();
  }

  // 动画帧
  animate() {
    this.delta = this.clock.getDelta();
    this.performanceMonitor && this.performanceMonitor.begin();
    this.things.forEach(thing => thing.animate(this.delta));
    this.layerGLs.forEach(layerGL => layerGL.handleRender(this.delta));
    this.option.enableUpdateTween && update();
    this.performanceMonitor && this.performanceMonitor.end();
    this.frame = requestAnimationFrame(this.animate.bind(this));
  }

  // 添加quickThree实例
  async createLayerGL(option) {
    const layerGL = await QuickThreeCreator(this.map, this.mapInstantiation.getAMap(), option);
    this.layerGLs.push(layerGL);
    return layerGL;
  }

  destroyLayerGL(layerGL) {
    this.layerGLs.splice(this.layerGLs.indexOf(layerGL), 1);
    layerGL.handleDestroy();
  }

  // 清楚被销毁的空事物
  clearDestroyedThings() {
    this.things = this.things.filter(thing => !thing.isDestroyed);
  }

  // 获取地图类
  getAMap() {
    if (!this.mapInstantiation) throw new Error(log("地图未就绪"));
    return this.mapInstantiation.getAMap();
  }

  // 获取地图可视化类
  getLoca() {
    if (!this.mapInstantiation) throw new Error(log("地图未就绪"));
    return this.mapInstantiation.getLoca();
  }

  // 启动性能监控工具
  bootPerformanceHelper() {
    this.performanceMonitor = new Stats();
    removeCss(this.performanceMonitor.dom, ["top", "left"]);
    addCss(this.performanceMonitor.dom, {
      right: "12px",
      bottom: "12px"
    });
    document.querySelector("body").appendChild(this.performanceMonitor.dom);
  }

  // 销毁
  destroy() {
    clearScheduledTasks(this.inter);
    cancelAnimationFrame(this.frame);
    Object.keys(this.tw).forEach(key => {
      remove(this.tw[key]);
      delete this.tw[key];
    });
    // 销毁quickThree
    this.layerGLs.forEach(layerGL => layerGL.handleDestroy());
    this.layerGLs = [];
    this.things.forEach(thing => thing.destroy());
    this.mapInstantiation && this.mapInstantiation.destroy();
    this.performanceMonitor && document.querySelector("body").removeChild(this.performanceMonitor.dom);
  }
}
