import { mergeDeep } from "@vci/helper/src/object";
import { Easing, remove, Tween } from "@tweenjs/tween.js";

export default class MaterialPatch {
  /**
   * 遍历属性材质
   * @param o3      模型实例            Object3D
   * @param call    每个材质的回调函数    Function
   */
  static TraverseMaterials(o3, call) {
    const materials = [];
    o3.traverse(o => o.material && materials.push(...(Array.isArray(o.material) ? o.material : [o.material]).map(material => ({
      material,
      object: o
    }))));
    const materialsRepeat = [];
    materials.forEach(material => !materialsRepeat.some(mr => mr.material.uuid === material.material.uuid) && materialsRepeat.push(material));
    materialsRepeat.forEach(material => call && call(material.material, material.object));
  }

  /**
   *
   * 修改模型材质属性
   * @param o3        模型实例      Object3D
   * @param targets   材质目标属性及值  Object  例: { transparent: true, opacity: 1 }
   * @returns Thing
   * @constructor
   */
  static ModifyMtlProperties(o3, targets) {
    MaterialPatch.TraverseMaterials(o3, material => Object.keys(targets).forEach(prop => {
      material[`origin-${prop}`] === undefined && (material[`origin-${prop}`] = material[prop]);
      material[prop] = targets[prop];
    }));
  }

  /**
   * 动画更新材质属性
   * @param o3              模型实例          Object3D
   * @param targets   材质目标属性及值  Object  例: { transparent: true, opacity: 1 }
   * @param tweenOption     Tween配置         Object
   * @param tw              tw容器            Object
   */
  static AnimationModifyMtlProperties(o3, targets, tweenOption, tw) {
    if (!targets || !o3) {
      !targets && console.warn("缺少targets");
      !o3 && console.warn("缺少o3");
      return Promise.resolve();
    } else {
      tweenOption = mergeDeep({
        duration: 800,
        delay: 0,
        repeat: 0,
        yoyo: false,
        easing: Easing.Quintic.InOut,
        onStart: null,
        onUpdate: null,
        onComplete: null
      }, tweenOption);
      tweenOption.next = false;
      return new Promise(resolve => {
        MaterialPatch.TraverseMaterials(o3, material => Object.keys(targets).forEach(prop => {
          const materialTwKey = `${material.uuid}-${prop}`;
          const targetValue = Number(targets[prop]);
          const isOpacity = prop === "opacity";
          if (typeof material[prop] === "number") {
            // if (typeof material[prop] === "number" && targetValue.toFixed(2) !== material[prop].toFixed(2)) {
            material[`origin-${prop}`] === undefined && (material[`origin-${prop}`] = material[prop]);
            const isUpper = targetValue >= material[prop];
            const targetValueEnd = isOpacity && isUpper ? Math.min(targetValue, material[`origin-${prop}`]) : targetValue;
            tw[materialTwKey] && remove(tw[materialTwKey]);
            tw[materialTwKey] = new Tween(material)
              .to({ [prop]: targetValueEnd })
              .duration(tweenOption.duration)
              .easing(tweenOption.easing)
              .delay(tweenOption.delay)
              .repeat(tweenOption.repeat)
              .yoyo(tweenOption.yoyo)
              .onStart(e => tweenOption.onStart && tweenOption.onStart(e))
              .onUpdate((e, p) => {
                material.needsUpdate = true;
                tweenOption.onUpdate && tweenOption.onUpdate(e, p);
              })
              .onComplete(e => {
                remove(tw[materialTwKey]);
                // material[prop] = targetValueEnd;
                tweenOption.onComplete && tweenOption.onComplete(e);
                // 材质透明度属性特殊处理
                if (isOpacity && isUpper && targetValueEnd >= 1 && material[`origin-transparent`] !== undefined) material.transparent = material[`origin-transparent`];
                if (!tweenOption.next) {
                  tweenOption.next = true;
                  resolve();
                }
              })
              .start();
          } else {
            material[prop] = targets[prop];
            // 材质透明度属性特殊处理
            if (isOpacity && material[`origin-transparent`] !== undefined) material.transparent = material[`origin-transparent`];
            tweenOption.onStart && tweenOption.onStart(material);
            tweenOption.onUpdate && tweenOption.onUpdate(material, 1);
            tweenOption.onComplete && tweenOption.onComplete(material);
            if (!tweenOption.next) {
              // tweenOption.next = true;
              // resolve();
              console.warn("请不要在AnimationModifyMtlProperties中传入非数字的属性进行变化");
            }
          }
        }));
      });
    }
  }

  static ResetMtlProperties(o3, props = []) {
    MaterialPatch.TraverseMaterials(o3, material => props.forEach(prop => material[`origin-${prop}`] !== undefined && (material[prop] = material[`origin-${prop}`])));
  }
}
