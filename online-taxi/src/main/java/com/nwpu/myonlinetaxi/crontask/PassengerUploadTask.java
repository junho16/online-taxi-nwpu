package com.nwpu.myonlinetaxi.crontask;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.dto.MqttPassengerDto;
import com.nwpu.myonlinetaxi.dto.MqttTaxiDto;
import com.nwpu.myonlinetaxi.dto.PassengerDto;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.init.PassengersInstance;
import com.nwpu.myonlinetaxi.mqtt.send.MqttSendMsg;
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
 * @author Junho
 * @date 2022/7/22 13:52
 */
@EnableScheduling
@Component
@Slf4j
public class PassengerUploadTask {

    @Value("${mqtt.passengerTopic}")
    private String passengerTopic;

//    private static final Logger log = LoggerFactory.getLogger(PassengerUploadTask.class);
    @Scheduled(cron = "*/1 * * * * ?")
    public void passengerUpload(){
        List<PassengerDto> list = new ArrayList<>();
        Map<String , PassengerMeta> map = PassengersInstance.getPassengerMap();
        for(Map.Entry<String , PassengerMeta> entry : map.entrySet()){
            list.add(PassengerDto.toPassengerDto(
                entry.getValue()
            ));
        }

        MqttPassengerDto mqttPassengerDto = new MqttPassengerDto("passenger" , System.currentTimeMillis() , list);
        String res = JSONObject.toJSONString(mqttPassengerDto);
        log.info(res);
        MqttSendMsg.sendMqttMsg(passengerTopic , res);

//        log.error(res);
//        CopyOnWriteArraySet<WebSocketServer> webSocketSet =
//                WebSocketServer.getWebSocketSet();
//        webSocketSet.forEach(c -> {
//            try {
//                if(c.getSid().equals("user"))
//                    c.sendMessage(res);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }
}
