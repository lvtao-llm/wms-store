import Thing from "@vci/quick-three/src/core/Thing";
import { Group, Mesh, PlaneGeometry, Vector3 } from "three";
import { ThingEvents } from "@vci/quick-three/src/events/ThingEvents";
import Ra from "./Ra";
import { degToRad } from "three/src/math/MathUtils";
import { delay } from "@/common/helper";
import { PluginLoading } from "@vci/vci/vue/v2/components/other/VpLoading";
import { uuid } from "@vci/helper/src/string";
import { PluginNotification } from "@vci/vci/vue/v2/components/other/VpNotification";
import { storeCore } from "@/store/store.core";
import { mergeDeep } from "@vci/helper/src/object";

export default class RiskArea extends Thing {
  init() {
    super.init();
    this.isRiskArea = true;
    this.object = new Group();
    this.thingsArea = [];
    this.dataAdded = [];
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.createO3Plane();
    this.cursor.hover = "crosshair";
    this.addEventListener(ThingEvents.Click, e => {
      const point = e.detail.targetRay.point;
      if (this._thingArea.isDrawEnd) return false;
      if (this._thingArea.isChoosing) {
        const path = this._thingArea.option.path;
        path[path.length - 1] = point;
        this._thingArea.isChoosing = false;
      } else this._thingArea.addCoordinate(point);
    });
    this.addEventListener(ThingEvents.Move, e => {
      if (this._thingArea) {
        if (this._thingArea.isDrawEnd) return false;
        const point = e.detail.targetRay.point;
        if (!this._thingArea.isChoosing) {
          this._thingArea.isChoosing = true;
          this._thingArea.addCoordinate(point);
        } else {
          const path = this._thingArea.option.path;
          path[path.length - 1] = point;
          this._thingArea.updateO3Shape();
        }
      }
    });
    this.addEventListener(ThingEvents.Dblclick, e => {
      if (this._thingArea) {
        const point = e.detail.targetRay.point;
        const path = this._thingArea.option.path;
        path[path.length - 1] = point;
        this._thingArea.isChoosing = false;
        this._thingArea.updateO3Shape();
        this._thingArea.isDrawEnd = true;
      }
    });
  }

  createO3Plane() {
    const geo = new PlaneGeometry(2e3, 2e3, 9, 9);
    geo.rotateX(degToRad(-90));
    const o3 = new Mesh(geo);
    o3.visible = false;
    this.object.add(o3);
    return this;
  }

  updateFromData(data) {
    const { qt } = this;
    if (!data) data = this._data || [];
    else this._data = data;
    this.thingsArea = qt.pluginThings.updateThings(
      data.concat(this.dataAdded).filter(v => v.path && v.path.length > 2).map(v => ({
        id: v.id,
        name: v.name,
        color: v.color,
        path: v.path.map(v => new Vector3().fromArray(qt.thingWorld.lngLatToCoords(v))),
        extData: v,
        creator: Ra
      })),
      null,
      this.thingsArea
    );
    this.thingsArea.forEach(thing => thing.updateO3Shape());
  }

  createThingArea() {
    const { qt } = this;
    this.thingsArea.forEach(thing => thing.sleep());
    return this._thingArea = new Ra({ qt, isDrawingMode: true, name: "区域" });
  }

  getThingArea() {
    return this._thingArea;
  }

  /**
   * 保存风险区域到服务端
   * @param info
   * @returns {Promise<boolean>}
   */
  async saveThingArea(info) {
    const isUpdate = storeCore().mapRiskArea.isUpdate;
    const path = this._thingArea.getPathOfLnglat();
    const data = {
      ...info,
      path
    };
    if (path.length < 3) {
      PluginNotification.error("请绘制区域后保存");
      return false;
    }
    if (isUpdate) data.id = this._thingArea.id;
    // TODO 保存数据到服务端
    PluginLoading.open({ text: "保存中" });
    try {
      await delay();
      if (!isUpdate) {
        data.id = "ra-" + uuid().substring(6);
        this._thingArea.destroy();
        this.dataAdded.push(data);
        this.thingsArea.forEach(thing => thing.wakeUp());
      } else {
        this._thingArea.thingMarkerOperation.blur();
        const targetData = this._data.find(v => v.id === data.id) || this.dataAdded.find(v => v.id === data.id);
        mergeDeep(targetData, data);
      }
      this.updateFromData();
      delete this._thingArea;
    } catch (e) {
      console.error("风险区域保存异常", e, data);
      PluginNotification.error("保存异常，请重试");
    }
    PluginLoading.close();
    return !this._thingArea;
  }

  deprecatedThingArea() {
    this._thingArea.destroy();
    this.thingsArea.forEach(thing => thing.wakeUp());
    delete this._thingArea;
  }

}