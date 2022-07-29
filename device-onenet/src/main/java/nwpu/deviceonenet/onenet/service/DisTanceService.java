package nwpu.deviceonenet.onenet.service;

import nwpu.deviceonenet.onenet.entity.Position;

import java.util.Map;

/**
 * @author Junho
 * @date 2022/7/18 14:24
 */
public interface DisTanceService {

    /**
     * 计算两个经纬度之间的距离
     * @param startLon
     * @param startLat
     * @param endLon
     * @param endLat
     * @return
     */
    public double calcDistance(double startLon, double startLat, double endLon, double endLat);


    /**
     * 获取 一个坐标的 一定距离以内的 经纬度值
     * 单位米 return minLat
     * 最小经度 minLng
     * 最小纬度 maxLat
     * 最大经度 maxLng
     * 最大纬度 minLat
     */
    public Map<Object,Object> getAround(String logStr, String latStr, String raidus);

    /**
     * 获取一定范围内的随机经纬度
     * @return
     */
    public Position getRandomPos(double MinLon, double MaxLon, double MinLat, double MaxLat);

}
