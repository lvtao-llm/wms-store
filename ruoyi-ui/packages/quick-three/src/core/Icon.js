import Thing from "./Thing";
import { mergeDeep } from "@vci/helper/src/object";
import {
  Mesh,
  MeshBasicMaterial,
  SphereGeometry,
  Sprite,
  SpriteMaterial,
  SRGBColorSpace,
  TextureLoader,
  Vector3
} from "three";
import { toVw } from "@vci/vci/other/adapt";
import { addCss } from "@vci/helper/src/element";
import { getScreenCoordsFromWorldCoords } from "../helper/ThingHelper";
import { ThingEvents } from "../events/ThingEvents";
import { remove, Tween } from "@tweenjs/tween.js";
import O3DisposerHelper from "../helper/O3DisposerHelper";
// 图标
export default class Icon extends Thing {
  constructor(option) {
    super(mergeDeep({
      enableCheckObject: false,
      url: null, // 图标资源路径
      size: [10, 10], // 图标大小
      isDomMode: false,
      enableDepth: true
    }, option));
  }

  init() {
    super.init();
    this.isThingIcon = true;
  }

  update(option) {
    super.update(option);
    const { size, url, isDomMode, enableDepth, enableEvent } = this.option;
    if (!isDomMode) {
      if (this.elImg) {
        if (this.object.parent) O3DisposerHelper.DisposeObject3D(this.object, this.object.parent);
        else O3DisposerHelper.DisposeObject3DHandle(this.object);
        this.object = null;
        this.qt.el.removeChild(this.elImg);
        this.elImg = null;
      }
      if (!this.object) {
        const spriteMtl = new SpriteMaterial({
          color: "#fff",
          depthWrite: enableDepth,
          depthTest: enableDepth,
          alphaTest: 0.001,
          toneMapped: false
        });
        const o3Sprite = new Sprite(spriteMtl);
        o3Sprite.center.setY(0);
        this.objectMount(o3Sprite);
        this.addEventListener(ThingEvents.HoverOn, () => {
          if (this.tw.hover) remove(this.tw.hover);
          this.tw.hover = new Tween({ scale: 1 })
            .to({
              scale: 1.1
            }, 200)
            .onUpdate(e => {
              o3Sprite.scale.set(size[0] * e.scale, size[1] * e.scale, 1);
            })
            .start();
        });
        this.addEventListener(ThingEvents.HoverOff, () => {
          if (this.tw.hover) remove(this.tw.hover);
          this.tw.hover = new Tween({ scale: 1.1 })
            .to({
              scale: 1
            }, 200)
            .onUpdate(e => {
              o3Sprite.scale.set(size[0] * e.scale, size[1] * e.scale, 1);
            })
            .start();
        });
      }
      this.option.scale = [size[0], size[1], 1];
      this.objectUpdate();
      const material = this.object.material;
      if (url) {
        if (this.urlCache !== url) {
          this.urlCache = url;
          material.map = new TextureLoader(this.qt.loadingManager).load(url);
          material.map.colorSpace = SRGBColorSpace;
          material.needsUpdate = true;
        }
      } else {
        this.urlCache = url;
        material.map = null;
        material.needsUpdate = true;
      }
      this.qt.helper.updateEventO3s();
    } else {
      if (!this.elImg) {
        if (this.object) {
          if (this.object.parent) O3DisposerHelper.DisposeObject3D(this.object, this.object.parent);
          else O3DisposerHelper.DisposeObject3DHandle(this.object);
          const _enableEvent = this.option.enableEvent;
          this.option.enableEvent = false;
          this.qt.helper.updateEventO3s();
          this.option.enableEvent = _enableEvent;
          this.object = null;
        }
        this.elImg = new Image();
        addCss(this.elImg, {
          position: "absolute",
          left: "0",
          top: "0",
          zIndex: 2,
          display: "block",
          cursor: "pointer",
          "user-select": "none",
          transition: "opacity .35s ease-in-out",
          opacity: 0,
          transform: `translate(-50%,-100%)`
        });
        this.qt.el.appendChild(this.elImg);
        this.objectMount(new Mesh(
          new SphereGeometry(0),
          new MeshBasicMaterial({ transparent: true, opacity: 0 })
        ));
        this.qt.helper.updateEventO3s();
        this.bindEventForDomMode();
        this.fadeIn();
      }
      addCss(this.elImg, {
        width: toVw(size[0]),
        height: toVw(size[1]),
        cursor: enableEvent ? "pointer" : "none"
      });
      this.elImg.getAttribute("src") !== url && this.elImg.setAttribute("src", url);
      setTimeout(this.syncPositionForDomMode.bind(this));
    }
    return this;
  }

  bindEventForDomMode() {
    const { isDomMode } = this.option;
    if (!isDomMode) {
      this.qt.log.warn("not domMode, can`t bind event");
      return false;
    }
    this.elImg.addEventListener("click", e => {
      if (this.option.enableEvent) {
        e.stopPropagation();
        if (this.qt.debug) console.info("rayCaster: ", this);
        this.dispatchEvent(ThingEvents.Click);
      }
    });
    this.elImg.addEventListener("mouseenter", () => {
      this.option.enableEvent && this.dispatchEvent(ThingEvents.HoverOn);
      this.qt.editor && this.qt.editor.isEdit && this.option.enableEdit && this.qt.editor.editThing(this.id);
    });
    this.elImg.addEventListener("mouseleave", () => this.option.enableEvent && this.dispatchEvent(ThingEvents.HoverOff));
    this.elImg.addEventListener("contextmenu", e => {
      e.preventDefault();
      this.option.enableEvent && this.dispatchEvent(ThingEvents.ContextMenu);
    });
    this.elImg.addEventListener("wheel", e => {
      e.preventDefault();
      e.stopPropagation();
      this.dispatchEvent(ThingEvents.Wheel, { originEvent: e });
    });
  }

  matrixChange(isWindowResize = false, handle) {
    return super.matrixChange(isWindowResize, () => {
      handle && handle();
      if (this.option.isDomMode) {
        const isInCameraView = this.qt.helper.isInCameraView(this.option.position, this.qt.camera);
        this.visible && addCss(this.elImg, { opacity: isInCameraView ? 1 : 0 });
        isInCameraView && this.syncPositionForDomMode();
      }
    });
  }

  syncPositionForDomMode() {
    const { left, top } = this.getPositionForDomMode();
    this.visible && addCss(this.elImg, { transform: `translate(-50%,-100%) matrix3d(1,0,0,0,0,1,0,0,0,0,1,0,${left},${top},0,1)` });
  }

  getPositionForDomMode() {
    return getScreenCoordsFromWorldCoords(this.option.position, this.qt.camera, this.qt.el);
  }

  getCenter(mode = Thing.CenterMode.Center) {
    const { isDomMode, position } = this.option;
    if (!isDomMode) return super.getCenter(mode);
    else return new Vector3(...position);
  }

  fadeIn(tweenOption) {
    this.option.isDomMode && addCss(this.elImg, {
      opacity: 1,
      pointerEvents: this.option.enableEvent ? "auto" : "none"
    });
    return super.fadeIn(tweenOption);
  }

  fadeOut(tweenOption) {
    this.option.isDomMode && addCss(this.elImg, {
      opacity: 0,
      pointerEvents: "none"
    });
    return super.fadeOut(tweenOption);
  }

  destroy(force = false) {
    const { isDomMode } = this.option;
    isDomMode && this.qt.el.removeChild(this.elImg);
    return super.destroy(force);
  }
}
