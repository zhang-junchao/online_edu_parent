package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.mapper.EduTeacherMapper;
import com.atguigu.edu.service.EduTeacherService;
import com.atguigu.request.TeacherConditon;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-10
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public void queryTeacherPageByCondition(Page<EduTeacher> teacherPage, TeacherConditon teacherConditon) {
        //获取每个参数
        String name = teacherConditon.getName();
        Integer level = teacherConditon.getLevel();
        String beginTime = teacherConditon.getBeginTime();
        String endTime = teacherConditon.getEndTime();
        //构建查询条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //判断是否为空
        if(StringUtils.isNotEmpty(name)){
            wrapper.like("name",name);
        }
        if(level!=null){
            wrapper.eq("level",level);
        }
        if(StringUtils.isNotEmpty(beginTime)){
            wrapper.ge("gmt_create",beginTime);
        }
        if(StringUtils.isNotEmpty(endTime)){
            wrapper.le("gmt_create",endTime);
        }
        baseMapper.selectPage(teacherPage,wrapper);
    }

    @Override
    public Map<String, Object> queryTeacherByPage(Page<EduTeacher> teacherPage) {
        baseMapper.selectPage(teacherPage, null);
        List<EduTeacher> teacherList = teacherPage.getRecords();
        long pages = teacherPage.getPages();
        long total = teacherPage.getTotal();
        long currentPage = teacherPage.getCurrent();
        boolean hasNext = teacherPage.hasNext();
        boolean hasPrevious = teacherPage.hasPrevious();
        long size = teacherPage.getSize();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("teacherList", teacherList);
        map.put("currentPage", currentPage);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;

    }
}
