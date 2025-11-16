
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

// WGS84转GCJ02（火星坐标系）- 高精度版本
function wgs84ToGcj02(wgsLon, wgsLat) {
  const a = 6378245.0;
  const ee = 0.00669342162296594323;
  let dLat = transformLat(wgsLon - 105.0, wgsLat - 35.0);
  let dLon = transformLon(wgsLon - 105.0, wgsLat - 35.0);
  const radLat = (wgsLat / 180.0) * Math.PI;
  let magic = Math.sin(radLat);
  magic = 1 - ee * magic * magic;
  const sqrtMagic = Math.sqrt(magic);
  dLat = (dLat * 180.0) / (((a * (1 - ee)) / (magic * sqrtMagic)) * Math.PI);
  dLon = (dLon * 180.0) / ((a / sqrtMagic) * Math.cos(radLat) * Math.PI);
  const mgLat = wgsLat + dLat;
  const mgLon = wgsLon + dLon;
  return [mgLon, mgLat];
}

function transformLat(lon, lat) {
  let ret = -100.0 + 2.0 * lon + 3.0 * lat + 0.2 * lat * lat + 0.1 * lon * lat + 0.2 * Math.sqrt(Math.abs(lon));
  ret += ((20.0 * Math.sin(6.0 * lon * Math.PI) + 20.0 * Math.sin(2.0 * lon * Math.PI)) * 2.0) / 3.0;
  ret += ((20.0 * Math.sin(lat * Math.PI) + 40.0 * Math.sin((lat / 3.0) * Math.PI)) * 2.0) / 3.0;
  ret += ((160.0 * Math.sin((lat / 12.0) * Math.PI) + 320 * Math.sin((lat * Math.PI) / 30.0)) * 2.0) / 3.0;
  return ret;
}

function transformLon(lon, lat) {
  let ret = 300.0 + lon + 2.0 * lat + 0.1 * lon * lon + 0.1 * lon * lat + 0.1 * Math.sqrt(Math.abs(lon));
  ret += ((20.0 * Math.sin(6.0 * lon * Math.PI) + 20.0 * Math.sin(2.0 * lon * Math.PI)) * 2.0) / 3.0;
  ret += ((20.0 * Math.sin(lon * Math.PI) + 40.0 * Math.sin((lon / 3.0) * Math.PI)) * 2.0) / 3.0;
  ret += ((150.0 * Math.sin((lon / 12.0) * Math.PI) + 300.0 * Math.sin((lon / 30.0) * Math.PI)) * 2.0) / 3.0;
  return ret;
}

// GCJ02转BD09（百度坐标系）- 高精度版本（修正偏移问题）
function gcj02ToBd09(gcjLon, gcjLat) {
  const x_PI = 3.14159265358979324 * 3000.0 / 180.0;
  const z = Math.sqrt(gcjLon * gcjLon + gcjLat * gcjLat) + 0.00002 * Math.sin(gcjLat * x_PI);
  const theta = Math.atan2(gcjLat, gcjLon) + 0.000003 * Math.cos(gcjLon * x_PI);
  // BD09的偏移量：经度+0.0065，纬度+0.006
  const bdLon = z * Math.cos(theta) + 0.0065;
  const bdLat = z * Math.sin(theta) + 0.006;
  return [bdLon, bdLat];
}

// WGS84转BD09（百度坐标系）- 带偏移修正
export function wgs84ToBd09(wgsLon, wgsLat, offsetLon = 0, offsetLat = 0) {
  const [gcjLon, gcjLat] = wgs84ToGcj02(wgsLon, wgsLat);
  const [bdLon, bdLat] = gcj02ToBd09(gcjLon, gcjLat);
  // 添加可配置的偏移修正（用于微调）
  return [bdLon + offsetLon, bdLat + offsetLat];
}

// 使用百度地图API转换坐标（异步，批量转换）
// 注意：离线版百度地图API可能不支持Convertor的网络请求，会直接使用本地算法
export function convertTrajectoryPointsWithBMap(trajectoryPoints, callback) {
  console.log("convertTrajectoryPointsWithBMap 开始执行", trajectoryPoints);

  if (!trajectoryPoints || !Array.isArray(trajectoryPoints)) {
    console.log("轨迹点数据无效，直接返回");
    callback(trajectoryPoints);
    return;
  }

  if (!window.BMap) {
    console.warn("百度地图API未加载，使用本地转换算法");
    const converted = convertTrajectoryPoints(trajectoryPoints);
    callback(converted);
    return;
  }

  // 检查是否有坐标的点
  const pointsWithCoords = trajectoryPoints.filter(
    point => point.longitude && point.latitude
  );

  if (pointsWithCoords.length === 0) {
    console.log("没有有效的坐标点，直接返回");
    callback(trajectoryPoints);
    return;
  }

  console.log("有效坐标点数量:", pointsWithCoords.length);

  // 离线版百度地图API可能不支持Convertor的网络请求
  // 为了确保回调一定会执行，直接使用本地转换算法
  // 如果将来需要使用在线版API的Convertor，可以在这里添加判断
  console.log("使用本地转换算法（离线版API不支持Convertor网络请求）");
  const converted = convertTrajectoryPoints(trajectoryPoints);
  callback(converted);
  return;

  // 以下代码保留，如果将来需要使用在线版API的Convertor时可以启用
  /*
  // 检查Convertor是否可用
  if (!window.BMap.Convertor) {
    console.warn("百度地图Convertor未加载，使用本地转换算法");
    const converted = convertTrajectoryPoints(trajectoryPoints);
    callback(converted);
    return;
  }

  */
}

// 转换轨迹点数组的坐标系（WGS84转BD09）- 同步版本（使用本地算法）
// offsetLon: 经度偏移修正（度），offsetLat: 纬度偏移修正（度）
// 如果转换后往右上偏移，可以尝试设置负的偏移值
export function convertTrajectoryPoints(trajectoryPoints, offsetLon = -0.0065, offsetLat = -0.004) {
  if (!trajectoryPoints || !Array.isArray(trajectoryPoints)) {
    return trajectoryPoints;
  }

  return trajectoryPoints.map(point => {
    if (point.longitude && point.latitude) {
      // 如果转换后往右上偏移，减小偏移量
      // 默认偏移修正：经度-0.008，纬度-0.006（可根据实际情况调整）
      const [bdLon, bdLat] = wgs84ToBd09(point.longitude, point.latitude, offsetLon, offsetLat);
      return {
        ...point,
        longitude: bdLon,
        latitude: bdLat
      };
    }
    return point;
  });
}