package com.nwpu.myonlinetaxi.controller;

import com.nwpu.myonlinetaxi.entity.Position;
import com.nwpu.myonlinetaxi.entity.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Junho
 * @date 2022/7/16 20:08
 */
@RestController
@RequestMapping("/taxi")
public class TaxiController {

    /**
     * 上传车辆位置信息
     * @param Pos
     * @return
     */
    @RequestMapping("/uploacPos")
    public R uploadPos(@RequestParam("id")String id , @RequestParam("position")Position Pos){
        return null;
    }
}
