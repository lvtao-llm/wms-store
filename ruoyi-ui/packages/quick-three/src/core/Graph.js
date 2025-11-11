import Thing from "./Thing";
import { mergeDeep } from "@vci/helper/src/object";
import { CanvasTexture, Sprite, SpriteMaterial, SRGBColorSpace } from "three";
import Drawer from "@vci/quick-drawer";
// 平面图形
export default class Graph extends Thing {
  constructor(option) {
    super(mergeDeep({
      width: 100,
      height: 100,
      center: [0.5, 0],
      enableCheckObject: false
    }, option));
  }

  init() {
    super.init();
    this.isThingGraph = true;
    this.object = this.createObject();
    this.createGraph();
  }

  createObject() {
    const { center } = this.option;
    const mtl = new SpriteMaterial({
      transparent: true,
      alphaTest: 0.001
    });
    const sprite = new Sprite(mtl);
    sprite.center.set(...center);
    return sprite;
  }

  createGraph() {
    const { object } = this;
    const { width, height } = this.option;
    this.drawer = new Drawer({ width, height, dpr: 4 });
    object.material.map = new CanvasTexture(this.drawer.canvas);
    object.material.map.colorSpace = SRGBColorSpace;
  }

  update(option) {
    super.update(option);
    const { object } = this;
    const { width, height, center } = this.option;
    object.center.set(...center);
    this.setSize(width, height);
    this.updateMaterial();
    return this;
  }

  setSize(width, height) {
    const { drawer } = this;
    drawer.setSize(width, height);
    this.option.scale = [width / 10, height / 10, this.option.scale[2]];
    this.objectUpdate();
  }

  updateMaterial() {
    const { object } = this;
    object.material.map.needsUpdate = object.material.needsUpdate = true;
  }

  openPenetration() {
    const { object } = this;
    object.material.depthWrite = object.material.depthTest = false;
    return this;
  }

  closePenetration() {
    const { object } = this;
    object.material.depthWrite = object.material.depthTest = true;
    return this;
  }

  destroy(force = false) {
    const { drawer } = this;
    drawer && drawer.destroy();
    return super.destroy(force);
  }
}
