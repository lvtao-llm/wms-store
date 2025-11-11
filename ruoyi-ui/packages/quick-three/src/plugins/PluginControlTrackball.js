import { QtPlugin } from "./QtPlugin";
import { mergeDeep } from "@vci/helper/src/object";
import { QtEvents } from "../events/QtEvents";
import { TrackballControls } from "three/examples/jsm/controls/TrackballControls";

class PluginControlTrackball extends QtPlugin {
  static namespace = "controlOrbit";

  constructor(qt, option) {
    super(qt, mergeDeep({
      control: {},
      target: [0, 0, 0]
    }, option));
  }

  init() {
    super.init();
    const { qt } = this;
    const { control, target } = this.option;
    this.control = new TrackballControls(qt.camera, qt.el);
    Object.keys(control).forEach(key => this.control[key] = control[key]);
    this.control.target.fromArray(target);
    qt.control = this.control;
    if (qt.debug && qt.gui) {
      const gui = qt.gui.guis.control.addFolder("control-trackball");
      gui.add(this.control, "enabled").name("开启控制器").listen();
      gui.add(this.control, "staticMoving").name("关闭阻尼").listen();
      gui.add(this.control, "dynamicDampingFactor").min(0.1).max(1).name("阻尼值").listen();
      gui.add(this.control, "noPan").listen();
      gui.add(this.control, "noRotate").listen();
      gui.add(this.control, "noZoom").listen();
      gui.add(this.control, "panSpeed").min(0).max(6).listen();
      gui.add(this.control, "rotateSpeed").min(0).max(6).listen();
      gui.add(this.control, "zoomSpeed").min(0).max(6).listen();
      // gui.add(this.control, "autoRotate").name("自动旋转");
      // gui.add(this.control, "autoRotateSpeed", 0, 100).name("自动旋转速度");
    }
    // this.control.listenToKeyEvents(window);
    this.control.addEventListener("start", () => qt.events && qt.events.setLimitMoveFn("isControlling"));
    this.control.addEventListener("end", () => qt.events && qt.events.deleteLimitMoveFn("isControlling"));
    this.control.addEventListener("change", () => {
      qt.matrixChangedHandle();
      clearTimeout(qt.inter.controlOnchange);
      qt.inter.controlOnchange = setTimeout(() => {
        if (qt.debug) {
          qt.log.info(`camera-position：${qt.camera.position.x}, ${qt.camera.position.y}, ${qt.camera.position.z}`);
          qt.log.info(`control-target：${this.control.target.x}, ${this.control.target.y}, ${this.control.target.z}`);
        }
      }, 100);
    });
    this.addEventListener(QtPlugin.Events.BeforeRaf, e => this.control.update(e.detail.delta));
    qt.addEventListener(QtEvents.DragStartEditThing, () => this.control.enabled = false);
    qt.addEventListener(QtEvents.DragEndEditThing, () => this.control.enabled = true);
  }

  destroy() {
    super.destroy();
    this.control.dispose();
  }
}

export { PluginControlTrackball };
