import Thing from "./../../../../src/core/Thing";
import { mergeDeep } from "@vci/helper/src/object";
import { isEmpty } from "@vci/helper/src/other";
import Polygon from "../Polygon";
import Polygon3D from "./Polygon3D";
import { cloneO3Materials } from "./../../../../src/helper/ThingHelper";

// 多边形组
export default class Polygon3DGroup extends Thing {
  constructor(option) {
    super(mergeDeep({
      // 功能性配置
      enableEdit: false,
      enableEvent: false,
      // 是否创建2D多边形
      enableCreateFlatPolygon: false,
      urlGeoJSON: null,
      mapPolygon: new Map()
    }, option));
  }

  init() {
    super.init();
    this.isPolygon3DGroup = true;
    this.thingsPolygon = new Map();
  }

  afterInstantiation() {
    super.afterInstantiation();
    const { qt } = this;
    const { enableCreateFlatPolygon, urlGeoJSON, mapPolygon } = this.option;
    if (enableCreateFlatPolygon) {
      if (isEmpty(urlGeoJSON)) qt.log.warn("缺少urlGeoJSON，无法创建2D区块");
      else this.thingArea = new Polygon({ qt, urlGeoJSON });
    }
    // 解析多边形
    mapPolygon.forEach((name, key) => {
      const o = this.object.getObjectByName(key);
      if (!o) qt.log.warn(`解析多边形时为找到[${key}]对应的模型`);
      else {
        cloneO3Materials(o);
        this.thingsPolygon.set(key, new Polygon3D({ qt, name, object: o, isInGroup: true }));
      }
    });
  }

  destroy(force = false, enableCr = true) {
    this.thingArea && !this.thingArea.isDestroyed && this.thingArea.destroy(force, enableCr);
    this.thingsPolygon.forEach(thing => !thing.isDestroyed && thing.destroy(force, enableCr));
    return super.destroy(force, enableCr);
  }
}