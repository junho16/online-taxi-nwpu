package com.nwpu.myonlinetaxi.service;

import com.nwpu.myonlinetaxi.entity.R;

/**
 * @author Junho
 * @date 2022/7/19 11:28
 */
public interface PassengerService {


    /**
     * 乘客打车
     * @param passenger_id
     * @return
     */
    public void callTaxi(String passenger_id);

//    public R changeState(String taxi_id, Integer state);
}
