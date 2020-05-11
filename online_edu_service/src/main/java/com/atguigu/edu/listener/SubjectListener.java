package com.atguigu.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.edu.entity.EduSubject;
import com.atguigu.edu.entity.ExcelSubject;
import com.atguigu.edu.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectListener extends AnalysisEventListener<ExcelSubject> {
    @Autowired
    private EduSubjectService subjectService;

    //一行一行的读excel里面的数据
    @Override
    public void invoke(ExcelSubject excelSubject, AnalysisContext analysisContext) {
        //1.第一级课程分类 都需要判断数据库中是否有该课程
        EduSubject parentSubject = subjectService.existSubject("0", excelSubject.getParentSubjectName());
        //如果为空说明数据库中没有该课程分类
        if(parentSubject==null){
            parentSubject = new EduSubject();
            parentSubject.setParentId("0");
            parentSubject.setTitle(excelSubject.getParentSubjectName());
            //保存到数据库中
            subjectService.save(parentSubject);
        }

        //2.第二级分类 保存二级学科的时候需要得到一级学科的id作为它的parentId
        String parentId = parentSubject.getId();
        EduSubject childSubject = subjectService.existSubject(parentId, excelSubject.getChildSubjectName());
        if(childSubject==null){
            childSubject = new EduSubject();
            childSubject.setParentId(parentId);
            childSubject.setTitle(excelSubject.getChildSubjectName());
            //保存到数据库中
            subjectService.save(childSubject);
        }
    }



    //读取完excel以后的操作
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("读取完成");
    }
}
