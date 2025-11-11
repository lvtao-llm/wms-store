/**
 * 计算两点间距离
 * @param start  [0, 0]
 * @param end    [0, 0]
 * @returns {number}
 */
function computeDistanceOfDoublePoints(start = [0, 0], end = [0, 0]) {
  return Math.sqrt(Math.abs(end[1] - start[1]) ** 2 + Math.abs(end[0] - start[0]) ** 2);
}
/**
 * 计算路径距离
 * @param path
 * @returns {number}
 */
function computeDistanceOfPath(path = []) {
  if (path.length < 2) return 0;
  let distance = 0;
  for (let i = 0; i < path.length - 1; i++) {
    const start = path[i];
    const next = path[i + 1];
    distance += computeDistanceOfDoublePoints(start, next);
  }
  return distance;
}
/**
 * 获取一段路径的均分点阵
 * @param path          线段路径 [...[]]
 * @param splitLength   均分长度
 * @returns {*[]}
 */
function getAvgPointsOfPath(path = [], splitLength = 1) {
  const avgPath = [];
  if (path.length > 1) path.forEach((start, i) => {
    if (i === path.length - 1) return false;
    const next = path[i + 1];
    const distance = computeDistanceOfDoublePoints(start, next);
    const size = Math.ceil(distance / splitLength) - 1;
    if (size > 0) for (let j = 0; j < size; j++) {
      avgPath.push([
        start[0] + (next[0] - start[0]) * (j / size),
        start[1] + (next[1] - start[1]) * (j / size)
      ]);
    } else avgPath.push(start);
    if (i === path.length - 2) avgPath.push(next);
  });
  return avgPath;
}
export {
  computeDistanceOfDoublePoints,
  computeDistanceOfPath,
  getAvgPointsOfPath
};