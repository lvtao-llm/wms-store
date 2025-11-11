import Thing from "./Thing";
import { mergeDeep } from "@vci/helper/src/object";
import { addCss, removeCss } from "@vci/helper/src/element";
import { CSS2DObject } from "three/examples/jsm/renderers/CSS2DRenderer";
// Element2D
export default class Element2D extends Thing {
  constructor(option) {
    super(mergeDeep({
      element: null
    }, option));
  }

  init() {
    super.init();
    this.isThingElement2D = true;
    this.object = new CSS2DObject();
  }

  update(option) {
    super.update(option);
    const { element } = this.option;
    if (!element || element && element.parentNode !== this.object.element) {
      this.objectDispose();
      this.objectMount(this.createObject(element));
      this.objectUpdate();
    }
    return this;
  }

  updateEvents() {
    super.updateEvents();
    addCss(this.object.element, { pointerEvents: this.enableEvent ? "auto" : "none" });
  }

  createObject(element) {
    const elWrap = document.createElement("div");
    addCss(elWrap, {
      cursor: "pointer",
      pointerEvents: this.enableEvent ? "auto" : "none"
    });
    elWrap.addEventListener("click", e => e.stopPropagation());
    element && elWrap.appendChild(element);
    return new CSS2DObject(elWrap);
  }

  objectDispose() {
    const { enableAddObjectToParent } = this.option;
    enableAddObjectToParent && this.parentObject.remove(this.object);
  }

  show(enableModifyMaterial = true, enableModifyStyle = true) {
    enableModifyStyle && addCss(this.object.element, { opacity: 1 });
    return super.show(enableModifyMaterial);
  }

  hide(enableModifyMaterial = true, enableModifyStyle = true) {
    enableModifyStyle && addCss(this.object.element, { opacity: 0 });
    return super.hide(enableModifyMaterial);
  }

  fadeIn() {
    return this.fadeTo(1);
  }

  fadeTo(opacity = 1) {
    clearTimeout(this.inter.fade);
    clearTimeout(this.inter.fadeIm);
    this.visible = true;
    this.object.visible = true;
    const duration = .2;
    addCss(this.object.element, { transition: `opacity ${duration}s ease-in-out` });
    this.inter.fadeIm = setTimeout(() => addCss(this.object.element, { opacity }), 10);
    return new Promise(resolve => this.inter.fade = setTimeout(() => {
      removeCss(this.object.element, ["transition"]);
      this.qt.helper.updateEventO3s();
      resolve(this);
    }, duration * 1e3));
  }

  fadeOut() {
    clearTimeout(this.inter.fade);
    clearTimeout(this.inter.fadeIm);
    this.visible = false;
    this.qt.helper.updateEventO3s();
    const duration = .2;
    addCss(this.object.element, { transition: `opacity ${duration}s ease-in-out` });
    this.inter.fadeIm = setTimeout(() => addCss(this.object.element, { opacity: 0 }), 10);
    return new Promise(resolve => this.inter.fade = setTimeout(() => {
      removeCss(this.object.element, ["transition"]);
      this.object.visible = false;
      resolve(this);
    }, duration * 1e3));
  }
}
