import Marker from "@vci/quick-three/components/MapD3/things/Marker";
import { instantiateComponentVm } from "@vci/quick-three/components/vue.component";
import CptSensor from "./CptSensor/CptSensor.vue";

export default class Sensor extends Marker {
  init() {
    this.option.vm = instantiateComponentVm(CptSensor);
    super.init();
    this.isSensor = true;
  }

  afterInstantiation() {
    super.afterInstantiation();
    this.vm.$on("click", () => {
      this.qt.things.forEach(thing => thing.isSensor && thing !== this && thing.vm.isFocused && thing.blur());
      this.vm.isFocused ? this.blur() : this.focus();
    });
  }

  update(option) {
    console.log(option);
    super.update(option);
    this.vm.list = [
      {
        name: "传感器名称1",
        value: this.name
      }
    ];
    return this;
  }

  focus() {
    this.vm.isFocused = true;
  }

  blur() {
    this.vm.isFocused = false;
  }
}