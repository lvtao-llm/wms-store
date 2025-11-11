import Thing from "./../../../../src/core/Thing";
import { mergeDeep } from "@vci/helper/src/object";
import { isEmpty } from "@vci/helper/src/other";
import Polygon from "../Polygon";

// 多边形
export default class Polygon3D extends Thing {
  constructor(option) {
    super(mergeDeep({
      isInGroup: false,
      enableCreateFlatPolygon: false,
      urlGeoJSON: null
    }, option));
  }

  init() {
    super.init();
    this.isPolygon3D = true;
    if (this.option.isInGroup) mergeDeep(this.option, {
      // 功能性配置
      enableUpdateMatrix: false,
      enableDisposeO3OnUpdate: false,
      // 功能性配置|销毁
      enableDisposeO3OnDestroy: false,
      enableRemoveO3FromParentOnDestroy: false,
      // 功能性配置|qt.updateThings
      enableRemovedWhenDestroyOnUpdate: false,
      enableAddObjectToParent: false
    });
  }

  afterInstantiation() {
    super.afterInstantiation();
    const { qt } = this;
    const { enableCreateFlatPolygon, urlGeoJSON } = this.option;
    if (enableCreateFlatPolygon) {
      if (isEmpty(urlGeoJSON)) qt.log.warn("缺少urlGeoJSON，无法创建2D区块");
      else this.thingArea = new Polygon({ qt, urlGeoJSON });
    }
  }

  destroy(force = false, enableCr = true) {
    this.thingArea && !this.thingArea.isDestroyed && this.thingArea.destroy(force, enableCr);
    return super.destroy(force, enableCr);
  }
}