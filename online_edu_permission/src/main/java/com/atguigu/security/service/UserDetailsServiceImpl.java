package com.atguigu.security.service;

import com.atguigu.acl.entity.TPermission;
import com.atguigu.acl.entity.TUser;
import com.atguigu.acl.service.TPermissionService;
import com.atguigu.acl.service.TUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private TUserService userService;
    @Autowired
    private TPermissionService permissionService;

    //实现身份认证与查询权限
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名称查询用户信息
        QueryWrapper<TUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        TUser dbUser = userService.getOne(wrapper);
        if(dbUser==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //2.查询查询用户拥有什么权限  type=2 封装成权限集合是为了successfulAuthentication调用
        List<TPermission> permissionList = permissionService.queryPermissionByUserId(2, dbUser.getId());
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (TPermission permission : permissionList) {
            authorities.add(new SimpleGrantedAuthority(permission.getPermissionValue()));
        }
        //3.把从数据库中查到的用户信息和权限封装成一个对象 同attemptAuthentication封装的对象进行比对
        return new User(username,dbUser.getPassword(),authorities);
    }
}
