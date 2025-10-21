package com.ruoyi.system.utils;

import java.util.List;

/**
 * 几何计算工具类 - 专门针对经纬度坐标优化
 *
 * @author 吕涛
 * @version 1.2
 * @since 2025/10/17
 */
public class GeometryUtil {

    /** 地球半径(km) */
    private static final double EARTH_RADIUS = 6371.0;

    /** 射线法：判断点是否在多边形内(适用于经纬度坐标) */
    public static boolean isInside(double longitude, double latitude, List<double[]> polygon) {
        int cnt = 0;
        for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            double lonI = polygon.get(i)[0], latI = polygon.get(i)[1];
            double lonJ = polygon.get(j)[0], latJ = polygon.get(j)[1];

            if (((latI > latitude) != (latJ > latitude)) &&
                    (longitude < (lonJ - lonI) * (latitude - latI) / (latJ - latI) + lonI)) {
                cnt++;
            }
        }
        return (cnt & 1) == 1;
    }

    /** 计算点到多边形边界的最短距离(单位:米) */
    public static double minDistanceToEdge(double longitude, double latitude, List<double[]> polygon) {
        double min = Double.MAX_VALUE;
        for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            min = Math.min(min, distanceBetweenPoints(
                    longitude, latitude,
                    polygon.get(j)[0], polygon.get(j)[1],
                    polygon.get(i)[0], polygon.get(i)[1]));
        }
        return min;
    }

    /**
     * 计算点到线段的最短距离(使用Haversine公式计算地球表面距离)
     */
    private static double distanceBetweenPoints(double lonP, double latP,
                                                double lon1, double lat1,
                                                double lon2, double lat2) {
        // 先计算点到直线两端点的距离
        double dist1 = haversineDistance(latP, lonP, lat1, lon1);
        double dist2 = haversineDistance(latP, lonP, lat2, lon2);

        // 如果线段退化为点
        if (lat1 == lat2 && lon1 == lon2) {
            return dist1;
        }

        // 计算点到线段的投影点
        double A = latP - lat1;
        double B = lat2 - lat1;
        double C = lonP - lon1;
        double D = lon2 - lon1;

        double dot = A * B + C * D;
        double lenSq = B * B + D * D;
        double param = lenSq != 0 ? dot / lenSq : -1;

        double projLat, projLon;

        if (param < 0) {
            projLat = lat1;
            projLon = lon1;
        } else if (param > 1) {
            projLat = lat2;
            projLon = lon2;
        } else {
            projLat = lat1 + param * B;
            projLon = lon1 + param * D;
        }

        // 返回到投影点的地球表面距离
        return haversineDistance(latP, lonP, projLat, projLon) * 1000; // 转换为米
    }

    /**
     * 使用Haversine公式计算两点间地球表面距离
     * @param lat1 第一点纬度
     * @param lon1 第一点经度
     * @param lat2 第二点纬度
     * @param lon2 第二点经度
     * @return 距离(单位:千米)
     */
    private static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
