import { QtPlugin } from "./QtPlugin";
import { mergeDeep } from "@vci/helper/src/object";
import { QtEvents } from "../events/QtEvents";
import CameraControls from "camera-controls";
import { Box3, Matrix4, Quaternion, Raycaster, Sphere, Spherical, Vector2, Vector3, Vector4 } from "three";
import { degToRad } from "three/src/math/MathUtils";

CameraControls.install({
  THREE: {
    Vector2: Vector2,
    Vector3: Vector3,
    Vector4: Vector4,
    Quaternion: Quaternion,
    Matrix4: Matrix4,
    Spherical: Spherical,
    Box3: Box3,
    Sphere: Sphere,
    Raycaster: Raycaster
  }
});

class PluginControl extends QtPlugin {
  static namespace = "control";

  constructor(qt, option) {
    super(qt, mergeDeep({
      control: {
        minDistance: 1,
        maxDistance: 166,
        minPolarAngle: degToRad(0),
        maxPolarAngle: degToRad(90),
        minAzimuthAngle: -Infinity,
        maxAzimuthAngle: Infinity,
        verticalDragToForward: true
      },
      target: [0, 0, 0]
    }, option));
  }

  init() {
    super.init();
    const { qt } = this;
    const { control, target } = this.option;
    this.control = new CameraControls(qt.camera, qt.renderer ? qt.renderer.domElement : qt.el);
    Object.keys(control).forEach(key => this.control[key] = control[key]);
    this.control.setTarget(...target);
    qt.control = this.control;
    if (qt.debug && qt.gui) {
      const gui = qt.gui.guis.control.addFolder("control");
      gui.add(this.control, "enabled").name("开启控制器").listen();
    }
    this.control.update();
    this.control.addEventListener("control", () => qt.events && qt.events.setLimitMoveFn("isControlling"));
    this.control.addEventListener("controlend", () => qt.events && qt.events.deleteLimitMoveFn("isControlling"));
    this.control.addEventListener("update", () => {
      qt.matrixChangedHandle();
      clearTimeout(qt.inter.controlOnchange);
      qt.inter.controlOnchange = setTimeout(() => {
        if (qt.debug) {
          qt.log.info(`camera-position：${qt.camera.position.x}, ${qt.camera.position.y}, ${qt.camera.position.z}`);
          const target = this.control.getTarget();
          qt.log.info(`control-target：${target.x}, ${target.y}, ${target.z}`);
        }
      }, 1e3);
    });
    this.addEventListener(QtPlugin.Events.BeforeRaf, e => this.control.update(e.detail.delta));
    qt.addEventListener(QtEvents.DragStartEditThing, () => this.control.enabled = false);
    qt.addEventListener(QtEvents.DragEndEditThing, () => this.control.enabled = true);
    qt.addEventListener(QtEvents.ChangeCamera, e => this.control.camera = e.detail.camera);
  }

  destroy() {
    super.destroy();
    this.control.dispose();
  }
}

export { PluginControl };
