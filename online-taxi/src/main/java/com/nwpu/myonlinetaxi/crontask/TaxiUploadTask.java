package com.nwpu.myonlinetaxi.crontask;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import com.nwpu.myonlinetaxi.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
//    private static final Logger log = LoggerFactory.getLogger(TaxiUploadTask.class);

    @Scheduled(cron = "*/1 * * * * ?")
    public void taxiUpload(){
        List<String> list = new ArrayList<>();
        Map<String , TaxiMeta> map = TaxisInstance.getTaxiMap();
        for(Map.Entry<String , TaxiMeta> entry : map.entrySet()){
            list.add(entry.getValue().toString());
//            log.info(entry.getKey()+" " + entry.getValue().toString());
        }
        String res = System.currentTimeMillis()+" : "+ JSONObject.toJSONString(list);
//        log.error(res);
        CopyOnWriteArraySet<WebSocketServer> webSocketSet =
                WebSocketServer.getWebSocketSet();
        webSocketSet.forEach(c -> {
            try {
                if(c.getSid().equals("taxi"))
                    c.sendMessage(res);
             } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
