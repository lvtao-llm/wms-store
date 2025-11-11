import Thing from "./../../../src/core/Thing";
import { mergeDeep } from "@vci/helper/src/object";
import { FileLoader, Group } from "three";

export default class Polygon extends Thing {
  constructor(option) {
    super(mergeDeep({
      position: [0, 2, 0],
      // 功能性配置
      enableEvent: false,
      enableEdit: false,
      enableUpdateRenderOrderDeep: false,
      // 其他
      styles: null,
      geoJSON: null,
      urlGeoJSON: null
    }, option));
  }

  init() {
    super.init();
    this.isPolygon = true;
    this.isReady = false;
    this.object = new Group();
  }

  async afterInstantiation() {
    super.afterInstantiation();
    const { geoJSON } = this.option;
    if (geoJSON) this.createO3(geoJSON);
    else await this.loadResource();
    this.isReady = true;
  }

  async loadResource() {
    const { qt, option } = this;
    const dataGeo = (await new FileLoader(qt.thingWorld.loadingManager).loadAsync(option.urlGeoJSON)).toString();
    this.createO3(JSON.parse(dataGeo));
  }

  createO3(geo) {
    const { qt } = this;
    const { styles } = this.option;
    const o3Geo = qt.thingWorld.createO3FromGeoJSONMercator(qt.thingWorld.transformGeoJSON(geo), styles);
    this.object.add(o3Geo.o3Area);
    this.object.add(o3Geo.o3Line);
  }

  async next() {
    await new Promise(resolve => this.inter.next = setInterval(() => {
      if (this.isReady) {
        clearInterval(this.inter.next);
        resolve();
      }
    }, 50));
  }
}
