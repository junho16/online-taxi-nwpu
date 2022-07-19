package com.nwpu.myonlinetaxi.util;

import java.text.DecimalFormat;

/**
 * @author Junho
 * @date 2022/7/17 14:19
 */
public class FormatUtil {

    public static Double getPosFormatDouble(Double number){
//        DecimalFormat decimalFormat = new DecimalFormat("0.0000000000000000");
        /**
         * 高德地图要求地址的小数点位数不能多于6位
         */
        DecimalFormat decimalFormat = new DecimalFormat("0.000000");
        return Double.parseDouble(decimalFormat.format(number));
    }

}
