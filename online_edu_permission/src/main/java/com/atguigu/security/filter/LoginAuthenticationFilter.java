package com.atguigu.security.filter;

import com.atguigu.acl.entity.TUser;
import com.atguigu.response.RetVal;
import com.atguigu.security.utils.ResponseUtil;
import com.atguigu.security.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//基于用户名和密码的身份认证过滤器
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private RedisTemplate redisTemplate;

    public LoginAuthenticationFilter(AuthenticationManager authenticationManager,RedisTemplate redisTemplate){
        this.authenticationManager=authenticationManager;
        this.redisTemplate=redisTemplate;
        this.setPostOnly(false);
        //地址匹配工具类
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/acl/index/login", "POST");
        this.setRequiresAuthenticationRequestMatcher(matcher);
    }

    //尝试身份认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //把页面输入的 账号密码 转换成Tuser对象
            TUser userUI = new ObjectMapper().readValue(request.getInputStream(), TUser.class);
            //进行认证 封装成一个对象(胸牌)
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userUI.getUsername(), userUI.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //认证成功
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //1.把用户信息封装成一个token信息
        User user = (User) authResult.getPrincipal();
        //当做把username加密为一段密文
        String token = TokenUtils.generateUserToken(user.getUsername());
        //2.把该用户拥有的权限放到redis当中
        List<String> permissionList = new ArrayList<>();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            permissionList.add(authority.getAuthority().toString());
        }
        //把redis键序列化一下
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //缓存权限信息 方便后面从缓存里面取得信息
        redisTemplate.opsForValue().set(user.getUsername(),permissionList);
        //3.把用户token信息输出给浏览器
        ResponseUtil.out(response, RetVal.success().data("token",token));
    }
}
