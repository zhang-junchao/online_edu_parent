package com.atguigu;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;

public class EasyExceTest {
    //1.往excel里面写数据
    @Test
    public void testWriteExcel(){
        //往那个文件写数据
        String fileName="C:\\191122\\one.xlsx";
        //写什么数据
        ArrayList<Student> students = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            Student student = new Student();
            student.setStuNo(i);
            student.setStuName("老王"+i);
            students.add(student);
        }
        //执行写数据
        EasyExcel.write(fileName,Student.class).sheet("学生列表").doWrite(students);
        System.out.println("write over");
    }
    //2.在excel读数据
    @Test
    public void testReadExcel(){
        //a.在哪里读
        String fileName="C:\\191122\\one.xlsx";
        //b.读了数据以后如何处理 读那个文件 读到的数据用那个对象匹配 对excel读到的数据如何处理
        EasyExcel.read(fileName,Student.class,new StudentListener()).doReadAll();

    }
}
