import { mergeDeep, release } from "@vci/helper/src/object";
import { addCss, isElement } from "@vci/helper/src/element";
import { isEmpty } from "@vci/helper/src/other";
import { clearScheduledTasks } from "@vci/helper/src/browser";
import O3DisposerHelper from "./helper/O3DisposerHelper";
import { remove, update } from "@tweenjs/tween.js";
import { createPerspectiveCamera } from "./creator/cameras/createPerspectiveCamera";
import { PluginHelper } from "./plugins/PluginHelper";
import { PluginLog } from "./plugins/PluginLog";
import { QtPlugin } from "./plugins/QtPlugin";
import { uuid } from "@vci/helper/src/string";
import EventDispatcher from "@vci/helper/src/EventDispatcher";
import { QtEvents } from "./events/QtEvents";
import { AxesHelper, Clock, GridHelper, Scene } from "three";

const debug = process.env.NODE_ENV === "development";

class QuickThree extends EventDispatcher {
  constructor(option = {}) {
    super();
    this.option = mergeDeep({
      // 开发环境标识
      debug: isEmpty(option.debug) ? debug : option.debug,
      // 实例名称
      name: "3D调试控制面板",
      // 挂载元素
      el: null,
      // dpr
      dpr: window.devicePixelRatio || 1.5,
      // 相机
      camera: {
        fov: 50,
        near: 0.001,
        far: 1e3,
        position: [0, 100, 0]
      },
      // 网格辅助
      gridHelper: {
        enabled: true,
        size: 100,
        divisions: 10,
        color: [0x999999, 0x616161]
      },
      // 是否开启坐标轴辅助
      axesHelper: {
        enabled: true,
        size: 100
      },
      // 是否启用动画帧
      enableAnimate: true,
      // 是否开启TWEEN.update
      enableUpdateTween: true,
      // 插件
      plugins: [],
      // 自定义渲染逻辑
      customRender: null
    }, option);
    this.init();
  }

  init() {
    const { name, enableAnimate } = this.option;
    // 必要属性
    this.initProperties();
    // 日志
    this.log = this.use(PluginLog, PluginLog.namespace, { name });
    // 辅助工具
    this.helper = this.use(PluginHelper, PluginHelper.namespace);
    // 安装插件
    this.installPlugins(QtPlugin.Location.AfterInitProperties);
    // 元素
    this.initElement();
    // Three.js
    this.initThree();
    // 启动动画帧
    enableAnimate && this.animate();
    setTimeout(() => this.dispatchEvent(QtEvents.AfterMount));
    // 安装插件
    this.installPlugins(QtPlugin.Location.AfterInit);
  }

  // 必要属性
  initProperties() {
    this.frame = -1;
    Object.defineProperty(this, "debug", { get: () => this.option.debug });
    Object.defineProperty(this, "dpr", { get: () => this.option.dpr });
    this.things = [];
    this.plugins = [];
    this.state = {};
    this.tw = {};
    this.inter = {};
    // three instance
    this.scenes = new Map();
    this.cameras = new Map();
    this.lights = new Map();
    // debug模式下将实例挂载到windows上
    this.debug && (window.qt = this);
  }

  // 挂在元素
  initElement() {
    const { option } = this;
    if (!isElement(this.option.el)) throw new Error(this.log.format(`请传入正确得HTML元素, 当前： ${option.el}`));
    const el = document.createElement("div");
    this.option.el.appendChild(el);
    addCss(el, {
      position: "relative",
      width: "100%",
      height: "100%"
    });
    Object.defineProperty(this, "el", { get: () => el });
    Object.defineProperty(this, "elWidth", { get: () => this.el.clientWidth });
    Object.defineProperty(this, "elHeight", { get: () => this.el.clientHeight });
  }

  // Three.js基础组件
  initThree() {
    const { elWidth, elHeight, option } = this;
    //---------- 场景
    this.scene = new Scene();
    this.scenes.set("core", this.scene);
    //---------- 辅助网格
    this.debug && option.gridHelper.enabled && this.scene.add(new GridHelper(option.gridHelper.size, option.gridHelper.divisions, option.gridHelper.color[0], option.gridHelper.color[1]));
    this.debug && option.axesHelper.enabled && this.scene.add(new AxesHelper(option.axesHelper.size));
    //---------- 相机
    Object.defineProperty(this, "camera", {
      set: camera => {
        this._camera = camera;
        this.dispatchEvent(QtEvents.ChangeCamera, { camera });
      },
      get: () => this._camera
    });
    this.camera = createPerspectiveCamera({ qt: this, ...option.camera, aspect: elWidth / elHeight });
    //---------- 时钟
    this.clock = new Clock();
    this.delta = this.clock.getDelta();
  }

  use(Plugin, namespace, option) {
    if (!Plugin || (Plugin && !Plugin.isQtPlugin)) throw new Error(`${Plugin}是不是一个QtPlugin`);
    if (this[namespace]) throw new Error(`已存在命名空间为${namespace}的插件`);
    const plugin = new Plugin(this, mergeDeep(option || {}, { namespace: namespace || `plugin@${uuid()}` }));
    option && !isEmpty(option.order) && (plugin.order = option.order);
    this.plugins.push(plugin);
    this.plugins = this.plugins.sort((a, b) => a.order - b.order);
    return plugin;
  }

  installPlugins(location = QtPlugin.Location.AfterInit) {
    this.option.plugins.forEach(plugin => {
      if (plugin.isQtPlugin) location === plugin.location && this.use(plugin, plugin.namespace);
      else location === (plugin.location || plugin.plugin.location)
      && this.use(plugin.plugin, plugin.namespace || plugin.plugin.namespace, plugin.option);
    });
  }

  // 动画帧
  animate(timestamp) {
    this.delta = this.clock.getDelta();
    this.dispatchEvent(QtEvents.BeforeAnimateFrame, { delta: this.delta, timestamp });
    this.plugins.forEach(plugin => plugin.dispatchEvent(QtPlugin.Events.BeforeRaf, { delta: this.delta, timestamp }));
    this.things.forEach(thing => thing.animate(this.delta, timestamp));
    this.render(this.delta, timestamp);
    this.option.enableUpdateTween && update();
    this.plugins.forEach(plugin => plugin.dispatchEvent(QtPlugin.Events.AfterRaf, { delta: this.delta, timestamp }));
    this.dispatchEvent(QtEvents.AfterAnimateFrame, { delta: this.delta, timestamp });
    this.frame = requestAnimationFrame(this.animate.bind(this));
  }

  render(delta, timestamp) {
    this.dispatchEvent(QtEvents.BeforeRender, { delta: this.delta, timestamp });
    if (this.option.customRender) this.option.customRender(this, delta, timestamp);
    else this.plugins.forEach(plugin => plugin.dispatchEvent(QtPlugin.Events.Render, { delta, timestamp }));
    this.dispatchEvent(QtEvents.AfterRender, { delta: this.delta, timestamp });
  }

  // 场景矩阵变换处理
  matrixChangedHandle(isWindowResize = false) {
    this.things.forEach(thing => thing.matrixChange(isWindowResize));
    this.dispatchEvent(QtEvents.AfterMatrixChange, { isWindowResize, qt: this });
  }

  // 清空实例化事物
  clearThings(force = false, enableCr = true) {
    const thingsImmortal = this.things.filter(thing => thing.isImmortal);
    this.things.forEach(thing => thing.destroy(force, enableCr));
    this.things = force ? [] : thingsImmortal;
    this.helper.updateEventO3s();
  }

  // 清除things属性中已经销毁的Thing实例
  clearDestroyedThings() {
    this.things = this.things.filter(t => !t.isDestroyed);
    this.helper.updateEventO3s();
  }

  // 销毁
  destroy() {
    this.dispatchEvent(QtEvents.BeforeDestroy);
    // 移除元素
    this.el.parentNode && this.el.parentNode.removeChild(this.el);
    // 清空所有定时器
    clearScheduledTasks(this.inter);
    // 停止帧动画
    cancelAnimationFrame(this.frame);
    // 移除Tween实例
    Object.keys(this.tw).forEach(k => {
      if (this.tw[k]) {
        remove(this.tw[k]);
        delete this.tw[k];
      }
    });
    // 销毁插件
    this.plugins.forEach(plugin => plugin.destroy());
    // 清空事物
    this.clearThings(true, false);
    // 销毁场景内所有模型实例
    this.scenes.forEach(scene => O3DisposerHelper.DisposeObject3D(scene.children, scene));
    this.dispatchEvent(QtEvents.AfterDestroy);
    this.log.info("QuickThree已销毁");
    // 释放对象
    release(this);
    this.isDestroyed = true;
  }
}

export { debug };
export default QuickThree;
