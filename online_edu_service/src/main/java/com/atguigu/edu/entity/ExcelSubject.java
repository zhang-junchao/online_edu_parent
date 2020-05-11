package com.atguigu.edu.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelSubject {
    @ExcelProperty(index = 0)
    private String parentSubjectName;
    @ExcelProperty(index = 1)
    private String childSubjectName;
}
