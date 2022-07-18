package com.nwpu.myonlinetaxi.controller;

import com.nwpu.myonlinetaxi.entity.R;
import com.nwpu.myonlinetaxi.entity.Taxi;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import com.nwpu.myonlinetaxi.init.PassengersInstance;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/16 16:56
 */
@RestController
public class InitController {

    @RequestMapping("/init")
    public R initData(){
        if(!TaxisInstance.isInit() && !PassengersInstance.isInit()){
            ConcurrentHashMap<String , Taxi> taxiMap = TaxisInstance.getTaxiMap();
            List<PassengerMeta> passengerMetaList = PassengersInstance.getUserList();
            Map<String , Object> res = new HashMap<>();
            res.put("taxiMap" , taxiMap);
            res.put("userList" , passengerMetaList);
            return R.ok(res);
        }else{
            return R.error(500 , "已初始化过。");
        }
    }

}
