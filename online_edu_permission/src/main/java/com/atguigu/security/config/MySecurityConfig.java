package com.atguigu.security.config;

import com.atguigu.security.filter.LoginAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
//开启安全框架
@EnableWebSecurity
//开启 判断用户是否对某个控制层是否有访问权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RedisTemplate redisTemplate;
    //认证 或者查询 权限相关内容
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    //主要是权限的相关配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //不拦截csrf
        http.csrf().disable();
        //登录认证过滤器   authenticationManager已经在spring容器中拥有了
        http.addFilter(new LoginAuthenticationFilter(authenticationManager(),redisTemplate));
    }




}
