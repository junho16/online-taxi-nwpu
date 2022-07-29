//package nwpu.deviceonenet.onenet.mqtt.tian;
//
//import com.alibaba.fastjson.JSONObject;
//import com.nwpu.myonlinetaxi.entity.TrafficLight;
//import com.nwpu.myonlinetaxi.mqtt.tian.config.MqttTianConfig;
//import com.nwpu.myonlinetaxi.mqtt.tian.dto.TrafficLightDTO;
//import com.nwpu.myonlinetaxi.mqtt.tian.entity.LightGroup;
//import com.nwpu.myonlinetaxi.mqtt.tian.entity.LightInfo;
//import lombok.Data;
//import org.eclipse.paho.client.mqttv3.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * 获取天安物联信号灯信息
// */
//@Component
//@Data
//public class TianMqttClient {
//    private static final Logger log = LoggerFactory.getLogger(TianMqttClient.class);
//
//    @Resource
//    private MqttTianConfig mqttConfig;
//
//    @Bean
//    public MqttClient init() throws MqttException {
//        MqttClient ret = new MqttClient(mqttConfig.getServerURI(), mqttConfig.getClientId());
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setUserName(mqttConfig.getUsername());
//        options.setPassword(mqttConfig.getPassword().toCharArray());
//        options.setCleanSession(false);
//        options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
//        options.setAutomaticReconnect(true);
//
//        ret.connect(options);
//
//        // 将数据的过滤放在mqtt
//        String topic = "tian_rsu/ecnu/#";
//        ret.subscribe(topic, mqttConfig.getQos(), new IMqttMessageListener() {
//            @Override
//            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//                try {
//                    String msg = new String(mqttMessage.getPayload());
//                    if(msg != null && !"".equals(msg)){
//                        JSONObject jsonObject = JSONObject.parseObject(msg);
//                        String tag = jsonObject.getString("tag");
//
//                        if("2504".equals(tag)) {
//                            TrafficLightDTO trafficLightDTO = JSONObject.parseObject(msg, TrafficLightDTO.class);
//                            String deviceSN = trafficLightDTO.getDevice_sn();
//                            if(lightMap.containsKey(deviceSN)){// 信号灯sn
//                                TrafficLight light = handleLightMessage(JSONObject.parseObject(msg, TrafficLightDTO.class));
//                                lightMap.get(deviceSN).setTrafficInfo(light.getColor() , light.getLeftTime());
////                                log.info("deviceSN:{},Light=>{}", deviceSN , light.getColor()+" " + light.getLeftTime());
//                             }
//                        }
//                    }
//                } catch (Exception e) {
//                    log.info("处理mqtt消息异常:" + e);
//                }
//            }
//        });
//
//        return ret;
//    }
//
//    private ConcurrentHashMap<String , TrafficLight> lightMap = new ConcurrentHashMap();
//
//    @Value("${light_sn}")
//    String light_sn;
//
//    @PostConstruct
//    public void initQueue(){
//        String[] ls = light_sn.split(",");
//        for(int i = 0 ; i < ls.length ; i++){
//            lightMap.put(ls[i] , new TrafficLight());
//        }
//    }
//
//    private TrafficLight handleLightMessage(TrafficLightDTO trafficLightDTO) {
//        List<LightInfo> LightInfoList = trafficLightDTO.getLight_info_list();
//        LightInfo lightInfo = LightInfoList.get(0);
//        List<LightGroup> groupList = lightInfo.getGroup_list();
//        LightGroup lightGroup = groupList.get(0);
////        double lat = trafficLightDTO.getLat();
////        double lon = trafficLightDTO.getLon();
//        int color = lightGroup.getG_color();
//        int leftTime = lightGroup.getG_time();
//        return new TrafficLight(color , leftTime);
////        TrafficLightStructDTO trafficLightStructDTO = new TrafficLightStructDTO(lat, lon, color, leftTime);
////        log.info(trafficLightStructDTO.toString());
////        return trafficLightStructDTO;
//    }
//}
