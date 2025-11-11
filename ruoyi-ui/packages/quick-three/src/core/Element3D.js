import Thing from "./Thing";
import { mergeDeep } from "@vci/helper/src/object";
import { CSS3DObject, CSS3DSprite } from "three/examples/jsm/renderers/CSS3DRenderer";
import { addCss, removeCss } from "@vci/helper/src/element";
// Element3D
export default class Element3D extends Thing {
  constructor(option) {
    super(mergeDeep({
      element: null,
      isSprite: false
    }, option));
  }

  init() {
    super.init();
    this.isThingElement3D = true;
    this.object = new CSS3DObject();
  }

  update(option) {
    super.update(option);
    const { element, isSprite } = this.option;
    if (isSprite) {
      if (this.object instanceof CSS3DSprite) {
        if (!element || element && element.parentNode !== this.object.element) {
          this.objectDispose();
          this.objectMount(this.createObject(element, isSprite));
        }
      } else {
        this.objectDispose();
        this.objectMount(this.createObject(element, isSprite));
      }
    } else {
      if (this.object instanceof CSS3DObject) {
        if (!element || element && element.parentNode !== this.object.element) {
          this.objectDispose();
          this.objectMount(this.createObject(element, isSprite));
        }
      } else {
        this.objectDispose();
        this.objectMount(this.createObject(element, isSprite));
      }
    }
    this.objectUpdate();
    return this;
  }

  updateEvents() {
    super.updateEvents();
    addCss(this.object.element, { pointerEvents: this.enableEvent ? "auto" : "none" });
  }

  createObject(element, isSprite = false) {
    const elWrap = document.createElement("div");
    addCss(elWrap, { cursor: "pointer" });
    elWrap.addEventListener("click", e => e.stopPropagation());
    element && elWrap.appendChild(element);
    setTimeout(() => addCss(elWrap, { pointerEvents: this.enableEvent ? "auto" : "none" }));
    return new (isSprite ? CSS3DSprite : CSS3DObject)(elWrap);
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
