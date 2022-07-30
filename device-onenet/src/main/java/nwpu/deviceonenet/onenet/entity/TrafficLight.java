//package nwpu.deviceonenet.onenet.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Random;
//
///**
// * @author Junho
// * @date 2022/7/18 19:58
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class TrafficLight {
//
//    private Integer color;
//
//    private Integer leftTime;
//
//    public void setTrafficInfo(Integer color , Integer leftTime){
//        this.color = color;
//        this.leftTime = leftTime;
//    }
//    /**
//     * 随机一个信号灯状态 1：绿灯   3：红灯    剩余时间：1 ~ 60s
//     * @return
//     */
//    public static TrafficLight getRandomLight(){
//        return new TrafficLight(new Random().nextInt(100) % 2 == 0 ? 1 : 3 , new Random().nextInt(60) + 1);
//    }
//}
