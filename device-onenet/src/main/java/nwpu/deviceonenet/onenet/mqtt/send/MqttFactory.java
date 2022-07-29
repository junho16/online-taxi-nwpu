//package nwpu.deviceonenet.onenet.mqtt.send;
//
//import com.nwpu.myonlinetaxi.config.MqttServerConfig;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//
///**
// * @author Junho
// * @date 2022/7/23 14:04
// */
//@Component
//public class MqttFactory {
//
//    private static MqttClient client;
//
//    @Resource
//    private MqttServerConfig mqttServerConfig;
//
//    private static MqttServerConfig mqttConfig;
//
//    @PostConstruct
//    public void initListener() {
//        mqttConfig = this.mqttServerConfig;
//    }
//
//    /**
//     * 单例 获取客户端实例
//     */
//    public static MqttClient getInstance(){
//        if (client == null) {
//            init();
//        }
//        return client;
//    }
//
//    /**
//     * 初始化客户端
//     */
//    public static void init(){
//        //MQTT协议前缀
//        String urlFrontSuffix = mqttConfig.getUrlFrontSuffix();
//        String clienId = mqttConfig.getClienId();
//        try{
//            MemoryPersistence memoryPersistence = new MemoryPersistence();
//            // 创建客户端
//            client = new MqttClient(urlFrontSuffix, clienId, memoryPersistence);
//            // 创建连接参数
//            MqttConnectOptions connOpts = new MqttConnectOptions();
//            // 创建连接
//            client.connect(connOpts);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}