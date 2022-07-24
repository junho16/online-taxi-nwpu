package com.nwpu.myonlinetaxi.entity.meta;

import com.nwpu.myonlinetaxi.entity.Position;
import lombok.Data;

/**
 * @author Junho
 * @date 2022/7/18 14:03
 */
@Data
public class DetectorMeta {

    private String detector_id;

    private String detector_name;

    private String detector_sn;

    private String light_sn;

    private Position light_pos;
}
