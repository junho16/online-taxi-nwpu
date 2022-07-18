package com.nwpu.myonlinetaxi.init;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.meta.DetectorMeta;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.util.JsonUtil;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/18 13:58
 */
@Component
public class DetectorsInstance {

    //路口探测器文件名
    @Value("${init.bean.detector_file_name}")
    private String detectionFileName;

    /**
     *  init crossing detector
     * @return
     */
    @Bean("detectorMap")
    private ConcurrentHashMap<String , DetectorMeta> initDetectorMap() {
        ConcurrentHashMap<String , DetectorMeta> map = new ConcurrentHashMap<>();
        List<DetectorMeta> list = JSONObject.parseArray(JsonUtil.readJsonFile(detectionFileName), DetectorMeta.class);
        for(DetectorMeta detectorMeta : list){
            map.put(detectorMeta.getDetector_id() , detectorMeta);
        }
        return map;
    }

}
