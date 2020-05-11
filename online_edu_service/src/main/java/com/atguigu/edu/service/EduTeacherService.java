package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.request.TeacherConditon;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-10
 */
public interface EduTeacherService extends IService<EduTeacher> {

    void queryTeacherPageByCondition(Page<EduTeacher> teacherPage, TeacherConditon teacherConditon);

    Map<String, Object> queryTeacherByPage(Page<EduTeacher> teacherPage);
}
