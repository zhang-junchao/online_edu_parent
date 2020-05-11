package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduSection;
import com.atguigu.edu.handler.EduException;
import com.atguigu.edu.mapper.EduSectionMapper;
import com.atguigu.edu.service.EduSectionService;
import com.atguigu.edu.service.VideoServiceFeign;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程小节 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-17
 */
@Service
public class EduSectionServiceImpl extends ServiceImpl<EduSectionMapper, EduSection> implements EduSectionService {
    @Autowired
    private VideoServiceFeign videoServiceFeign;
    @Override
    public void addSection(EduSection section) {
        //1.判断是否存在小节
        QueryWrapper<EduSection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",section.getCourseId());
        queryWrapper.eq("chapter_id",section.getChapterId());
        queryWrapper.eq("title",section.getTitle());
        EduSection existSection = baseMapper.selectOne(queryWrapper);
        if(existSection==null){
            baseMapper.insert(section);
        }else{
            throw new EduException(20001,"存在重复的小节");
        }
    }

    @Override
    public void deleteSection(String id) {
        //1.通过小节id查询小节信息
        EduSection section = baseMapper.selectById(id);
        //2.查询到小节信息以后得到小节的videoSourceId
        String videoSourceId = section.getVideoSourceId();
        //3.判断videoSourceId是否为空
        if(StringUtils.isNotEmpty(videoSourceId)){
            //TODO 远程调用video微服务删除视频
            videoServiceFeign.deleteSingleVideo(videoSourceId);
        }
        baseMapper.deleteById(id);
    }

    @Override
    public void deleteSectionByCourseId(String id) {
        //1.根据课程id查询所有该课程拥有的小节
        QueryWrapper<EduSection> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",id);
        List<EduSection> sectionList = baseMapper.selectList(wrapper);
        //2.迭代 把该课程所有的小节视频id拿到
        List<String> videoIdList = new ArrayList<>();
        for (EduSection section : sectionList) {
            String videoSourceId = section.getVideoSourceId();
            if(StringUtils.isNotEmpty(videoSourceId)){
                videoIdList.add(videoSourceId);
            }
        }
        //TODO 3.通过RPC的形式删除视频
        videoServiceFeign.deleteMultiVideo(videoIdList);
        //4.根据课程id删除小节
        baseMapper.delete(wrapper);
    }
}
