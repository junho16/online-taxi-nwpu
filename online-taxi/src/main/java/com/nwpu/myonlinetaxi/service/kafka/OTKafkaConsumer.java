package com.nwpu.myonlinetaxi.service.kafka;

import com.alibaba.fastjson.JSONArray;
import com.nwpu.myonlinetaxi.algorithm.*;
import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.dto.MqttPassengerDto;
import com.nwpu.myonlinetaxi.dto.MqttTaxiDto;
import com.nwpu.myonlinetaxi.dto.PassengerDto;
import com.nwpu.myonlinetaxi.dto.TaxiDto;
import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.TrafficLight;
import com.nwpu.myonlinetaxi.entity.meta.DetectorMeta;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.mqtt.send.MqttSendMsg;
import com.nwpu.myonlinetaxi.mqtt.tian.TianMqttClient;
import com.nwpu.myonlinetaxi.service.CrossingService;
import com.nwpu.myonlinetaxi.service.DisTanceService;
import com.nwpu.myonlinetaxi.websocket.WebSocketProducer;
import com.nwpu.myonlinetaxi.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Junho
 * @date 2022/7/17 19:39
 */
@Slf4j
@Component
public class OTKafkaConsumer {

    @Value("${mqtt.taxiTopic}")
    private String taxiTopic;

    @Value("${mqtt.passengerTopic}")
    private String passengerTopic;

    @Resource
    CrossingService crossingService;

//    @Resource
//    TaxiService taxiService;

    @Resource
    DisTanceService disTanceService;

    @Resource
    WebSocketProducer webSocketProducer;

    @Resource
    TianMqttClient tianMqttClient;

//    @Resource
//    TaxiMongoDao taxiMongoDao;

    /**
     * 设备消息转发至CPS
     *  判断车是否在某个路口的探测范围之内 如是 则计算距离路口的距离 计算出速度 并给出建议
     */
    @KafkaListener(topics = "${topic.oneNetBridge.deviceProperty}", groupId = "device_property_persistence")
    public void eventTopicConsumer(String message) {
        log.info("message: {}" , message) ;
        if(!StringUtils.isBlank(message)){
            JSONObject msgJson = JSONObject.parseObject(message);
//            log.info(msgJson.toJSONString());
            JSONArray value = msgJson.getJSONArray("value");
            if(msgJson.getString("type").equals("taxi")){
                List<TaxiDto> list = new ArrayList<>();
                for(int i = 0 ; i < value.size() ;i++){
                    TaxiMeta item = value.getObject(i , TaxiMeta.class);
                    DetectorMeta detectorMeta = crossingService.getMinDisDetector(item.getLon() , item.getLat());
                    String suggest = "按照原速度行驶";
                    boolean isCrossing = false;
                    try{
                        if(detectorMeta != null){
                            isCrossing = true;
                            suggest = Guidance.getSuggest(
                                (int) disTanceService.calcDistance(item.getLon() , item.getLat() , detectorMeta.getLight_pos().getLon() , detectorMeta.getLight_pos().getLat())   ,
                                (int) item.getSpeed(),
                                tianMqttClient.getLightMap().get(detectorMeta.getLight_sn()).getColor(),
                                tianMqttClient.getLightMap().get(detectorMeta.getLight_sn()).getLeftTime()
                            );
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("检测车辆{}路口的行驶情况失败：{}",item.getTaxi_name() , e.getMessage());
                    }
                    list.add(new TaxiDto(
                        item.getTaxi_id(),
                        item.getLon(),
                        item.getLat(),
                        item.getState(),
                        isCrossing,
                        item.getSpeed(),
                        suggest
                    ));
                }
                MqttTaxiDto mqttTaxiDto = new MqttTaxiDto("taxi" , System.currentTimeMillis() , list);
                String res = JSONObject.toJSONString(mqttTaxiDto);
                log.info("taxi ==》 {}" , res);
                MqttSendMsg.sendMqttMsg(taxiTopic , res);

                CopyOnWriteArraySet<WebSocketServer> webSocketSet =
                        WebSocketServer.getWebSocketSet();
                webSocketSet.forEach(c -> {
                    try {
                        if(c.getSid().equals("taxi"))
                            c.sendMessage(JSONObject.toJSONString(res));
                     } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }else{
                List<PassengerDto> list = new ArrayList<>();
                for(int i = 0 ; i < value.size() ;i++){
                    PassengerMeta item = value.getObject(i , PassengerMeta.class);
                    list.add(PassengerDto.toPassengerDto( item ));
                }
                MqttPassengerDto mqttPassengerDto = new MqttPassengerDto("passenger" , System.currentTimeMillis() , list);
                String res = JSONObject.toJSONString(mqttPassengerDto);
                log.info("passengers ==》 {}" , res);
                MqttSendMsg.sendMqttMsg(passengerTopic , res);


                CopyOnWriteArraySet<WebSocketServer> webSocketSet =
                        WebSocketServer.getWebSocketSet();
                webSocketSet.forEach(c -> {
                    try {
                        if(c.getSid().equals("user"))
                            c.sendMessage(res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
//            String deviceName = (String) msgJson.get("deviceName");
//            JSONObject posObj = msgJson.getJSONObject("data").getJSONObject("params").getJSONObject("geoLocation").getJSONObject("value");
//            try{
//                if(TaxisInstance.getTaxiMap().containsKey(deviceName)){
//                    Taxi taxi = TaxisInstance.getTaxiMap().get(deviceName);
//                    Double taxi_lon = Double.parseDouble(posObj.get("lon")+"");
//                    Double taxi_lat = Double.parseDouble(posObj.get( "lat")+"");
//
//                    //在内存中记录一下
//                    taxi.getTaxiMeta().setLon(taxi_lon);
//                    taxi.getTaxiMeta().setLat(taxi_lat);
//                    taxi.getTrace().add(new Position(taxi_lon , taxi_lat));
//
//                    //在mongodb中记录一下
//                    taxiMongoDao.changeLonLat(taxi.getTaxiMeta().getTaxi_id() , taxi.getTaxiMeta().getLon() , taxi.getTaxiMeta().getLat());
//
//                    /**
//                     * 判断车是否在某个路口探测范围之内 如是 则计算距离路口的距离 计算出速度 并给出建议
//                     */
//                    //获取距离当前网约车最近的设备
//                    DetectorMeta detectorMeta = crossingService.getMinDisDetector(taxi_lon, taxi_lat);
//
//                    //计算当前网约车的速度
//                    double speed = taxiService.getSpeed(taxi);
//
//                    //行车建议
//                    String suggest = "";
//                    if(detectorMeta == null){
//                        suggest = "保持速度行驶";
//                    }else{
//                        //信号灯此处模拟一下
//                        TrafficLight light = TrafficLight.getRandomLight();
//                        suggest = Guidance.getSuggest(
//                                (int) disTanceService.calcDistance(
//                                        detectorMeta.getLon(),
//                                        detectorMeta.getLat(),
//                                        taxi.getTaxiMeta().getLon(),
//                                        taxi.getTaxiMeta().getLat()
//                                ),
//                                (int) speed ,
//                                light.getColor(),
//                                light.getLeftTime()
//                        );
//                    }
//                    Map<String , Object> res = new HashMap<>();
//                    res.put("type" , "online-taxi");
//                    res.put("data" , new TaxiDto(
//                            taxi.getTaxiMeta().getTaxi_id(),
//                            System.currentTimeMillis(),
//                            taxi.getTaxiMeta().getLon(),
//                            taxi.getTaxiMeta().getLat(),
//                            speed,
//                            suggest,
//                            1));
//                    String resMsg = JSONObject.toJSONString(res);
//                    webSocketProducer.sendToWs(resMsg);
//                }else{
//                    log.info("网约车：{} 未加载。" , deviceName);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
    }
}
