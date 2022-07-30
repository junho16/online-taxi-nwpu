//package com.nwpu.myonlinetaxi.dao.impl;
//
//import com.nwpu.myonlinetaxi.dao.PassengerMongoDao;
//import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
//import com.nwpu.myonlinetaxi.service.PassengerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @author Junho
// * @date 2022/7/19 12:18
// */
//@Component
//public class PassengerMongoImpl implements PassengerMongoDao {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Override
//    public void savePassenger(PassengerMeta passengerMeta) {
//        mongoTemplate.save(passengerMeta);
//    }
//
//    @Override
//    public List<PassengerMeta> getPassengerList() {
//        return mongoTemplate.findAll(PassengerMeta.class);
//    }
//
//    @Override
//    public void changeState(String passenger_id, int state) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("passenger_id").is(passenger_id));
//        Update update = Update.update("state", state);
//        mongoTemplate.upsert(query, update, "passengerMeta");
//    }
//
//    @Override
//    public PassengerMeta getPassenger(String passenger_id) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("passenger_id").is(passenger_id));
//        List<PassengerMeta> list = mongoTemplate.find(query , PassengerMeta.class);
//        return list.size() == 0 ? null : list.get(0);
//    }
//
//    @Override
//    public void changeLonLat(String passenger_id, double lon, double lat) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("passenger_id").is(passenger_id));
//        Update update =new Update();
//        update.set("lon", lon);
//        update.set("lat",lat);
//        mongoTemplate.upsert(query, update, "passengerMeta");
//    }
//}
