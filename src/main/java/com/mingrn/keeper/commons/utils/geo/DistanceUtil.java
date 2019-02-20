package com.mingrn.keeper.commons.utils.geo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 两经纬度点距离计算
 * </p>
 * 根据圆周率计算两点地图经纬度距离
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public class DistanceUtil {

    private DistanceUtil() {
    }

    /**
     * 地球半径(km)
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 等同于 Math.toRadians(),将用角度表示的角转换为近似相等的用弧度表示的角
     * </p>
     * JS 没有 Math.toRadians(),如在 JS 直接使用该方法即可。
     * eg:java
     * Math.toRadians(startLat);
     * eg:js
     * radians(startLat)
     *
     * @param angle 角度
     */
    private static double radians(double angle) {
        return angle * Math.PI / 180.0;
    }

    /**
     * 计算两个坐标点的距离
     * </p>
     * 距离四舍五入保留一位小数,单位KM
     *
     * @param startLng 起点经度
     * @param startLat 起点纬度
     * @param endLng   终点经度
     * @param endLat   终点纬度
     * @return 距离（千米）
     */
    public static double twoLocationDistance(double startLng, double startLat, double endLng, double endLat) {
        double startRadLat = Math.toRadians(startLat), endRadLat = Math.toRadians(endLat);
        double radLatDiff = startRadLat - endRadLat, radLngDiff = Math.toRadians(startLng) - Math.toRadians(endLng);

        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(radLatDiff / 2), 2) +
                Math.cos(startRadLat) * Math.cos(endRadLat) * Math.pow(Math.sin(radLngDiff / 2), 2)));

        distance = distance * EARTH_RADIUS;
        //distance = Math.round(distance * 10000) / 10000;
        //距离四舍五入保留一位小数
        distance = new BigDecimal(distance).setScale(1, RoundingMode.HALF_UP).doubleValue();
        return distance;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime) + "毫秒");
        double dist = twoLocationDistance(121.455244, 31.234076, 121.488301, 31.237534);
        System.out.println("两点相距：" + dist + "千米");
    }
}