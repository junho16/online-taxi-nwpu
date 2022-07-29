package nwpu.deviceonenet.onenet.crontask;

import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import lombok.extern.slf4j.Slf4j;
import nwpu.deviceonenet.api.dto.PassengerListStructDTO;
import nwpu.deviceonenet.api.dto.TaxiListStructDTO;
import nwpu.deviceonenet.api.extention.PassengerOpenApiExtention;
import nwpu.deviceonenet.api.extention.TaxiOpenApiExtention;
import nwpu.deviceonenet.onenet.entity.meta.PassengerMeta;
import nwpu.deviceonenet.onenet.init.PassengersInstance;
import nwpu.deviceonenet.onenet.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Junho
 * @date 2022/7/22 13:52
 */
@EnableScheduling
@Component
@Slf4j
public class PassengerUploadTask {

    @Resource
    ConcurrentHashMap<String , OpenApi> passengerOpenApiMap;

    @Scheduled(cron = "*/1 * * * * ?")
    public void passengerUpload() {
        List<PassengerMeta> list = new ArrayList<>();
        Map<String, PassengerMeta> map = PassengersInstance.getPassengerMap();
        Map<Integer , List<PassengerMeta>> groupMap = new ConcurrentHashMap<>();
        for (Map.Entry<String, PassengerMeta> entry : map.entrySet()) {
            List<PassengerMeta> groupItemList = groupMap.getOrDefault(entry.getValue().getPassenger_group() , new ArrayList<>());
            groupItemList.add(entry.getValue());
            groupMap.put(entry.getValue().getPassenger_group() , groupItemList);

            list.add(entry.getValue());
        }
        for(Map.Entry<Integer, List<PassengerMeta>> entry : groupMap.entrySet()){
            OpenApi openApi = passengerOpenApiMap.get(entry.getKey());
            if(openApi != null){
                List<PassengerMeta> passengerList = entry.getValue();
                PassengerListStructDTO[] passengerListStructDTOS = new PassengerListStructDTO[passengerList.size()];
                for(int i = 0 ; i < passengerList.size() ; i++){
                    passengerListStructDTOS[i] = new PassengerListStructDTO(
                        passengerList.get(i).getPassenger_id(),
                        passengerList.get(i).getLon(),
                        passengerList.get(i).getLat(),
                        passengerList.get(i).getState(),
                        passengerList.get(i).getPassenger_group()
                    );
                }
                try {
                    //TODO-将长度扩展到10
                    PassengerOpenApiExtention apiExt = new PassengerOpenApiExtention(openApi);
                    apiExt.passengerListPropertyUpload( passengerListStructDTOS , 500 );
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("上报乘客数据失败 ，异常为{}." , e.getMessage());
                }
            }else{
                log.info("上报数据失败 ，组别 为 {} 的乘客组未加载." , entry.getKey());
            }
        }

//        MqttPassengerDto mqttPassengerDto = new MqttPassengerDto("passenger" , System.currentTimeMillis() , list);
//        String res = JSONObject.toJSONString(mqttPassengerDto);
//        log.info("passengers ==》 {}" , res);
//        MqttSendMsg.sendMqttMsg(passengerTopic , res);
//
////        log.error(res);
        String res = JSONObject.toJSONString(list);
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
