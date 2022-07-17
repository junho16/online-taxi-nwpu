package com.nwpu.myonlinetaxi.init;

import com.alibaba.fastjson.JSONObject;
import com.nwpu.myonlinetaxi.entity.Passenger;
import com.nwpu.myonlinetaxi.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author Junho
 * @date 2022/7/16 17:08
 */
public class PassengersInstance {

    //用户json文件名
    @Value("${init.bean.user_file_name}")
    private static String userFileName;

    private static List<Passenger> passengerList;

    private PassengersInstance(){
    }

    public static List<Passenger> getUserList() {
        if (passengerList == null) {
            initUserList();
        }
        return passengerList;
    }

    /**
     *  init user
     * @return
     */
    private static void initUserList() {
        List<Passenger> list = JSONObject.parseArray(JsonUtil.readJsonFile(userFileName), Passenger.class);
        passengerList = list;
    }

}
