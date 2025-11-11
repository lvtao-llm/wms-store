import { Vector3 } from "three";

/**
 * 从一系列点组成的曲线中获取长度均匀的系列点
 * @param points          点集[vec3...]
 * @param distanceSplit   曲线步长 如果过大会导致曲线失去精度
 * @returns {Vector3[]}
 */
function getSpacedPoints(points, distanceSplit = 1) {
  let length = 0;
  for (let i = 0; i < points.length - 1; i++) {
    const current = points[i];
    const next = points[i + 1];
    const distance = current.distanceTo(next);
    current._distance = distance;
    length += distance;
  }
  let _distanceSplit = distanceSplit,
    _points = [new Vector3().copy(points[0])],
    _index = 0,
    _l = 0;
  while (_distanceSplit <= length) {
    for (let i = _index; i < points.length - 1; i++) {
      const current = points[i], next = points[i + 1];
      if (_distanceSplit > _l && _distanceSplit <= (_l + current._distance)) {
        const vec3 = new Vector3().copy(current);
        _points.push(vec3.lerp(next, (_distanceSplit - _l) / current._distance));
        _index = i;
        break;
      } else _l += current._distance;
    }
    _distanceSplit += distanceSplit;
  }
  _points.push(new Vector3().copy(points[points.length - 1]));
  return _points;
}

export { getSpacedPoints };