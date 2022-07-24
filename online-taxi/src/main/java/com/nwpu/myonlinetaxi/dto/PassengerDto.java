package com.nwpu.myonlinetaxi.dto;

import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Junho
 * @date 2022/7/24 15:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {

    private String pid;

    private Double lon;

    private Double lat;

    private int state;

//    private String taxi_id;

    public static PassengerDto toPassengerDto(PassengerMeta passengerMeta){
        return new PassengerDto(
            passengerMeta.getPassenger_id(),
            passengerMeta.getLon(),
            passengerMeta.getLat(),
            passengerMeta.getState()
        );
    }
}
