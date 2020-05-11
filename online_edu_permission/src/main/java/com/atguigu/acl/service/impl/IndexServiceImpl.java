package com.atguigu.acl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.acl.entity.TPermission;
import com.atguigu.acl.entity.TUser;
import com.atguigu.acl.helper.MenuHelper;
import com.atguigu.acl.helper.TPermissionHelper;
import com.atguigu.acl.service.IndexService;
import com.atguigu.acl.service.TPermissionService;
import com.atguigu.acl.service.TRoleService;
import com.atguigu.acl.service.TUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private TRoleService roleService;
    @Autowired
    private TUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TPermissionService permissionService;

    @Override
    public Map<String, Object> getUserInfo(String userName) {
        //2.根据用户信息获得用户角色
        QueryWrapper<TUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userName);
        TUser dbUser = userService.getOne(wrapper);
        List<String> roleNameList = roleService.queryRoleNameByUserId(dbUser.getId());
        Map<String, Object> userInfo=new HashMap<>();
        userInfo.put("name",userName);
        userInfo.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        userInfo.put("roles", roleNameList);

        //permissionList作用是为了什么？ 页面去判断 是否有 hasPerm
        List<String> permissionList  = (List<String>)redisTemplate.opsForValue().get(userName);
        userInfo.put("permissionNameList", permissionList);
        return userInfo;
    }

    @Override
    public List<JSONObject> getDynamicRouter(String userName) {
        QueryWrapper<TUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userName);
        TUser dbUser = userService.getOne(wrapper);
        List<TPermission> permissionList=null;
        //如果是超级用户不需要 再去连接查询相关表
        if("admin".equals(userName)){
            permissionList = permissionService.list(null);
        }else{
            permissionList = permissionService.queryPermissionByUserId(null,dbUser.getId());
        }
        List<TPermission> formatPermission = TPermissionHelper.bulid(permissionList);
        List<JSONObject> jsonList = MenuHelper.bulid(formatPermission);
        return jsonList;
    }
}
