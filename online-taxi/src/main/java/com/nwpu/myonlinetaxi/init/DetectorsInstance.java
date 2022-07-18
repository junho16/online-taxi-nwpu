package com.nwpu.myonlinetaxi.init;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.meta.DetectorMeta;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.util.JsonUtil;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Junho
 * @date 2022/7/18 13:58
 */
@Component
public class DetectorsInstance {

    //路口探测器文件名
    @Value("${init.bean.detector_file_name}")
    private String detectionFileName;

    private static List<DetectorMeta> detectorList;

    private DetectorsInstance(){
    }

    /**
     *  init crossing detector
     * @return
     */
    @PostConstruct
    private void initDetectorList() {
        List<DetectorMeta> list = JSONObject.parseArray(JsonUtil.readJsonFile(detectionFileName), DetectorMeta.class);
        detectorList = list;
    }

}
