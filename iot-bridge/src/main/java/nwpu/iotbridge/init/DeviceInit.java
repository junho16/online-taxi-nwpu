package nwpu.iotbridge.init;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import nwpu.iotbridge.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/30 13:57
 */
@Component
public class DeviceInit {

    @Value("${devices.bean.productId.taxi}")
    private String productStr;

    @Value("${devices.bean.productId.passenger}")
    private String passengerStr;

    @Bean("product_passenger_set")
    public Set<String> initPassengerDevice(){
        Set<String> deviceSet = new HashSet<>();
        String[] strings = passengerStr.split(",");
        for(int i = 0 ; i < strings.length ;i++){
            deviceSet.add(strings[i]);
        }
        return deviceSet;
    }

    @Bean("product_taxi_set")
    public Set<String> initTaxiDevice(){
        Set<String> deviceSet = new HashSet<>();
        String[] strings = productStr.split(",");
        for(int i = 0 ; i < strings.length ;i++){
            deviceSet.add(strings[i]);
        }
        return deviceSet;
    }
}


