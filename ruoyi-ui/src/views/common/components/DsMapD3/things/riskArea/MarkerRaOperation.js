import Marker from "@vci/quick-three/components/MapD3/things/Marker";
import { instantiateComponentVm } from "@vci/quick-three/components/vue.component";
import CptMarkerRaOperation from "./CptMarkerRaOperation/CptMarkerRaOperation.vue";

export default class MarkerRaOperation extends Marker {
  init() {
    this.option.vm = instantiateComponentVm(CptMarkerRaOperation);
    super.init();
    this.isMarkerRaOperation = true;
    this.isFocused = false;
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.object.center.set(.5, -.2);
    this.vm.$on("hover-on", () => clearTimeout(this.inter.blur));
    this.vm.$on("hover-off", () => this.blur());
    this.vm.$on("edit", () => this.dispatchEvent("edit"));
    this.vm.$on("delete", () => this.dispatchEvent("delete"));
  }

  focus() {
    clearTimeout(this.inter.blur);
    this.isFocused = true;
    this.fadeIn();
  }

  blur() {
    clearTimeout(this.inter.blur);
    this.inter.blur = setTimeout(() => {
      this.isFocused = false;
      this.fadeOut();
    }, .2e3);
  }
}