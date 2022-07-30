//package com.nwpu.myonlinetaxi.controller;
//
//import com.nwpu.myonlinetaxi.entity.R;
//import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
//import com.nwpu.myonlinetaxi.init.PassengersInstance;
//import com.nwpu.myonlinetaxi.init.TaxisInstance;
//import com.nwpu.myonlinetaxi.service.PassengerService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author Junho
// * @date 2022/7/19 11:22
// */
//@RestController
//@RequestMapping("/passenger")
//public class PassengerController {
//
//    @Resource
//    PassengerService passengerService;
//
////    //顾客叫车（顾客的目标位置是前端传或者后端模拟）
////    @RequestMapping("/callTaxi")
////    public R callTaxi(@RequestParam String passengerId){
////        R r = passengerService.callTaxi(passengerId);
////        return r;
////    }
//
////    /**
////     * 当前系统中顾客状态
////     * @return
////     */
////    @RequestMapping("/passengers")
////    public R initData(){
////        ConcurrentHashMap<String , PassengerMeta> passengerMap = PassengersInstance.getPassengerMap();
////        return R.ok(passengerMap);
////    }
////
////    @RequestMapping(method = RequestMethod.POST , value = "/state")
////    public R changeState(
////            @RequestParam("passenger_id")String passenger_id,
////            @RequestParam("state")Integer state ) {
////        return passengerService.changeState(passenger_id , state);
////    }
//}
