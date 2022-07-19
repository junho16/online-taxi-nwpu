package nwpu.deviceonenet.onenet.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.onenet.studio.acc.sdk.OpenApi;
import lombok.extern.slf4j.Slf4j;
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
public class CarInit {

    //设备接入路径
    @Value("${devices.bean.url}")
    private String url;

    //设备产品id
    @Value("${devices.bean.product_id}")
    private String productId;

    //设备json文件名
    @Value("${devices.bean.file_name}")
    private String fileName;

    /**
     * 创建设备的openAPI
     * @return
     */
    @Bean(name = "openApiMap")
    public ConcurrentHashMap<String , OpenApi> creatDeviceOpenAPI() {
        ConcurrentHashMap<String , OpenApi> openApiMap = new ConcurrentHashMap<>();
        JSONArray devices = JSON.parseArray(readJsonFile(fileName));
        if (devices != null) {
            for (int i = 0; i < devices.size(); i++) {
                //设备json数据
                JSONObject device = devices.getJSONObject(i);
                try {
                    OpenApi openApi = OpenApi.Builder.newBuilder()
                            .url(url)
                            .productId(productId)
                            .devKey(device.getString("name"))
                            .accessKey(device.getString("sec_key"))
                            .build();
                    openApiMap.put((String) device.get("taxi_id"), openApi);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("网约车OpenApi创建异常：productId：{}，deviceName：{}，sec_key：{}", productId, device.getString("name"), device.getString("sec_key"));
                }
            }
        }
        log.info("网约车OpenApi创建完毕：已加载{}个网约车设备" , openApiMap.size());
        return openApiMap;
    }

    public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            InputStream inputStream = CarInit.class.getResourceAsStream(fileName);
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = inputStream.read()) != -1) {
                sb.append((char) ch);
            }
            inputStream.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
