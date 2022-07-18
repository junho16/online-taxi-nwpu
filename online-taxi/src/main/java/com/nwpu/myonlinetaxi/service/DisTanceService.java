package com.nwpu.myonlinetaxi.service;

/**
 * @author Junho
 * @date 2022/7/18 14:24
 */
public interface DisTanceService {

    /**
     * 计算两个经纬度之间的距离
     * @param startLon
     * @param startLat
     * @param endLon
     * @param endLat
     * @return
     */
    public double calcDistance(double startLon, double startLat, double endLon, double endLat);

}
