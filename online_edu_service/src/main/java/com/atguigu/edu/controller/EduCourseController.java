package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.request.CourseInfoVo;
import com.atguigu.request.QueryCourse;
import com.atguigu.response.EduCourseConfirmVo;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-15
 */
@RestController
@RequestMapping("/edu/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    //1.保存课程信息
    @PostMapping("saveCourseInfo")
    public RetVal saveCourseInfo(CourseInfoVo courseInfoVo){
        courseService.saveCourseInfo(courseInfoVo);
        return RetVal.success();
    }
    //2.课程管理之列表查询
    @PostMapping("getCoursePageByCondition/{pageNum}/{pageSize}")
    public RetVal getCoursePageByCondition(
            @PathVariable long pageNum,
            @PathVariable long pageSize,
            QueryCourse queryCourse){
        Page<EduCourse> coursePage = new Page<>(pageNum,pageSize);
        courseService.getCoursePageByCondition(coursePage,queryCourse);
        long total = coursePage.getTotal();
        List<EduCourse> courseList = coursePage.getRecords();
        return RetVal.success().data("total",total).data("courseList",courseList);
    }
    //3.根据课程id查询课程信息
    @GetMapping("{id}")
    public RetVal getCourseById(@PathVariable String id){
        CourseInfoVo courseInfoVo=courseService.getCourseById(id);
        return RetVal.success().data("courseInfo",courseInfoVo);
    }
    //4.更改课程信息
    @PostMapping("updateCourseInfo")
    public RetVal updateCourseInfo(CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return RetVal.success();
    }
    //5.删除课程信息
    @DeleteMapping("{id}")
    public RetVal deleteCourseInfo(@PathVariable String id){
        courseService.deleteCourseInfo(id);
        return RetVal.success();
    }
    //6.课程发布确认信息
    @GetMapping("getCourseConfirmInfo/{coureseId}")
    public RetVal getCourseConfirmInfo(@PathVariable String coureseId){
        EduCourseConfirmVo courseConfirmVo=courseService.getCourseConfirmInfo(coureseId);
        return  RetVal.success().data("courseConfirm",courseConfirmVo);
    }
    //7.发布课程
    @GetMapping("publishCourse/{coureseId}")
    public RetVal publishCourse(@PathVariable String coureseId){
        EduCourse course = new EduCourse();
        course.setId(coureseId);
        //修改课程状态
        course.setStatus("Normal");
        courseService.updateById(course);
        return  RetVal.success();
    }



}

