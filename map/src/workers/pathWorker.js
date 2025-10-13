// 路径抽稀算法 (Ramer-Douglas-Peucker)
function simplifyPath(points, tolerance) {
  if (points.length <= 2) return points;

  let maxDistance = 0;
  let maxIndex = 0;
  const end = points.length - 1;

  for (let i = 1; i < end; i++) {
    const distance = perpendicularDistance(points[i], points[0], points[end]);
    if (distance > maxDistance) {
      maxDistance = distance;
      maxIndex = i;
    }
  }

  if (maxDistance > tolerance) {
    const left = simplifyPath(points.slice(0, maxIndex + 1), tolerance);
    const right = simplifyPath(points.slice(maxIndex), tolerance);
    return left.slice(0, -1).concat(right);
  }

  return [points[0], points[end]];
}

// 计算点到线段的垂直距离
function perpendicularDistance(point, lineStart, lineEnd) {
  const x = point.longitude;
  const y = point.latitude;
  const x1 = lineStart.longitude;
  const y1 = lineStart.latitude;
  const x2 = lineEnd.longitude;
  const y2 = lineEnd.latitude;

  const l2 = (x2 - x1) ** 2 + (y2 - y1) ** 2;
  if (l2 === 0) return Math.sqrt((x - x1) ** 2 + (y - y1) ** 2);

  const t = Math.max(0, Math.min(1, ((x - x1) * (x2 - x1) + (y - y1) * (y2 - y1)) / l2));
  const projX = x1 + t * (x2 - x1);
  const projY = y1 + t * (y2 - y1);

  return Math.sqrt((x - projX) ** 2 + (y - projY) ** 2);
}

// 计算线段角度
function calculateSegmentAngle(start, end) {
  const dx = end.lng - start.lng;
  const dy = end.lat - start.lat;
  let angle = (Math.atan2(dx, dy) * 180) / Math.PI;
  return angle < 0 ? angle + 360 : angle - 90;
}

// Worker消息处理
self.onmessage = function (e) {
  const { action, data, options } = e.data;

  if (action === 'process') {
    // 分阶段处理数据
    const chunkSize = 5000;
    const chunks = [];
    for (let i = 0; i < data.length; i += chunkSize) {
      chunks.push(data.slice(i, i + chunkSize));
    }

    // 逐步处理并返回进度
    const result = [];
    chunks.forEach((chunk, index) => {
      const simplified = simplifyPath(chunk, options.tolerance);
      result.push(...simplified);

      // 返回处理进度
      self.postMessage({
        type: 'progress',
        percent: Math.round(((index + 1) / chunks.length) * 100)
      });
    });

    // 返回最终结果
    self.postMessage({
      type: 'result',
      data: result,
      originalLength: data.length,
      simplifiedLength: result.length
    });
  }

  if (action === 'calculate_angles') {
    const angles = [];
    for (let i = 0; i < data.length - 1; i++) {
      angles.push(calculateSegmentAngle(
        { lng: data[i].longitude, lat: data[i].latitude },
        { lng: data[i + 1].longitude, lat: data[i + 1].latitude }
      ));
    }
    self.postMessage({
      type: 'angles_result',
      angles: angles
    });
  }
};