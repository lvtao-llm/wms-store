import Vue from "vue";

/**
 * 生成组件Vue实例
 * @param component
 * @param option
 */
function instantiateComponentVm(component, option = {}) {
  return new (Vue.extend(component))(option).$mount();
}
export {
  instantiateComponentVm
};
