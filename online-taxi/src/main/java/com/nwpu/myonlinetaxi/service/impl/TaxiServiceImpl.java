package com.nwpu.myonlinetaxi.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.dao.TaxiMongoDao;
import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.R;
import com.nwpu.myonlinetaxi.entity.TracePos;
import com.nwpu.myonlinetaxi.entity.enums.PassengerState;
import com.nwpu.myonlinetaxi.entity.enums.TaxiState;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.init.PassengersInstance;
import com.nwpu.myonlinetaxi.service.DisTanceService;
import com.nwpu.myonlinetaxi.service.TaxiService;
import com.nwpu.myonlinetaxi.util.FormatUtil;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    DisTanceService disTanceService;

    @Resource
    TaxiMongoDao taxiMongoDao;

    @Override
    public List<TracePos> reqDriving(Double startLon, Double startLat, Double endLon, Double endLat) {

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
                        pathList.add(new TracePos(Double.parseDouble(posArr[0]) , Double.parseDouble(posArr[1]) , speed));
                    }
                }

            }else{
                log.info("请求路径规划失败: {}" ,  jsonObject.get("info"));
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("请求路径失败，异常为：{}" , e.getMessage());
        }
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

//    @Override
//    public double getSpeed(Taxi taxi) {
//        if(taxi.getTrace().size() < 2 ){
//            return new Random().nextDouble() * 100;
//        }
//        Position start = taxi.getTrace().get(taxi.getTrace().size() - 2);
//        Position end = taxi.getTrace().get(taxi.getTrace().size() - 1);
//        return disTanceService.calcDistance(
//            start.getLon() , start.getLat() , end.getLon() , end.getLat()
//        );
//    }

//    @Override
//    public R reqDriving(Double startLon, Double startLat, Double endLon, Double endLat) {
//
//        List<Position> pathList = new ArrayList<>();
//
//        try{
//            String parameters = "&origin=" +
//                    FormatUtil.getPosFormatDouble(startLon) + "," +
//                    FormatUtil.getPosFormatDouble(startLat) + "&destination=" +
//                    FormatUtil.getPosFormatDouble(endLon) + "," +
//                    FormatUtil.getPosFormatDouble(endLat);
//            String url = baseurl + parameters;
//            //经纬度也可以直接写在方法的参数里
//            String result = restTemplate.getForObject(url, String.class);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//
//            if( jsonObject.get("infocode").equals("10000") ){
//                String pathStr = "";
//                JSONArray paths = jsonObject.getJSONObject("route").getJSONArray("paths");
//                JSONObject path = (JSONObject) paths.get(0);
//                JSONArray steps = (JSONArray) path.get("steps");
//                for(Object obj : steps){
//                    JSONObject o = (JSONObject) obj;
//                    pathStr += o.get("polyline") + ";";
//                }
//                pathStr = pathStr.substring(0 , pathStr.length() - 1);
////                log.info("pathStr:{}" , pathStr.substring(0 , pathStr.length() - 1));
//                String[] stepStrs = pathStr.split(";");
//                for(String item : stepStrs){
//                    String[] posStrs = item.split(",");
//                    pathList.add(new Position(Double.parseDouble(posStrs[0]) , Double.parseDouble(posStrs[1])));
//                }
//
//            }else{
//                log.info("请求路径规划失败: {}" ,  jsonObject.get("info"));
//                return R.error(500 , "请求路径规划失败。");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            log.error("请求路径失败，异常为：{}" , e.getMessage());
//        }
//
//        return R.ok(pathList);
//    }

//    @Override
//    public R changeState(String taxi_id, Integer state) {
//        if(state != 1 && state != 0){
//            return R.error(500 , "不存在该状态" );
//        }
//        try{
//            taxiMongoDao.changeState(taxi_id , state);
//            return R.ok(null);
//        }catch (Exception e){
//            return R.error(500 , "更新id为：{} 的车辆的状态失败。" );
//        }
//    }

}
