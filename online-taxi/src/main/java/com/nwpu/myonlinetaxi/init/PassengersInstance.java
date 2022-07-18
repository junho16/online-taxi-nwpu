package com.nwpu.myonlinetaxi.init;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.entity.meta.PassengerMeta;
import com.nwpu.myonlinetaxi.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Junho
 * @date 2022/7/16 17:08
 */
@Component
public class PassengersInstance {

    //用户json文件名
    private static String passengerFileName;

    @Value("${init.bean.passenger_file_name}")
    public void setFileName(String passenger_file_name) {
        passengerFileName = passenger_file_name;
    }

    private static List<PassengerMeta> passengerMetaList;

    private PassengersInstance(){
    }

    public static List<PassengerMeta> getUserList() {
        if (passengerMetaList == null) {
            initUserList();
        }
        return passengerMetaList;
    }

    public static boolean isInit(){
        return passengerMetaList != null;
    }

    /**
     *  init passenger
     * @return
     */
    private static void initUserList() {
        List<PassengerMeta> list = JSONObject.parseArray(JsonUtil.readJsonFile(passengerFileName), PassengerMeta.class);
        passengerMetaList = list;
    }

}
