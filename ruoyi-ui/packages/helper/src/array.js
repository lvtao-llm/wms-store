// 打乱
function shuffle(array) {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
  return array;
}
/**
 * 数组特定元素求和
 * @param array     求和数组
 * @param key     特定元素  不传则计算数组元素
 */
function sumArray(array, key) {
  return eval(array.map(v => key ? v[key] : v).join("+")) || 0;
}
/**
 * 一维数组分组
 * @param array   一维数组
 * @param size    每组的大小
 */
function grpArray(array, size) {
  const grp = [[]];
  size = Math.max(size, 1);
  if (Array.isArray(array)) for (let i = 0; i < array.length; i++) {
    if (grp[grp.length - 1].length >= size) grp[grp.length] = [];
    grp[grp.length - 1].push(array[i]);
  }
  return grp;
}
/**
 * 生成数组，长度为length
 * @param length
 * @returns {unknown[]}
 */
function array(length = 12) {
  return Array.from({ length });
}
export {
  shuffle,
  sumArray,
  grpArray,
  array
};