package nwpu.car.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import com.onenet.studio.acc.sdk.interfaces.OpenApiCallback;
import nwpu.car.api.dto.GeoLocationStructDTO;

import java.lang.Exception;
import java.lang.Integer;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 编译器自动生成，请勿修改<p/>
 *
 * {@link OpenApi} 扩展类，该类根据配置的物模型自动生成对应的上报与下发方法，开发者根据这些方法实现相应功能即可
 */
public class CarOpenApiExtention {
    /**
     * 物模型上报格式公共字段-id
     */
    private static final String ONEJSON_ID_KEY = "id";

    /**
     * 物模型上报格式公共字段-version
     */
    private static final String ONEJSON_VERSION_KEY = "version";

    /**
     * 物模型上报格式公共字段-version的值
     */
    private static final String ONEJSON_VERSION_VAL = "1.0";

    private OpenApi openApi;

    public CarOpenApiExtention(OpenApi openApi) {
        this.openApi = openApi;
    }

    /**
     * 单个属性功能点上报
     *
     *  @param geoLocation 标识符为geoLocation的属性功能点的值
     *  @param timeout 超时时间，单位为毫秒
     */
    public int geoLocationPropertyUpload(GeoLocationStructDTO geoLocation, long timeout) throws
            Exception {
        return this.propertyUpload(timeout, geoLocation);
    }

    /**
     * 设备属性上报
     * 该方法会上报参数值不为null的属性
     *
     *  @param timeout 调用超时时间，单位为毫秒
     *  @param geoLocation 标识符为geoLocation的属性功能点的值
     */
    public int propertyUpload(long timeout, GeoLocationStructDTO geoLocation) throws Exception {
        Map<String, Object> oneJson = new HashMap<>();
        long now = System.currentTimeMillis();
        String id = String.valueOf(now);
        oneJson.put(ONEJSON_ID_KEY, id);
        oneJson.put(ONEJSON_VERSION_KEY, ONEJSON_VERSION_VAL);
        Map<String, Object> params = new HashMap<>();
        if (!Objects.isNull(JSON.toJSON(geoLocation))) {
            Map<String, Object> val1 = new HashMap<>();
            val1.put("value", JSON.toJSON(geoLocation));
            val1.put("time", now);
            params.put("geoLocation", val1);
        }
        oneJson.put("params", params);
        String oneJsonStr = JSON.toJSONString(oneJson);
        return this.openApi.propertyPost(id, oneJsonStr, timeout);
    }

    /**
     * 设备期望值获取
     *
     *  @param identifiers 要获取期望值的属性的标识符(identifier)数组
     *  @param timeout 调用超时时间，单位为毫秒
     */
    public JSONObject propertyDesiredGet(String[] identifiers, long timeout) throws Exception {
        Map<String, Object> oneJson = new HashMap<>();
        long now = System.currentTimeMillis();
        String id = String.valueOf(now);
        oneJson.put(ONEJSON_ID_KEY, id);
        oneJson.put(ONEJSON_VERSION_KEY, ONEJSON_VERSION_VAL);
        Object params = JSON.toJSON(identifiers);
        oneJson.put("params", params);
        String oneJsonStr = JSON.toJSONString(oneJson);
        return this.openApi.desiredGet(id, oneJsonStr, timeout);
    }

    /**
     * 删除设备属性期望值
     *
     *  @param identifiers 要删除的期望值属性的标识符(String)和版本(int)的map，版本可在期望值获取时可知
     *  @param timeout 调用超时时间，单位为毫秒
     */
    public int propertyDesiredDel(long timeout, Map identifiers) throws Exception {
        Map<String, Object> oneJson = new HashMap<>();
        long now = System.currentTimeMillis();
        String id = String.valueOf(now);
        oneJson.put(ONEJSON_ID_KEY, id);
        oneJson.put(ONEJSON_VERSION_KEY, ONEJSON_VERSION_VAL);
        Map<String, Object> params = new HashMap<>();
        Set<String> keys = identifiers.keySet();
        for (String identifier : keys) {
            Map<String, Object> version = new HashMap<>();
            version.put("version", identifiers.get(identifier));
            params.put(identifier, version);
        }
        oneJson.put("params", params);
        String oneJsonStr = JSON.toJSONString(oneJson);
        return this.openApi.desiredDel(id, oneJsonStr, timeout);
    }

    /**
     * 设备属性设置订阅
     *
     *  @param callback 设备接收消息回调方法，由用户自定义实现
     */
    public void propertySet(OpenApiCallback callback) throws Exception {
        this.openApi.propertySetSubscribe(callback);
    }

    /**
     * 设备属性设置返回结果方法
     *
     *  @param messageId 消息id，设备属性设置中获取到的字段id的值
     *  @param code 返回状态码，200为成功
     *  @param msg 返回消息
     */
    public void propertySetReply(String messageId, Integer code, String msg) throws Exception {
        Map map = new HashMap();
        map.put("id", messageId);
        map.put("code", code);
        map.put("msg", msg);
        String replyStr = JSON.toJSONString(map);
        this.openApi.propertySetPublish(replyStr);
    }

    /**
     * 设备属性d获取订阅
     *
     *  @param callback 设备接收消息回调方法，由用户自定义实现
     */
    public void propertyGet(OpenApiCallback callback) throws Exception {
        this.openApi.propertyGetSubscribe(callback);
    }

    /**
     * 设备属性获取返回结果方法
     *
     *  @param messageId 消息id，设备属性设置中获取到的字段id的值
     *  @param code 返回状态码，200为成功
     *  @param msg 返回消息
     *  @param data 属性值数据，key为标识符(identifier),value为对应的数据值
     */
    public void propertyGetReply(String messageId, Integer code, String msg, Map data) throws
            Exception {
        Map map = new HashMap();
        map.put("id", messageId);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", JSON.toJSON(data));
        String replyStr = JSON.toJSONString(map);
        this.openApi.propertyGetPublish(replyStr);
    }

    /**
     * 设备事件上报
     *
     *  @param timeout 调用超时时间,单位为毫秒
     */
    public int eventUpload(long timeout) throws Exception {
        Map<String, Object> oneJson = new HashMap<>();
        long now = System.currentTimeMillis();
        String id = String.valueOf(now);
        oneJson.put(ONEJSON_ID_KEY, id);
        oneJson.put(ONEJSON_VERSION_KEY, ONEJSON_VERSION_VAL);
        Map<String, Object> params = new HashMap<>();
        oneJson.put("params", params);
        String oneJsonStr = JSON.toJSONString(oneJson);
        return this.openApi.eventPost(id, oneJsonStr, timeout);
    }
}
