package com.nwpu.myonlinetaxi.entity;

import lombok.Data;

/**
 * @author Junho
 * @date 2022/7/16 16:35
 */
@Data
public class Passenger {

    private String passenger_id;

    private double lon;

    private double lat;

    /**
     * 0:未打车   1:已打车
     */
    private int state;

}
