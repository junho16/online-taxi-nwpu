package com.nwpu.myonlinetaxi.entity.meta;

import com.nwpu.myonlinetaxi.entity.Position;
import javafx.geometry.Pos;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author Junho
 * @date 2022/7/16 16:35
 */
@Data
public class PassengerMeta {

    private String passenger_id;

    private double lon;

    private double lat;

    /**
     * 0:未打车   1:已打车
     */
    private int state;

}
