import Thing from "@vci/quick-three/src/core/Thing";
import { mergeDeep } from "@vci/helper/src/object";
import {
  BufferGeometry,
  CanvasTexture,
  Group,
  Mesh,
  MeshBasicMaterial,
  Shape,
  SRGBColorSpace,
  Vector2,
  Vector3
} from "three";
import { degToRad } from "three/src/math/MathUtils";
import Drawer from "@vci/quick-drawer";
import { addCss, getElementBody } from "@vci/helper/src/element";
import { hexToRgba } from "@vci/helper/src/color";
import ShapeUvGeometry from "@vci/quick-three/src/custom/ShapeUvGeometry";
import RaText from "./RaText";
import MarkerRaInfo from "./MarkerRaInfo";
import { PerspectiveModes, TypesRiskArea } from "@/constants";
import { ThingEvents } from "@vci/quick-three/src/events/ThingEvents";
import MarkerRaOperation from "./MarkerRaOperation";
import { delay } from "@/common/helper";
import { PluginLoading } from "@vci/vci/vue/v2/components/other/VpLoading";
import { storeCore } from "@/store/store.core";

export default class Ra extends Thing {
  constructor(option) {
    super(mergeDeep({
      position: [0, .1, 0],
      path: [],
      color: "#ff9292",
      isDrawingMode: false,
      renderOrder: 9
    }, option));
  }

  init() {
    super.init();
    this.isRa = true;
    this.isChoosing = false;
    this.object = new Group();
  }

  afterInstantiation() {
    super.afterInstantiation();
    const { isDrawingMode } = this.option;
    isDrawingMode && this.sleep();
    this.createText();
    this.createO3Shape();
    this.createDrawer();
    this.updateO3Shape();
    !isDrawingMode && this.createMarkerInfo();
    this.addEventListener(ThingEvents.Click, () => {
      if (!isDrawingMode) {
        this.qt.things.forEach(thing => thing.isMarkerRaInfo && thing !== this.thingMarkerInfo && thing.isFocused && thing.blur());
        !this.thingMarkerInfo.isFocused ? this.thingMarkerInfo.focus() : this.thingMarkerInfo.blur();
      }
    });
    !isDrawingMode && this.createMarkerOperation();
    this.addEventListener(ThingEvents.ContextMenu, () => {
      if (!isDrawingMode) this.thingMarkerOperation.focus();
    });
    this.addEventListener(ThingEvents.HoverOff, () => {
      if (!isDrawingMode) this.thingMarkerOperation.blur();
    });
  }

  createO3Shape() {
    const { object } = this;
    const o3 = new Mesh(
      new BufferGeometry(),
      new MeshBasicMaterial({
        transparent: true,
        depthTest: false,
        depthWrite: false
      })
    );
    this.o3 = o3;
    object.add(o3);
  }

  createDrawer() {
    const { qt, o3 } = this;
    const { isDrawingMode } = this.option;
    const drawer = this.drawer = new Drawer({
      width: 100,
      height: 100,
      dpr: 1
    });
    if (qt.debug && isDrawingMode) {
      drawer.mount(getElementBody());
      addCss(drawer.canvas, {
        position: "fixed",
        left: 0,
        top: 0,
        zIndex: 9,
        pointsEvent: "none",
        transform: "scale(.5)",
        transformOrigin: "0 0"
      });
    }
    o3.material.map = new CanvasTexture(drawer.canvas);
    o3.material.map.colorSpace = SRGBColorSpace;
  }

  updateO3Shape() {
    const { o3 } = this;
    const { path } = this.option;
    if (path.length < 3) return this;
    else {
      const shape = new Shape(path.map(v => new Vector2(v.x, -v.z)));
      o3.geometry.dispose();
      o3.geometry = new ShapeUvGeometry(shape);
      o3.geometry.rotateX(degToRad(-90));
      o3.geometry.computeBoundingBox();
      o3.geometry.computeVertexNormals();
      this.size = o3.geometry.boundingBox.getSize(new Vector3());
      this.coordinateGeoMin = new Vector2(o3.geometry.boundingBox.min.x, o3.geometry.boundingBox.min.z);
      this.updateShapeTexture();
    }
    path.length < 3 ? this.thingRaText.hide() : this.thingRaText.show();
    this.updateText();
    return this;
  }

  updateShapeTexture() {
    const { drawer, coordinateGeoMin, size, o3 } = this;
    const { path, color } = this.option;
    if (path.length < 3) return this;
    const width = 1080;
    const height = size.z / size.x * width;
    const ratio = width / size.x;
    drawer.setSize(width, height);
    drawer.render((ctx, drawer) => {
      drawer.clear();
      ctx.setLineDash([5, 10]);
      const pathCanvas = path.map(v => [(v.x - coordinateGeoMin.x) * ratio, (v.z - coordinateGeoMin.y) * ratio]);
      ctx.fillStyle = hexToRgba(color, .2);
      ctx.strokeStyle = hexToRgba(color, 1);
      ctx.lineWidth = 4;
      ctx.lineCap = ctx.lineJoin = "round";
      if (path.length < 1) {
        // 啥也不做
      } else if (path.length < 2) {
        ctx.arc(...pathCanvas[0], 2, 0, Math.PI * 2);
        ctx.fill();
      } else {
        ctx.beginPath();
        ctx.moveTo(...pathCanvas[0]);
        pathCanvas.slice(1).forEach(v => ctx.lineTo(...v));
        ctx.closePath();
        ctx.fill();
        ctx.stroke();
      }
      o3.material.map.dispose();
      o3.material.map = new CanvasTexture(drawer.canvas);
      o3.material.map.colorSpace = SRGBColorSpace;
      o3.material.needsUpdate = o3.material.map.needsUpdate = true;
    });
    return this;
  }

  addCoordinate(coordinate = new Vector3()) {
    const { path } = this.option;
    path.push(coordinate);
    this.updateO3Shape();
  }

  getPathOfLnglat() {
    const { qt } = this;
    const { path } = this.option;
    return path.map(v => qt.thingWorld.coordsToLngLat([v.x, v.z]));
  }

  createText() {
    const { qt } = this;
    this.thingRaText = new RaText({ qt });
    this.thingRaText.hide();
    this.updateText(this.name);
  }

  updateText(name, position) {
    !name && (name = this.name);
    !position && (position = this.getCenter().toArray());
    this.thingRaText.update({ name, position });
    return this;
  }

  destroyText() {
    this.thingRaText && this.thingRaText.destroy();
  }

  createMarkerInfo() {
    const { qt } = this;
    this.thingMarkerInfo = new MarkerRaInfo({
      qt,
      position: this.getCenter().toArray()
    });
    this.updateMarkerInfo();
    this.thingMarkerInfo.hide();
  }

  updateMarkerInfo(idType) {
    const { name, color, extData } = this.option;
    const targetTra = TypesRiskArea.find(tra => tra.value === (idType || extData.idType)) || {
      name: "未知",
      value: extData.idType,
      color: "#fff"
    };
    this.thingMarkerInfo && this.thingMarkerInfo.update({
      name,
      color: color || targetTra.color,
      levelRisk: targetTra.name
    });
    return this;
  }

  destroyMarkerInfo() {
    this.thingMarkerInfo && this.thingMarkerInfo.destroy();
  }

  createMarkerOperation() {
    const { qt } = this;
    this.thingMarkerOperation = new MarkerRaOperation({
      qt,
      position: this.getCenter().toArray()
    });
    this.thingMarkerOperation.hide();
    this.thingMarkerOperation.addEventListener("edit", async () => {
      const thingRiskArea = qt.thingRiskArea;
      storeCore().mapRiskArea.drawing = true;
      storeCore().mapRiskArea.isUpdate = true;
      storeCore().mapRiskArea.info.name = this.name;
      storeCore().mapRiskArea.info.idType = this.extData.idType;
      setTimeout(() => storeCore().mapRiskArea.info.color = this.extData.color);
      thingRiskArea._thingArea = this;
      await qt.switchPerspectiveMode(PerspectiveModes.D2);
      await qt.control.fitToBox(this.box, true);
    });
    this.thingMarkerOperation.addEventListener("delete", async () => {
      // TODO 删除服务端区域数据
      PluginLoading.open({ text: "删除中" });
      await delay();
      PluginLoading.close();
      const thingRiskArea = qt.thingRiskArea;
      thingRiskArea._data = thingRiskArea._data.filter(v => v.id !== this.id);
      thingRiskArea.dataAdded = thingRiskArea.dataAdded.filter(v => v.id !== this.id);
      qt.thingRiskArea.updateFromData();
    });
  }

  deleteMarkerOperation() {
    this.thingMarkerOperation && this.thingMarkerOperation.destroy();
  }

  destroy(force = false, enableCr = true) {
    this.drawer.destroy();
    this.destroyText();
    this.destroyMarkerInfo();
    this.deleteMarkerOperation();
    return super.destroy(force, enableCr);
  }
}