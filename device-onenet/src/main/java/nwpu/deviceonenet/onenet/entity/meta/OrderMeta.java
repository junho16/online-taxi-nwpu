package nwpu.deviceonenet.onenet.entity.meta;


import lombok.AllArgsConstructor;
import lombok.Data;
import nwpu.deviceonenet.onenet.entity.Position;

/**
 * @author Junho
 * @date 2022/7/22 13:21
 */
@Data
@AllArgsConstructor
public class OrderMeta {

    private String taxi_id;

    private String passenger_id;

    /**
     * 注意 这个起止位置是通过地图接口拿到的 不能是乘客本身的定位
     */
    private Position startPos;

    private Position endPos;
//
//    private int taxiState;
//
//    private int passengerState;
//
//    private String taxiTip;
//
//    private String passengerTip;

}
