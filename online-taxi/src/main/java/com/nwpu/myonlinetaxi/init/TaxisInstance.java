package com.nwpu.myonlinetaxi.init;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.entity.Taxi;
import com.nwpu.myonlinetaxi.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author Junho
 * @date 2022/7/16 12:16
 */
@Component
@Slf4j
public class TaxisInstance {

    //网约车json文件名
    @Value("${init.bean.taxi_file_name}")
    private static String taxiFileName;

    private static List<Taxi> taxiList;

    private TaxisInstance(){
    }

    public static List<Taxi> getTaxiList() {
        if (taxiList == null) {
            initTaxiList();
        }
        return taxiList;
    }

    /**
     * init taxi
     * @return
     */
    private static void initTaxiList() {
        List<Taxi> list = JSONObject.parseArray(JsonUtil.readJsonFile(taxiFileName),Taxi.class);
        taxiList = list;
    }

}
