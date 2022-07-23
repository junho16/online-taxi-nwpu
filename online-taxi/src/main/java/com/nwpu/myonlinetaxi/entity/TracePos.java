package com.nwpu.myonlinetaxi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Junho
 * @date 2022/7/21 22:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TracePos {

    private double lon;

    private double lat;

    private double speed;
}
