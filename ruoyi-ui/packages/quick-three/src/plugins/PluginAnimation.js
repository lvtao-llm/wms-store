import { mergeDeep } from "@vci/helper/src/object";
import { Easing, remove, Tween } from "@tweenjs/tween.js";
import { addCss } from "@vci/helper/src/element";
import { QtPlugin } from "./QtPlugin";
import { QtEvents } from "../events/QtEvents";

class PluginAnimation extends QtPlugin {
  static namespace = "animation";
  static Events = {
    // 动画入场前
    BeforeAnimationIn: QtEvents.BeforeAnimationIn = "BeforeAnimationIn",
    // 动画入场动画帧
    AnimationInFrame: QtEvents.AnimationInFrame = "AnimationInFrame",
    // 动画入场后
    AfterAnimationIn: QtEvents.AfterAnimationIn = "AfterAnimationIn",
    // 动画出场前
    BeforeAnimationOut: QtEvents.BeforeAnimationOut = "BeforeAnimationOut",
    // 动画出场动画帧
    AnimationOutFrame: QtEvents.AnimationOutFrame = "AnimationOutFrame",
    // 动画出场后
    AfterAnimationOut: QtEvents.AfterAnimationOut = "AfterAnimationOut"
  };

  constructor(qt, option) {
    super(qt, mergeDeep({
      immediate: false,
      enableOpacity: true,
      enableZoom: false
    }, option));
  }

  init() {
    super.init();
    this.visible = false;
    const { qt } = this;
    addCss(qt.el, { opacity: 0 });
  }

  // 入场
  animationIn(option) {
    const { call, twOption } = mergeDeep({ call: null, twOption: {} }, option);
    const { qt } = this;
    if (this.visible) {
      call && call(this);
      return this;
    }
    this.visible = true;
    qt.tw.animation && remove(qt.tw.animation);
    if (this.option.immediate) {
      addCss(qt.el, {
        transitionDuration: "0s",
        opacity: 1,
        transform: `scale(1)`
      });
      qt.dispatchEvent(QtEvents.AfterAnimationIn);
      setTimeout(() => addCss(qt.el, { transitionDuration: "0.2s" }));
      call && call(this);
    } else {
      qt.dispatchEvent(QtEvents.BeforeAnimationIn);
      qt.tw.animation = new Tween({ zoom: 2, opacity: 0 })
        .to({ zoom: 1, opacity: 1 }, .35e3)
        .delay(twOption.delay || 0)
        .easing(Easing.Quadratic.InOut)
        .onStart(() => addCss(qt.el, { transitionDuration: "0s" }))
        .onUpdate(({ zoom, opacity }, p) => {
          addCss(qt.el, {
            opacity: this.option.enableOpacity ? opacity : 1,
            transform: `scale(${this.option.enableZoom ? zoom : 1})`
          });
          qt.dispatchEvent(QtEvents.AnimationInFrame, { p });
        })
        .onComplete(() => {
          addCss(qt.el, { transitionDuration: "0.2s" });
          if (qt.tw.animation) {
            remove(qt.tw.animation);
            delete qt.tw.animation;
          }
          qt.dispatchEvent(QtEvents.AfterAnimationIn);
          call && call();
        })
        .start();
    }
    return this;
  }

  animationInAsync(option) {
    return new Promise(resolve => this.animationIn({ ...(option || {}), call: resolve }));
  }

  // 隐藏
  animationOut(option) {
    const { call, twOption } = mergeDeep({ call: null, twOption: {} }, option);
    const { qt } = this;
    if (!this.visible) {
      call && call(this);
      return this;
    }
    this.visible = false;
    if (this.option.immediate) {
      addCss(qt.el, {
        transitionDuration: "0s",
        opacity: this.option.enableOpacity ? 0 : 1,
        transform: `scale(${this.option.enableZoom ? 2 : 1})`
      });
      qt.dispatchEvent(QtEvents.AfterAnimationOut);
      setTimeout(() => addCss(qt.el, { transitionDuration: "0.2s" }));
      call && call(this);
    } else {
      qt.dispatchEvent(QtEvents.BeforeAnimationOut);
      qt.tw.animation && remove(qt.tw.animation);
      qt.tw.animation = new Tween({ zoom: 1, opacity: 1 })
        .to({ zoom: 2, opacity: 0 }, .35e3)
        .delay(twOption.delay || 0)
        .easing(Easing.Quadratic.InOut)
        .onStart(() => addCss(qt.el, { transitionDuration: "0s" }))
        .onUpdate(({ zoom, opacity }, p) => {
          addCss(qt.el, {
            opacity: this.option.enableOpacity ? opacity : 1,
            transform: `scale(${this.option.enableZoom ? zoom : 1})`
          });
          qt.dispatchEvent(QtEvents.AnimationOutFrame, { p });
        })
        .onComplete(() => {
          addCss(qt.el, { transitionDuration: "0.2s" });
          if (qt.tw.animation) {
            remove(qt.tw.animation);
            delete qt.tw.animation;
          }
          this.option.enableZoom && addCss(qt.el, { transform: "scale(2)" });
          qt.dispatchEvent(QtEvents.AfterAnimationOut);
          call && call(this);
        })
        .start()
      ;
    }
    return this;
  }

  animationOutAsync(option) {
    return new Promise(resolve => this.animationOut({ ...(option || {}), call: resolve }));
  }
}

export { PluginAnimation };
