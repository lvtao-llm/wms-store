export const showPresetPointsData = [
  [
    [124.892625, 46.636498],
    [124.894709, 46.636448],
    [124.899595, 46.636597],
    [124.908219, 46.635904],
    [124.911812, 46.635359],
    [124.912962, 46.635211],
    [124.91289, 46.633379],
    [124.91289, 46.630755],
    [124.912886, 46.631857],
    [124.912742, 46.631807],
    [124.912706, 46.631708],
    [124.912958, 46.631461],
    [124.912778, 46.631139],
    [124.912778, 46.630867],
    [124.912706, 46.630595],
    [124.912742, 46.63],
    [124.912383, 46.629505],
    [124.91188, 46.628837],
    [124.911161, 46.628812],
  ],
];
export const showPresetLinesData = [
  [
    [124.892625, 46.636498],
    [124.894709, 46.636448],
    [124.899595, 46.636597],
    [124.908219, 46.635904],
    [124.911812, 46.635359],
    [124.912962, 46.635211],
    [124.91289, 46.633379],
    [124.91289, 46.630755],
    [124.912886, 46.631857],
    [124.912742, 46.631807],
    [124.912706, 46.631708],
    [124.912958, 46.631461],
    [124.912778, 46.631139],
    [124.912778, 46.630867],
    [124.912706, 46.630595],
    [124.912742, 46.63],
    [124.912383, 46.629505],
    [124.91188, 46.628837],
    [124.911161, 46.628812],
  ],
];
export const showAnimatedLineData = [
  [124.892625, 46.636498],
  [124.894709, 46.636448],
  [124.899595, 46.636597],
  [124.908219, 46.635904],
  [124.911812, 46.635359],
  [124.912962, 46.635211],
  [124.91289, 46.633379],
  [124.91289, 46.630755],
  [124.912172, 46.629419],
  [124.91174, 46.628676],
  [124.909872, 46.628676],
  [124.90556, 46.628478],
  [124.902111, 46.628429],
  [124.899128, 46.628404],
  [124.899308, 46.626745],
  [124.899236, 46.625012],
  [124.900817, 46.625136],
  [124.901787, 46.625136],
];

/**
* Douglas-Peucker 路径简化算法
* @param {Array} points 原始路径点数组
* @param {Number} tolerance 简化容差（单位：经纬度）
* @returns {Array} 简化后的路径点数组
*/
export const simplifyPath = (points, tolerance) => {
  if (points.length <= 2) return points;

  // 转换为{lat, lng}格式的数组
  const pointArray = points.map(p => ({
    lat: p.latitude,
    lng: p.longitude,
    originalData: p // 保留原始数据
  }));

  // 递归简化函数
  function douglasPeucker(points, tolerance) {
    if (points.length <= 2) return points;

    let maxDistance = 0;
    let index = 0;
    const end = points.length - 1;

    // 找到离线段最远的点
    for (let i = 1; i < end; i++) {
      const distance = perpendicularDistance(
        points[i],
        points[0],
        points[end]
      );

      if (distance > maxDistance) {
        index = i;
        maxDistance = distance;
      }
    }

    // 根据最大距离决定如何处理
    if (maxDistance > tolerance) {
      const left = douglasPeucker(points.slice(0, index + 1), tolerance);
      const right = douglasPeucker(points.slice(index), tolerance);
      return left.slice(0, -1).concat(right);
    }

    // 如果所有点都在容差范围内，则只保留起点和终点
    return [points[0], points[end]];
  }

  // 计算点到线段的垂直距离
  function perpendicularDistance(point, lineStart, lineEnd) {
    const area = Math.abs(
      (lineEnd.lng - lineStart.lng) * (point.lat - lineStart.lat) -
      (lineEnd.lat - lineStart.lat) * (point.lng - lineStart.lng)
    );
    const lineLength = Math.sqrt(
      Math.pow(lineEnd.lng - lineStart.lng, 2) +
      Math.pow(lineEnd.lat - lineStart.lat, 2)
    );
    return lineLength > 0 ? area / lineLength : 0;
  }

  // 执行简化并恢复原始数据结构
  const simplified = douglasPeucker(pointArray, tolerance);
  return simplified.map(p => p.originalData);
}