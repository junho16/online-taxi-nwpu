package nwpu.iotbridge.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nwpu.iotbridge.exception.MqttConnectException;
import nwpu.iotbridge.exception.MqttSubOrPubException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * @author Junho
 * @date 2022/7/17 16:25
 */
@Slf4j
public class MqttUtil {

    /**
     * 生成token认证
     *
     * @param version         签名算法版本
     * @param resourceName    访问资源实例名称
     * @param expirationTime  过期时间
     * @param signatureMethod 签名方法
     * @param accessKey       密钥
     * @return String
     */
    public static String assembleToken(String version, String resourceName, String expirationTime,
                                       String signatureMethod, String accessKey) throws MqttConnectException {

        StringBuilder sb = null;
        try {
            sb = new StringBuilder();
            String res = URLEncoder.encode(resourceName, "UTF-8");
            String sig = URLEncoder.encode(generatorSignature(version, resourceName, expirationTime
                    , accessKey, signatureMethod), "UTF-8");
            sb.append("version=")
                    .append(version)
                    .append("&res=")
                    .append(res)
                    .append("&et=")
                    .append(expirationTime)
                    .append("&method=")
                    .append(signatureMethod)
                    .append("&sign=")
                    .append(sig);
        } catch (UnsupportedEncodingException e) {
            throw new MqttConnectException("生成token失败：" + e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * 生成签名
     *
     * @param version         签名算法版本
     * @param resourceName    访问资源实例名称
     * @param expirationTime  过期时间
     * @param accessKey       密钥
     * @param signatureMethod 签名方法
     * @return String
     * @throws MqttConnectException mqtt客户端连接异常
     */
    public static String generatorSignature(String version, String resourceName, String expirationTime,
                                            String accessKey, String signatureMethod) throws MqttConnectException {
        String encryptText = expirationTime + "\n" + signatureMethod + "\n" + resourceName + "\n" + version;
        String signature;
        byte[] bytes = HmacEncrypt(encryptText, accessKey, signatureMethod);
        signature = Base64.getEncoder().encodeToString(bytes);
        return signature;
    }

    /**
     * 加密
     *
     * @param data            加密数据
     * @param key             密钥
     * @param signatureMethod 签名方法
     * @return byte[]
     * @throws MqttConnectException mqtt客户端连接异常
     */
    public static byte[] HmacEncrypt(String data, String key, String signatureMethod) throws MqttConnectException {
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKeySpec signinKey = null;
        Mac mac = null;
        try {
            signinKey = new SecretKeySpec(Base64.getDecoder().decode(key),
                    "Hmac" + signatureMethod.toUpperCase());

            //生成一个指定 Mac 算法 的 Mac 对象
            mac = null;
            mac = Mac.getInstance("Hmac" + signatureMethod.toUpperCase());

            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new MqttConnectException("加密失败：" + e.getMessage(), e);
        }

        //完成 Mac 操作
        return mac.doFinal(data.getBytes());
    }

    /**
     * 获取SSLSocketFactory
     *
     * @param caCrtFile 证书文件
     * @return SSLSocketFactory
     * @throws MqttConnectException mqtt客户端连接异常
     */
    public static SSLSocketFactory getSocketFactory(final InputStream caCrtFile) throws MqttConnectException {
        Security.addProvider(new BouncyCastleProvider());
        SSLContext context = null;
        try {
            //===========加载 ca 证书==================================
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            if (null != caCrtFile) {
                // 加载本地指定的 ca 证书
                PEMReader reader = new PEMReader(new InputStreamReader(caCrtFile));
                X509Certificate caCert = (X509Certificate) reader.readObject();
                reader.close();

                // CA certificate is used to authenticate server
                KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
                caKs.load(null, null);
                caKs.setCertificateEntry("ca-certificate", caCert);
                // 把ca作为信任的 ca 列表,来验证服务器证书
                tmf.init(caKs);
            } else {
                //使用系统默认的安全证书
                tmf.init((KeyStore) null);
            }

            // ============finally, create SSL socket factory==============
            context = SSLContext.getInstance("TLSv1.2");
            context.init(null, tmf.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException | KeyManagementException | CertificateException | KeyStoreException | IOException e) {
            throw new MqttConnectException("证书加载处理失败：" + e.getMessage(), e);
        }
        return context.getSocketFactory();
    }

    /**
     * 订阅主题
     *
     * @param topic               主题
     * @param mqttMessageListener 消息监听处理器
     * @throws MqttConnectException  mqtt客户端连接异常
     * @throws MqttSubOrPubException mqtt客户端订阅或发布异常
     */
    public static void subscribe(String topic, int qos, IMqttMessageListener mqttMessageListener) throws MqttConnectException, MqttSubOrPubException {
        try {
            MqttClient client = MqttFactory.getInstance();
            client.subscribe(topic, mqttMessageListener);
        } catch (MqttException e) {
            throw new MqttSubOrPubException("MQTT: 订阅主题失败：" + e.getMessage(), e);
        }
    }

    /**
     * 发送消息
     *
     * @param topic 主题
     * @param data  消息内容
     * @throws MqttConnectException  mqtt客户端连接异常
     * @throws MqttSubOrPubException mqtt客户端订阅或发布异常
     */
    public static void send(String topic, Object data) throws MqttConnectException, MqttSubOrPubException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 获取客户端实例
            MqttClient client = MqttFactory.getInstance();
            // 转换消息为json字符串
            String json = mapper.writeValueAsString(data);
            client.publish(topic, new MqttMessage(json.getBytes(StandardCharsets.UTF_8)));
        } catch (JsonProcessingException e) {
            throw new MqttSubOrPubException("MQTT: 主题" + topic + "发送消息转换json失败：" + e.getMessage(), e);
        } catch (MqttException e) {
            throw new MqttSubOrPubException("MQTT: 主题" + topic + "发送消息失败：" + e.getMessage(), e);
        }
    }
}
