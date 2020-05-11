package com.atguigu.acl.service.impl;

import com.atguigu.acl.entity.TPermission;
import com.atguigu.acl.entity.TRolePermission;
import com.atguigu.acl.helper.TPermissionHelper;
import com.atguigu.acl.mapper.TPermissionMapper;
import com.atguigu.acl.service.TPermissionService;
import com.atguigu.acl.service.TRolePermissionService;
import com.atguigu.acl.service.TUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
@Service
public class TPermissionServiceImpl extends ServiceImpl<TPermissionMapper, TPermission> implements TPermissionService {
    @Autowired
    private TUserService userService;
    @Autowired
    private TRolePermissionService rolePermissionService;

    @Override
    public List<TPermission> queryAllPermission() {
        List<TPermission> permissionList = baseMapper.selectList(null);
        List<TPermission> formatPermissionList = TPermissionHelper.bulid(permissionList);
        return formatPermissionList;
    }
    //递归删除菜单
    @Override
    public void removeChildById(String id) {
        List<String> idList = new ArrayList<>();
        this.queryChildIdList(id, idList);
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }



    /**
     * 递归获取子节点
     */
    private void queryChildIdList(String id, List<String> idList) {
        QueryWrapper<TPermission> wrapper = new QueryWrapper<TPermission>();
        wrapper.eq("pid", id).select("id");
        List<TPermission> children = baseMapper.selectList(wrapper);
        children.stream().forEach(child -> {
            idList.add(child.getId());
            this.queryChildIdList(child.getId(), idList);
        });
    }


    //------------------------------分割线----------------------
    //1.回显角色已分配的权限
    @Override
    public List<TPermission> queryAssignedPermission(String roleId) {
        //a.查询所有的权限
        List<TPermission> allPermission = baseMapper.selectList(null);
        //b.查询已经分配的权限
        QueryWrapper<TRolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",roleId);
        List<TRolePermission> rolePermissionList = rolePermissionService.list(wrapper);
        //c.迭代所有的权限 对已经分配的权限进行标记
        for (TPermission permission : allPermission) {
            for (TRolePermission rolePermission : rolePermissionList) {
                //获取已经分配了的权限对象
                if(rolePermission.getPermissionId().equals(permission.getId())){
                    permission.setSelect(true);
                }
            }
        }
        //对所有的权限进行格式化数据 前端需要这样的数据结构
        List<TPermission> permissionList = TPermissionHelper.bulid(allPermission);
        return permissionList;
    }
    //2.修改角色拥有的权限
    @Override
    public void assignPermissionToRole(String roleId, String[] permissionIdList) {
        //a.根据角色id删除已有的权限
        QueryWrapper<TRolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id",roleId);
        rolePermissionService.remove(wrapper);
        //b.批量添加该角色拥有的权限(t_role_permission)
        Set<TRolePermission> rolePermissionSet = new HashSet<>();
        for (String permissionId : permissionIdList) {
            TRolePermission rolePermission = new TRolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionSet.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissionSet);
    }
    //3.查询用户拥有什么权限
    @Override
    public List<TPermission> queryPermissionByUserId(Integer type,String userId) {
        List<TPermission> permissionList= baseMapper.queryPermissionByUserId(type,userId);
        return permissionList;
    }
}
