package com.nwpu.myonlinetaxi.service.kafka;

import com.nwpu.myonlinetaxi.algorithm.*;
import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.dto.TaxiDto;
import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.Taxi;
import com.nwpu.myonlinetaxi.entity.TrafficLight;
import com.nwpu.myonlinetaxi.entity.meta.DetectorMeta;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import com.nwpu.myonlinetaxi.service.CrossingService;
import com.nwpu.myonlinetaxi.service.DisTanceService;
import com.nwpu.myonlinetaxi.service.TaxiService;
import com.nwpu.myonlinetaxi.websocket.WebSocketProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/17 19:39
 */
@Slf4j
@Component
public class OTKafkaConsumer {

    @Resource
    CrossingService crossingService;

    @Resource
    TaxiService taxiService;

    @Resource
    DisTanceService disTanceService;

    @Resource
    WebSocketProducer webSocketProducer;

    /**
     * 设备消息转发至CPS
     *  判断车是否在某个路口的探测范围之内 如是 则计算距离路口的距离 计算出速度 并给出建议
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceProperty}", groupId = "device_property_persistence")
    public void eventTopicConsumer(String message) {
        log.info("message: {}" , message) ;
        if(!StringUtils.isBlank(message)){
            JSONObject msgJson = JSONObject.parseObject(message);
            log.info(msgJson.toJSONString());
            String deviceName = (String) msgJson.get("deviceName");
            JSONObject posObj = msgJson.getJSONObject("data").getJSONObject("params").getJSONObject("geoLocation").getJSONObject("value");
            try{
                if(TaxisInstance.getTaxiMap().containsKey(deviceName)){
                    Taxi taxi = TaxisInstance.getTaxiMap().get(deviceName);
                    Double taxi_lon = Double.parseDouble(posObj.get("lon")+"");
                    Double taxi_lat = Double.parseDouble(posObj.get("lat")+"");
                    taxi.getTaxiMeta().setLon(taxi_lon);
                    taxi.getTaxiMeta().setLat(taxi_lat);
                    taxi.getTrace().add(new Position(taxi_lon , taxi_lat));

                    //TODO-存一份到MongoDb

                    /**
                     * 判断车是否在某个路口探测范围之内 如是 则计算距离路口的距离 计算出速度 并给出建议 （信号灯？）
                     */
                    //获取距离当前网约车最近的设备
                    DetectorMeta detectorMeta = crossingService.getMinDisDetector(taxi_lon, taxi_lat);

                    //计算当前网约车的速度
                    double speed = taxiService.getSpeed(taxi);

                    //行车建议
                    String suggest = "";
                    if(detectorMeta == null){
                        suggest = "保持速度行驶";
                    }else{
                        TrafficLight light = TrafficLight.getRandomLight();
                        suggest = Guidance.getSuggest(
                                (int) disTanceService.calcDistance(
                                        detectorMeta.getLon(),
                                        detectorMeta.getLat(),
                                        taxi.getTaxiMeta().getLon(),
                                        taxi.getTaxiMeta().getLat()
                                ),
                                (int) speed ,
                                light.getColor(),
                                light.getLeftTime()
                        );
                    }
                    Map<String , Object> res = new HashMap<>();
                    res.put("type" , "online-taxi");
                    res.put("data" , new TaxiDto(
                            taxi.getTaxiMeta().getTaxi_id(),
                            System.currentTimeMillis(),
                            taxi.getTaxiMeta().getLon(),
                            taxi.getTaxiMeta().getLat(),
                            speed,
                            suggest,
                            1));
                    String resMsg = JSONObject.toJSONString(res);
                    webSocketProducer.sendToWs(resMsg);
                }else{
                    log.info("网约车：{} 未加载。" , deviceName);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
