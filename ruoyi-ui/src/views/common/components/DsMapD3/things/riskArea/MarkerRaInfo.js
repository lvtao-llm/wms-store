import Marker from "@vci/quick-three/components/MapD3/things/Marker";
import { instantiateComponentVm } from "@vci/quick-three/components/vue.component";
import CptMarkerRaInfo from "./CptMarkerRaInfo/CptMarkerRaInfo.vue";
import { mergeDeep } from "@vci/helper/src/object";

export default class MarkerRaInfo extends Marker {
  constructor(option) {
    super(mergeDeep({
      levelRisk: null,
      color: null
    }, option));
  }

  init() {
    this.option.vm = instantiateComponentVm(CptMarkerRaInfo);
    super.init();
    this.isMarkerRaInfo = true;
    this.isFocused = false;
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.object.center.set(.5, 1.6);
    this.vm.$on("blur", this.blur.bind(this));
  }

  update(option) {
    super.update(option);
    const { name, levelRisk, color } = this.option;
    this.vm.infos = [
      {
        name: "名称：",
        value: name
      },
      {
        name: "风险等级：",
        value: levelRisk,
        color
      }
    ];
    return this;
  }

  focus() {
    this.isFocused = true;
    this.fadeIn();
  }

  blur() {
    this.isFocused = false;
    this.fadeOut();
  }
}