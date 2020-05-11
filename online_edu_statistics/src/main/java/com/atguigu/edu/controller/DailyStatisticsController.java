package com.atguigu.edu.controller;


import com.atguigu.edu.entity.DailyStatistics;
import com.atguigu.edu.service.DailyStatisticsService;
import com.atguigu.edu.service.impl.DailyStatisticsServiceImpl;
import com.atguigu.response.RetVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author 张俊超
 * @since 2020-04-20
 */
@RestController
@RequestMapping("/daily/statistics")
@CrossOrigin
public class DailyStatisticsController {

    @Autowired
    private DailyStatisticsService dailyStatisticsService;
    @GetMapping("generate/{day}")
    public RetVal generate(@PathVariable String day){
        dailyStatisticsService.generate(day);
        return RetVal.success();
    }
    @GetMapping("showStatistics/{dataType}/{beginTime}/{endTime}")
    public RetVal showStatistics(@PathVariable String dataType,
                                 @PathVariable String beginTime,
                                 @PathVariable String endTime){
        Map<String,Object> retMap= dailyStatisticsService.showStatistics(dataType,beginTime,endTime);


        return RetVal.success().data("retMap",retMap);
    }


}

