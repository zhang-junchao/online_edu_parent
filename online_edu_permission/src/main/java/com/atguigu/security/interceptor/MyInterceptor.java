package com.atguigu.security.interceptor;

import com.atguigu.security.utils.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //当前请求的路径
        System.out.println(request.getRequestURL());
        String token = request.getHeader("token");
        if(StringUtils.isNotEmpty(token)){
            //用户名称
            String userName = TokenUtils.getUserByToken(token);
            //a.通过redis获取用户有哪些权限
            List<String> permissionList  = (List<String>)redisTemplate.opsForValue().get(userName);
            //b.把这些权限放到Security上下文环境中？

            //原因:就是为了 当别人跳过前端暴力执行相关操作 @PreAuthorize("hasAuthority('permission.add')")
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for (String permission : permissionList) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
            //把用户信息与权限相关内容放到security上下文环境中
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName, token, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //返回true通过拦截  false不让通过
        return true;
    }
}
