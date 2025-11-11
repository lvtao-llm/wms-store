import { camelCaseToHyphen } from "./string";

/**
 * 判断元素是否为一个HTML标签元素
 * @param element
 */
const PREFIX = ["-webkit-", "-ms-", "-moz-", "-o-"];
/**
 * 是否为一个普通的HTML元素
 * @param element
 */
function isElement(element) {
  let validate = element && element.nodeType === 1;
  if (!validate) console.warn(`${element} 不是一个HTML元素!`);
  return validate;
}
// 获取Html元素
function getElementHtml() {
  return document.querySelector("html");
}
// 获取Body元素
function getElementBody() {
  return document.querySelector("body");
}
/**
 * 移除类名
 * @param element
 * @param classAdd
 * @returns {*}
 */
function addClass(element, classAdd) {
  element.classList.add(classAdd);
  return element;
}
/**
 * 移除类名
 * @param element
 * @param classRemove
 * @returns {*}
 */
function removeClass(element, classRemove) {
  element.classList.remove(classRemove);
  return element;
}
/**
 * 添加样式
 * @param element
 * @param properties
 * @param addPrefix
 */
function addCss(element, properties = {}, addPrefix = true) {
  Object.keys(properties).forEach(key => {
    element.style[key] = properties[key];
    addPrefix && PREFIX.forEach(p => element.style[`${p}${camelCaseToHyphen(key)}`] = properties[key]);
  });
  return element;
}
/**
 * 移出样式属性
 * @param element
 * @param properties
 * @returns {*}
 */
function removeCss(element, properties = []) {
  properties.forEach(property => {
    element.style[property] = "";
    PREFIX.forEach(p => element.style[`${p}${camelCaseToHyphen(property)}`] = "");
  });
  return element;
}
/**
 * 获取样式属性值
 * @param element
 * @param property
 * @returns {*}
 */
function getElementProperty(element, property) {
  return window.getComputedStyle(element, null)[property];
}
/**
 * 获取样式属性值|浮点数格式
 * @param element
 * @param property
 * @returns {number}
 */
function getElementPropertyOfNumber(element, property) {
  return parseFloat(getElementProperty(element, property).replace("px", ""));
}
/**
 * 得到目标元素下最大层级
 * @param element  默认为document.body
 */
function getElementMaxZIndex(element = getElementBody()) {
  if (isElement(element)) {
    const poolZIndex = [];
    Array.from(element.childNodes).forEach((node) => {
      if (node.nodeType === 1) {
        const zIndex = getElementPropertyOfNumber(node, "zIndex");
        if (!isNaN(zIndex)) poolZIndex.push(zIndex);
      }
    });
    return (poolZIndex.length < 1 ? 0 : Math.max(...poolZIndex)) + 1;
  } else return 0;
}
/**
 * 获取当前元素所在屏幕未知
 * @param element
 * @returns {{top: number, left: number}}
 */
function getElementOffset(element) {
  function fn(element) {
    const offset = {
      left: element.offsetLeft - element.scrollLeft,
      top: element.offsetTop - element.scrollTop
    };
    if (element.offsetParent) {
      const op = fn(element.offsetParent);
      offset.left += op.left;
      offset.top += op.top;
    }
    return offset;
  }
  return fn(element);
}
export {
  isElement,
  addClass,
  removeClass,
  addCss,
  removeCss,
  getElementHtml,
  getElementBody,
  getElementProperty,
  getElementPropertyOfNumber,
  getElementMaxZIndex,
  getElementOffset
};