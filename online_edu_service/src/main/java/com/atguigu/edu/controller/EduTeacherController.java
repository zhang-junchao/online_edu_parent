package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.service.EduTeacherService;
import com.atguigu.request.TeacherConditon;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-10
 */
@RestController
@RequestMapping("/edu/teacher")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService teacherService;

    //1.查询所有讲师
    @GetMapping
    public RetVal getAllTeacher() /**throws EduException */ {
//        try {
//            int a=1/0;
//        }catch (Exception e){
//           throw new EduException();
//        }
        List<EduTeacher> teacherList = teacherService.list(null);
        return RetVal.success().data("teacherList",teacherList);
    }
    //2.删除讲师功能
    @DeleteMapping("{id}")
    public RetVal deleteTeacherById(@PathVariable("id") String id){
        boolean result = teacherService.removeById(id);
        if(result){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }
    //3.讲师分页查询
    @GetMapping("queryTeacherPage/{pageNum}/{pageSize}")
    public RetVal queryTeacherPage(@PathVariable long pageNum,@PathVariable long pageSize){
        Page<EduTeacher> teacherPage = new Page<>(pageNum,pageSize);
        teacherService.page(teacherPage, null);
        //总记录数
        long total = teacherPage.getTotal();
        List<EduTeacher> teacherList = teacherPage.getRecords();
        return RetVal.success().data("total",total).data("rows",teacherList);

    }
    //4.讲师分页查询 带条件
    @GetMapping("queryTeacherPageByCondition/{pageNum}/{pageSize}")
    public RetVal queryTeacherPageByCondition(@PathVariable long pageNum,@PathVariable long pageSize,
        TeacherConditon teacherConditon){
        Page<EduTeacher> teacherPage = new Page<>(pageNum,pageSize);
        teacherService.queryTeacherPageByCondition(teacherPage,teacherConditon);
        //总记录数
        long total = teacherPage.getTotal();
        List<EduTeacher> teacherList = teacherPage.getRecords();
        return RetVal.success().data("total",total).data("rows",teacherList);

    }
    //5.添加讲师
    @PostMapping
    public RetVal saveTeacher(EduTeacher teacher){
        boolean result = teacherService.save(teacher);
        if(result){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }
    //6.根据id查询讲师
    @GetMapping("{id}")
    public RetVal queryTeacherById(@PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        return RetVal.success().data("teacher",teacher);
    }
    //7.更新讲师
    @PutMapping
    public RetVal updateTeacher(EduTeacher teacher){
        boolean result = teacherService.updateById(teacher);
        if(result){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }

}

