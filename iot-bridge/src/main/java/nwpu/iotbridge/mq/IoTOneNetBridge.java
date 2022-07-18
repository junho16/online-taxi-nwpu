package nwpu.iotbridge.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.cm.heclouds.onenet.studio.api.IotClient;
import com.github.cm.heclouds.onenet.studio.api.IotProfile;
import com.github.cm.heclouds.onenet.studio.api.entity.application.device.CallServiceRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.device.CallServiceResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.group.AddGroupDeviceRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.group.AddGroupDeviceResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.group.CreateGroupRequest;
import com.github.cm.heclouds.onenet.studio.api.entity.application.group.CreateGroupResponse;
import com.github.cm.heclouds.onenet.studio.api.entity.application.project.*;
import com.github.cm.heclouds.onenet.studio.api.entity.common.*;
import com.github.cm.heclouds.onenet.studio.api.exception.IotClientException;
import com.github.cm.heclouds.onenet.studio.api.exception.IotServerException;
import lombok.extern.slf4j.Slf4j;
import nwpu.iotbridge.exception.SendMessageException;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/17 16:38
 */
@Service
@Slf4j
public class IoTOneNetBridge {

    //kafka消费生产者
    @Resource
    private KafkaProducer kafkaProducer;

    // 设备属性消息队列
    @Value("${topic.oneNetBridge.deviceProperty}")
    private String topicDevicePropertyTopic;

    @Value("${iotDeviceConfig_filename}")
    public String iotDeviceConfigFilename;

    /**
     * 将OneNet收到的网约车位置信息转发至CPS
     *
     * @param iotEventMessage 消息内容
     * @throws SendMessageException 消息发送异常
     */
    public void sendDevicePropertyToCPS(String iotEventMessage) throws SendMessageException {
        try {
            JSONObject eventMsgJson = JSONObject.parseObject(iotEventMessage);

//            // 获取元数据需要根据设备iotUuid查询到设备元数据
            String iotUuid = eventMsgJson.getString("productId") + "_" + eventMsgJson.getString("deviceName");

            // 消息的数据值
            JSONObject msgParams = eventMsgJson.getJSONObject("data").getJSONObject("params");
            if (msgParams != null) {
                for (String msgKey : msgParams.keySet()) {
                    // 判断iot消息的的属性名称是否存在于元数据中。
                    try {
                        // 拼装 属性值 和 时间
                        JSONObject msgValJson = msgParams.getJSONObject(msgKey).getJSONObject("value");
                        msgValJson.put("uploadTime", msgParams.getJSONObject(msgKey).getString("time"));
                        kafkaProducer.send(topicDevicePropertyTopic, iotEventMessage);
                    } catch (Exception e) {
                        //记录不支持字段异常信息，目前只处理设备需要的映射属性名称，其他属性做以记录
                        log.warn("发送 属性消息 至 CPS 异常，异常信息：{}", e.getMessage());
                    }
                }
            } else {
                log.error("无{}对应的设备元数据！", iotUuid);
            }
        } catch (Exception e) {
            throw new SendMessageException("设备事件转发异常：{}" + e.getMessage(), e);
        }
    }

}
