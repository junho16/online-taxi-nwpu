package com.nwpu.myonlinetaxi.controller;

import com.nwpu.myonlinetaxi.entity.R;
import com.nwpu.myonlinetaxi.entity.Taxi;
import com.nwpu.myonlinetaxi.entity.Passenger;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import com.nwpu.myonlinetaxi.init.PassengersInstance;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Junho
 * @date 2022/7/16 16:56
 */
@RestController
public class InitController {

    @RequestMapping("/init")
    public R initData(){
        List<Taxi> taxiList = TaxisInstance.getTaxiList();
        List<Passenger> passengerList = PassengersInstance.getUserList();
        Map<String , Object> res = new HashMap<>();
        res.put("taxiList" , taxiList);
        res.put("userList" , passengerList);
        return R.ok(res);
    }

}
