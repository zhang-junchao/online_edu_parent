package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduSubject;
import com.atguigu.response.SubjectVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-14
 */
public interface EduSubjectService extends IService<EduSubject> {

    void uploadSubject(MultipartFile file) throws Exception;

    //判断数据库中是否有该课程
    EduSubject existSubject(String id, String subjectName);

    List<SubjectVo> getAllSubject();

    boolean deleteSubjectById(String id);

    boolean saveParentSubject(EduSubject subject);

    boolean saveChildSubject(EduSubject subject);
}
