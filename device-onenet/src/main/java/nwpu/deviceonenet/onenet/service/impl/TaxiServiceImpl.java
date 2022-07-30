package nwpu.deviceonenet.onenet.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import nwpu.deviceonenet.onenet.entity.TracePos;
import nwpu.deviceonenet.onenet.entity.enums.PassengerState;
import nwpu.deviceonenet.onenet.entity.enums.TaxiState;
import nwpu.deviceonenet.onenet.entity.meta.PassengerMeta;
import nwpu.deviceonenet.onenet.entity.meta.TaxiMeta;
import nwpu.deviceonenet.onenet.init.PassengersInstance;
import nwpu.deviceonenet.onenet.init.PathInstance;
import nwpu.deviceonenet.onenet.service.DisTanceService;
import nwpu.deviceonenet.onenet.service.TaxiService;
import nwpu.deviceonenet.onenet.util.FormatUtil;
import nwpu.deviceonenet.onenet.util.GpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Junho
 * @date 2022/7/18 16:47
 */
@Slf4j
@Service
public class TaxiServiceImpl implements TaxiService {

    @Value("${amap.driving.baseurl}")
    private String baseurl;

    @Resource
    RestTemplate restTemplate;

    @Override
    public List<TracePos> reqDriving(Double startLon, Double startLat, Double endLon, Double endLat) {

        Map<String , List<TracePos> > pathMap = PathInstance.getPathMap();
        String pathKey = startLon + " " + startLat + " " + endLon + " " + endLat;
        if(pathMap.containsKey(pathKey)){
            return pathMap.get(pathKey);
        }

        List<TracePos> pathList = new ArrayList<>();
        try{
            String parameters = "&origin=" +
                    FormatUtil.getPosFormatDouble(startLon) + "," +
                    FormatUtil.getPosFormatDouble(startLat) + "&destination=" +
                    FormatUtil.getPosFormatDouble(endLon) + "," +
                    FormatUtil.getPosFormatDouble(endLat);
            String url = baseurl + parameters;
            //经纬度也可以直接写在方法的参数里
            String result = restTemplate.getForObject(url, String.class);
//            log.info("result:{}",result);
            JSONObject jsonObject = JSONObject.parseObject(result);

            if( jsonObject.get("infocode").equals("10000") ){

                JSONArray paths = jsonObject.getJSONObject("route").getJSONArray("paths");
                JSONObject path = (JSONObject) paths.get(0);
                JSONArray steps = (JSONArray) path.get("steps");
                for(int i = 0 ; i < steps.size() ; i++){
                    JSONObject o = steps.getJSONObject(i);

                    //速度为m/s
                    double speed = Double.parseDouble(o.getString("distance")) / Double.parseDouble(o.getString("duration"));
                    //第i段的经纬度列表
                    String[] polylines = o.getString("polyline").split(";");
                    for(String polylinesItem : polylines){
                        String[] posArr = polylinesItem.split(",");
//                        pathList.add(
//                            new TracePos(Double.parseDouble(posArr[0]) , Double.parseDouble(posArr[1]) , speed)
//                        );
                        pathList.add(
                            GpsUtil.calGCJ02toWGS84(new TracePos(Double.parseDouble(posArr[0]) , Double.parseDouble(posArr[1]) , speed))
                        );
                    }
                }

            }else{
                log.info("请求路径规划失败: {}" ,  jsonObject.get("info"));
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("请求路径失败，异常为：{}" , e.getMessage());
        }

        pathMap.put(pathKey , pathList);
        return pathList;
    }

    @Override
    public void pickPassenger(TaxiMeta taxiMeta) {
        PassengerMeta passengerMeta = PassengersInstance.getPassengerMap().get(taxiMeta.getOrder().getPassenger_id());
        passengerMeta.setState(PassengerState.run.getState());
    }

    @Override
    public void endOrder(TaxiMeta taxiMeta) {
        PassengerMeta passengerMeta = PassengersInstance.getPassengerMap().get(taxiMeta.getOrder().getPassenger_id());
        taxiMeta.setOrder(null);
        taxiMeta.setState(TaxiState.free.getState());
        taxiMeta.getTrace().clear();
        taxiMeta.getTrace().addAll(reqDriving(taxiMeta.getLon() , taxiMeta.getLat() , taxiMeta.getStartPos().getLon() , taxiMeta.getStartPos().getLat()));
        passengerMeta.setState(PassengerState.free.getState());
     }
}
