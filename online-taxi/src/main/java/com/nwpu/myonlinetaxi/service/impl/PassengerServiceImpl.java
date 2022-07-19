package com.nwpu.myonlinetaxi.service.impl;

import com.nwpu.myonlinetaxi.dao.PassengerMongoDao;
import com.nwpu.myonlinetaxi.dao.TaxiMongoDao;
import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.R;
import com.nwpu.myonlinetaxi.entity.Taxi;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.init.PassengersInstance;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import com.nwpu.myonlinetaxi.service.DisTanceService;
import com.nwpu.myonlinetaxi.service.PassengerService;
import com.nwpu.myonlinetaxi.service.TaxiService;
import com.onenet.studio.acc.sdk.util.OneJsonUtil;
import javafx.geometry.Pos;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.PolicySpi;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Junho
 * @date 2022/7/19 11:30
 */
@Service
public class PassengerServiceImpl implements PassengerService {

    //只找不超过5km的网约车
    private final double maxDistance = 5000;

    @Resource
    TaxiMongoDao taxiMongoDao;

    @Resource
    PassengerMongoDao passengerMongoDao;

    @Resource
    DisTanceService disTanceService;

    @Resource
    TaxiService taxiService;

    @Override
    @Synchronized
    public R callTaxi(String passenger_id) {
        PassengerMeta passengerMeta = passengerMongoDao.getPassenger(passenger_id);
        if(passengerMeta.getState() == 1){
            return R.error(500,"该乘客有未完成订单，其正在出行");
        }
        String taxi_id = findMinDisTaxi(passengerMeta);
        if(taxi_id != null){
            TaxiMeta taxiMeta = taxiMongoDao.getTaxi(taxi_id);
            // 获得一个随机位置并规划路径
            Map<Object , Object> aroundMap = disTanceService.getAround(passengerMeta.getLon()+"" , passengerMeta.getLat()+"" , maxDistance+"");
            Position targetPos = disTanceService.getRandomPos(
                    Double.parseDouble((String) aroundMap.get("minLng")) ,
                    Double.parseDouble((String) aroundMap.get("maxLng")) ,
                    Double.parseDouble((String) aroundMap.get("minLat")) ,
                    Double.parseDouble((String) aroundMap.get("maxLat")));
            R tarPlan = taxiService.reqDriving(passengerMeta.getLon() , passengerMeta.getLat() , taxiMeta.getLon() , taxiMeta.getLat());
            List<Position> pathList = (List<Position>) tarPlan.getData();

            // 更改网约车 和 乘客状态 乘客位置 设置为目标地点的位置
            taxiMongoDao.changeState( taxi_id , 1);
            passengerMongoDao.changeState( passenger_id , 1);
            passengerMongoDao.changeLonLat( passenger_id , targetPos.getLon() , targetPos.getLat() );

            TaxiMeta taxiMeta1 = TaxisInstance.getTaxiMap().get(taxiMeta.getTaxi_name()).getTaxiMeta();
            taxiMeta1.setState(1);
            PassengersInstance.getPassengerMap().get(passenger_id).setState(1);

            Map<String , Object> res = new HashMap<>();
            res.put("taxi_id" , taxi_id);
            res.put("target_pos" , targetPos);
            res.put("target_path" , pathList);
            return R.ok(res);
        }else{
            return R.ok(200 , maxDistance + " 范围内无可用网约车。");
        }
    }

    @Override
    public R changeState(String passenger_id, Integer state) {
        if (state != 1 && state != 0) {
            return R.error(500, "不存在该状态");
        }
        try {
            taxiMongoDao.changeState(passenger_id, state);
            return R.ok(null);
        } catch (Exception e) {
            return R.error(500, "更新id为：{} 的乘客的状态失败。");
        }
    }

    /**
     * 给某顾客分配网约车
     *  TODO-可以抽象出接口 并添加其他算法实现
     * @param passengerMeta
     * @return
     */
    public String findMinDisTaxi(PassengerMeta passengerMeta){
        List<TaxiMeta> taxilist = taxiMongoDao.getTaxiList();

        double passengerLon = passengerMeta.getLon();
        double passengerLat = passengerMeta.getLat();
        TaxiMeta res = new TaxiMeta();
        Double minDistance = maxDistance;
        for(TaxiMeta taxiMeta : taxilist){
            if(taxiMeta.getState() == 0){
                double dis = disTanceService.calcDistance(taxiMeta.getLon() , taxiMeta.getLat() , passengerLon, passengerLat);
                if(dis < maxDistance && maxDistance > 0 && dis < maxDistance){
                    minDistance = dis;
                    res = taxiMeta;
                }
            }
        }

        return maxDistance == minDistance ? null : res.getTaxi_id();
    }
}
