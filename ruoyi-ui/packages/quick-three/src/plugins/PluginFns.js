import MaterialPatch from "../helper/MaterialPatch";
import { guiMaterial } from "../helper/MaterialDebugGuiHelper";
import { QtPlugin } from "./QtPlugin";
import { QtEvents } from "../events/QtEvents";

class PluginFns extends QtPlugin {
  static namespace = "fns";

  init() {
    super.init();
    const { qt } = this;
    this.enableModifyMaterial = false;
    if (qt.debug) {
      qt.gui.guis.fns.add(this, "enableModifyMaterial").name("调整材质").listen();
      qt.addEventListener(QtEvents.ClickThing, e => this.enableModifyMaterial && this.createGuiForDebugMaterial(e.detail.thing.name, e.detail.thing.object));
      qt.addEventListener(QtEvents.ClickEmpty, () => this.enableModifyMaterial && this.destroyGuiForDebugMaterial());
    }
  }

  /**
   * 创建材质调试面板
   * @param nameGui   gui标题
   * @param object    要调试的Object3D
   */
  createGuiForDebugMaterial(nameGui, object) {
    const { qt } = this;
    !object && qt.log.warn("请传入object进行材质调试");
    if (object) {
      qt.gui.guis.material && qt.gui.guis.material.destroy();
      qt.gui.guis.material = qt.gui.gui().addFolder(nameGui + "-材质调整");
      this.addGuiModifyMaterial(qt.gui.guis.material, object);
    }
  }

  addGuiModifyMaterial(folderMr, object) {
    const { qt } = this;
    const params = {
      currentMaterial: null,
      materials: []
    };
    MaterialPatch.TraverseMaterials(object, (material, o3) => {
      params.materials.push({
        name: (o3.name + "@" || "") + material.name,
        material,
        geometry: o3.geometry,
        object: o3
      });
    });
    qt.debug && qt.log.info("当前正在调整的模型: ", object);
    qt.debug && qt.log.info("当前正在调整的模型材质集: ", params.materials);
    folderMr.add(params, "currentMaterial", params.materials.map(material => material.name)).name("当前子模型材质").onChange(e => {
      const targetMaterial = params.materials.find(v => v.name === e);
      qt.debug && qt.log.info("当前正在调整的材质: ", targetMaterial);
      if (targetMaterial) {
        if (folderMr.fdp) folderMr.fdp.destroy();
        const folder = folderMr.fdp = folderMr.addFolder(targetMaterial.name);
        const { material, geometry } = targetMaterial;
        guiMaterial(folder, material, geometry);
      }
    });
    return folderMr;
  }

  // 销毁材质调试面板
  destroyGuiForDebugMaterial() {
    const { qt } = this;
    qt.gui.guis.material && qt.gui.guis.material.destroy();
    delete qt.gui.guis.material;
  }

  sleep() {
    if (this.qt.state.isSleep) return this;
    this.qt.state.isSleep = true;
    cancelAnimationFrame(this.qt.frame);
    return this;
  }

  wakeUp() {
    const { qt } = this;
    if (!this.qt.state.isSleep) return this;
    this.qt.state.isSleep = false;
    qt.option.enableAnimate && qt.animate();
    return this;
  }
}

export { PluginFns };
