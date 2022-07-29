//package nwpu.deviceonenet.onenet.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import nwpu.deviceonenet.onenet.entity.meta.PassengerMeta;
//
///**
// * @author Junho
// * @date 2022/7/24 15:23
// */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class PassengerDto {
//
//    private String pid;
//
//    private Double lon;
//
//    private Double lat;
//
//    private int state;
//
//    public static PassengerDto toPassengerDto(PassengerMeta passengerMeta){
//        return new PassengerDto(
//            passengerMeta.getPassenger_id(),
//            passengerMeta.getLon(),
//            passengerMeta.getLat(),
//            passengerMeta.getState()
//        );
//    }
//}
