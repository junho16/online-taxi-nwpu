package com.nwpu.myonlinetaxi;

import com.onenet.studio.acc.sdk.annotations.ThingsModelConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ThingsModelConfiguration("src/main/resources/model.json")
public class MyOnlineTaxiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyOnlineTaxiApplication.class, args);
    }

}
