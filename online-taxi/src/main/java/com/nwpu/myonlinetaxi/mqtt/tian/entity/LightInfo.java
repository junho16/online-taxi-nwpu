package com.nwpu.myonlinetaxi.mqtt.tian.entity;

import lombok.Data;

import java.util.List;

@Data
public class LightInfo {
    private int enter_pos;
    private List<LightGroup> group_list;
    private int group_num;
}
