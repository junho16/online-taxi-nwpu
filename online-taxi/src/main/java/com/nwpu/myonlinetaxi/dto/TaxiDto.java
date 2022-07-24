package com.nwpu.myonlinetaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxiDto {

    private String tid;

    private Double lon;

    private Double lat;

    private int state;

    private boolean isCrossing;

    private Double speed;

    private String suggest;

}
