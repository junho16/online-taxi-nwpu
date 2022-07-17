package nwpu.iotbridge;

import nwpu.iotbridge.exception.MqttConnectException;
import nwpu.iotbridge.mqtt.MqttFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Junho
 * @date 2022/7/17 14:39
 */
@SpringBootApplication
public class IoTBridgeOneNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(IoTBridgeOneNetApplication.class, args);
        createMqttClient();
    }

    //创建MQTT实例
    private static void createMqttClient() {
        try {
            MqttFactory.getInstance();
        } catch (MqttConnectException e) {
            e.printStackTrace();
        }
    }
}
