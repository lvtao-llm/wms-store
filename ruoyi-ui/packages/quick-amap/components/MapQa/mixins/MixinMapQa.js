import { mergeDeep } from "@vci/helper/src/object";
import O3DisposerHelper from "@vci/quick-three/src/helper/O3DisposerHelper";

const Mmq = {
  qa: null,
  map: null,
  layerGL: null
};
export default {
  computed: {
    qa() {
      return Mmq.qa;
    },
    layerGL() {
      return Mmq.layerGL;
    },
    qt() {
      return Mmq.layerGL.qt;
    },
    map() {
      return Mmq.map;
    },
    mapLayerBuilding() {
      return Mmq.map.getLayers().find(layer => layer["CLASS_NAME"] === "AMap.Buildings");
    }
  },
  methods: {
    setQa(qa, map, layerGL) {
      Mmq.qa = qa;
      Mmq.map = map;
      Mmq.layerGL = layerGL;
    },
    async resetPerspective() {
      const mapInstantiation = this.qa.mapInstantiation;
      const { center, zoom, pitch, rotation } = mapInstantiation.option.mapOption;
      await this.setPerspective({ center, zoom, pitch, rotation });
    },
    setPerspective(option, immediate = false, duration = 1e3) {
      const { center, zoom, pitch, rotation } = mergeDeep({
        center: [104.218193, 33.630513],
        zoom: 3.6,
        pitch: 0,
        rotation: 0
      }, option);
      this.map.setPitch(pitch, immediate, duration);
      this.map.setRotation(rotation, immediate, duration);
      this.map.setZoomAndCenter(zoom, center, immediate, duration);
      clearTimeout(Mmq.interPerspective);
      return new Promise(resolve => Mmq.interPerspective = setTimeout(resolve, duration));
    },
    updateThings(data, cu, things) {
      const { qt } = this;
      if (!cu) {
        console.error("updateThings时请传入函数cu来更新和创建实例");
        return false;
      }
      if (!things) things = qt.things;
      // 销毁最新数据里没有的实例
      qt.debug && console.info("销毁无效实例前: things.length", things.length);
      things.forEach(thing => {
        const enableDestroy = !data.some(item => item.id === thing.id);
        enableDestroy && thing.destroy();
      });
      things = things.filter(thing => !thing.isDestroyed);
      qt.debug && console.info("销毁无效实例后: things.length", things.length);
      // 根据数据更新和创建新的实例
      data.forEach(item => cu(item, things));
      return things;
    },
    clearQt() {
      const { qt } = this;
      qt.effect.effectOutline.selection.clear();
      qt.clearThings();
      O3DisposerHelper.DisposeObject3D(qt.scene.children.filter(o => !["GridHelper", "AxesHelper"].includes(o.type)), qt.scene);
    }
  }
};
