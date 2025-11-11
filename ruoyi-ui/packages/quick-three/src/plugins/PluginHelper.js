import { Frustum, Matrix4, Vector3 } from "three";
import { QtPlugin } from "./QtPlugin";
import MaterialPatch from "../helper/MaterialPatch";
import { QtEvents } from "../events/QtEvents";
import { mergeDeep } from "@vci/helper/src/object";

class PluginHelper extends QtPlugin {
  static namespace = "helper";
  static Events = {
    UpdateEventO3s: QtEvents.UpdateEventO3s = "UpdateEventO3s"
  };

  init() {
    super.init();
    // 视锥体
    this.frustum = new Frustum();
    this.frustumPoint = new Vector3();
    this.cameraViewProjectionMatrix = new Matrix4();
  }

  updateEventO3s() {
    clearTimeout(this.qt.inter.updateEventO3s);
    this.qt.inter.updateEventO3s = setTimeout(() => !this.qt.isDestroyed && this.qt.dispatchEvent(QtEvents.UpdateEventO3s, { eventO3s: this.getEnableEventThingObjects() }));
  }

  // 获取场景中可见的事物
  getVisibleThings() {
    return this.qt.things.filter(thing => thing.visible && thing.object);
  }

  // 获取能够编辑的事物模型
  getEnableEditThing() {
    return this.getVisibleThings().filter(thing => thing.enableEdit);
  }

  getEnableEditThingObjects() {
    return this.getEnableEditThing().map(thing => thing.object);
  }

  // 获取能够进行事件交互得模型
  getEnableEventThingObjects() {
    return this.getVisibleThings().filter(thing => thing.enableEvent).map(t => t.object);
  }

  /**
   * 通过模型个体找到事物模型
   * @param o3        模型实例Object3D
   * @param option    检索配置
   * @returns {null|Thing}
   */
  findThingO3ByO3(o3, option = {}) {
    const { checkEdit, checkEvent } = mergeDeep({
      checkEvent: false,
      checkEdit: false
    }, option);
    if (!o3) {
      return null;
    } else if (o3.isThing) {
      if (checkEdit) {
        if (o3.thing.enableEdit) return o3;
        else return o3.parent ? this.findThingO3ByO3(o3.parent, option) : null;
      }
      if (checkEvent) {
        if (o3.thing.enableEvent) return o3;
        else return o3.parent ? this.findThingO3ByO3(o3.parent, option) : null;
      }
      return o3;
    } else return o3.parent ? this.findThingO3ByO3(o3.parent, option) : null;
  }

  // 判断某个位置是否在相机视野范围内
  isInCameraView(position = [0, 0, 0], camera) {
    this.cameraViewProjectionMatrix.multiplyMatrices(camera.projectionMatrix, camera.matrixWorldInverse);
    this.frustum.setFromProjectionMatrix(this.cameraViewProjectionMatrix);
    return this.frustum.containsPoint(this.frustumPoint.fromArray(position));
  }

  // 更新场景中的所有模型材质
  updateSceneMaterials() {
    const { qt } = this;
    qt.scenes.forEach(scene => MaterialPatch.TraverseMaterials(scene, material => material.needsUpdate = true));
  }
}

export { PluginHelper };
