package com.ruoyi.system.utils;

import java.util.List;

/**
 * @author 吕涛
 * @version 1.0
 * @since 2025/10/17
 */
public class GeometryUtil {
    /** 射线法：点是否在多边形内 */
    public static boolean isInside(double px, double py, List<double[]> region) {
        int cnt = 0;
        for (int i = 0, j = region.size() - 1; i < region.size(); j = i++) {
            double xi = region.get(i)[0], yi = region.get(i)[1];
            double xj = region.get(j)[0], yj = region.get(j)[1];
            if (((yi > py) != (yj > py)) &&
                    (px < (xj - xi) * (py - yi) / (yj - yi) + xi)) {
                cnt++;
            }
        }
        return (cnt & 1) == 1;
    }

    /** 点到折线最短距离 */
    public static double minDistanceToEdge(double px, double py, List<double[]> region) {
        double min = Double.MAX_VALUE;
        for (int i = 0, j = region.size() - 1; i < region.size(); j = i++) {
            min = Math.min(min, distancePointToSegment(px, py,
                    region.get(j)[0], region.get(j)[1],
                    region.get(i)[0], region.get(i)[1]));
        }
        return min;
    }

    /** 点(px,py) 到线段 (x1,y1)-(x2,y2) 距离 */
    private static double distancePointToSegment(double px, double py,
                                                 double x1, double y1,
                                                 double x2, double y2) {
        double dx = x2 - x1, dy = y2 - y1;
        double segLen = dx * dx + dy * dy;
        if (segLen == 0) return Math.hypot(px - x1, py - y1);
        double t = ((px - x1) * dx + (py - y1) * dy) / segLen;
        t = Math.max(0, Math.min(1, t));
        double projX = x1 + t * dx, projY = y1 + t * dy;
        return Math.hypot(px - projX, py - projY);
    }
}
