package nwpu.deviceonenet.onenet.crontask;

import lombok.extern.slf4j.Slf4j;
import nwpu.deviceonenet.onenet.entity.enums.PassengerState;
import nwpu.deviceonenet.onenet.entity.meta.PassengerMeta;
import nwpu.deviceonenet.onenet.init.PassengersInstance;
import nwpu.deviceonenet.onenet.service.PassengerService;
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
     * 每个小时的10分30秒选一个幸运用户
     */
//    @Scheduled(cron = "*/10 * * * * ?")
    @Scheduled(cron = "30 10 * * * ?")
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
