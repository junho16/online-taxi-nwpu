package com.nwpu.myonlinetaxi.service;

import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.R;
import com.nwpu.myonlinetaxi.entity.Taxi;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Junho
 * @date 2022/7/18 16:47
 */
public interface TaxiService {

    public double getSpeed(Taxi taxi);

    public R reqDriving( Double startLon , Double startLat ,
         Double endLon , Double endLat );

    public R changeState(String taxi_id, Integer state);
}
