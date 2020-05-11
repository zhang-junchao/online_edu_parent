package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.DailyStatistics;
import com.atguigu.edu.mapper.DailyStatisticsMapper;
import com.atguigu.edu.service.DailyStatisticsService;
import com.atguigu.edu.service.UserServiceFeign;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author 张俊超
 * @since 2020-04-20
 */
@Service
public class DailyStatisticsServiceImpl extends ServiceImpl<DailyStatisticsMapper, DailyStatistics> implements DailyStatisticsService {

    @Autowired
    private UserServiceFeign userServiceFeign;
    @Override
    public void generate(String day) {
        RetVal retVal = userServiceFeign.queryRegisterNum(day);
        Integer registerNum = (Integer)retVal.getData().get("registerNum");
        DailyStatistics dailyStatistics = new DailyStatistics();
        dailyStatistics.setRegisterNum(registerNum);
        dailyStatistics.setLoginNum(RandomUtils.nextInt(300,400));
        dailyStatistics.setCourseNum(RandomUtils.nextInt(300,400));
        dailyStatistics.setVideoViewNum(RandomUtils.nextInt(300,400));
        dailyStatistics.setDateCalculated(day);

        baseMapper.insert(dailyStatistics);
        //new DailyStatisticsService();
    }

    @Override
    public Map<String,Object> showStatistics(String dataType, String beginTime, String endTime) {
        ArrayList<String> xData = new ArrayList<>();
        ArrayList<Integer> yData = new ArrayList<>();
        QueryWrapper<DailyStatistics> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",beginTime,endTime);
        List<DailyStatistics> dailyStatisticsList = baseMapper.selectList(wrapper);
        for (DailyStatistics dailyStatistics : dailyStatisticsList) {
            // x轴 时间信息
            xData.add(dailyStatistics.getDateCalculated());
            // y轴 数量
            switch (dataType){
                case "login_num" :
                    yData.add(dailyStatistics.getLoginNum());
                    break;
                case "register_num" :
                    yData.add(dailyStatistics.getRegisterNum());
                    break;
                case "video_view_num" :
                    yData.add(dailyStatistics.getVideoViewNum());
                    break;
                case "course_num" :
                    yData.add(dailyStatistics.getCourseNum());
                    break;
            }
        }
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("xData",xData);
        retMap.put("yData",yData);
        return retMap;
    }
}
