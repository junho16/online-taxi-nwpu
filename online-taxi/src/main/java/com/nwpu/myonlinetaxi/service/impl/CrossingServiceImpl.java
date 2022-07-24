package com.nwpu.myonlinetaxi.service.impl;

import com.nwpu.myonlinetaxi.entity.meta.DetectorMeta;
import com.nwpu.myonlinetaxi.service.CrossingService;
import com.nwpu.myonlinetaxi.service.DisTanceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.beans.PropertyEditorSupport;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/18 16:02
 */
@Service
public class CrossingServiceImpl implements CrossingService {

    //探测器500米范围内
    @Value("${detector_range}")
    private double minDis;

    @Resource
    ConcurrentHashMap<String , DetectorMeta> detectorMap;

    @Resource
    DisTanceService disTanceService;

    @Override
    public DetectorMeta getMinDisDetector(Double lon, Double lat) {
        for(Map.Entry<String , DetectorMeta> entry : detectorMap.entrySet()){
            if(minDis >= disTanceService.calcDistance(lon , lat , entry.getValue().getLight_pos().getLon(), entry.getValue().getLight_pos().getLat())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
