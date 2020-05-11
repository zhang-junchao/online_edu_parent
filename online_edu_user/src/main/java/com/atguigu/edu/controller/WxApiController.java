package com.atguigu.edu.controller;

import com.atguigu.edu.entity.MemberCenter;
import com.atguigu.edu.service.MemberCenterService;
import com.atguigu.edu.utils.HttpClientUtils;
import com.atguigu.edu.utils.JwtUtils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

//@RestController
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Value("${wx.open.app_id}")
    private String WX_OPEN_APP_ID;
    @Value("${wx.open.app_secret}")
    private String WX_OPEN_APP_SECRET;
    @Value("${wx.open.redirect_url}")
    private String WX_OPEN_REDIRECT_URL;

    @Autowired
    private MemberCenterService memberCenterService;


    //1.获取一个二维码
    @GetMapping("login")
    public String login() throws Exception {
        /**a.获取二维码地址
                appid创建应用的时候微信那边给
                redirect_uri扫描二维码以后跳到那个网站
                state可以设置为一个随机值
         */
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //b.对rediect_url进行编码
        String ENCODE_REDIRECT = URLEncoder.encode(WX_OPEN_REDIRECT_URL, "utf-8");
        //c.拼接微信二维码地址
        String state="atguigu";
        String qrCodeUrl = String.format(baseUrl, WX_OPEN_APP_ID, ENCODE_REDIRECT, state);
        return "redirect:"+qrCodeUrl;
    }

    //2.当扫描二维码并且授权后 将重定向到redirect_url路径
    //code=001ZMOrb1PC9ay0a6svb1cIQrb1ZMOrm&state=atguigu
    @GetMapping("callback")
    public Object callback(String code,String state) throws Exception {
        //3.通过code+appid+appsecret得到用户的access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        baseAccessTokenUrl = String.format(baseAccessTokenUrl, WX_OPEN_APP_ID, WX_OPEN_APP_SECRET, code);
        //模拟http请求
        String info = HttpClientUtils.get(baseAccessTokenUrl);
        //从返回信息中拿到access_token与openid
        Gson gson = new Gson();
        HashMap infoMap = gson.fromJson(info, HashMap.class);
        String access_token = (String)infoMap.get("access_token");
        String openid = (String)infoMap.get("openid");

        //3.根据access_token和openid获取用户信息
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        userInfoUrl = String.format(userInfoUrl, access_token, openid);
        String userInfo = HttpClientUtils.get(userInfoUrl);
        //拿到用户信息
        HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
        String nickname = (String)userInfoMap.get("nickname");
        //Double sex = (Double)userInfoMap.get("sex");
        String headimgurl = (String)userInfoMap.get("headimgurl");
        //先判断数据库是否有 根据openid
        MemberCenter existMemberCenter= memberCenterService.queryUserByOpenId(openid);
        //4.拿到个人信息以后会存储到第三方网站数据库里面
        if(existMemberCenter==null){
            existMemberCenter=new MemberCenter();
            existMemberCenter.setAvatar(headimgurl);
            existMemberCenter.setNickname(nickname);
            existMemberCenter.setOpenid(openid);
            //existMemberCenter.setSex();
            memberCenterService.save(existMemberCenter);
        }
        String token = JwtUtils.geneJsonWebToken(existMemberCenter);
        return "redirect:http://127.0.0.1:3000?token="+token;
    }


}
