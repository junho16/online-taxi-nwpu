package nwpu.deviceonenet.onenet.service.impl;

import nwpu.deviceonenet.onenet.entity.Position;
import nwpu.deviceonenet.onenet.entity.TracePos;
import nwpu.deviceonenet.onenet.entity.enums.PassengerState;
import nwpu.deviceonenet.onenet.entity.enums.TaxiState;
import nwpu.deviceonenet.onenet.entity.meta.OrderMeta;
import nwpu.deviceonenet.onenet.entity.meta.PassengerMeta;
import nwpu.deviceonenet.onenet.entity.meta.TaxiMeta;
import nwpu.deviceonenet.onenet.init.PassengersInstance;
import nwpu.deviceonenet.onenet.init.TaxisInstance;
import nwpu.deviceonenet.onenet.service.DisTanceService;
import nwpu.deviceonenet.onenet.service.PassengerService;
import nwpu.deviceonenet.onenet.service.TaxiService;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Junho
 * @date 2022/7/19 11:30
 */
@Service
@Slf4j
public class PassengerServiceImpl implements PassengerService {

    //探测器500米范围内
    @Value("${maxDistance}")
    private double maxDistance;

    @Resource
    DisTanceService disTanceService;

    @Resource
    TaxiService taxiService;

    @Override
    @Synchronized
    public void callTaxi(String passenger_id) {
        PassengerMeta passengerMeta = PassengersInstance.getPassengerMap().get(passenger_id);

        TaxiMeta taxi = findMinDisTaxi(passengerMeta);
        if(taxi != null){
            log.info("乘客：{} 申请叫车，为其分配网约车为：{}",passenger_id , taxi.getTaxi_id());

            // 获得一个随机位置并规划路径
            Map<Object , Object> aroundMap = disTanceService.getAround(
                passengerMeta.getLon()+"" ,
                passengerMeta.getLat()+"" ,
                maxDistance+"");
            Position targetPos = disTanceService.getRandomPos(
                    Double.parseDouble((String) aroundMap.get("minLng")) ,
                    Double.parseDouble((String) aroundMap.get("maxLng")) ,
                    Double.parseDouble((String) aroundMap.get("minLat")) ,
                    Double.parseDouble((String) aroundMap.get("maxLat")));
            List<TracePos> pathToPassenger = taxiService.reqDriving(taxi.getLon() , taxi.getLat() , passengerMeta.getLon() , passengerMeta.getLat());
            List<TracePos> pathToTarget = taxiService.reqDriving(passengerMeta.getLon() , passengerMeta.getLat() , targetPos.getLon() , targetPos.getLat());

            OrderMeta orderMeta = new OrderMeta(
                    taxi.getTaxi_id() ,
                    passengerMeta.getPassenger_id() ,
                    new Position(
                            pathToPassenger.get(pathToPassenger.size() - 1).getLon() ,
                            pathToPassenger.get(pathToPassenger.size() - 1).getLat()
                    ),
                    new Position(
                            pathToTarget.get(pathToTarget.size() - 1).getLon() ,
                            pathToTarget.get(pathToTarget.size() - 1).getLat()
                    )
            );
            taxi.setOrder(orderMeta);

            taxi.getTrace().clear();
            taxi.getTrace().addAll(pathToPassenger);
            taxi.getTrace().addAll(pathToTarget);

            taxi.setState(TaxiState.busy.getState());
            passengerMeta.setState(PassengerState.call.getState());

        }
        else{
            log.info("{} : 此时无车空闲" , System.currentTimeMillis());
        }
    }

    /**
     * 给某乘客分配网约车
     *  TODO-可以抽象出接口 并添加其他算法实现
     * @param passengerMeta
     * @return
     */
    public TaxiMeta findMinDisTaxi(PassengerMeta passengerMeta){
        Map<String , TaxiMeta> taxiMap = TaxisInstance.getTaxiMap();

        double passengerLon = passengerMeta.getLon();
        double passengerLat = passengerMeta.getLat();
        TaxiMeta res = new TaxiMeta();
        Double minDistance = maxDistance;

        for(Map.Entry<String , TaxiMeta> entry : taxiMap.entrySet()){
            TaxiMeta taxiMeta = entry.getValue();
            if(taxiMeta.getState() == 0){
                double dis = disTanceService.calcDistance(taxiMeta.getLon() , taxiMeta.getLat() , passengerLon, passengerLat);
                if(dis < maxDistance && maxDistance > 0 && dis < maxDistance){
                    minDistance = dis;
                    res = taxiMeta;
                }
            }
        }
        return maxDistance == minDistance ? null : res;
    }

}
