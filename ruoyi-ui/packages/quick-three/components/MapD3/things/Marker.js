import { mergeDeep } from "@vci/helper/src/object";
import { ThingEvents } from "./../../../src/events/ThingEvents";
import Element2D from "./../../../src/core/Element2D";

export default class Marker extends Element2D {
  constructor(option) {
    super(mergeDeep({
      vm: null,
      extData: {}
    }, option));
  }

  init() {
    super.init();
    this.isMarker = true;
    const { vm } = this.option;
    if (vm) {
      this.vm = vm;
      this.option.element = vm.$el;
    }
  }

  afterInstantiation() {
    super.afterInstantiation();
    const { qt } = this;
    this.object.element.addEventListener("mousemove", () => this.dispatchEvent(ThingEvents.HoverOn));
    this.object.element.addEventListener("mouseleave", () => this.dispatchEvent(ThingEvents.HoverOff));
    this.object.element.addEventListener("click", e => this.dispatchEvent(ThingEvents.Click, { event: e }));
    this.addEventListener(ThingEvents.HoverOn, () => {
      const thingsE2 = qt.things.filter(thing => thing.isThingElement2D);
      this.object.renderOrder = Math.max(...thingsE2.map(thing => thing.object.renderOrder)) + 1;
    });
  }

  update(option) {
    super.update(option);
    this.vm.name = this.name;
    return this;
  }

  destroy(force = false, enableCr = true) {
    this.vm && this.vm.$destroy();
    return super.destroy(force, enableCr);
  }
}
