package com.atguigu;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class StudentListener extends AnalysisEventListener<Student> {
    //一行一行的读excel里面的数据
    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        System.out.println("*****--"+student);
    }

    //读取表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头---"+headMap);
    }

    //读取完excel以后的操作
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("读取数据完成");
    }
}
