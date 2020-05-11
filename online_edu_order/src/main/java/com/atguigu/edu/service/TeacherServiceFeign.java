package com.atguigu.edu.service;

import com.atguigu.response.RetVal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "EDU-SERVICE")
public interface TeacherServiceFeign {

    @GetMapping("/edu/teacher/{id}")
    public RetVal queryTeacherById(@PathVariable String id);
}
