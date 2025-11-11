import { mergeDeep, release } from "@vci/helper/src/object";
import { log } from "../helper/log";
import { uuid } from "@vci/helper/src/string";
import EventDispatcher from "@vci/helper/src/EventDispatcher";

export default class Thing extends EventDispatcher {

  constructor(option) {
    super();
    this.option = mergeDeep({
      id: uuid(),
      qa: null,
      name: "事物",
      position: [],
      extData: {}
    }, option);
    this.init();
    this.update(option);
    this.afterInstantiation();
  }

  static clear(ClsThing) {
    ClsThing.getInstances().forEach(thing => thing.destroy());
  }

  static clearDestroyedThings(ClsThing) {
    ClsThing.Pool = ClsThing.Pool.filter(v => v && !v.isDestroyed);
  }

  // 构造函数运行完成后
  afterInstantiation() {

  }

  init() {
    if (!this.option.qa) throw new Error(log("请传入quick-amap实例"));
    this.isThing = true;
    this.visible = true;
    this.isDestroyed = false;
    this.qa = this.option.qa;
    this.qa.things.push(this);
  }

  update(option) {
    this.option = mergeDeep(this.option, option);
    return this;
  }

  animate() {

  }

  getAMap() {
    return this.qa.getAMap();
  }

  getLoca() {
    return this.qa.getLoca();
  }

  getAMapInstance() {
    return this.qa.map;
  }

  getLocaInstance() {
    return this.qa.loca;
  }

  show() {
    this.visible = true;
    return this;
  }

  hide() {
    this.visible = false;
    return this;
  }

  destroy() {
    this.isDestroyed = true;
    this.qa.clearDestroyedThings();
    release(this, /^is\s*/);
  }
}
