package com.nwpu.myonlinetaxi.service;

import com.nwpu.myonlinetaxi.entity.R;

/**
 * @author Junho
 * @date 2022/7/19 11:28
 */
public interface PassengerService {


    /**
     * 乘客打车
     * @param id
     * @return
     */
    public R callTaxi(String id);

    public R changeState(String taxi_id, Integer state);
}
