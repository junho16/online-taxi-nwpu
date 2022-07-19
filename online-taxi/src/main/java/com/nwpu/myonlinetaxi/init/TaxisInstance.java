package com.nwpu.myonlinetaxi.init;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.dao.TaxiMongoDao;
import com.nwpu.myonlinetaxi.entity.Taxi;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/16 12:16
 */
@Component
@Slf4j
public class TaxisInstance {


    @Resource
    TaxiMongoDao taxiMongoDao;

    private static TaxiMongoDao taxidao;

    @PostConstruct
    public void initMongo(){
        taxidao = taxiMongoDao;
    }

    //网约车json文件名
    private static String taxiFileName;

    @Value("${init.bean.taxi_file_name}")
    public void setFileName(String taxi_file_name) {
        taxiFileName = taxi_file_name;
    }

    private static ConcurrentHashMap<String , Taxi> taxiMap;

    private TaxisInstance(){
    }

    public static boolean isInit(){
        return taxiMap != null;
    }

    public static ConcurrentHashMap<String , Taxi> getTaxiMap() {
        if (taxiMap == null) {
            initTaxiMap();
        }
        return taxiMap;
    }

    /**
     * init taxi
     * @return
     */
    private static void initTaxiMap() {
        taxiMap = new ConcurrentHashMap<>();
        List<TaxiMeta> list = JSONObject.parseArray(JsonUtil.readJsonFile(taxiFileName), TaxiMeta.class);
        for(TaxiMeta taxiMeta : list){
            taxidao.saveTaxi(taxiMeta);
            //注意 此处需要使用Taxi_name（DEV_CAR_1） 因为onenet传过来的是设备名称
            taxiMap.put(taxiMeta.getTaxi_name() , new Taxi(taxiMeta));
        }
    }
}
