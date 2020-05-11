package com.atguigu;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Student {
    //设置那一个属性 在第几列
    @ExcelProperty(value = "学生编号",index = 0)
    private Integer stuNo;
    @ExcelProperty(value = "学生姓名",index = 1)
    private String  stuName;
}
