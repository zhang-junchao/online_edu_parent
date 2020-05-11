package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduChapter;
import com.atguigu.response.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-17
 */
public interface EduChapterService extends IService<EduChapter> {

    boolean saveChapter(EduChapter chapter);

    boolean deleteChapter(String chapterId);

    List<ChapterVo> getChapterAndSection(String courseId);

    void deleteChapterByCourseId(String id);
}
