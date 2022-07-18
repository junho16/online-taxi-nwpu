package com.nwpu.myonlinetaxi.entity.meta;

import lombok.Data;

/**
 * @author Junho
 * @date 2022/7/18 14:03
 */
@Data
public class DetectorMeta {

    private String detector_id;

    private String detector_name;

    private Double lat;

    private Double lon;
}
