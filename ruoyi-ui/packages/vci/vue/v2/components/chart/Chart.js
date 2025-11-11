import { mergeDeep } from "@vci/helper/src/object";
import * as echarts from "echarts";
import { clearScheduledTasks } from "@vci/helper/src/browser";
import { viewport } from "../../../../other/adapt";
import EventDispatcher from "@vci/helper/src/EventDispatcher";


const ChartAdapt = {
  _target: window,
  width: window.innerWidth,
  height: window.innerHeight
};
Object.defineProperty(ChartAdapt, "target", {
  set(target) {
    ChartAdapt._target = target;
    ChartAdapt.resize();
  },
  get() {
    return ChartAdapt._target;
  }
});
ChartAdapt.resize = function () {
  const target = this.target;
  ChartAdapt.width = target === window ? window.innerWidth : target.offsetWidth;
  ChartAdapt.height = target === window ? window.innerHeight : target.offsetHeight;
};
export { ChartAdapt };

export default class Chart extends EventDispatcher {
  constructor(option) {
    super();
    this.option = mergeDeep({
      el: null
    }, option);
    this.init();
  }

  init() {
    this.isChart = true;
    const { el } = this.option;
    this.el = el;
    this.inter = {};
    this.initEcOption();
    this.initEc();
  }

  initEcOption(ecOption) {
    if (!ecOption) throw new Error("请设置初始化图表[echarts]配置");
    this.ecOption = ecOption;
  }

  initEc() {
    this.ec = echarts.init(this.el);
    this.addAdapt();
  }

  setData(data) {
    this._data = data;
  }

  setRenderOption(renderOption) {
    this._renderOption = renderOption;
  }

  render(data, renderOption) {
    this.setData(data);
    this.setRenderOption(renderOption);
  }

  w(px, option) {
    console.log(ChartAdapt);
    const { min, max } = mergeDeep({ min: 960, max: viewport.width }, option);
    return px * (Math.max(ChartAdapt.width, min) / max);
  }

  h(hpx, option) {
    const { min, max } = mergeDeep({ min: 0, max: viewport.height }, option);
    return hpx * (Math.max(ChartAdapt.height, min) / max);
  }

  addAdapt() {
    this.removeAdapt();
    this._resize = this.resize.bind(this);
    window.addEventListener("resize", this._resize);
  }

  removeAdapt() {
    if (this._resize) {
      window.removeEventListener("resize", this._resize);
      delete this._resize;
    }
  }

  resize() {
    clearTimeout(this.inter.resize);
    this.inter.resize = setTimeout(() => {
      ChartAdapt.resize();
      this.initEcOption();
      this.dispatchEvent("beforeResize");
      this.ec && this.ec.resize();
      this.render(this._data, this._renderOption);
      this.dispatchEvent("afterResize");
    }, 100);
  }

  destroy() {
    this.removeAdapt();
    clearScheduledTasks(this.inter);
    this.ec && this.ec.dispose();
  }
}
