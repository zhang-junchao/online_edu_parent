package com.atguigu.edu.controller;

import com.atguigu.response.RetVal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/edu")
@CrossOrigin
public class EntranceController {
    @PostMapping("login")
    public RetVal login(){
        //登录逻辑
        return RetVal.success().data("token","admin");
    }
    @GetMapping("info")
    public RetVal info(){
        //登录逻辑
        return RetVal.success().data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
