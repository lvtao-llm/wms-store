// 是否基础类型 number|boolean|string|null|undefined|Symbol|BigInt
function isFoundationType(arg) {
  return arg === null || (typeof arg !== "object" && typeof arg !== "function");
}
// 是否引用类型
function isReferenceType(arg) {
  return arg !== null && (typeof arg === "object" || typeof arg === "function");
}
// 是否需要合并的类型
function isMergeType(arg) {
  return isReferenceType(arg) && (arg.constructor === Object || arg.constructor === Array);
}
/**
 * 对象合并
 * @param origin    源对象
 * @param sources   目标对象
 * @returns {*}
 */
function mergeDeep(origin, ...sources) {
  isFoundationType(origin) && (origin = {});
  sources.forEach(source => {
    !isFoundationType(source) && Object.entries(source).forEach(([key, value]) => {
      if (isMergeType(value)) {
        (!origin[key] || isFoundationType(origin[key])) && Object.assign(origin, { [key]: Array.isArray(value) ? [] : {} });
        mergeDeep(origin[key], value);
      } else Object.assign(origin, { [key]: value });
    });
  });
  return origin;
}
/**
 * 深拷贝
 * @param origin
 */
function cloneDeep(origin) {
  if (!isMergeType(origin)) return origin;
  else {
    const target = Array.isArray(origin) ? [] : {};
    Object.entries(origin).forEach(([key, value]) => {
      if (isMergeType(value)) target[key] = cloneDeep(value);
      else Object.assign(target, { [key]: value });
    });
    return target;
  }
}
/**
 * 深拷贝 - JSON.stringify
 * @param origin  源对象
 * @returns {{}}
 */
function cloneDeepByJSON(origin) {
  if (!isMergeType(origin)) return origin;
  else return JSON.parse(JSON.stringify(origin));
}
/**
 * 释放对象
 * @param object
 * @param regExpKey 需要保留字段的正则匹配,匹配到的字段不会被释放掉
 */
function release(object, regExpKey = null) {
  Object.keys(object || {}).forEach(key => {
    try {
      if ((regExpKey ? !new RegExp(regExpKey).test(key) : true)) {
        object[key] = null;
        delete object[key];
      }
    } catch {
      delete object[key];
    }
  });
}
/**
 * 对象转数组
 * @param object 被转换对象
 * @returns {*[]}
 */
function toArrayFromObject(object) {
  if (!isMergeType(object)) return [object];
  return Object.entries(object).map(([, value]) => value);
}
export {
  isFoundationType,
  isReferenceType,
  mergeDeep,
  cloneDeep,
  cloneDeepByJSON,
  release,
  toArrayFromObject
};
