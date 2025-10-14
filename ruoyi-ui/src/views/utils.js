
export class DynamicCanvasLayer {
  constructor(map, drawCallback) {
    this._map = map;
    this._drawCallback = drawCallback;
    this._canvas = document.createElement('canvas');
    this._canvas.style.position = 'absolute';
    this._canvas.style.pointerEvents = 'none';
    this._ctx = this._canvas.getContext('2d');
    this._container = map.getPanes().markerPane;
    this._container.appendChild(this._canvas);

    // 初始设置
    this.resize();

    // 绑定事件
    map.addEventListener('movestart', this.clear.bind(this));
    map.addEventListener('moveend', this.update.bind(this));
    map.addEventListener('zoomstart', this.clear.bind(this));
    map.addEventListener('zoomend', this.update.bind(this));
  }

  resize() {
    const size = this._map.getSize();
    this._canvas.width = size.width;
    this._canvas.height = size.height;
    this._canvas.style.width = size.width + 'px';
    this._canvas.style.height = size.height + 'px';
  }

  clear() {
    this._ctx.clearRect(0, 0, this._canvas.width, this._canvas.height);
  }

  update() {
    this.resize();
    this._drawCallback(this._ctx, this._canvas);
  }

  destroy() {
    this._container.removeChild(this._canvas);
    this._map.removeEventListener('movestart', this.clear);
    this._map.removeEventListener('moveend', this.update);
    this._map.removeEventListener('zoomstart', this.clear);
    this._map.removeEventListener('zoomend', this.update);
  }
}

// 路径简化算法 - Douglas-Peucker实现
export function simplifyPath(points, tolerance) {
  if (points.length <= 2) return points;

  // 转换为{lat, lng}格式的数组
  const pointArray = points.map(p => ({
    lat: p.latitude || p.lat,
    lng: p.longitude || p.lng,
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

// 时间字符串转时间戳
export function parseTimeString(timeStr) {
  if (!timeStr) return 0;
  const [datePart, timePart] = timeStr.split(" ");
  const [year, month, day] = datePart.split("-").map(Number);
  const [hours, minutes, seconds] = timePart.split(":").map(Number);
  return new Date(year, month - 1, day, hours, minutes, seconds).getTime();
}

// 计算线段角度
export function calculateSegmentAngle(start, end) {
  const dx = end.lng - start.lng;
  const dy = end.lat - start.lat;
  let angle = (Math.atan2(dx, dy) * 180) / Math.PI;
  if (angle < 0) angle += 360;
  return angle - 90;
}

// 创建SVG图标
export function createSvgIcon(svgStr, size, anchor) {
  const encodedSvg = encodeURIComponent(svgStr.trim().replace(/\s+/g, " "))
    .replace(/'/g, "%27")
    .replace(/"/g, "%22");

  return new window.BMap.Icon(
    `data:image/svg+xml;charset=utf-8,${encodedSvg}`,
    new window.BMap.Size(size.width, size.height),
    { anchor: new window.BMap.Size(anchor.width, anchor.height) }
  );
}