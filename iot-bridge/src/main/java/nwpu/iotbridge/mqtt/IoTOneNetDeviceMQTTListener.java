package nwpu.iotbridge.mqtt;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import nwpu.iotbridge.exception.SendMessageException;
import nwpu.iotbridge.mq.IoTOneNetBridge;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

/**
 * @author Junho
 * @date 2022/7/17 15:25
 */
@Slf4j
@Component
public class IoTOneNetDeviceMQTTListener implements IMqttMessageListener {

    @Resource
    private IoTOneNetBridge ioTOneNetBridge;

    /**
     * 将获取到（获取方式不限于监听iot的设备mq、扫描iot北向接口等手段）的设备属性信息 转发到北向队列
     *
     * @param topic      该设备在北向设备属性kafka中的队列名称
     * @param iotMessage 该设备在iot中的原始报文，本方法需负责将该报文转码为北向标准编码
     */
    public void forwardIoTDeviceMessageToCPS(String topic, String iotMessage) {
        /*
        接收到的OneNet消息分类：
            物模型数据（设备属性事件上报）notifyType = "property"为属性；notifyType ="event"为事件
            设备生命周期（在线、离线）lifeCycle
            场景联动触发日志  sceneLog
            物模型数据（设备服务调用）
            设备位置数据
         */
        JSONObject iotMessageJson = JSONObject.parseObject(iotMessage);
        String messageType = iotMessageJson.getString("messageType");
        log.info("topic: {} , message: {}" , topic , iotMessage);
        try {
            //转发仅需要的消息
            if ("notify".equals(messageType)) {
                //区分事件/属性
                if ("property".equals(iotMessageJson.get("notifyType"))) {
                    log.info("属性变化！");
//                    ioTOneNetBridge.sendDevicePropertyToCPS(iotMessage);
//                    ioTOneNetBridge.sendDevicePropertyToCPS(iotMessage);
                } else if ("event".equals(iotMessageJson.get("notifyType"))) {
//                    ioTOneNetBridge.sendDeviceEventToCPS(iotMessage);
                }
            } else if ("lifeCycle".equals(messageType)) {
//                ioTOneNetBridge.sendDevicePropertyToCPS(iotMessage);
            } else {
                //区分消息为位置上报
                if (iotMessageJson.getString("type") != null) {
//                    ioTOneNetBridge.sendDevicePropertyToCPS(iotMessage);
                } else {
                    log.debug("收到IOT消息：队列名：{}；消息内容：{}", topic, iotMessage);
                }
            }
        } catch (Exception e) {
            log.error("消息转发异常：{}", e.getMessage());
        }
    }


    /**
     * 接收到mqtt消息方法
     *
     * @param topic       主题
     * @param mqttMessage mqtt消息
     * @throws Exception 异常
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        byte[] payload = mqttMessage.getPayload();
        OnenetMq.Msg obj = OnenetMq.Msg.parseFrom(payload);
        long at = obj.getTimestamp();
        long msgId = obj.getMsgid();
        String body = new String(obj.getData().toByteArray());

        SimpleDateFormat slf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = slf.format(at);

//        logger.debug("MQTT消息详情：time = " + time + ",msg id: " + msgId + ",topic: " + topic + ", body: " + body);
//        //将物联网设备消息转发至CPS端
//        this.forwardIoTDeviceMessageToCPS(topic, body);

//      =.= FIXME 暂时只处理车辆的
        JSONObject bodyObj = JSONObject.parseObject(body);
        if(bodyObj.get("productId").equals("hlcv91wiJ7")){
            //将物联网设备消息转发至CPS端
            log.info("MQTT消息详情：time = " + time + ",msg id: " + msgId + ",topic: " + topic + ", body: " + body);
            this.forwardIoTDeviceMessageToCPS(topic, body);
        }
    }
}
