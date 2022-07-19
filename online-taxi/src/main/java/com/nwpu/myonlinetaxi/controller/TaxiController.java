package com.nwpu.myonlinetaxi.controller;

import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.R;
import com.nwpu.myonlinetaxi.entity.Taxi;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.init.PassengersInstance;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import com.nwpu.myonlinetaxi.service.TaxiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/16 20:08
 */
@RestController
@RequestMapping("/taxi")
public class TaxiController {

    @Resource
    TaxiService taxiService;

    /**
     * 上传车辆位置信息
     * @param Pos
     * @return
     */
    @RequestMapping("/uploacPos")
    public R uploadPos(@RequestParam("id")String id , @RequestParam("position")Position Pos){
        //TODO-调用device-onenet中上传服务
        return null;
    }

    @RequestMapping(method = RequestMethod.GET , value = "/driving")
    public R reqDriving(
            @RequestParam("startLon")Double startLon,
            @RequestParam("startLat")Double startLat,
            @RequestParam("endLon")Double endLon,
            @RequestParam("endLat")Double endLat) {

        R res = taxiService.reqDriving(startLon , startLat , endLon , endLat);
        return res;
    }

    @RequestMapping(method = RequestMethod.POST , value = "/state")
    public R changeState(
            @RequestParam("taxi_id")String taxi_id,
            @RequestParam("state")Integer state ) {
        return taxiService.changeState(taxi_id , state);
    }

    /**
     * 当前系统中网约车状态
     * @return
     */
    @RequestMapping("/taxis")
    public R initData(){
        ConcurrentHashMap<String , Taxi> taxiMap = TaxisInstance.getTaxiMap();
        return R.ok(taxiMap);
    }
}
