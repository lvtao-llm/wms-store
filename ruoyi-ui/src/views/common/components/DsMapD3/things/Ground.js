import Thing from "@vci/quick-three/src/core/Thing";
import { Mesh, MeshStandardMaterial, PlaneGeometry, RepeatWrapping, SRGBColorSpace } from "three";
import { degToRad } from "three/src/math/MathUtils";

export default class Ground extends Thing {
  init() {
    super.init();
    this.isGround = true;
    this.object = this.createO3Plane();
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.sleep();
  }

  createO3Plane() {
    const { qt } = this;
    const geo = new PlaneGeometry(10e3, 10e3, 9, 9);
    geo.rotateX(degToRad(-90));
    const mapGround = qt.assets.textures.mapGround;
    mapGround.colorSpace = SRGBColorSpace;
    mapGround.wrapS = mapGround.wrapT = RepeatWrapping;
    mapGround.repeat.set(100, 100);
    const mtl = new MeshStandardMaterial({
      color: "#e6fecd",
      transparent: true,
      roughness: .9,
      metalness: .2,
      alphaMap: qt.assets.textures.mapGroundMask,
      map: mapGround,
      envMap: null
    });
    return new Mesh(geo, mtl);
  }

}
