package com.atguigu.acl.service.impl;

import com.atguigu.acl.entity.TRole;
import com.atguigu.acl.entity.TUserRole;
import com.atguigu.acl.mapper.TRoleMapper;
import com.atguigu.acl.service.TRoleService;
import com.atguigu.acl.service.TUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
@Service
public class TRoleServiceImpl extends ServiceImpl<TRoleMapper, TRole> implements TRoleService {
    @Autowired
    private TUserRoleService userRoleService;

    @Override
    public List<TRole> queryRoleByUserId(String id) {
        //根据用户id拥有的角色id
        QueryWrapper<TUserRole> wrapper = new QueryWrapper<TUserRole>().eq("user_id", id);
        wrapper.select("role_name");
        List<TUserRole> userRoleList = userRoleService.list(wrapper);
        List<String> roleIdList = userRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        List<TRole> roleList = new ArrayList<>();
        if (roleIdList.size() > 0) {
            roleList = baseMapper.selectBatchIds(roleIdList);
        }
        return roleList;
    }

    @Override
    public List<String> queryRoleNameByUserId(String id) {
        return baseMapper.queryRoleNameByUserId(id);
    }
    //------------------------------分割线----------------------

    //1.回显用户已分配的角色
    @Override
    public Map<String, Object> queryAssignedRole(String userId) {
        //a.查询所有的角色
        List<TRole> allRoles = baseMapper.selectList(null);
        //b.查询已经分配的角色
        List<TRole> assignedRoles = baseMapper.queryRoleByUserId(userId);
        //c.分别返回 以上两个集合 map结构
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("assignedRoles",assignedRoles);
        retMap.put("allRoles",allRoles);
        return retMap;
    }
    //2.修改用户拥有的角色
    @Override
    public void assignRolesToUser(String userId, String[] roleIdList) {
        //a.根据用户id删除已有的角色
        QueryWrapper<TUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        userRoleService.remove(wrapper);
        //b.批量添加该用户拥有的角色(t_user_role)
        Set<TUserRole> userRoleSet = new HashSet<>();
        for (String roleId : roleIdList) {
            TUserRole userRole = new TUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleSet.add(userRole);
        }
        userRoleService.saveBatch(userRoleSet);
    }


}
