package com.nwpu.myonlinetaxi.crontask;

import com.nwpu.myonlinetaxi.entity.enums.PassengerState;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.init.PassengersInstance;
import com.nwpu.myonlinetaxi.service.PassengerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Junho
 * @date 2022/7/22 14:11
 */
@EnableScheduling
@Component
@Slf4j
public class OrderTask {

    @Resource
    PassengerService passengerService;

    /**
     * 10s的间隔时间选一个幸运用户并让其叫车
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void callTaxi(){

        Map<String , PassengerMeta> passengerMap = PassengersInstance.getPassengerMap();
        for(Map.Entry<String , PassengerMeta> entry : passengerMap.entrySet()){
            if(entry.getValue().getState() == PassengerState.free.getState()){

                passengerService.callTaxi(entry.getKey());
                return;
            }
        }
        log.info("时刻：{} ，此时没有用户空闲。" , System.currentTimeMillis());
    }

}
