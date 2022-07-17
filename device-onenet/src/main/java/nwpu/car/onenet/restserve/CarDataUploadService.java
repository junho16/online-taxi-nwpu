package nwpu.car.onenet.restserve;

import com.onenet.studio.acc.sdk.OpenApi;
import lombok.extern.slf4j.Slf4j;
import nwpu.car.api.CarOpenApiExtention;
import nwpu.car.api.dto.GeoLocationStructDTO;
import nwpu.car.onenet.util.FormatUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * rest上报网约车实时数据
 * @author Junho
 * @date 2022/7/16 22:23
 */
@Slf4j
@Component
public class CarDataUploadService {

    @Resource
    ConcurrentHashMap<String , OpenApi> openApiMap;

    /**
     * 上传车辆实时数据
     * @param id
     * @param lon
     * @param lat
     */
    public void uploadPos(String id , double lon , double lat){
        OpenApi openApi = openApiMap.get(id);
        if(openApi != null){
            GeoLocationStructDTO geoLocationStructDTO = new GeoLocationStructDTO(
                    FormatUtil.getPosFormatDouble(lat) , FormatUtil.getPosFormatDouble(lon));
            try {
                CarOpenApiExtention apiExt = new CarOpenApiExtention(openApi);
                apiExt.geoLocationPropertyUpload( geoLocationStructDTO , 5000);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("上报数据失败 ，异常为{}." , e.getMessage());
            }
        }else{
            log.info("上报数据失败 ，id 为 {} 的网约车未加载." , id);
        }
    }
}
