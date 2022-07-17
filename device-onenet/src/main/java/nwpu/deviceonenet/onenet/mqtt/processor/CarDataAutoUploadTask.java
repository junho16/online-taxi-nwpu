package nwpu.deviceonenet.onenet.mqtt.processor;

import com.onenet.studio.acc.sdk.OpenApi;
import nwpu.deviceonenet.onenet.mqtt.MqttClientBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

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
