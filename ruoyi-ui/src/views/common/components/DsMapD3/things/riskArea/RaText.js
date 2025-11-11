import Marker from "@vci/quick-three/components/MapD3/things/Marker";
import { instantiateComponentVm } from "@vci/quick-three/components/vue.component";
import CptRaText from "./CptRaText/CptRaText.vue";

export default class RaText extends Marker {
  init() {
    this.option.vm = instantiateComponentVm(CptRaText);
    super.init();
    this.isRaText = true;
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.sleep();
  }
}