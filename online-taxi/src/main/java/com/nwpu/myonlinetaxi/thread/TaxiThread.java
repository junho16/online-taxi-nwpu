package com.nwpu.myonlinetaxi.thread;


import com.nwpu.myonlinetaxi.entity.TracePos;
import com.nwpu.myonlinetaxi.entity.enums.PassengerState;
import com.nwpu.myonlinetaxi.entity.enums.TaxiState;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.init.PassengersInstance;
import com.nwpu.myonlinetaxi.init.TaxisInstance;
import com.nwpu.myonlinetaxi.service.TaxiService;
import lombok.Synchronized;
import org.checkerframework.checker.units.qual.C;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.plugin2.util.PluginTrace;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网约车的线程 用于更新数据
 * @author Junho
 * @date 2022/7/21 22:29
 */
public class TaxiThread implements Runnable {

    private TaxiMeta taxi;

    private TaxiService taxiService;

    public TaxiThread(TaxiMeta taxi , TaxiService taxiService){
        this.taxi = taxi;
        this.taxiService = taxiService;
    }

    @Override
    public void run() {
        while (true){
            uploadPos();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 定时上传网约车的位置信息 并根据状态进行判断
     */
    @Synchronized
    public void uploadPos(){

        if(taxi.getTrace().isEmpty() && taxi.getState() == TaxiState.free.getState()){
             taxi.getTrace().clear();
             taxi.getTrace().addAll(getNextTraceForFree(taxi));
        }

        if(!taxi.getTrace().isEmpty()){
            TracePos nowPos = taxi.getTrace().poll();
            taxi.setLon(nowPos.getLon());
            taxi.setLat(nowPos.getLat());
            taxi.setSpeed(nowPos.getSpeed());
        }

        if(taxi.getOrder() != null){
            //在送人的时候 乘客的Pos要跟随车而改变
            PassengerMeta passengerMeta = PassengersInstance.getPassengerMap().get(taxi.getOrder().getPassenger_id());
            if(passengerMeta.getState() == PassengerState.run.getState()){
                passengerMeta.setLon(taxi.getLon());
                passengerMeta.setLat(taxi.getLat());
            }

            //taxi手中已有订单
            if(taxi.getOrder().getStartPos().getLon() == taxi.getLon() && taxi.getOrder().getStartPos().getLat() == taxi.getLat()){
                //已到达乘客地址
                taxiService.pickPassenger(taxi);
            }else if(taxi.getOrder().getEndPos().getLon() == taxi.getLon() && taxi.getOrder().getEndPos().getLat() == taxi.getLat()){
                //已送完乘客
                taxiService.endOrder(taxi);
            }
        }
    }

    /**
     * FIXME：是否应该使用state而不是位置 --》应该根据状态来
     * 网约车空闲时的后续轨迹经纬度列表
     * @param taxi
     * @return
     */
    public List<TracePos> getNextTraceForFree(TaxiMeta taxi){
        if(taxi.getState() == TaxiState.free.getState()){
            boolean f = taxi.isDirection();
            taxi.setDirection(!f);
            if(f){
                return taxiService.reqDriving(taxi.getStartPos().getLon() , taxi.getStartPos().getLat() , taxi.getEndPos().getLon() , taxi.getEndPos().getLat());
            }else{
                return taxiService.reqDriving(taxi.getEndPos().getLon() , taxi.getEndPos().getLat() , taxi.getStartPos().getLon() , taxi.getStartPos().getLat());
            }
        }
        //应该不会是送完人之后出现的trace为空的情况 因为endorder中已经加入了后续返航路径
        return new ArrayList<>();
//        if( taxi.getStartPos().getLat() == taxi.getLat() && taxi.getStartPos().getLon() == taxi.getLon() ){
//            return taxiService.reqDriving(taxi.getStartPos().getLon() , taxi.getStartPos().getLat() , taxi.getEndPos().getLon() , taxi.getEndPos().getLat());
//        }else if(taxi.getEndPos().getLat() == taxi.getLat() && taxi.getEndPos().getLon() == taxi.getLon()){
//            return taxiService.reqDriving(taxi.getEndPos().getLon() , taxi.getEndPos().getLat() , taxi.getStartPos().getLon() , taxi.getStartPos().getLat());
//        }else{
//            return taxiService.reqDriving(taxi.getLon() , taxi.getLat() , taxi.getStartPos().getLon() , taxi.getStartPos().getLat());
//        }
    }

    /**
     * TODO-直接发MQTT 就不用再设置定时任务上报了
      */
    public void sendMsg(){

    }
}
