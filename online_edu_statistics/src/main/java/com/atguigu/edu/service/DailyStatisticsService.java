package com.atguigu.edu.service;

import com.atguigu.edu.entity.DailyStatistics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author 张俊超
 * @since 2020-04-20
 */
public interface DailyStatisticsService extends IService<DailyStatistics> {

    void generate(String day);

    Map<String,Object> showStatistics(String dataType, String beginTime, String endTime);
}
