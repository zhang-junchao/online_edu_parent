package com.atguigu.edu.controller.front;

import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.edu.service.EduTeacherService;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edu/front/teacher")
@CrossOrigin
public class FrontTeacherController {
    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;
    //1.查询讲师列表
    @GetMapping("queryTeacherByPage/{pageNum}/{pageSize}")
    public RetVal queryTeacherByPage(@PathVariable Long pageNum,
                                     @PathVariable Long pageSize){
        Page<EduTeacher> teacherPage = new Page<>(pageNum, pageSize);
        Map<String, Object> pageMap = teacherService.queryTeacherByPage(teacherPage);
        return RetVal.success().data(pageMap);
    }
    //2.根据讲师id查询讲师信息
    @GetMapping("queryTeacherDetailById/{teacherId}")
    public RetVal queryTeacherDetailById(@PathVariable String teacherId){
        //查询讲师的基本信息
        EduTeacher teacher = teacherService.getById(teacherId);
        //查询该讲师所讲的课程内容
        List<EduCourse> courseList=courseService.queryCourseByTeacherId(teacherId);
        return RetVal.success().data("teacher",teacher).data("courseList",courseList);
    }
}
