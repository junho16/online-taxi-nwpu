package com.nwpu.myonlinetaxi.entity.meta;

import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.TracePos;
import javafx.geometry.Pos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Junho
 * @date 2022/7/16 16:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxiMeta {

    private String taxi_id;

    private String taxi_name;

    private double lon;

    private double lat;

    /**
     * TODO-再议
     * 网约车状态
     * 0:空闲（在初始轨迹上走、送完乘客返航）  1:送客（去接乘客、送乘客去目的地）
     */
    private int state;

    private double speed;

    /**
     * 轨迹
     */
    private ConcurrentLinkedQueue<TracePos> trace;

    /**
     * 初始化轨迹的起止位置
     */
    private Position startPos;

    private Position endPos;

    /**
     * 如果网约车此时未接单 则此属性应为null
     */
    private OrderMeta order;

    /**
     * 轨迹方向
     * true:startPos -> endPos;
     * false:endPos -> startPos
     */
    private boolean direction;

    public TaxiMeta(String taxi_id, String taxi_name, int state, double lon, double lat, Position startPos, Position endPos) {
        this.taxi_id = taxi_id;
        this.taxi_name = taxi_name;
        this.state = state;
        this.startPos = startPos;
        this.endPos = endPos;
        this.lon = lon;
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "TaxiMeta{" +
                "taxi_id='" + taxi_id + '\'' +
                ", taxi_name='" + taxi_name + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", state=" + state +
                ", speed=" + speed +
                ", trace=" + trace.size() +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                ", order=" + order +
                ", direction=" + direction +
                '}';
    }
}
