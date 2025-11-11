import { TransformControls } from "three/examples/jsm/controls/TransformControls";
import { DragControls } from "three/examples/jsm/controls/DragControls";
import { Vector3 } from "three";
import { QtPlugin } from "./QtPlugin";
import { mergeDeep } from "@vci/helper/src/object";
import { QtEvents } from "../events/QtEvents";

class PluginEdit extends QtPlugin {
  static namespace = "editor";
  static Events = {
    BeforeInEdit: QtEvents.BeforeInEdit = "BeforeInEdit",
    AfterInEdit: QtEvents.AfterInEdit = "AfterInEdit",
    BeforeExitEdit: QtEvents.BeforeExitEdit = "BeforeExitEdit",
    AfterExitEdit: QtEvents.AfterExitEdit = "AfterExitEdit",
    HoverEditThing: QtEvents.HoverEditThing = "HoverEditThing",
    DragStartEditThing: QtEvents.DragStartEditThing = "DragStartEditThing",
    DraggingEditThing: QtEvents.DraggingEditThing = "DraggingEditThing",
    DragEndEditThing: QtEvents.DragEndEditThing = "DragEndEditThing",
    BeforeChangeEditMode: QtEvents.BeforeChangeEditMode = "BeforeChangeEditMode",
    AfterChangeEditMode: QtEvents.AfterChangeEditMode = "AfterChangeEditMode"
  };

  constructor(qt, option) {
    super(qt, mergeDeep({
      // 是否启用键盘控制编辑模式
      enableEditByKeyboard: true,
      // 是否开启编辑时候的吸附辅助
      enableEditAdsorbHelper: false,
      editAdsorb: {
        distanceGround: 1,
        scopeFromThings: 6,
        distanceThing: 1,
        distanceThingFix: 0.1
      }
    }, option));
  }

  init() {
    super.init();
    const { qt } = this;
    this.isEdit = false;
    const { enableEditByKeyboard, enableEditAdsorbHelper, editAdsorb } = this.option;
    enableEditByKeyboard && this.bootKeyboardWakeUp();
    this.enableEditAdsorbHelper = enableEditAdsorbHelper;
    this.editAdsorb = editAdsorb;
    qt.debug && qt.gui.guis.fns
      .add(this, "isEdit")
      .name("编辑模型")
      .onChange(v => v ? this.inEdit(true) : this.exitEdit(true))
      .listen();
  }

  // 启动按键唤醒
  bootKeyboardWakeUp() {
    const Action = {
      inEdit: {
        keys: ["Control", "Alt", "e"],
        handle: () => this.inEdit()
      },
      exitEdit: {
        keys: ["Escape"],
        handle: () => this.exitEdit()
      },
      editTranslate: {
        keys: ["Shift", "T"],
        handle: () => this.switchEditMode("translate")
      },
      editRotation: {
        keys: ["Shift", "R"],
        handle: () => this.switchEditMode("rotate")
      },
      editScale: {
        keys: ["Shift", "S"],
        handle: () => this.switchEditMode("scale")
      }
    };
    const keys = [];
    this._keydownEdit = (function (e) {
      const key = e.key;
      !keys.includes(key) && keys.push(key);
    }).bind(this);
    this._keyupEdit = (function () {
      Object.values(Action).forEach(ac => ac.keys.toString() === keys.toString() && ac.handle());
      keys.length = 0;
    }).bind(this);
    window.addEventListener("keydown", this._keydownEdit);
    window.addEventListener("keyup", this._keyupEdit);
  }

  // 终止按键唤醒
  terminateKeyboardWakeUp() {
    this._keydownEdit && window.removeEventListener("keydown", this._keydownEdit);
    this._keyupEdit && window.removeEventListener("keyup", this._keyupEdit);
    delete this._keydownEdit;
    delete this._keyupEdit;
  }

  // 进入编辑模式
  inEdit(force) {
    const { qt } = this;
    if (this.isEdit && !force) return this;
    qt.events && qt.events.setLimitMoveFn("isEdit");
    qt.events && qt.events.setLimitClickFn("isEdit");
    qt.dispatchEvent(QtEvents.BeforeInEdit);
    qt.log.info(`已进入编辑模式`);
    this.isEdit = true;
    this.controlTransform = new TransformControls(qt.camera, qt.el);
    this.controlTransform.addEventListener("mouseDown", () => {
      qt.dispatchEvent(QtEvents.DragStartEditThing, qt.helper.findThingO3ByO3(this.o3IsEditing, { checkEdit: true }));
      this.disposeDragControl();
    });
    this.controlTransform.addEventListener("mouseUp", () => {
      qt.dispatchEvent(QtEvents.DragEndEditThing);
      this.createDragControl();
    });
    this.controlTransform.addEventListener("objectChange", () => {
      const transformObject = this.controlTransform.object;
      const transformThing = qt.helper.findThingO3ByO3(transformObject, { checkEdit: true })["thing"];
      const { option } = transformThing;
      if (this.enableEditAdsorbHelper) {
        // 贴地辅助
        transformObject.position.y < this.editAdsorb.distanceGround && transformObject.position.y > 0 && transformObject.position.setY(0);
        // 贴贴辅助...
        const thingsEdit = qt.helper.getEnableEditThing()
          .filter(thing => thing !== transformThing).map(thing => ({
            id: thing.id,
            distance: transformObject.position.distanceTo(thing.object.getWorldPosition(new Vector3())),
            thing
          }))
          .filter(dti => dti.distance < this.editAdsorb.scopeFromThings)
          .sort((a, b) => a.distance - b.distance);
        if (thingsEdit.length > 0) {
          const distanceNear = this.editAdsorb.distanceThing;
          const thingCompare = thingsEdit[0].thing;
          const worldPositionThing = thingCompare.object.getWorldPosition(new Vector3());
          const sizingThing = thingCompare.boxSizing;
          const sX = transformThing.boxSizing.x / 2 + sizingThing.x / 2;
          const sZ = transformThing.boxSizing.z / 2 + sizingThing.z / 2;
          const diffX = Math.abs(transformObject.position.x - worldPositionThing.x);
          const diffZ = Math.abs(transformObject.position.z - worldPositionThing.z);
          if (this.controlTransform.axis === "X") {
            if (diffX <= sX + distanceNear) {
              if (diffZ < sZ - this.editAdsorb.distanceThingFix) {
                if (transformObject.position.x > worldPositionThing.x) {
                  transformObject.position.setX(worldPositionThing.x + sizingThing.x / 2 + transformThing.boxSizing.x / 2);
                } else {
                  transformObject.position.setX(worldPositionThing.x - sizingThing.x / 2 - transformThing.boxSizing.x / 2);
                }
              } else {
                transformObject.position.setX(worldPositionThing.x);
              }
            }
          } else if (this.controlTransform.axis === "Z") {
            if (diffZ <= sZ + distanceNear) {
              if (diffX < sX - this.editAdsorb.distanceThingFix) {
                if (transformObject.position.z > worldPositionThing.z) {
                  transformObject.position.setZ(worldPositionThing.z + sizingThing.z / 2 + transformThing.boxSizing.z / 2);
                } else {
                  transformObject.position.setZ(worldPositionThing.z - sizingThing.z / 2 - transformThing.boxSizing.z / 2);
                }
              } else {
                transformObject.position.setZ(worldPositionThing.z);
              }
            }
          }
        }
      }
      qt.log.info(`物体: ${transformObject.userData.name}-${transformObject.userData.id}`);
      qt.log.info(`定位: [ ${transformObject.position.x + ", " + transformObject.position.y + ", " + transformObject.position.z} ]`);
      qt.log.info(`旋转: [ ${transformObject.rotation.x + ", " + transformObject.rotation.y + ", " + transformObject.rotation.z} ]`);
      qt.log.info(`缩放: [ ${transformObject.scale.x + ", " + transformObject.scale.y + ", " + transformObject.scale.z} ]`);
      if (option.enableEditUpdateOptionPosition) {
        option.position = transformObject.position.toArray().slice(0, 3);
        option.rotation = transformObject.rotation.toArray().slice(0, 3);
        option.scale = transformObject.scale.toArray().slice(0, 3);
        transformThing.matrixChange(false);
      }
      transformThing.updateBox();
      qt.dispatchEvent(QtEvents.DraggingEditThing, { transformThing });
    });
    qt.scene.add(this.controlTransform);
    // 开启物体包围盒
    qt.helper.getEnableEditThingObjects().forEach(editO3 => editO3.thing.update({ enableBoxHelper: true }));
    // 创建拖拽触发
    this.createDragControl();
    qt.dispatchEvent(QtEvents.AfterInEdit);
    return this;
  }

  createDragControl() {
    const { qt } = this;
    this.controlDrag && this.disposeDragControl();
    this.controlDrag = new DragControls(qt.helper.getEnableEditThingObjects(), qt.camera, qt.el);
    this.controlDrag.transformGroup = true;
    this.controlDrag.addEventListener("hoveron", e => {
      const o3 = qt.helper.findThingO3ByO3(e.object, { checkEdit: true });
      this.detach();
      this.controlTransform.attach(o3);
      this.o3IsEditing = o3;
      qt.dispatchEvent(QtEvents.HoverEditThing, o3.thing);
    });
    this.controlDrag.addEventListener("hoveroff", () => {});
  }

  disposeDragControl() {
    this.controlDrag.enabled = false;
    this.controlDrag.dispose();
    delete this.controlDrag;
  }

  // 切换编辑模式
  switchEditMode(mode = "translate") {
    const { qt } = this;
    if (this.isEdit && this.controlTransform) {
      qt.dispatchEvent(QtEvents.BeforeChangeEditMode, { mode });
      this.controlTransform.setMode(mode);
      qt.dispatchEvent(QtEvents.AfterChangeEditMode, { mode });
    }
  }

  // 编辑特定事物
  editThing(thingId) {
    const { qt } = this;
    const thing = qt.things.find(thing => thing.id === thingId);
    const o3 = qt.helper.findThingO3ByO3(thing.object, { checkEdit: true });
    this.detach();
    this.controlTransform.attach(o3);
    this.o3IsEditing = o3;
    qt.dispatchEvent(QtEvents.HoverEditThing, thing);
  }

  // 退出编辑模式
  exitEdit(force) {
    const { qt } = this;
    if (!this.isEdit && !force) return false;
    qt.events && qt.events.deleteLimitMoveFn("isEdit");
    qt.events && qt.events.deleteLimitClickFn("isEdit");
    qt.dispatchEvent(QtEvents.BeforeExitEdit);
    qt.log.info(`已退出编辑模式`);
    // 关闭物体包围盒
    qt.helper.getEnableEditThingObjects().forEach(editO3 => editO3.thing.update({ enableBoxHelper: false }));
    this.isEdit = false;
    this.controlDrag && this.disposeDragControl();
    this.detach();
    this.controlTransform.dispose();
    qt.scene.remove(this.controlTransform);
    qt.dispatchEvent(QtEvents.AfterExitEdit);
  }

  detach() {
    this.controlTransform && this.controlTransform.detach();
    delete this.o3IsEditing;
  }

  destroy() {
    super.destroy();
    this.controlDrag && this.controlDrag.dispose();
    this.controlTransform && this.controlTransform.dispose();
    this.terminateKeyboardWakeUp();
  }
}

export { PluginEdit };
