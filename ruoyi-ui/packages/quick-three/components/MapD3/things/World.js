import Thing from "./../../../src/core/Thing";
import { mergeDeep } from "@vci/helper/src/object";
import {
  BufferGeometry,
  ExtrudeGeometry,
  Group,
  Line,
  LineBasicMaterial,
  Mesh,
  MeshBasicMaterial,
  Path,
  Shape,
  Vector2,
  Vector3
} from "three";
import { degToRad } from "three/src/math/MathUtils";
import { coordsToLnglat, lngLatToCoords, transformGeoJSON } from "./../../../src/helper/GeoHelper";
import O3DisposerHelper from "./../../../src/helper/O3DisposerHelper";
import { LineGeometry } from "three/examples/jsm/lines/LineGeometry";
import { Line2 } from "three/examples/jsm/lines/Line2.js";
import { LineMaterial } from "three/examples/jsm/lines/LineMaterial.js";
import ShapeUvGeometry from "./../../../src/custom/ShapeUvGeometry";

/**
 * 世界
 * 经纬度转换为世界坐标理论流程
 * 1、将经纬度坐标转换为墨卡托坐标(经纬度坐标系 -proj4-> 墨卡托坐标系)
 * 2、墨卡托坐标映射到世界坐标(此步骤无需代码，因为墨卡托坐标系和世界坐标系的原点和单位1是一致的(重合的)) (墨卡托坐标系 -> 世界坐标系)
 * 3、距离世界坐标系原点过远导致的模型渲染精度缺失问题需要设置映射到世界坐标系后坐标点的偏移量
 * 4、根据世界坐标点去渲染
 */
export default class World extends Thing {
  constructor(option) {
    super(mergeDeep({
      enableEvent: false,
      enableEdit: false,
      enableDisplayWorld: false,
      enableDisplayChina: false
    }, option));
  }

  init() {
    super.init();
    this.isWorld = true;
    this.object = new Group();
    this.offset = [0, 0];
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.setOffsetFromLngLat([0, 0]);
  }

  createO3World() {
    const { qt } = this;
    console.time("耗时 createO3World");
    const {
      o3Area,
      o3Line
    } = this.createO3FromGeoJSONMercator(transformGeoJSON(qt.assets.geos.world, this.offset));
    const grpWorld = new Group();
    grpWorld.add(o3Area, o3Line);
    grpWorld.name = "world";
    this.grpWorld = grpWorld;
    this.object.add(grpWorld);
    console.timeEnd("耗时 createO3World");
  }

  createO3China() {
    const { qt } = this;
    console.time("耗时 createO3China");
    const {
      o3Area,
      o3Line
    } = this.createO3FromGeoJSONMercator(transformGeoJSON(qt.assets.geos.china, this.offset));
    const grpChina = new Group();
    grpChina.add(o3Area, o3Line);
    grpChina.name = "china";
    this.grpChina = grpChina;
    this.object.add(grpChina);
    console.timeEnd("耗时 createO3China");
  }

  createO3FromGeoJSONMercator(geoJSONMercator, option) {
    const {
      flat,
      elevation,
      enableCreateLines,
      useLine2,
      materialArea,
      materialLine,
      renderOrder,
      onCreateArea
    } = mergeDeep({
      materialArea: { color: "#373d41" },
      enableCreateLines: true,
      materialLine: { color: "#89989b", linewidth: 1 },
      useLine2: false,
      flat: true, // 是否扁平
      elevation: 1, // flat=false时此参数用来设置厚度
      renderOrder: 0,
      onCreateArea: null
    }, option);
    const shapes = [];
    const o3Area = new Group();
    const grpLine = new Group();
    geoJSONMercator["features"].forEach(feature => {
      const _shapes = [];
      feature.geometry.coordinates.forEach(coordinate => {
        const shape = new Shape(coordinate[0].map(cd => new Vector2(cd[0], -cd[1])));
        shape.holes = coordinate.slice(1).map(cds => new Path(cds.map(cd => new Vector2(cd[0], -cd[1]))));
        _shapes.push(shape);
      });
      _shapes.userData = feature["properties"];
      shapes.push(_shapes);
    });
    shapes.forEach((shapes, i) => {
      const geometry = (() => {
        if (flat) return new ShapeUvGeometry(shapes);
        else return new ExtrudeGeometry(shapes, {
          curveSegments: Math.floor(eval(shapes.map(shape => shape.getLength()).join("+")) * 20),
          steps: 1,
          depth: elevation,
          bevelEnabled: false
        });
      })();
      geometry.rotateX(degToRad(-90));
      // 区块
      const o3 = onCreateArea ? onCreateArea(geometry, materialArea, shapes) : new Mesh(
        geometry,
        new MeshBasicMaterial({ ...materialArea })
      );
      o3.userData = shapes.userData;
      o3Area.add(o3);
      // 区块轮廓线
      enableCreateLines && shapes.forEach((shape, is) => {
        if (useLine2) {
          const geoLine = new LineGeometry();
          geoLine.setPositions((shape.getPoints() || []).map(es => [es.x, es.y, flat ? 0 : elevation + 0.1]).flat(1));
          geoLine.rotateX(degToRad(-90));
          const o3Line = new Line2(
            geoLine,
            new LineMaterial({ ...materialLine })
          );
          o3Line.material.name = `line ${i}-${is}`;
          o3Line.computeLineDistances();
          grpLine.add(o3Line);
        } else {
          const geoLine = new BufferGeometry();
          geoLine.setFromPoints((shape.getPoints() || []).map(es => new Vector3(es.x, es.y, flat ? 0 : elevation + 0.1)));
          geoLine.rotateX(degToRad(-90));
          const o3Line = new Line(
            geoLine,
            new LineBasicMaterial({ ...materialLine })
          );
          o3Line.material.name = `line ${i}-${is}`;
          grpLine.add(o3Line);
        }
      });
    });
    o3Area.renderOrder = renderOrder;
    grpLine.renderOrder = renderOrder + 1;
    // 区域
    return { o3Area, o3Line: grpLine };
  }

  /**
   * 设置映射到世界坐标系后坐标点的偏移量
   * 解决距离世界坐标系原点过远导致的模型渲染精度缺失问题
   * @param lngLat    经纬度(EPSG:4326)
   * @returns {World}
   */
  setOffsetFromLngLat(lngLat = [0, 0]) {
    const { enableDisplayWorld, enableDisplayChina } = this.option;
    this.offset = lngLatToCoords(lngLat).map(v => -v);
    this.grpWorld && O3DisposerHelper.DisposeObject3D([this.grpWorld], this.object);
    this.grpChina && O3DisposerHelper.DisposeObject3D([this.grpChina], this.object);
    delete this.grpWorld;
    delete this.grpChina;
    enableDisplayWorld && this.createO3World();
    enableDisplayChina && this.createO3China();
    return this;
  }

  lngLatToCoords(lngLat = [0, 0], elevation = 0) {
    const coords = lngLatToCoords(lngLat, this.offset);
    coords.splice(1, 0, elevation);
    return coords;
  }

  coordsToLngLat(coords = [0, 0]) {
    return coordsToLnglat(coords, this.offset);
  }

  transformGeoJSON(geoJSON) {
    return transformGeoJSON(geoJSON, this.offset);
  }
}