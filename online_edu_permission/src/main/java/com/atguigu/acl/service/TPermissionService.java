package com.atguigu.acl.service;

import com.atguigu.acl.entity.TPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
public interface TPermissionService extends IService<TPermission> {


    List<TPermission> queryAllPermission();

    //递归删除菜单
    void removeChildById(String id);

    List<TPermission> queryAssignedPermission(String roleId);

    void assignPermissionToRole(String roleId, String[] permissionIdList);

    List<TPermission> queryPermissionByUserId(Integer type,String id);

}
