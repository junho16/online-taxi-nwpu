//package com.nwpu.myonlinetaxi.init;
//
//import com.alibaba.fastjson.JSONObject;
//import com.nwpu.myonlinetaxi.dao.PassengerMongoDao;
//import com.nwpu.myonlinetaxi.dao.TaxiMongoDao;
//import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
//import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
//import com.nwpu.myonlinetaxi.util.JsonUtil;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author Junho
// * @date 2022/7/16 17:08
// */
//@Component
//public class PassengersInstance {
//
//    @Resource
//    PassengerMongoDao passengerMongoDao;
//
//    private static PassengerMongoDao passengerdao;
//
//    //用户json文件名
//    private static String passengerFileName;
//
//    @Value("${init.bean.passenger_file_name}")
//    public void setFileName(String passenger_file_name) {
//        passengerFileName = passenger_file_name;
//    }
//
//    private static ConcurrentHashMap<String , PassengerMeta> passengerMap;
//
//    private PassengersInstance(){
//    }
//
//    @PostConstruct
//    public void initPassenger(){
//        passengerdao = passengerMongoDao;
//        initPassengerMap();
//    }
//
//    public static ConcurrentHashMap<String , PassengerMeta> getPassengerMap() {
//        if (passengerMap == null) {
//            initPassengerMap();
//        }
//        return passengerMap;
//    }
//
//    /**
//     *  init passenger
//     * @return
//     */
//    private static void initPassengerMap() {
//        passengerMap = new ConcurrentHashMap<>();
//        List<PassengerMeta> list = JSONObject.parseArray(JsonUtil.readJsonFile(passengerFileName), PassengerMeta.class);
//
//        for(PassengerMeta passengerMeta : list){
////            passengerdao.savePassenger(passengerMeta);
//            passengerMap.put(passengerMeta.getPassenger_id() , passengerMeta);
//        }
//    }
//}
