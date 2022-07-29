package nwpu.deviceonenet.onenet.entity.enums;

/**
 * @author Junho
 * @date 2022/7/29 23:05
 */
public enum DeviceType {

    //0：空闲可叫车 1：已叫车 2：在路上即运动中
    taxi("taxi"),
    passenger("passenger");

    private String type;

    DeviceType(String type) {
        this.type = type;
    }

    public String getDeviceType() {
        return type;
    }
}
