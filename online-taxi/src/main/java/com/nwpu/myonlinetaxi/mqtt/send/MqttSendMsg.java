package com.nwpu.myonlinetaxi.mqtt.send;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * @author Junho
 * @date 2022/7/23 14:02
 */
@Component
@Slf4j
public class MqttSendMsg {

    public static void sendMqttMsg(String topic , String msg){
        MqttClient client = MqttFactory.getInstance();
        // 创建消息
        MqttMessage message = new MqttMessage(msg.getBytes());
        //  设置消息的服务质量
        // 0:最多一次传送 (只负责传送，发送过后就不管数据的传送情况)
        // 1:至少一次传送 (确认数据交付)
        // 2:正好一次传送 (保证数据交付成功)
        message.setQos(1);
        //  发布消息
        try {
            client.publish(topic, message);
//            log.info("topic：{} =》 发送成功", topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
//        //  断开连接
//        client.disconnect();
//        // 关闭客户端
//        client.close();
    }
}
