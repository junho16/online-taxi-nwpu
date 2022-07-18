package com.nwpu.myonlinetaxi.service;

import com.nwpu.myonlinetaxi.entity.meta.DetectorMeta;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/18 15:47
 */
public interface CrossingService {

    /**
     * 给出车辆的经纬度 判断并返回距离其最近的探测器
     * @param lon
     * @param lat
     * @return
     */
    public DetectorMeta getMinDisDetector(Double lon , Double lat);

}
