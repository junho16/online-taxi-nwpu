package nwpu.deviceonenet.onenet.mqtt;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Data
@Slf4j
public class MqttClientBuilder {

    @Resource
    private MqttConfig mqttConfig;

    @Bean
    public MqttClient init() throws MqttException {
        MqttClient ret = new MqttClient(mqttConfig.getServerURI(), mqttConfig.getClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttConfig.getUsername());
        options.setPassword(mqttConfig.getPassword().toCharArray());
        options.setCleanSession(false);
        options.setKeepAliveInterval(mqttConfig.getKeepAliveInterval());
        options.setAutomaticReconnect(true);

        ret.connect(options);

        String topic = "tian_rsu/ecnu/#";

        ret.subscribe(topic, mqttConfig.getQos(), new IMqttMessageListener() {
            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                try {
                    String msg = new String(mqttMessage.getPayload());
//                    log.info("==>" + msg);
                    if(msg != null && !"".equals(msg)){
                        JSONObject jsonObject = JSONObject.parseObject(msg);
                    }
                } catch (Exception e) {
                    log.info("处理mqtt消息异常:" + e);
                }
            }
        });
        return ret;
    }

    private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue();

}
