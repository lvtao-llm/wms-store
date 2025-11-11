import Thing from "@vci/quick-amap/src/core/Thing";
import { mergeDeep } from "@vci/helper/src/object";

export default class TemplateMapThing extends Thing {
  static Pool = [];

  constructor(option) {
    super(mergeDeep({}, option, true));
  }

  static getInstances() {
    return TemplateMapThing.Pool;
  }

  // 初始化
  init(option) {
    super.init(option);
    this.isTemplateMapThing = true;
    // const AMap = this.getAMap();
    // const map = this.getAMapInstance();
    // const Loca = this.getLoca();
    // const loca = this.getLocaInstance();
    TemplateMapThing.Pool.push(this);
  }

  // 更新
  update(option) {
    super.update(option);
    // const AMap = this.getAMap();
    // const map = this.getAMapInstance();
    // const Loca = this.getLoca();
    // const loca = this.getLocaInstance();
    // const { position } = this.option;
    return this;
  }

  destroy() {
    // const AMap = this.getAMap();
    // const map = this.getAMapInstance();
    super.destroy();
    TemplateMapThing.clearDestroyedThings(TemplateMapThing);
  }
}
