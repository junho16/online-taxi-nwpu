package com.nwpu.myonlinetaxi.service;

import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.R;
import com.nwpu.myonlinetaxi.entity.TracePos;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author Junho
 * @date 2022/7/18 16:47
 */
public interface TaxiService {

//    public double getSpeed(Taxi taxi);

    public List<TracePos> reqDriving(Double startLon , Double startLat ,
                                     Double endLon , Double endLat );

    public void pickPassenger(TaxiMeta taxiMeta);

    public void endOrder(TaxiMeta taxiMeta);
//    public R changeState(String taxi_id, Integer state);
}
