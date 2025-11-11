import { QtPlugin } from "./QtPlugin";
import { mergeDeep } from "@vci/helper/src/object";
import { OrbitControls } from "three/examples/jsm/controls/OrbitControls";
import { QtEvents } from "../events/QtEvents";
import { degToRad } from "three/src/math/MathUtils";

class PluginControlOrbit extends QtPlugin {
  static namespace = "controlOrbit";

  constructor(qt, option) {
    super(qt, mergeDeep({
      control: {
        minAzimuthAngle: -Infinity,
        maxAzimuthAngle: Infinity,
        minPolarAngle: degToRad(0),
        maxPolarAngle: degToRad(90),
        minDistance: 1,
        maxDistance: 100,
        enableDamping: true,
        screenSpacePanning: false,
        dampingFactor: 0.04,
        panSpeed: 1.2,
        rotateSpeed: 1.2,
        zoomSpeed: 1
      },
      target: [0, 0, 0]
    }, option));
  }

  init() {
    super.init();
    const { qt } = this;
    const { control, target } = this.option;
    this.control = new OrbitControls(qt.camera, qt.el);
    Object.keys(control).forEach(key => this.control[key] = control[key]);
    this.control.target.fromArray(target);
    qt.control = this.control;
    if (qt.debug && qt.gui) {
      const gui = qt.gui.guis.control.addFolder("control-orbit");
      gui.add(this.control, "enabled").name("开启控制器").listen();
      gui.add(this.control, "enableDamping").name("开启阻尼").listen();
      gui.add(this.control, "dampingFactor").min(0.001).max(0.1).name("阻尼值").listen();
      gui.add(this.control, "autoRotate").name("自动旋转");
      gui.add(this.control, "autoRotateSpeed", 0, 100).name("自动旋转速度");
      gui.add(this.control, "enablePan").listen();
      gui.add(this.control, "enableRotate").listen();
      gui.add(this.control, "enableZoom").listen();
      gui.add(this.control, "keyPanSpeed").min(0).max(6).listen();
      gui.add(this.control, "panSpeed").min(0).max(6).listen();
      gui.add(this.control, "rotateSpeed").min(0).max(6).listen();
      gui.add(this.control, "zoomSpeed").min(0).max(6).listen();
      gui.add(this.control, "minPolarAngle").min(0).max(degToRad(180)).listen();
      gui.add(this.control, "maxPolarAngle").min(0).max(degToRad(180)).listen();
      gui.add(this.control, "minAzimuthAngle").min(degToRad(-360)).max(degToRad(360)).listen();
      gui.add(this.control, "maxAzimuthAngle").min(degToRad(-360)).max(degToRad(360)).listen();
      gui.add(this.control, "minDistance").listen();
      gui.add(this.control, "maxDistance").listen();
    }
    this.control.listenToKeyEvents(window);
    this.control.update();
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
    qt.addEventListener(QtEvents.ChangeCamera, e => this.control.object = e.detail.camera);
  }

  destroy() {
    super.destroy();
    this.control.dispose();
  }
}

export { PluginControlOrbit };
