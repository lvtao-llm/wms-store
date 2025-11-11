import { mergeDeep, release } from "@vci/helper/src/object";
import { Box3, Box3Helper, Vector3 } from "three";
import { Easing, remove, Tween } from "@tweenjs/tween.js";
import { clearScheduledTasks } from "@vci/helper/src/browser";
import { uuid } from "@vci/helper/src/string";
import MaterialPatch from "../helper/MaterialPatch";
import { isEmpty } from "@vci/helper/src/other";
import EventDispatcher from "@vci/helper/src/EventDispatcher";
import O3DisposerHelper from "../helper/O3DisposerHelper";
import { ThingEvents } from "../events/ThingEvents";
// 核心
export default class Thing extends EventDispatcher {
  // mode-获取中心点的模式 cc-中段中心  cb-底部中心  ct-顶部中心
  static CenterMode = {
    "CenterTop": "ct",
    "Center": "cc",
    "CenterBottom": "cb"
  };

  constructor(option) {
    super();
    this.option = mergeDeep({
      id: uuid(),
      name: "物体",
      extData: null,
      // 模型相关
      object: null,
      position: [0, 0, 0],
      scale: [1, 1, 1],
      rotation: [0, 0, 0],
      renderOrder: null,
      // 功能性配置
      enableEvent: true,
      enableEdit: true,
      enableUpdateMatrix: true,
      enableEditUpdateOptionPosition: true,
      enableUpdateRenderOrderDeep: true,
      enableBoxHelper: false,
      enableDisposeO3OnUpdate: true,
      // 功能性配置|销毁
      isImmortal: false,
      enableDisposeO3OnDestroy: true,
      enableRemoveO3FromParentOnDestroy: true, // 仅当enableDisposeO3OnDestroy=false时生效
      // 功能性配置|qt.updateThings
      enableRemovedWhenDestroyOnUpdate: true,
      enableFadeOutWhenDestroyOnUpdate: false,
      // 初始化后不可变的配置，调用update更新后也无效
      qt: null,
      parentObject: null,
      enableCheckObject: true,
      enableAddToThingPool: true,
      enableAddObjectToParent: true
    }, option);
    this.init();
    if (this.option.enableCheckObject && !this.object) throw new Error(this.qt.log.format(`绑定模型不存在，请检查: ${this.object}`));
    this.object && this.objectMount(this.object);
    this.update();
    this.afterInstantiation();
  }

  // 初始化
  init() {
    this.isThing = true;
    this.visible = true;
    this.isDestroyed = false;
    this.inter = {};
    this.tw = {};
    this.alias();
    if (!this.qt) throw new Error("请传入QuickThree实例");
    if (!this.parentObject) this.parentObject = this.qt.scene;
    if (this.option.enableAddObjectToParent && !this.parentObject) throw new Error("物体必须有程载容器, Group|Scene|Object3D");
    this.box = new Box3();
    this.size = new Vector3();
    this.center = new Vector3();
    this.option.enableAddToThingPool && this.qt.things.push(this);
    this.cursor = { hover: null };
  }

  alias() {
    Object.defineProperty(this, "id", {
      get: () => this.option.id,
      set: id => { this.option.id = id; },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "name", {
      get: () => this.option.name,
      set: name => { this.option.name = name; },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "object", {
      get: () => this.option.object,
      set: object => { this.option.object = object; },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "objectFlat", {
      get() {
        const o3s = [];
        this.object.traverse(o => (o.isMesh || o.isLine) && o3s.push(o));
        return o3s;
      },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "position", {
      get: () => this.option.position,
      set: position => {
        this.option.position = position;
        this.object && this.option.enableUpdateMatrix && this.object.position.fromArray(position);
      },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "rotation", {
      get: () => this.option.rotation,
      set: rotation => {
        this.option.rotation = rotation;
        this.object && this.option.enableUpdateMatrix && this.object.rotation.fromArray(rotation);
      },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "scale", {
      get: () => this.option.scale,
      set: scale => {
        this.option.scale = scale;
        this.object && this.option.enableUpdateMatrix && this.object.scale.fromArray(scale);
      },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "parentObject", {
      get: () => this.option.parentObject,
      set: parentObject => { this.option.parentObject = parentObject; },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "extData", {
      get: () => this.option.extData || {},
      set: extData => { this.option.extData = extData; },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "qt", {
      get: () => this.option.qt,
      set: qt => { this.option.qt = qt; },
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "isImmortal", {
      get: () => this.option.isImmortal,
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "enableEdit", {
      get: () => this.option.enableEdit,
      enumerable: true,
      configurable: true
    });
    Object.defineProperty(this, "enableEvent", {
      get: () => this.option.enableEvent,
      enumerable: true,
      configurable: true
    });
  }

  // 构造函数运行完成后
  afterInstantiation() {
  }

  // 更新配置
  update(option) {
    option && mergeDeep(this.option, option);
    // 更新模型基本状态
    this.objectUpdate();
    // 辅助盒子
    this.updateBoxHelper();
    // 更新事件事物
    this.updateEvents();
    return this;
  }

  // 挂载模型
  objectMount(object) {
    if (!object) throw new Error(this.qt.log.format(`绑定模型不存在，请检查: ${object}`));
    if (!object.isObject3D) throw new Error(this.qt.log.format(`绑定模型不是Object3D的实例，请检查: ${object}`));
    if (this.object && this.object !== object) this.objectDetach();
    this.object = object;
    // 添加模型到父级
    this.option.enableAddObjectToParent && this.parentObject.add(this.object);
    // 注入属性到模型userData
    this.objectEmpowerment();
    return this;
  }

  objectDetach() {
    this.objectDeleteEmpowerment();
    this.option.enableDisposeO3OnUpdate && O3DisposerHelper.DisposeObject3D(this.object, this.parentObject);
    this.object = null;
    return this;
  }

  objectUpdate() {
    // 定位|缩放|方位
    if (this.option.enableUpdateMatrix) {
      this.position = this.option.position;
      this.scale = this.option.scale;
      this.rotation = this.option.rotation;
    }
    // 渲染顺序
    this.objectUpdateRenderOrder();
    // 更新事物中心点
    this.updateCenter();
    return this;
  }

  // 注入属性到模型userData
  objectEmpowerment() {
    if (this.object) {
      this.object.thing = this;
      this.object.isThing = true;
      this.object.userData = this.option;
    }
    return this;
  }

  // 删除注入到模型userData的属性
  objectDeleteEmpowerment() {
    if (this.object) {
      delete this.object.thing;
      delete this.object.isThing;
      delete this.object.userData;
    }
    return this;
  }

  // 更新模型渲染顺序
  objectUpdateRenderOrder() {
    const { enableUpdateRenderOrderDeep } = this.option;
    const renderOrder = Number(this.option.renderOrder);
    if (this.object && !isNaN(renderOrder) && !isEmpty(this.option.renderOrder)) {
      this.object.renderOrder = Number(renderOrder);
      enableUpdateRenderOrderDeep && this.object.traverse(o3 => o3.renderOrder = renderOrder);
    }
    return this;
  }

  // 更新包围盒
  updateBox() {
    if (this.object) {
      this.box.setFromObject(this.object);
      this.box.getSize(this.size);
      // 更新辅助包围盒
      this.boxHelper && this.boxHelper.updateMatrixWorld();
    }
    return this;
  }

  // 更新中心点
  updateCenter() {
    if (this.object) {
      this.updateBox();
      this.box.getCenter(this.center);
    }
    return this;
  }

  // 获取中心点 mode-CenterMode
  getCenter(mode = Thing.CenterMode.Center) {
    this.updateCenter();
    const center = new Vector3();
    switch (mode) {
      case Thing.CenterMode.CenterBottom:
        center.set(
          this.center.x,
          this.center.y - this.size.y / 2,
          this.center.z
        );
        break;
      case Thing.CenterMode.Center:
        center.copy(this.center);
        break;
      case Thing.CenterMode.CenterTop:
        center.set(
          this.center.x,
          this.center.y + this.size.y / 2,
          this.center.z
        );
        break;
    }
    return center;
  }

  // 获取尺寸
  getSize() {
    const _rotation = this.object.rotation.clone();
    this.object.rotation.set(0, 0, 0);
    this.updateBox();
    const size = this.size.clone();
    this.object.rotation.copy(_rotation);
    this.updateBox();
    return size;
  }

  // 更新辅助包围盒
  updateBoxHelper() {
    const { enableBoxHelper } = this.option;
    if (enableBoxHelper) {
      if (!this.boxHelper) {
        this.boxHelper = new Box3Helper(this.box, "#ffff00");
        this.parentObject.add(this.boxHelper);
      }
      this.boxHelper.visible = true;
    } else this.boxHelper && (this.boxHelper.visible = false);
  }

  // 更新事件支持
  updateEvents() {
    this.qt.helper.updateEventO3s();
  }

  // 整个场景发生改变的对应处理
  matrixChange(isWindowResize = false, handle) {
    clearTimeout(this.inter.matrixChange);
    const matrixChangeHandle = () => handle && handle();
    if (isWindowResize) this.inter.matrixChange = setTimeout(() => matrixChangeHandle(), 200);
    else matrixChangeHandle();
    return this;
  }

  // 动画 delta 动画帧
  animate() {
    this.updateBox();
  }

  modifyShadowState(enabled = false) {
    this.object && this.object.traverse(o => o.castShadow = o.receiveShadow = enabled);
    return this;
  }

  /**
   * 显示模型
   * @param enableModifyMaterial  是否只改变模型的材质
   * @returns {Thing}
   */
  show(enableModifyMaterial = true) {
    this.visible = true;
    this.qt.helper.updateEventO3s();
    const o3 = this.object;
    o3.visible = true;
    this.modifyShadowState(true);
    enableModifyMaterial && MaterialPatch.ModifyMtlProperties(o3, { transparent: true, opacity: 1 });
    return this;
  }

  /**
   * 隐藏模型
   * @param enableModifyMaterial  是否只改变模型的材质
   * @returns {Thing}
   */
  hide(enableModifyMaterial = true) {
    this.visible = false;
    this.qt.helper.updateEventO3s();
    const o3 = this.object;
    o3.visible = false;
    this.modifyShadowState(false);
    enableModifyMaterial && MaterialPatch.ModifyMtlProperties(o3, { transparent: true, opacity: 0 });
    return this;
  }

  o3Show(object) {
    const o3 = object || this.object;
    o3 && (o3.visible = true);
    return this;
  }

  o3Hide(object) {
    const o3 = object || this.object;
    o3 && (o3.visible = false);
    return this;
  }

  // 动画显示模型
  async fadeIn(tweenOption = {}) {
    return this.fadeTo(1, tweenOption);
  }

  async fadeTo(opacity = .5, tweenOption) {
    this.visible = true;
    const o3 = this.object;
    o3.visible = true;
    this.modifyShadowState(true);
    MaterialPatch.ModifyMtlProperties(o3, { transparent: true });
    await this.animationModifyMtlProperties({ opacity }, tweenOption);
    this.qt.helper.updateEventO3s();
    return this;
  }

  // 动画隐藏模型
  async fadeOut(tweenOption = {}) {
    this.visible = false;
    this.qt.helper.updateEventO3s();
    const o3 = this.object;
    MaterialPatch.ModifyMtlProperties(o3, { transparent: true });
    await this.animationModifyMtlProperties({ opacity: 0 }, tweenOption);
    this.modifyShadowState(false);
    o3.visible = false;
    return this;
  }

  resetMtlProps(props = []) {
    MaterialPatch.ResetMtlProperties(this.object, props);
    return this;
  }

  /**
   * 实例化的动画更改材质属性
   * @param targets       目标属性对象 例: { opacity: 1 }
   * @param tweenOption     Tween配置         Object
   * @returns {Promise<Thing>}
   */
  async animationModifyMtlProperties(targets, tweenOption = {}) {
    await MaterialPatch.AnimationModifyMtlProperties(
      this.object,
      targets,
      tweenOption,
      this.tw
    );
    return this;
  }

  // 唤醒 使模型能够触发事件以及编辑
  wakeUp() {
    this.option.enableEdit = true;
    this.option.enableEvent = true;
    this.updateEvents();
    return this;
  }

  // 休眠 使模型不能够触发事件以及编辑
  sleep() {
    this.option.enableEdit = false;
    this.option.enableEvent = false;
    this.updateEvents();
    return this;
  }

  // 移动
  move(vec3 = new Vector3(), twOption) {
    const { duration, delay, easing } = mergeDeep({
      duration: 1e3,
      delay: 0,
      easing: Easing.Linear.None
    }, twOption);
    const start = new Vector3().copy(this.object.position);
    const end = new Vector3().copy(start).add(vec3);
    return new Promise(resolve => {
      this.tw.move && remove(this.tw.move);
      this.tw.move = new Tween(start)
        .to(end)
        .onStart(() => {
          if (!this.option.enableUpdateMatrix) {
            this.option.enableUpdateMatrix = true;
            this._oeu = true;
          }
        })
        .duration(duration)
        .delay(delay)
        .easing(easing)
        .onUpdate(e => this.position = [e.x, e.y, e.z])
        .onComplete(() => {
          if (this._oeu) {
            this.option.enableUpdateMatrix = false;
            delete this._oeu;
          }
          resolve();
        })
        .start();
    });
  }

  // 销毁
  destroy(force = false, enableCr = true) {
    if (this.isDestroyed || this.isImmortal && !force) return this;
    // 标记已销毁
    this.isDestroyed = true;
    this.dispatchEvent(ThingEvents.Destroyed);
    // 清除定时器
    clearScheduledTasks(this.inter);
    // 移除Tween实例
    Object.keys(this.tw).forEach(k => {
      if (this.tw[k]) {
        remove(this.tw[k]);
        delete this.tw[k];
      }
    });
    // 销毁boxHelper
    this.boxHelper && O3DisposerHelper.DisposeObject3D(this.boxHelper, this.parentObject);
    // 销毁当前模型
    this.objectDeleteEmpowerment();
    if (this.object)
      if (this.option.enableDisposeO3OnDestroy) O3DisposerHelper.DisposeObject3D(this.object, this.parentObject);
      else this.option.enableRemoveO3FromParentOnDestroy && this.parentObject.remove(this.object);
    if (enableCr) {
      // 清除已经销毁的事物实例
      this.qt.clearDestroyedThings();
      // 清除对象属性
      release(this, /^is\s*/);
    }
  }
}
