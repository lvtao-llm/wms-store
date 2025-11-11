import { mergeDeep } from "@vci/helper/src/object";
import { addCss } from "@vci/helper/src/element";
import { clearScheduledTasks } from "@vci/helper/src/browser";
import EventDispatcher from "@vci/helper/src/EventDispatcher";

class Drawer extends EventDispatcher {
  constructor(option) {
    super();
    this.option = mergeDeep(
      {
        width: 200,
        height: 200,
        dpr: window.devicePixelRatio || 1
      },
      option
    );
    this.init();
  }

  init() {
    const { width, height, dpr, on } = this.option;
    this.width = width;
    this.height = height;
    this.dpr = dpr;
    this.proportion = this.width / this.height;
    this.on = on;
    this.inter = {};
    this.initCanvas();
    this.adapt();
  }

  initCanvas() {
    this.canvas = document.createElement("canvas");
    addCss(this.canvas, { width: this.width + "px", height: this.height + "px" });
    this.canvas.width = this.width * this.dpr;
    this.canvas.height = this.height * this.dpr;
    this.ctx = this.canvas.getContext("2d");
  }

  mount(element) {
    element.appendChild(this.canvas);
    this.elementParent = element;
  }

  detach() {
    this.elementParent && this.elementParent.removeChild(this.canvas);
    delete this.elementParent;
  }

  render(call, delta) {
    this.ctx.save();
    this.ctx.scale(this.dpr, this.dpr);
    call && call(this.ctx, this, delta);
    this.ctx.restore();
  }

  clear() {
    this.ctx.clearRect(0, 0, this.width, this.height);
  }

  setSize(width, height) {
    this.width = this.option.width = width;
    this.height = this.option.height = height;
    addCss(this.canvas, { width: width + "px", height: height + "px" });
    this.canvas.width = this.width * this.dpr;
    this.canvas.height = this.height * this.dpr;
    this.proportion = this.width / this.height;
    return this;
  }

  adapt() {
    this.resize();
    this._resize = () => {
      clearTimeout(this.inter.resize);
      this.inter.resize = setTimeout(() => this.resize(), 200);
    };
    window.addEventListener("resize", this._resize);
  }

  resize() {
    this.dispatchEvent("resize");
  }

  destroy() {
    super.destroy();
    this.detach();
    this._resize && window.removeEventListener("resize", this._resize);
    clearScheduledTasks(this.inter);
  }
}

export default Drawer;