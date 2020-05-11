package com.atguigu.acl.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.acl.service.IndexService;
import com.atguigu.acl.service.TPermissionService;
import com.atguigu.response.RetVal;
import com.atguigu.security.utils.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/acl/index")
@CrossOrigin
public class IndexController {
    @Autowired
    private TPermissionService permissionService;
    @Autowired
    private IndexService indexService;
    @Autowired
    private RedisTemplate redisTemplate;

//    //登录的方法
//    @PostMapping("login")
//    public RetVal login(){
//        return RetVal.success().data("token","admin");
//    }
    //获取信息的方法
    @GetMapping("info")
    public RetVal info(){
        //1.获知是哪一个用户
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> userInfo = indexService.getUserInfo(userName);
        return RetVal.success().data(userInfo);
    }

    /**
     * 动态获取用户菜单
     * @return
     */
    @GetMapping("router")
    public RetVal getDynamicRouter(){
        //获取当前登录用户用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JSONObject> jsonList=indexService.getDynamicRouter(userName);
        return RetVal.success().data("permissionList", jsonList);
    }

    @PostMapping("logout")
    public RetVal logout(HttpServletRequest request){
        String token = request.getHeader("token");
        if(StringUtils.isNotEmpty(token)){
            String userName = TokenUtils.getUserByToken(token);
            redisTemplate.delete(userName);
        }
        return RetVal.success();
    }

}