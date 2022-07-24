package com.nwpu.myonlinetaxi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Junho
 * @date 2022/7/24 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttPassengerDto {

    private String type;

    private Long timeStamp;

    private List<PassengerDto> passengers;

}
