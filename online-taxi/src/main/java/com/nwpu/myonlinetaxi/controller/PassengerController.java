package com.nwpu.myonlinetaxi.controller;

import com.nwpu.myonlinetaxi.entity.R;
import com.nwpu.myonlinetaxi.service.PassengerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Junho
 * @date 2022/7/19 11:22
 */
@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Resource
    PassengerService passengerService;


    //顾客叫车（顾客的目标位置是前端传或者后端模拟）
    @RequestMapping("/callTaxi")
    public R callTaxi(@RequestParam String passengerId){

        R r = passengerService.callTaxi(passengerId);
        return r;

    }
}
