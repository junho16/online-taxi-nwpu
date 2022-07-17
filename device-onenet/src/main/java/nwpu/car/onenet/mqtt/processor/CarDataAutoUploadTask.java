package nwpu.car.onenet.mqtt.processor;

import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import nwpu.car.onenet.mqtt.MqttClientBuilder;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 通过mqtt上报网约车实时位置
 * @author Junho
 * @date 2022/7/16 21:32
 */
@Component
@EnableScheduling
public class CarDataAutoUploadTask implements ApplicationListener<ContextRefreshedEvent>{

    @Resource
    ConcurrentHashMap<String , OpenApi> openApiMap;

    @Resource
    MqttClientBuilder mqttClientBuilder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        while (true){

        }
    }
}
