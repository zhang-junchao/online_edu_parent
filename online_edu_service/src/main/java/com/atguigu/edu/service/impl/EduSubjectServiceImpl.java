package com.atguigu.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.edu.entity.EduSubject;
import com.atguigu.edu.entity.ExcelSubject;
import com.atguigu.edu.handler.EduException;
import com.atguigu.edu.listener.SubjectListener;
import com.atguigu.edu.mapper.EduSubjectMapper;
import com.atguigu.edu.service.EduSubjectService;
import com.atguigu.response.SubjectVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-14
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    @Autowired
    private SubjectListener subjectListener;

    @Override
    public void uploadSubject(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream, ExcelSubject.class,subjectListener).doReadAll();
    }
    @Override
    //判断数据库中是否有该课程
    public EduSubject existSubject(String id, String subjectName){
        //根据学科parentId与学科的title判断是否存在
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",subjectName);
        wrapper.eq("parent_id",id);
        EduSubject subject = baseMapper.selectOne(wrapper);
        return subject;
    }

    @Override
    public List<SubjectVo> getAllSubject() {
        //a.查询所有的课程分类(查询到所有的学员)
        List<EduSubject> allSubject = baseMapper.selectList(null);
        //所有的父课程
        List<SubjectVo> parentSubjectVos = new ArrayList<>();
        //b.查找所有的父课程(找组长)
        for (EduSubject subject : allSubject) {
            //判断标准parent_id=0
            if(subject.getParentId().equals("0")){
                SubjectVo subjectVo = new SubjectVo();
                //把两个类里面拥有相同的属性 赋值过去
                BeanUtils.copyProperties(subject,subjectVo);
                parentSubjectVos.add(subjectVo);
            }
        }
        //c.把所有的父课程放到一个角落(map) key   父课程的id  value 父课程对象本身
        Map<String, SubjectVo> parentSubjectMap = new HashMap<>();
        for (SubjectVo parentSubjectVo : parentSubjectVos) {
            parentSubjectMap.put(parentSubjectVo.getId(),parentSubjectVo);
        }
        //d.找到所有的子课程
        for (EduSubject subject : allSubject) {
            //判断标准 parent_id!=0
            if(!subject.getParentId().equals("0")){
                //得到子课程的parent_id 根据parent_id从角落里面找到父课程
                SubjectVo parentSubjectVo = parentSubjectMap.get(subject.getParentId());
                //把子课程放到父课程的children属性当中 成为它的儿子节点
                SubjectVo child = new SubjectVo();
                //把两个类里面拥有相同的属性 赋值过去
                BeanUtils.copyProperties(subject,child);
                parentSubjectVo.getChildren().add(child);
            }
        }
        //e.返回所有的父课程
        return parentSubjectVos;
    }

    @Override
    public boolean deleteSubjectById(String id) {
        //在删除节点之前判断 节点里面是否有子节点
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        //没有子节点
        if(count==0){
            int rows = baseMapper.deleteById(id);
            return rows>0;
        }else{
            //你为啥要抛异常呢
           throw new EduException(20001,"该节点存在子节点");
        }
    }

    @Override
    public boolean saveParentSubject(EduSubject subject) {
        EduSubject existSubject = existSubject("0", subject.getTitle());
        if(existSubject==null){
            existSubject=new EduSubject();
            existSubject.setParentId("0");
            existSubject.setTitle(subject.getTitle());
            int rows = baseMapper.insert(existSubject);
            return rows>0;
        }
        return false;
    }

    @Override
    public boolean saveChildSubject(EduSubject subject) {
        EduSubject existSubject = existSubject(subject.getParentId(), subject.getTitle());
        if(existSubject==null){
            int rows = baseMapper.insert(subject);
            return rows>0;
        }
        return false;
    }
}
