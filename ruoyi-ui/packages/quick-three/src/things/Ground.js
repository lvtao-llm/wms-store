import Thing from "../core/Thing";
import { Mesh, PlaneGeometry } from "three";
import MeshReflectorMaterial from "../custom/MeshReflectorMaterial";
import MaterialPatch from "../helper/MaterialPatch";
import { mergeDeep } from "@vci/helper/src/object";

export default class Ground extends Thing {
  constructor(option) {
    super(mergeDeep({
      enableEvent: false,
      enableEdit: false,
      width: 1e4,
      height: 1e4
    }, option, true));
  }

  init() {
    super.init();
    this.isGround = true;
    const { width, height } = this.option;
    this.object = this.genGround(width, height);
  }

  update(option) {
    super.update(option);
    return this;
  }

  updateSize(width, height) {
    this.update({
      width,
      height,
      object: this.genGround(width, height)
    });
  }

  genGround(width, height) {
    const geo = new PlaneGeometry(width, height);
    const mesh = new Mesh(geo);
    const { renderer, camera, scene } = this.qt;
    const paramsMtl = {
      mixBlur: 0.88,
      mixStrength: 0.66,
      resolution: 1024,
      blur: [512, 512],
      minDepthThreshold: 0.9,
      maxDepthThreshold: 1,
      depthScale: 0,
      depthToBlurRatioBias: 0.25,
      mirror: 1,
      distortion: 1,
      mixContrast: 1,
      reflectorOffset: 0,
      bufferSamples: 8
    };
    const mtl = mesh.material = new MeshReflectorMaterial(renderer, camera, scene, mesh, paramsMtl);
    mtl.transparent = true;
    mtl.opacity = 0.2;
    if (this.qt.debug) {
      this.gui = this.qt.gui.gui().addFolder("Debug@MeshReflectorMaterial");
      this.gui.add(paramsMtl, "mixBlur", 0, 1, 0.01).onChange(value => mtl.reflectorProps["mixBlur"] = value);
      this.gui.add(paramsMtl, "mixStrength", 0, 1, 0.01).onChange(value => mtl.reflectorProps["mixStrength"] = value);
      this.gui.add(paramsMtl, "minDepthThreshold", 0, 1, 0.01).onChange(value => mtl.reflectorProps["minDepthThreshold"] = value);
      this.gui.add(paramsMtl, "maxDepthThreshold", 0, 1, 0.01).onChange(value => mtl.reflectorProps["maxDepthThreshold"] = value);
      this.gui.add(paramsMtl, "depthScale", 0, 1, 0.01).onChange(value => mtl.reflectorProps["depthScale"] = value);
      this.gui.add(paramsMtl, "depthToBlurRatioBias", 0, 1, 0.01).onChange(value => mtl.reflectorProps["depthToBlurRatioBias"] = value);
      this.gui.add(paramsMtl, "mirror", 0, 1, 0.01).onChange(value => mtl.reflectorProps["mirror"] = value);
      this.gui.add(paramsMtl, "distortion", 0, 1, 0.01).onChange(value => mtl.reflectorProps["distortion"] = value);
      this.gui.add(paramsMtl, "mixContrast", 0, 1, 0.01).onChange(value => mtl.reflectorProps["mixContrast"] = value);
      // this.gui.add(paramsMtl, "resolution", 0, 4096, 2).onChange(value => {
      //   mtl.reflectorProps["resolution"] = value;
      //   mtl.setupBuffers(value, paramsMtl.blur, paramsMtl.bufferSamples);
      //   mtl.needsUpdate = true;
      // });
    }
    return mesh;
  }

  show(onlyVisible = false) {
    super.show(onlyVisible);
    !onlyVisible && MaterialPatch.ModifyMtlProperties(this.object, { opacity: 0.2 });
    return this;
  }

  destroy(force = false) {
    this.gui && this.gui.destroy();
    super.destroy(force);
  }
}
