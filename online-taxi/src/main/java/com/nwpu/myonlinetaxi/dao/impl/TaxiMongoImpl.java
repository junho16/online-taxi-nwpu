//package com.nwpu.myonlinetaxi.dao.impl;
//
//import com.nwpu.myonlinetaxi.dao.TaxiMongoDao;
//import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
//import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestOperations;
//
//import java.util.List;
//
///**
// * @author Junho
// * @date 2022/7/19 11:54
// */
//@Component
//public class TaxiMongoImpl implements TaxiMongoDao {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Override
//    public void saveTaxi(TaxiMeta taxiMeta) {
//        mongoTemplate.save(taxiMeta);
//    }
//
//    @Override
//    public List<TaxiMeta> getTaxiList() {
//        return mongoTemplate.findAll(TaxiMeta.class);
//    }
//
//    @Override
//    public void changeState(String taxi_id, int state) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("taxi_id").is(taxi_id));
//        Update update = Update.update("state", state);
//        mongoTemplate.upsert(query, update, "taxiMeta");
//    }
//
//    @Override
//    public void changeLonLat(String taxi_id, double lon, double lat) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("taxi_id").is(taxi_id));
//        Update update =new Update();
//        update.set("lon", lon);
//        update.set("lat", lat);
//        mongoTemplate.upsert(query, update, "taxiMeta");
//    }
//
//    @Override
//    public TaxiMeta getTaxi(String taxi_id) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("taxi_id").is(taxi_id));
//        List<TaxiMeta> list = mongoTemplate.find(query , TaxiMeta.class);
//        return list.size() == 0 ? null : list.get(0);
//    }
//
//}
