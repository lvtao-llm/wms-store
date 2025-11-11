import { instantiateComponentVm } from "../../components/vue.component";
import { getScreenCoordsFromWorldCoords } from "../helper/ThingHelper";
import { getElementMaxZIndex } from "@vci/helper/src/element";
import { ThingEvents } from "../events/ThingEvents";
import { mergeDeep } from "@vci/helper/src/object";
import Thing from "./Thing";
import { Vector3 } from "three";
import Animator from "../core/Animator";

// 弹框
export default class Popup extends Animator {
  constructor(option) {
    super(mergeDeep(
      {
        bindEventClick: true,
        enableAdapt: true,
        enableAdaptHorizontal: false,
        componentPopup: null,
        positionPopup: null,
        offset: [0, 0, 0]
      },
      option
    ));
  }

  init() {
    super.init();
    this.isThingPopup = true;
    Object.defineProperty(this, "enableAdapt", {
      get: () => this.option.enableAdapt,
      set: enableAdapt => { this.option.enableAdapt = enableAdapt; },
      enumerable: true,
      configurable: true
    });
  }

  afterInstantiation() {
    super.afterInstantiation();
    const { bindEventClick } = this.option;
    bindEventClick && this.addEventListener(ThingEvents.Click, async () => !this.vm ? await this.openPopup() : await this.closePopup());
    this.addEventListener(ThingEvents.HoverOn, this.topping.bind(this));
  }

  getAdaptPosition() {
    if (this.option.positionPopup) return this.option.positionPopup;
    else return this.getCenter(Thing.CenterMode.CenterTop).add(new Vector3(...this.option.offset)).toArray();
  }

  async openPopup(componentPopup) {
    componentPopup = componentPopup || this.option.componentPopup;
    if (this.vm) return Promise.resolve();
    if (!componentPopup) return Promise.reject("未配置弹框组件");
    this.vm = instantiateComponentVm(componentPopup, { propsData: { extData: this.extData } });
    this.vm.topping = () => this.topping();
    setTimeout(this.adaptPopupPosition.bind(this));
    this.qt.el.appendChild(this.vm.$el);
    this.vm.$on("close", () => this.vm && this.closePopup());
    this.topping();
    await this.vm.fadeIn();
  }

  async closePopup() {
    if (!this.vm) return false;
    const vm = this.vm;
    delete this.vm;
    const elQt = this.qt.el;
    await vm.fadeOut();
    elQt.removeChild(vm.$el);
    vm.$destroy();
  }

  adaptPopupPosition() {
    const { enableAdaptHorizontal } = this.option;
    if (this.vm && this.enableAdapt) {
      const widthEl = this.vm.$el.clientWidth;
      const { left, top } = getScreenCoordsFromWorldCoords(
        this.getAdaptPosition(),
        this.qt.camera,
        this.qt.el
      );
      this.vm.left = left;
      this.vm.top = top;
      if (!enableAdaptHorizontal) return false;
      const isOverRight = this.vm.isOverRight = widthEl + left >= window.innerWidth;
      isOverRight && (this.vm.left = left - widthEl);
    }
  }

  adaptPopupPositionIcv() {
    const isInCameraView = this.qt.helper.isInCameraView(this.getAdaptPosition(), this.qt.camera);
    this.vm && isInCameraView && this.adaptPopupPosition();
    if (isInCameraView) {
      if (this.enableRecoveryPopup) {
        delete this.enableRecoveryPopup;
        this.openPopup();
      }
    } else {
      if (this.vm) {
        this.enableRecoveryPopup = true;
        this.closePopup();
      }
    }
  }

  // 置顶
  topping() {
    const zIndex = getElementMaxZIndex(this.qt.el);
    this.vm && (this.vm.zIndex = zIndex);
  }

  matrixChange(isWindowResize = false, handle) {
    return super.matrixChange(isWindowResize, () => {
      handle && handle();
      this.enableAdapt && this.adaptPopupPositionIcv();
    });
  }

  fadeOut(tweenOption) {
    this.vm && this.closePopup();
    return super.fadeOut(tweenOption);
  }

  destroy(force = false) {
    this.vm && this.closePopup();
    return super.destroy(force);
  }
}
