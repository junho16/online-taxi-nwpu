package nwpu.deviceonenet.onenet.init;

import com.alibaba.fastjson.JSONObject;
import nwpu.deviceonenet.onenet.entity.meta.PassengerMeta;
import nwpu.deviceonenet.onenet.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/16 17:08
 */
@Component
public class PassengersInstance {

    //用户json文件名
    private static String passengerFileName;

    @Value("${init.bean.passenger_file_name}")
    public void setFileName(String passenger_file_name) {
        passengerFileName = passenger_file_name;
    }

    private static ConcurrentHashMap<String , PassengerMeta> passengerMap;

    private PassengersInstance(){
    }

    @PostConstruct
    public void initPassenger(){
        initPassengerMap();
    }

    public static ConcurrentHashMap<String , PassengerMeta> getPassengerMap() {
        if (passengerMap == null) {
            initPassengerMap();
        }
        return passengerMap;
    }

    /**
     *  init passenger
     * @return
     */
    private static void initPassengerMap() {
        passengerMap = new ConcurrentHashMap<>();
        List<PassengerMeta> list = JSONObject.parseArray(JsonUtil.readJsonFile(passengerFileName), PassengerMeta.class);
        for(PassengerMeta passengerMeta : list){
            passengerMap.put(passengerMeta.getPassenger_id() , passengerMeta);
        }
    }
}
