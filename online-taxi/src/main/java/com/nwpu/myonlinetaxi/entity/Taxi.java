//package com.nwpu.myonlinetaxi.entity;
//
//import com.nwpu.myonlinetaxi.entity.Position;
//import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
//import lombok.Data;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//public class Taxi {
//
//    private final int traceLimit = 100;
//
//    private TaxiMeta taxiMeta;
//
//
//    /**
//     * 记录其轨迹
//     */
//    private List<Position> trace;
//
//    public List<Position> getTrace() {
//        if(this.trace.size() > traceLimit){
//            this.trace.clear();
//        }
//        return trace;
//    }
//
//    public Taxi(TaxiMeta taxiMeta){
//        this.taxiMeta = taxiMeta;
//        this.trace = new ArrayList<>();
//    }
//}
