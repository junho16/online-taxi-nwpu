package com.nwpu.myonlinetaxi.service;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.Taxi;
import com.nwpu.myonlinetaxi.entity.meta.DetectorMeta;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Junho
 * @date 2022/7/17 19:39
 */
@Slf4j
@Component
public class OTKafkaConsumer {

    @Resource
    CrossingService crossingService;

    /**
     * 设备消息转发至CPS
     *  判断车是否在某个路口的探测范围之内 如是 则计算距离路口的距离 计算出速度 并给出建议
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceProperty}", groupId = "device_property_persistence")
    public void eventTopicConsumer(String message) {
        log.info("message: {}" , message) ;
        if(!StringUtils.isBlank(message)){
            JSONObject msgJson = JSONObject.parseObject(message);
            String deviceName = (String) msgJson.get("deviceName");
            JSONObject posObj = msgJson.getJSONObject("data").getJSONObject("params").getJSONObject("geoLocation").getJSONObject("value");
            try{
                if(TaxisInstance.getTaxiMap().contains(deviceName)){
                    Taxi taxi = TaxisInstance.getTaxiMap().get(deviceName);
                    taxi.getTaxiMeta().setLon((double)posObj.get("lon"));
                    taxi.getTaxiMeta().setLat((double)posObj.get("lat"));
                    taxi.getTrace().add(new Position((double)posObj.get("lon") , (double)posObj.get("lat")));
                    /**
                     * TODO-判断车是否在某个路口探测范围之内 如是 则计算距离路口的距离 计算出速度 并给出建议
                     * 如果不在路口 则给出建议随意 给出速度
                     * 在路口 则通过算法计算并给出建议
                     */
                    DetectorMeta detectorMeta = crossingService.getMinDisDetector((double)posObj.get("lon") , (double)posObj.get("lat"));
                    
                    if(detectorMeta == null){
                        //TODO-模拟给个建议
                    }else{
                        //TODO-模型给出建议
                    }

                }else{
                    log.info("网约车：{} 未加载。" , deviceName);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
