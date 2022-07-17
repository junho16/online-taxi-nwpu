package nwpu.iotbridge.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * kafka消息生产者类：用于kafka发送消息到指定队列
 */
@Component
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 消息发送
     *
     * @param topic         消息队列
     * @param messageObject 消息对象
     */
    public void send(String topic, Object messageObject) {
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, messageObject);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                logger.error(topic + " - 生产者 发送消息失败：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //成功的处理
                logger.debug(topic + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
            }
        });
    }
}
