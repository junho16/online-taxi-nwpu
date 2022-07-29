package nwpu.deviceonenet.onenet.service;


import nwpu.deviceonenet.onenet.entity.TracePos;
import nwpu.deviceonenet.onenet.entity.meta.TaxiMeta;

import java.util.List;

/**
 * @author Junho
 * @date 2022/7/18 16:47
 */
public interface TaxiService {

    public List<TracePos> reqDriving(Double startLon , Double startLat ,
                                     Double endLon , Double endLat );

    public void pickPassenger(TaxiMeta taxiMeta);

    public void endOrder(TaxiMeta taxiMeta);

}
