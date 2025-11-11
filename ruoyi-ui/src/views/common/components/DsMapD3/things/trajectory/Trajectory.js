import Thing from "@vci/quick-three/src/core/Thing";
import {
  BufferGeometry,
  CatmullRomCurve3,
  DoubleSide,
  Euler,
  Group,
  InstancedMesh,
  Line,
  LineBasicMaterial,
  Matrix4,
  Mesh,
  MeshBasicMaterial,
  PlaneGeometry,
  RepeatWrapping,
  SRGBColorSpace,
  TextureLoader,
  Vector2,
  Vector3
} from "three";
import { mergeDeep } from "@vci/helper/src/object";
import { getSpacedPoints } from "@vci/quick-three/src/helper/ThreeHelper";
import { degToRad } from "three/src/math/MathUtils";
import LineGeometry from "@vci/quick-three/src/custom/LineGeometry";
import { remove, Tween } from "@tweenjs/tween.js";
import O3DisposerHelper from "@vci/quick-three/src/helper/O3DisposerHelper";
import { PluginNotification } from "@vci/vci/vue/v2/components/other/VpNotification";

// 轨迹
export default class Trajectory extends Thing {
  constructor(option) {
    super(mergeDeep({
      path: [],
      visibleStrong: true,
      arrow: {
        urlTexture: null
      },
      line: {
        urlTexture: null
      }
    }, option));
  }

  init() {
    super.init();
    this.isTrajectory = true;
    this.object = new Group();
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.updateStyle();
    this.modifyShadowState(false);
    this.sleep();
  }

  updateStyle() {
    const { arrow, line } = this.option;
    if (arrow.urlTexture) {
      this.textureArrow && this.textureArrow.dispose();
      this.textureArrow = new TextureLoader().load(arrow.urlTexture);
      this.textureArrow.colorSpace = SRGBColorSpace;
    }
    if (line.urlTexture) {
      this.textureLine = new TextureLoader().load(line.urlTexture);
      this.textureLine.colorSpace = SRGBColorSpace;
      this.textureLine.wrapS = this.textureLine.wrapT = RepeatWrapping;
    }
  }

  updateTrajectory(path) {
    O3DisposerHelper.DisposeObject3D(this.object.children, this.object);
    this.option.path = path;
    this.createO3Trajectory();
  }

  createO3Trajectory() {
    const { qt } = this;
    const { path, visibleStrong } = this.option;
    if (path.length < 2) {
      PluginNotification.tip("轨迹数据少于两条，无法展示");
      console.warn(`轨迹-createO3Trajectory, 点位过少`, path);
      return this;
    }
    const points = getSpacedPoints(path.map(point => new Vector3().fromArray(point)), .1);
    const curve = new CatmullRomCurve3(points, false, "catmullrom", .5);
    const lengthCurve = curve.getLength();
    const pointsCurve = curve.getSpacedPoints(Math.round(lengthCurve));
    if (qt.debug) {
      const geo = new BufferGeometry();
      geo.setFromPoints(pointsCurve);
      const mtl = new LineBasicMaterial({ color: "#ff3d3d" });
      const o3 = new Line(geo, mtl);
      this.object.add(o3);
    }
    // 箭头
    const amountArrow = Math.floor(lengthCurve / 6) + 1;
    const uStep = 6 / lengthCurve;
    const geoArrow = new PlaneGeometry(1.8, 1.8);
    geoArrow.rotateX(degToRad(-90));
    const mtlArrow = new MeshBasicMaterial({
      name: "arrow",
      transparent: true,
      map: this.textureArrow,
      side: DoubleSide,
      depthTest: !visibleStrong,
      depthWrite: false
    });
    const o3Arrow = new InstancedMesh(geoArrow, mtlArrow, amountArrow + 1);
    const matrix4 = new Matrix4();
    function updateArrow(u, i, needsUpdate = false, isEndArrow = false) {
      u = Math.min(u, 1);
      const point = curve.getPointAt(u);
      const tangent = curve.getTangentAt(u);
      const euler = new Euler();
      euler.set(0, new Vector2(tangent.x, -tangent.z).angle(), 0);
      o3Arrow.getMatrixAt(i, matrix4);
      matrix4.makeRotationFromEuler(euler);
      matrix4.setPosition(point.x, point.y + (isEndArrow ? 0.1 : 0), point.z);
      o3Arrow.setMatrixAt(i, matrix4);
      if (needsUpdate) o3Arrow.instanceMatrix.needsUpdate = true;
    }
    updateArrow(1, amountArrow, false, true);
    for (let i = 0; i < amountArrow; i++) {
      updateArrow(i * uStep, i, i === amountArrow - 1);
    }
    o3Arrow.position.set(0, .1, 0);
    o3Arrow.renderOrder = 2;
    this.object.add(o3Arrow);
    this.tw.arrow && remove(this.tw.arrow);
    this.tw.arrow = new Tween({ u: 0 })
      .to({ u: 6 / lengthCurve }, 2e3)
      .repeat(Infinity)
      .onUpdate(e => {
        for (let i = 0; i < amountArrow; i++) {
          updateArrow(i * uStep + e.u, i, i === amountArrow - 1);
        }
      })
      .start();
    // 线条
    const geoLine = new LineGeometry(curve, Math.floor(lengthCurve), .45);
    const textureLine = this.textureLine.clone();
    textureLine.repeat.set(Math.max(Math.floor(lengthCurve / 20), 1), 2);
    textureLine.offset.set(0, 0);
    const o3Line = new Mesh(geoLine, new MeshBasicMaterial({
      name: "line",
      transparent: true,
      map: textureLine,
      depthTest: !visibleStrong,
      depthWrite: false
    }));
    o3Line.renderOrder = 1;
    this.tw.flow && remove(this.tw.flow);
    this.tw.flow = new Tween(textureLine.offset)
      .to({ x: -1 }, 10e3)
      .repeat(Infinity)
      .start();
    this.object.add(o3Line);
    return this;
  }

  async fadeIn(tweenOption = {}) {
    setTimeout(() => this.modifyShadowState(false));
    await super.fadeIn(tweenOption);
    return this;
  }

  async fadeOut(tweenOption = {}) {
    await super.fadeOut(tweenOption);
    return this;
  }
}