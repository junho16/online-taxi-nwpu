package com.nwpu.myonlinetaxi.crontask;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.algorithm.Guidance;
import com.nwpu.myonlinetaxi.dto.MqttTaxiDto;
import com.nwpu.myonlinetaxi.dto.TaxiDto;
import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.meta.DetectorMeta;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import com.nwpu.myonlinetaxi.mqtt.send.MqttSendMsg;
import com.nwpu.myonlinetaxi.mqtt.tian.TianMqttClient;
import com.nwpu.myonlinetaxi.mqtt.tian.config.MqttTianConfig;
import com.nwpu.myonlinetaxi.service.CrossingService;
import com.nwpu.myonlinetaxi.service.DisTanceService;
import com.nwpu.myonlinetaxi.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 定时上报网约车信息
 * @author Junho
 * @date 2022/7/22 13:41
 */
@EnableScheduling
@Component
@Slf4j
public class TaxiUploadTask {

    @Value("${mqtt.taxiTopic}")
    private String taxiTopic;

    @Resource
    private CrossingService crossingService;

    @Resource
    private DisTanceService disTanceService;

    @Resource
    TianMqttClient tianMqttClient;

    @Scheduled(cron = "*/1 * * * * ?")
    public void taxiUpload(){
        List<String> wsTes = new ArrayList<>();
        List<TaxiDto> list = new ArrayList<>();
        Map<String , TaxiMeta> map = TaxisInstance.getTaxiMap();
        for(Map.Entry<String , TaxiMeta> entry : map.entrySet()){
            wsTes.add(entry.getValue().toString());
            DetectorMeta detectorMeta = crossingService.getMinDisDetector(entry.getValue().getLon() , entry.getValue().getLat());
            String suggest = "按照原速度行驶";
            boolean isCrossing = false;
            try{
                if(detectorMeta != null){
                    isCrossing = true;
                    suggest = Guidance.getSuggest(
                            (int) disTanceService.calcDistance(entry.getValue().getLon() , entry.getValue().getLat() , detectorMeta.getLight_pos().getLon() , detectorMeta.getLight_pos().getLat())   ,
                            (int) entry.getValue().getSpeed(),
                            tianMqttClient.getLightMap().get(detectorMeta.getLight_sn()).getColor(),
                            tianMqttClient.getLightMap().get(detectorMeta.getLight_sn()).getLeftTime()
                    );
                }
            }catch (Exception e){
                log.error("检测车辆{}路口的行驶情况失败：{}",entry.getValue().getTaxi_name() , e.getMessage());
            }
            list.add(new TaxiDto(
                entry.getValue().getTaxi_id(),
                entry.getValue().getLon(),
                entry.getValue().getLat(),
                entry.getValue().getState(),
                isCrossing,
                entry.getValue().getSpeed(),
                suggest
            ));
            log.info(entry.getKey()+" " + entry.getValue().toString());
        }
        MqttTaxiDto mqttTaxiDto = new MqttTaxiDto("taxi" , System.currentTimeMillis() , list);
        String res = JSONObject.toJSONString(mqttTaxiDto);
        log.info("taxi ==》 {}" , res);
        MqttSendMsg.sendMqttMsg(taxiTopic , res);

//        log.error(res);
        CopyOnWriteArraySet<WebSocketServer> webSocketSet =
                WebSocketServer.getWebSocketSet();
        webSocketSet.forEach(c -> {
            try {
                if(c.getSid().equals("taxi"))
                    c.sendMessage(JSONObject.toJSONString(wsTes));
             } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
