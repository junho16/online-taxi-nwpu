package com.nwpu.myonlinetaxi.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.dao.TaxiMongoDao;
import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.TracePos;
import com.nwpu.myonlinetaxi.entity.meta.TaxiMeta;
import com.nwpu.myonlinetaxi.service.TaxiService;
import com.nwpu.myonlinetaxi.thread.TaxiThread;
import com.nwpu.myonlinetaxi.util.JsonUtil;
import com.sun.deploy.trace.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Junho
 * @date 2022/7/16 12:16
 */
@Component
@Slf4j
public class TaxisInstance {

    @Resource
    TaxiService taxiService;

    @Resource
    TaxiMongoDao taxiMongoDao;

    private static TaxiMongoDao taxidao;

    private static TaxiService taxiserve;

    @PostConstruct
    public void initTaxis(){
        taxidao = taxiMongoDao;
        taxiserve = taxiService;
        initTaxiMap();
    }

    //网约车json文件名
    private static String taxiFileName;

    @Value("${init.bean.taxi_file_name}")
    public void setFileName(String taxi_file_name) {
        taxiFileName = taxi_file_name;
    }

    private static ConcurrentHashMap<String , TaxiMeta> taxiMap;

    private TaxisInstance(){
    }

    public static ConcurrentHashMap<String , TaxiMeta> getTaxiMap() {
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
        JSONArray jsonArray = JSONArray.parseArray(JsonUtil.readJsonFile(taxiFileName));

        for(int i = 0; i < jsonArray.size() ; i++ ){
            JSONObject obj = jsonArray.getJSONObject(i);
//            taxidao.saveTaxi(taxiMeta);

            Position startPos = JSON.parseObject(obj.getString("startPos") , Position.class);
            Position endPos = JSON.parseObject(obj.getString("endPos") , Position.class);
            TaxiMeta meta = new TaxiMeta(
                    obj.getString("taxi_id"),
                    obj.getString("taxi_name"),
                    obj.getInteger("state"),
                    Double.parseDouble(obj.getString("lon")),
                    Double.parseDouble(obj.getString("lat")),
                    startPos,
                    endPos
            );

            List<TracePos> initPath = taxiserve.reqDriving(startPos.getLon() , startPos.getLat() , endPos.getLon() , endPos.getLat());
            ConcurrentLinkedQueue<TracePos> initQ = new ConcurrentLinkedQueue();
            initQ.addAll(initPath);
            meta.setTrace(initQ);

            //注意 此处需要使用Taxi_name（DEV_CAR_1） 因为onenet传过来的是设备名称
            taxiMap.put( obj.getString("taxi_name"), meta);

            Thread t = new Thread(new TaxiThread(meta , taxiserve));
            t.start();
        }
    }
}
