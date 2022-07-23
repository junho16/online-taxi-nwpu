package com.nwpu.myonlinetaxi.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.entity.enums.PassengerState;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.init.PassengersInstance;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import com.nwpu.myonlinetaxi.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.LogManager;

/**
 * @author Junho
 * @date 2022/7/22 13:52
 */
@EnableScheduling
@Component
@Slf4j
public class PassengerUploadTask {
//    private static final Logger log = LoggerFactory.getLogger(PassengerUploadTask.class);
    @Scheduled(cron = "*/1 * * * * ?")
    public void passengerUpload(){
        List<String> list = new ArrayList<>();
        Map<String , PassengerMeta> map = PassengersInstance.getPassengerMap();
        for(Map.Entry<String , PassengerMeta> entry : map.entrySet()){
            list.add(entry.getValue().toString());
//            log.info(entry.getKey()+" " + entry.getValue().toString());
        }
        String res = System.currentTimeMillis()+" : "+ JSONObject.toJSONString(list);
//        log.error(res);
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
}
