package nwpu.deviceonenet.onenet.crontask;

import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import lombok.extern.slf4j.Slf4j;
import nwpu.deviceonenet.api.dto.TaxiListStructDTO;
import nwpu.deviceonenet.api.extention.TaxiOpenApiExtention;
import nwpu.deviceonenet.onenet.entity.meta.TaxiMeta;
import nwpu.deviceonenet.onenet.init.TaxisInstance;
import nwpu.deviceonenet.onenet.websocket.WebSocketServer;
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
 * 定时上报网约车信息
 * @author Junho
 * @date 2022/7/22 13:41
 */
@EnableScheduling
@Component
@Slf4j
public class TaxiUploadTask {

    @Resource
    ConcurrentHashMap<String , OpenApi> taxiOpenApiMap;

    @Scheduled(cron = "*/1 * * * * ?")
    public void taxiUpload(){
        List<String> wsTes = new ArrayList<>();

        Map<String , TaxiMeta> map = TaxisInstance.getTaxiMap();

        //初始化车组
//        int groupNum = map.size() % 10 == 0 ? map.size() / 10 : map.size() / 10 + 1;
        Map<Integer , List<TaxiMeta>> groupMap = new ConcurrentHashMap<>();
//        for(int i = 1 ; i <= groupNum ; i++){
//            groupMap.put(i , new ArrayList());
//        }
        for(Map.Entry<String , TaxiMeta> entry : map.entrySet()){
            wsTes.add(entry.getValue().toString());

            List<TaxiMeta> groupItemList = groupMap.getOrDefault(entry.getValue().getTaxi_group() , new ArrayList<TaxiMeta>());
            groupItemList.add(entry.getValue());
//            groupItemList.add(new TaxiDto(
//                entry.getValue().getTaxi_id(),
//                entry.getValue().getLon(),
//                entry.getValue().getLat(),
//                entry.getValue().getState(),
//                entry.getValue().getSpeed(),
//                entry.getValue().getTaxi_group(),
//                entry.getValue().getTaxi_name()
//            ));
            groupMap.put(entry.getValue().getTaxi_group() , groupItemList);
//            list.add(new TaxiDto(
//                entry.getValue().getTaxi_id(),
//                entry.getValue().getLon(),
//                entry.getValue().getLat(),
//                entry.getValue().getState(),
//                entry.getValue().getSpeed()
//            ));
//            log.info(entry.getKey()+" " + entry.getValue().toString());
//            DetectorMeta detectorMeta = crossingService.getMinDisDetector(entry.getValue().getLon() , entry.getValue().getLat());
//            String suggest = "按照原速度行驶";
//            boolean isCrossing = false;
//            try{
//                if(detectorMeta != null){
//                    isCrossing = true;
//                    suggest = Guidance.getSuggest(
//                            (int) disTanceService.calcDistance(entry.getValue().getLon() , entry.getValue().getLat() , detectorMeta.getLight_pos().getLon() , detectorMeta.getLight_pos().getLat())   ,
//                            (int) entry.getValue().getSpeed(),
//                            tianMqttClient.getLightMap().get(detectorMeta.getLight_sn()).getColor(),
//                            tianMqttClient.getLightMap().get(detectorMeta.getLight_sn()).getLeftTime()
//                    );
//                }
//            }catch (Exception e){
//                log.error("检测车辆{}路口的行驶情况失败：{}",entry.getValue().getTaxi_name() , e.getMessage());
//            }
//            list.add(new TaxiDto(
//                entry.getValue().getTaxi_id(),
//                entry.getValue().getLon(),
//                entry.getValue().getLat(),
//                entry.getValue().getState(),
//                isCrossing,
//                entry.getValue().getSpeed(),
//                suggest
//            ));
//            log.info(entry.getKey()+" " + entry.getValue().toString());
        }

        for(Map.Entry<Integer, List<TaxiMeta>> entry : groupMap.entrySet()){
            OpenApi openApi = taxiOpenApiMap.get(entry.getKey());
            if(openApi != null){
                List<TaxiMeta> list = entry.getValue();
                TaxiListStructDTO[] taxiListStructDTOS = new TaxiListStructDTO[list.size()];
                for(int i = 0 ; i < list.size() ; i++){
                    taxiListStructDTOS[i] = new TaxiListStructDTO(
                        list.get(i).getTaxi_id(),
                        list.get(i).getTaxi_name(),
                        list.get(i).getLon(),
                        list.get(i).getLat(),
                        list.get(i).getState(),
                        list.get(i).getSpeed(),
                        list.get(i).getTaxi_group()
                    );
                }
//                GeoLocationStructDTO geoLocationStructDTO = new GeoLocationStructDTO(
//                        FormatUtil.getPosFormatDouble(lat) , FormatUtil.getPosFormatDouble(lon));
                try {
                    //TODO-将长度扩展到10
                    TaxiOpenApiExtention apiExt = new TaxiOpenApiExtention(openApi);
                    apiExt.taxiListPropertyUpload( taxiListStructDTOS , 500 );
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("上报网约车数据失败 ，异常为{}." , e.getMessage());
                }
            }else{
                log.info("上报数据失败 ，组别 为 {} 的网约车组未加载." , entry.getKey());
            }
        }



//        MqttTaxiDto mqttTaxiDto = new MqttTaxiDto("taxi" , System.currentTimeMillis() , list);
//        String res = JSONObject.toJSONString(mqttTaxiDto);
//        log.info("taxi ==》 {}" , res);
//        MqttSendMsg.sendMqttMsg(taxiTopic , res);

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
