//package com.nwpu.myonlinetaxi.dao;
//
//import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//
//import java.util.List;
//
///**
// * @author Junho
// * @date 2022/7/19 12:18
// */
//public interface PassengerMongoDao {
//
//    public void savePassenger(PassengerMeta user);
//
//    public List<PassengerMeta> getPassengerList();
//
//    public void changeState(String passenger_id, int state);
//
//    public PassengerMeta getPassenger(String passenger_id);
//
//    public void changeLonLat(String passenger_id, double lon, double lat);
//
//}
