package com.atguigu.edu.controller.front;

import com.atguigu.edu.entity.CourseDetailInfo;
import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.service.EduChapterService;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.response.ChapterVo;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edu/front/course")
@CrossOrigin
public class FrontCourseController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;
    //1.查询课程列表
    @GetMapping("queryCourseByPage/{pageNum}/{pageSize}")
    public RetVal queryCourseByPage(@PathVariable Long pageNum,
                                     @PathVariable Long pageSize){
        Page<EduCourse> coursePage = new Page<>(pageNum, pageSize);
        Map<String, Object> pageMap = courseService.queryCourseByPage(coursePage);
        return RetVal.success().data(pageMap);
    }
    //2.查询课程详情信息
    @GetMapping("queryCourseDetailById/{courseId}")
    public RetVal queryCourseDetailById(@PathVariable String courseId){
        //a.根据课程id得到章节和小节信息
        List<ChapterVo> chapterAndSection = chapterService.getChapterAndSection(courseId);
        //b.获取其他四张表里面
        CourseDetailInfo courseDetailInfo= courseService.queryCourseDetailById(courseId);
        return RetVal.success().data("chapterAndSection",chapterAndSection).data("courseDetailInfo",courseDetailInfo);
    }
}
