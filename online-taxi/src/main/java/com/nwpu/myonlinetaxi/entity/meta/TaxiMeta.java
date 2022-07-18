package com.nwpu.myonlinetaxi.entity.meta;

import lombok.Data;

import java.util.List;

/**
 * @author Junho
 * @date 2022/7/16 16:33
 */
@Data
public class TaxiMeta {

    private String taxi_id;

    private String taxi_name;

    private double lon;

    private double lat;

    /**
     * 网约车状态
     * 0:等待接客  1:正在前往接客  2:送客
     */
    private int state;
}
