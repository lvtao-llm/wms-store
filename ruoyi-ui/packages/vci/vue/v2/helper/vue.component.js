import Vue from "vue";

/**
 * 生成组件Vue实例
 * @param component
 * @param option
 */
function instantiateComponentVm(component, option = {}) {
  return new (Vue.extend(component))(option).$mount();
}
/**
 * 合并对象 - 渲染函数组件
 * @param props     原属性
 * @param target    传入属性
 */
function mergeForRenderFn(props, target) {
  Object.keys(props).forEach(key => {
    if ([undefined, null].includes(target[key])) target[key] = props[key];
  });
  return target;
}
export {
  instantiateComponentVm,
  mergeForRenderFn
};
