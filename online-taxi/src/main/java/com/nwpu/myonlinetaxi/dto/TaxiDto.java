package com.nwpu.myonlinetaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxiDto {

    //信号灯信息 便于调试
    private Integer leftTime;
    private Integer lightColor;

    private String taxi_id; // 车辆Id

    private Long timeStamp;

    private double lon;

    private double lat;

    private double speed;

    private String suggest;

    private int gpsType; // gcj82:1

    public TaxiDto(String taxi_id, Long timeStamp, double lon, double lat, double speed, String suggest, int gpsType) {
        this.taxi_id = taxi_id;
        this.timeStamp = timeStamp;
        this.lon = lon;
        this.lat = lat;
        this.speed = speed;
        this.suggest = suggest;
        this.gpsType = gpsType;
    }
}
