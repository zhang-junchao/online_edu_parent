package com.atguigu.edu.handler;

import com.atguigu.response.RetVal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    //全局异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RetVal error(Exception e){
        //处理异常逻辑代码
        e.printStackTrace();
        System.out.println("处理公司的复杂业务");
        return RetVal.error().message("全局异常出现啦");
    }
    //特殊异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public RetVal error(ArithmeticException e){
        //处理异常逻辑代码
        //e.printStackTrace();
        System.out.println("处理公司的复杂业务");
        return RetVal.error().message("特殊异常出现啦");
    }
    //自定义异常
    @ExceptionHandler(EduException.class)
    @ResponseBody
    public RetVal error(EduException e){
        //处理异常逻辑代码
        return RetVal.error().message(e.getMessage());
    }
}
