package nwpu.deviceonenet.onenet.init;

import com.alibaba.fastjson.JSONObject;
import nwpu.deviceonenet.onenet.entity.TracePos;
import nwpu.deviceonenet.onenet.entity.meta.PassengerMeta;
import nwpu.deviceonenet.onenet.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/30 13:11
 */
@Component
public class PathInstance {

    /**
     * key：起始位置 经度 + " " + 纬度 + " " + 终止位置 经度 + " " + 纬度 （当然也可以重写hashcode）
     */
    private static ConcurrentHashMap<String , List<TracePos>> pathMap;

    private PathInstance(){
    }

    @PostConstruct
    public void initPassenger(){
        initPathMap();
    }

    public static ConcurrentHashMap<String , List<TracePos>> getPathMap() {
        if (pathMap == null) {
            initPathMap();
        }
        return pathMap;
    }

    /**
     *  init pathMap
     * @return
     */
    private static void initPathMap() {
        pathMap = new ConcurrentHashMap<>();
    }
}
