package com.nwpu.myonlinetaxi.service.impl;

import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.Taxi;
import com.nwpu.myonlinetaxi.service.DisTanceService;
import com.nwpu.myonlinetaxi.service.TaxiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author Junho
 * @date 2022/7/18 16:47
 */
@Service
public class TaxiServiceImpl implements TaxiService {

    @Resource
    DisTanceService disTanceService;

    @Override
    public double getSpeed(Taxi taxi) {
        if(taxi.getTrace().size() < 2 ){
            return new Random().nextDouble() * 100;
        }
        Position start = taxi.getTrace().get(taxi.getTrace().size() - 2);
        Position end = taxi.getTrace().get(taxi.getTrace().size() - 1);
        return disTanceService.calcDistance(
            start.getLon() , start.getLat() , end.getLon() , end.getLat()
        );
    }

}
