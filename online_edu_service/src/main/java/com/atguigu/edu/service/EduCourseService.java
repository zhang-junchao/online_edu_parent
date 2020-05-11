package com.atguigu.edu.service;

import com.atguigu.edu.entity.CourseDetailInfo;
import com.atguigu.edu.entity.EduCourse;
import com.atguigu.request.CourseInfoVo;
import com.atguigu.request.QueryCourse;
import com.atguigu.response.EduCourseConfirmVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-15
 */
public interface EduCourseService extends IService<EduCourse> {

    void saveCourseInfo(CourseInfoVo courseInfoVo);

    void getCoursePageByCondition(Page<EduCourse> coursePage, QueryCourse queryCourse);

    CourseInfoVo getCourseById(String id);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    void deleteCourseInfo(String id);

    EduCourseConfirmVo getCourseConfirmInfo(String coureseId);

    List<EduCourse> queryCourseByTeacherId(String teacherId);

    Map<String, Object> queryCourseByPage(Page<EduCourse> coursePage);

    CourseDetailInfo queryCourseDetailById(String courseId);

}
