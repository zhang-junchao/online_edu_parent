package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.CourseDetailInfo;
import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.EduCourseDescription;
import com.atguigu.edu.handler.EduException;
import com.atguigu.edu.mapper.EduCourseMapper;
import com.atguigu.edu.service.EduChapterService;
import com.atguigu.edu.service.EduCourseDescriptionService;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.edu.service.EduSectionService;
import com.atguigu.request.CourseInfoVo;
import com.atguigu.request.QueryCourse;
import com.atguigu.response.EduCourseConfirmVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-15
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    private EduCourseDescriptionService descriptionService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduSectionService sectionService;
    @Override
    public void saveCourseInfo(CourseInfoVo courseInfoVo) {
        //保存课程基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        baseMapper.insert(eduCourse);
        //保存课程描述信息
        EduCourseDescription description = new EduCourseDescription();
        String id = eduCourse.getId();
        //共用一个主键  把id生成策略改为input
        description.setId(id);
        description.setDescription(courseInfoVo.getDescription());
        descriptionService.save(description);

    }

    @Override
    public void getCoursePageByCondition(Page<EduCourse> coursePage, QueryCourse queryCourse) {
        String title = queryCourse.getTitle();
        String status = queryCourse.getStatus();
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断是否为空
        if(StringUtils.isNotEmpty(title)){
            wrapper.like("title",title);
        }
        if(StringUtils.isNotEmpty(status)){
            wrapper.like("status",status);
        }
        baseMapper.selectPage(coursePage,wrapper);
    }

    @Override
    public CourseInfoVo getCourseById(String id) {
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        //课程基本信息
        EduCourse eduCourse = baseMapper.selectById(id);
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        //课程描述信息
        EduCourseDescription courseDescription = descriptionService.getById(id);
        if(courseDescription!=null){
            courseInfoVo.setDescription(courseDescription.getDescription());
        }

        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程基本信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,course);
        baseMapper.updateById(course);
        //修改课程描述信息
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        descriptionService.updateById(description);
    }

    @Override
    public void deleteCourseInfo(String id) {
        //1.删除章节
        chapterService.deleteChapterByCourseId(id);
        //2.删除小节信息
        sectionService.deleteSectionByCourseId(id);

        //3.删除课程信息
        int rows = baseMapper.deleteById(id);
        if(rows==0){
            throw new EduException(20001,"删除课程信息失败");
        }
        //4.删除课程描述信息
        descriptionService.removeById(id);
        //删除阿里云里面的内容
    }

    @Override
    public EduCourseConfirmVo getCourseConfirmInfo(String coureseId) {
        return baseMapper.getCourseConfirmInfo(coureseId);
    }

    @Override
    public List<EduCourse> queryCourseByTeacherId(String teacherId) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);
        List<EduCourse> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    @Override
    public Map<String, Object> queryCourseByPage(Page<EduCourse> coursePage) {
        baseMapper.selectPage(coursePage, null);
        List<EduCourse> teacherList = coursePage.getRecords();
        long pages = coursePage.getPages();
        long total = coursePage.getTotal();
        long currentPage = coursePage.getCurrent();
        boolean hasNext = coursePage.hasNext();
        boolean hasPrevious = coursePage.hasPrevious();
        long size = coursePage.getSize();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseList", teacherList);
        map.put("currentPage", currentPage);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    @Override
    public CourseDetailInfo queryCourseDetailById(String courseId) {
        return baseMapper.queryCourseDetailById(courseId);
    }
}
