package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduChapter;
import com.atguigu.edu.entity.EduSection;
import com.atguigu.edu.handler.EduException;
import com.atguigu.edu.mapper.EduChapterMapper;
import com.atguigu.edu.service.EduChapterService;
import com.atguigu.edu.service.EduSectionService;
import com.atguigu.response.ChapterVo;
import com.atguigu.response.SectionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-17
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduSectionService sectionService;
    @Override
    public boolean saveChapter(EduChapter chapter) {
        //判断是否存在
        EduChapter existChapter = existChapter(chapter.getCourseId(), chapter.getTitle());
        if(existChapter==null){
            int insert = baseMapper.insert(chapter);
            return insert>0;
        }else{
            throw new EduException(20001,"章节已经重复");
        }
    }

    public EduChapter existChapter(String courseId, String chapterName) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("title", chapterName);
        EduChapter chapter = baseMapper.selectOne(queryWrapper);
        return chapter;

    }

    @Override
    public boolean deleteChapter(String chapterId) {
        //判断是否有小节
        QueryWrapper<EduSection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);
        int count = sectionService.count(queryWrapper);
        //没有小节
        if(count==0){
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }else{
            throw new EduException(20001,"该章节存在小节");
        }
    }

    @Override
    public List<ChapterVo> getChapterAndSection(String courseId) {
        //1.查询所有的章节
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduChapter> chapterList = baseMapper.selectList(wrapper);
        //2.查询所有的小节
        QueryWrapper<EduSection> sectionWrapper = new QueryWrapper<>();
        sectionWrapper.eq("course_id",courseId);
        List<EduSection> sectionList = sectionService.list(sectionWrapper);
        //3.把小节信息封装到章节信息中
        //a.章节进行迭代
        List<ChapterVo> chapterVos=new ArrayList<>();
        for (EduChapter chapter : chapterList) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            //b.迭代小节
            ArrayList<SectionVo> chapterChildren = new ArrayList<>();
            for (EduSection section : sectionList) {
                //c.用小节的chapterId与章节id比对 如果相同说明属于该章节
                if(section.getChapterId().equals(chapter.getId())){
                    SectionVo sectionVo = new SectionVo();
                    BeanUtils.copyProperties(section,sectionVo);
                    chapterChildren.add(sectionVo);
                }
            }
            //把该章节的小节信息赋值给章节children属性
            chapterVo.setChildren(chapterChildren);
            chapterVos.add(chapterVo);
        }
        return chapterVos;
    }

    @Override
    public void deleteChapterByCourseId(String id) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        baseMapper.delete(wrapper);
    }
}
