package nwpu.car.onenet.restserve;

import nwpu.car.onenet.entity.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Junho
 * @date 2022/7/17 10:42
 */
@RestController
@RequestMapping("/onenet")
public class UploadController {

    @Resource
    CarDataUploadService carDataUploadService;

    @RequestMapping("/upload")
    public R uploadPos(@RequestParam("id") String id, @RequestParam("lon") double lon, @RequestParam("lat") double lat) {
        try{
            carDataUploadService.uploadPos(id, lon, lat);
        }catch (Exception e){
            return R.error(500, null);
        }
        return R.ok(null);
    }
}
