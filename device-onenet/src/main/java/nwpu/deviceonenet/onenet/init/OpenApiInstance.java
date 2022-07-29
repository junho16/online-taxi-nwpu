package nwpu.deviceonenet.onenet.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import lombok.extern.slf4j.Slf4j;
import nwpu.deviceonenet.onenet.entity.enums.DeviceType;
import nwpu.deviceonenet.onenet.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/16 22:34
 */
@Component
@Slf4j
public class OpenApiInstance {

    //设备接入路径
    @Value("${devices.bean.url}")
    private String url;

    //设备产品id
    @Value("${devices.bean.taxi.product_id}")
    private String taxiProductId;

    //设备产品id
    @Value("${devices.bean.passenger.product_id}")
    private String passengerProductId;

    //设备json文件名
    @Value("${devices.bean.taxi.file_name}")
    private String taxiFileName;

    //设备json文件名
    @Value("${devices.bean.passenger.file_name}")
    private String passengerFileName;

    /**
     * 创建网约车车辆组设备的openAPI
     * @return
     */
    @Bean(name = "taxiOpenApiMap")
    public ConcurrentHashMap<Integer , OpenApi> creatTaxiDeviceOpenAPI() {
        return creatDeviceOpenAPI(taxiFileName ,  taxiProductId , DeviceType.taxi.getDeviceType());
    }

    /**
     * 创建网约车乘客组的openAPI
     * @return
     */
    @Bean(name = "passengerOpenApiMap")
    public ConcurrentHashMap<Integer , OpenApi> creatPassengerDeviceOpenAPI() {
        return creatDeviceOpenAPI(passengerFileName , passengerProductId , DeviceType.passenger.getDeviceType());
    }

    public ConcurrentHashMap<Integer , OpenApi> creatDeviceOpenAPI(String fileName , String productId , String DeviceType) {
        ConcurrentHashMap<Integer , OpenApi> openApiMap = new ConcurrentHashMap<>();
        JSONArray devices = JSON.parseArray(JsonUtil.readJsonFile(fileName));
        if (devices != null) {
            for (int i = 0; i < devices.size(); i++) {
                JSONObject device = devices.getJSONObject(i);
                try {
                    OpenApi openApi = OpenApi.Builder.newBuilder()
                            .url(url)
                            .productId(productId)
                            .devKey(device.getString("name"))
                            .accessKey(device.getString("sec_key"))
                            .build();
                    openApiMap.put(device.getInteger("group_id"), openApi);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("OpenApi创建异常：productId：{}，deviceName：{}，sec_key：{}", productId, device.getString("name"), device.getString("sec_key"));
                }
            }
        }
        log.info("OpenApi创建完毕：已加载{}个 {} 设备" , openApiMap.size() , DeviceType);
        return openApiMap;
    }

}
