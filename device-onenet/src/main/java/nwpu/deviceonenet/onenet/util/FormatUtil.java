package nwpu.deviceonenet.onenet.util;

import java.text.DecimalFormat;

/**
 * @author Junho
 * @date 2022/7/17 14:19
 */
public class FormatUtil {

    public static Double getPosFormatDouble(Double number){
        DecimalFormat decimalFormat = new DecimalFormat("0.0000000000000000");
        return Double.parseDouble(decimalFormat.format(number));
    }

}
