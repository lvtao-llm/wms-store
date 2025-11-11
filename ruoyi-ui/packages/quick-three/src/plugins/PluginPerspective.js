import { QtPlugin } from "./QtPlugin";
import { mergeDeep } from "@vci/helper/src/object";
import { Easing, remove, Tween } from "@tweenjs/tween.js";
import { isEmpty } from "@vci/helper/src/other";
import { Vector3 } from "three";

class PluginPerspective extends QtPlugin {
  static namespace = "perspective";

  constructor(qt, option) {
    super(qt, mergeDeep({ enableClickOnPs: false }, option));
  }

  init() {
    super.init();
    this.isPerspectiveSwitching = false;
    this._perspectives = [];
  }

  // 视角切换
  update(option, camera, control) {
    const { position, target, immediate, twOption, save } = mergeDeep(
      {
        position: [100, 100, 100],
        target: [0, 0, 0],
        immediate: false,
        twOption: {
          duration: 1.6e3,
          delay: 0,
          easing: Easing.Cubic.InOut,
          onUpdate: null
        },
        save: true
      },
      option
    );
    const { qt } = this;
    if (!camera) camera = qt.camera;
    if (!control) control = qt.control;
    if (!control || !camera) {
      qt.log.warn("control|camera 丢失");
      return Promise.resolve(this);
    }
    this.isPerspectiveSwitching = true;
    qt.events && qt.events.setLimitMoveFn("isPerspectiveSwitching");
    !this.option.enableClickOnPs && qt.events && qt.events.setLimitClickFn("isPerspectiveSwitching");
    save && this._perspectives.push({ position: camera.position.clone(), target: control.target.clone() });
    // 移除原有Tween实例
    qt.tw.psCamera && remove(qt.tw.psCamera);
    qt.tw.psControl && remove(qt.tw.psControl);
    const vec3Position = new Vector3().fromArray(position);
    const vec3Target = new Vector3().fromArray(target);
    // 动画处理
    return new Promise(resolve => {
      // 开始新的动画实例
      clearTimeout(qt.inter.perspective);
      if (immediate) {
        camera.position.copy(vec3Position);
        this.isPerspectiveSwitching = false;
        qt.events && qt.events.deleteLimitMoveFn("isPerspectiveSwitching");
        !this.option.enableClickOnPs && qt.events && qt.events.deleteLimitClickFn("isPerspectiveSwitching");
        resolve(this);
      } else qt.inter.perspective = setTimeout(() => {
        // 禁用控制器
        isEmpty(control._enabled) && (control._enabled = control.enabled);
        control.enabled = false;
        // 相机位置
        qt.tw.psCamera = new Tween(camera.position)
          .to(vec3Position, twOption.duration)
          .easing(twOption.easing)
          .delay(twOption.delay)
          .onComplete(() => {
            // 恢复控制器默认状态
            control.enabled = control._enabled;
            delete control._enabled;
            this.isPerspectiveSwitching = false;
            qt.events && qt.events.deleteLimitMoveFn("isPerspectiveSwitching");
            !this.option.enableClickOnPs && qt.events && qt.events.deleteLimitClickFn("isPerspectiveSwitching");
            resolve(this);
          })
          .start();
      }, 0);
      // 控制器目标点
      if (immediate) {
        control.target.copy(vec3Target);
        control.update();
      } else qt.tw.psControl = new Tween(control.target)
        .to(vec3Target, twOption.duration)
        .easing(twOption.easing)
        .delay(twOption.delay)
        .onUpdate((e, p) => {
          control.update();
          twOption.onUpdate && twOption.onUpdate(p);
        })
        .start();
    });
  }

  //回复上一次视角
  async recovery(option) {
    if (this._perspectives.length) {
      const perspective = this._perspectives.pop();
      await this.update(mergeDeep(
        {
          position: perspective.position.toArray(),
          target: perspective.target.toArray()
        },
        option
      ));
    }
    return this;
  }
}

export { PluginPerspective };
