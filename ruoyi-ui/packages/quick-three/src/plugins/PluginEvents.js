import { addCss } from "@vci/helper/src/element";
import HandClose from "../../assets/cursor/closedhand.cur";
import HandOpen from "../../assets/cursor/openhand.cur";
import { Raycaster, Vector2, Vector3 } from "three";
import { ThingEvents } from "../events/ThingEvents";
import { QtPlugin } from "./QtPlugin";
import { mergeDeep } from "@vci/helper/src/object";
import { QtEvents } from "../events/QtEvents";

class PluginEvents extends QtPlugin {
  static namespace = "events";
  static Events = {
    ClickThing: QtEvents.ClickThing = "ClickThing",
    DblclickThing: QtEvents.DblclickThing = "DblclickThing",
    InThing: QtEvents.InThing = "InThing",
    OutThing: QtEvents.OutThing = "OutThing",
    ClickEmpty: QtEvents.ClickEmpty = "ClickEmpty",
    DblClickEmpty: QtEvents.DblClickEmpty = "DblClickEmpty",
    RayCaster: QtEvents.RayCaster = "RayCaster"
  };

  constructor(qt, option) {
    super(qt, mergeDeep({
      // 是否开启双击交互，开启后可能单击会有延迟
      enableEventDblClick: false
    }, option));
  }

  init() {
    super.init();
    const { enableEventDblClick } = this.option;
    const { qt } = this;
    const el = qt.el;
    this.mouse = new Vector2();
    this.eventO3s = [];
    this.enableEventDblClick = enableEventDblClick;
    this.isDragging = false;
    this.limitMoveFn = {};
    this.limitClickFn = {};
    Object.defineProperty(this, "enableMoveFn", { get: () => Object.keys(this.limitMoveFn).length < 1 });
    Object.defineProperty(this, "enableClickFn", { get: () => Object.keys(this.limitClickFn).length < 1 && !this.isDragging });
    // 鼠标交互效果
    this.interactionEffectMouse();
    // 解决拖拽场景结束后，如果鼠标处于事物上，会触发点击时间的问题
    this.dragPatch();
    // 鼠标拾取物体
    const rcs = [];
    this.rayCaster = new Raycaster(new Vector3(), new Vector3(0, 0, -1));
    qt.addEventListener(QtEvents.UpdateEventO3s, e => this.eventO3s = e.detail.eventO3s);
    const inThing = (targetObject, rcs, e) => {
      this.cursorToPointer();
      this.hoverObject = targetObject;
      const event = {
        event: e,
        thing: this.hoverObject.thing,
        targetObject,
        targetRay: rcs[0],
        rcs
      };
      event.thing.cursor.hover && addCss(qt.el, { cursor: event.thing.cursor.hover });
      this.hoverObject.thing && this.hoverObject.thing.dispatchEvent(ThingEvents.HoverOn, event);
      qt.dispatchEvent(QtEvents.InThing, event);
    };
    const outThing = () => {
      const event = {
        thing: this.hoverObject.thing,
        targetObject: this.hoverObject
      };
      this.hoverObject.thing && this.hoverObject.thing.dispatchEvent(ThingEvents.HoverOff, event);
      qt.dispatchEvent(QtEvents.OutThing, event);
      this.hoverObject = null;
      this.cursorToUp();
    };
    const moveFn = e => {
      this.updateMouseProperty(e);
      if (this.enableMoveFn) {
        this.rayCaster.setFromCamera(this.mouse, qt.camera);
        rcs.length = 0;
        this.rayCaster.intersectObjects(this.eventO3s.filter(o => o.uuid), true, rcs);
        qt.dispatchEvent(QtEvents.RayCaster, { rcs });
        if (rcs.length > 0) {
          const targetObject = qt.helper.findThingO3ByO3(rcs[0].object, { checkEvent: true });
          if (!this.hoverObject) inThing(targetObject, rcs, e);
          else {
            if (this.hoverObject.userData && this.hoverObject.userData.id !== targetObject.userData.id) {
              outThing();
              inThing(targetObject, rcs, e);
            }
          }
          targetObject.thing && targetObject.thing.dispatchEvent(ThingEvents.Move, {
            event: e,
            thing: targetObject.thing,
            targetObject,
            targetRay: rcs[0],
            rcs
          });
        } else this.hoverObject && outThing();
      }
    };
    el.addEventListener("mousemove", moveFn);
    el.addEventListener("touchstart", moveFn);
    // 投射事物
    const rayThing = (mouse, call, isDblClick = false) => {
      this.rayCaster.setFromCamera(mouse, qt.camera);
      rcs.length = 0;
      this.rayCaster.intersectObjects(this.eventO3s.filter(o => o.uuid), true, rcs);
      if (qt.debug) console.info("rayCaster: ", rcs[0] || "暂无");
      qt.dispatchEvent(QtEvents.RayCaster, { rcs });
      if (rcs.length > 0) {
        const targetObject = qt.helper.findThingO3ByO3(rcs[0].object, { checkEvent: true });
        targetObject && call && call(targetObject, rcs);
      } else qt.dispatchEvent(isDblClick ? QtEvents.DblClickEmpty : QtEvents.ClickEmpty);
    };
    const clickFn = e => {
      this.updateMouseProperty(e);
      if (this.enableClickFn) {
        const mouse = new Vector2().copy(this.mouse);
        clearTimeout(qt.inter.click);
        qt.inter.click = setTimeout(() => rayThing(
          mouse,
          (object, rcs) => {
            const event = {
              event: e,
              thing: object.thing,
              targetObject: object,
              targetRay: rcs[0],
              rcs
            };
            object.thing && object.thing.dispatchEvent(e.type === "contextmenu" ? ThingEvents.ContextMenu : ThingEvents.Click, event);
            qt.dispatchEvent(QtEvents.ClickThing, event);
          }
        ), this.enableEventDblClick ? 200 : 100);
      }
    };
    el.addEventListener("click", e => clickFn(e));
    el.addEventListener("contextmenu", e => clickFn(e));
    el.addEventListener("touchend", e => clickFn(e));
    // 双击选中物体
    el.addEventListener("dblclick", e => {
      if (!this.enableEventDblClick) return false;
      this.updateMouseProperty(e);
      if (this.enableClickFn) {
        const mouse = new Vector2().copy(this.mouse);
        clearTimeout(qt.inter.click);
        qt.inter.click = setTimeout(() => rayThing(
          mouse,
          (object, rcs) => {
            const event = {
              event: e,
              thing: object.thing,
              targetObject: object,
              targetRay: rcs[0],
              rcs
            };
            object.thing && object.thing.dispatchEvent(ThingEvents.Dblclick, event);
            qt.dispatchEvent(QtEvents.DblclickThing, event);
          },
          true
        ), 200);
      }
    });
  }

  interactionEffectMouse() {
    const el = this.qt.el;
    this.cursorToUp();
    el.addEventListener("pointerdown", () => !this.hoverObject && this.cursorToDown());
    el.addEventListener("pointerup", () => !this.hoverObject && this.cursorToUp());
  }

  dragPatch() {
    const { qt } = this;
    const el = qt.el;
    const dragPatchFn = e => {
      const isTouchMode = e.touches;
      if (isTouchMode) {
        e.clientX = e.touches[0].clientX;
        e.clientY = e.touches[0].clientY;
      }
      clearTimeout(qt.inter.eventsDragging);
      this.isDragging = false;
      const v2Start = new Vector2(e.clientX, e.clientY);
      const v2Move = new Vector2(e.clientX, e.clientY);
      const moveFn = em => {
        this.isDragging = true;
        if (isTouchMode) {
          em.clientX = em.touches[0].clientX;
          em.clientY = em.touches[0].clientY;
        }
        v2Move.set(em.clientX, em.clientY);
      };
      const upFn = () => {
        if (qt.isDestroyed) return false;
        if (v2Start.distanceTo(v2Move) < 2) this.isDragging = false;
        else qt.inter.eventsDragging = setTimeout(() => this.isDragging = false, .2e3);
        !isTouchMode && window.removeEventListener("mousemove", moveFn);
        !isTouchMode && window.removeEventListener("mouseup", upFn);
        isTouchMode && window.removeEventListener("touchmove", moveFn);
        isTouchMode && window.removeEventListener("touchend", upFn);
      };
      !isTouchMode && window.addEventListener("mousemove", moveFn);
      !isTouchMode && window.addEventListener("mouseup", upFn);
      isTouchMode && window.addEventListener("touchmove", moveFn);
      isTouchMode && window.addEventListener("touchend", upFn);
    };
    el.addEventListener("mousedown", e => dragPatchFn(e));
    el.addEventListener("touchstart", e => dragPatchFn(e));
  }

  setLimitMoveFn(property) {
    this.limitMoveFn[property] = true;
  }

  deleteLimitMoveFn(property) {
    delete this.limitMoveFn[property];
  }

  setLimitClickFn(property) {
    this.limitClickFn[property] = true;
  }

  deleteLimitClickFn(property) {
    delete this.limitClickFn[property];
  }

  cursorToDown() {
    const { qt } = this;
    addCss(qt.el, { cursor: `url(${HandClose}), default` });
  }

  cursorToUp() {
    const { qt } = this;
    addCss(qt.el, { cursor: `url(${HandOpen}), default` });
  }

  cursorToPointer() {
    const { qt } = this;
    addCss(qt.el, { cursor: `pointer` });
  }

  cursorToDefault() {
    const { qt } = this;
    addCss(qt.el, { cursor: `default` });
  }

  updateMouseProperty(event) {
    const { qt } = this;
    const el = qt.el;
    const isTouchMode = event.touches;
    const e = {
      x: (isTouchMode ? event.touches[0].offsetX : event.offsetX),
      y: (isTouchMode ? event.touches[0].offsetY : event.offsetY)
    };
    this.mouse.set(
      (e.x / el.clientWidth) * 2 - 1,
      -(e.y / el.clientHeight) * 2 + 1
    );
  }
}

export { PluginEvents };
