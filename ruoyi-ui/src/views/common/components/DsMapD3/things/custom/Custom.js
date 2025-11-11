import Marker from "@vci/quick-three/components/MapD3/things/Marker";
import { instantiateComponentVm } from "@vci/quick-three/components/vue.component";
import Custom from "./Custom/custom.vue";

export default class Camera extends Marker {
  init() {
    this.option.vm = instantiateComponentVm(Custom)
    super.init();
    this.isCamera = true;
  }

  afterInstantiation() {
    () => { console.log(this.$store); }
    super.afterInstantiation();

    this.vm.$on("click", () => {
      this.qt.things.forEach(thing => thing.isCamera && thing !== this && thing.vm.isFocused && thing.blur());

      this.vm.isFocused ? this.blur() : this.focus();
    });
  }

  focus() {
    this.vm.isFocused = true;
  }

  blur() {
    this.vm.isFocused = false;
  }
}