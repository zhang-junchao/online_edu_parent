package com.atguigu.edu.controller;


import com.atguigu.edu.entity.MemberCenterVo;
import com.atguigu.edu.service.MemberCenterService;
import com.atguigu.edu.utils.JwtUtils;
import com.atguigu.response.RetVal;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-20
 */
@RestController
@RequestMapping("/member/center")
@CrossOrigin
public class MemberCenterController {
    @Autowired
    private MemberCenterService memberCenterService;

    //1.获取每日有多少人注册网站
    @GetMapping("queryRegisterNum/{day}")
    public RetVal queryRegisterNum(@PathVariable("day") String day){
        Integer registerNum=memberCenterService.queryRegisterNum(day);
        return RetVal.success().data("registerNum",registerNum);
    }
    //2.根据token获取用户信息
    @PostMapping("info/{token}")
    public RetVal info(@PathVariable String token){
        Claims claims = JwtUtils.checkJWT(token);
        String nickname = (String)claims.get("nickname");
        String avatar = (String)claims.get("avatar");
        String id = (String)claims.get("id");

        MemberCenterVo memberCenterVo = new MemberCenterVo();
        memberCenterVo.setId(id);
        memberCenterVo.setNickname(nickname);
        memberCenterVo.setAvatar(avatar);
        return RetVal.success().data("memberCenterVo",memberCenterVo);
    }
}

