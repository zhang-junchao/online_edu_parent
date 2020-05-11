package com.atguigu.acl.service;

import com.atguigu.acl.entity.TRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
public interface TRoleService extends IService<TRole> {

    List<TRole> queryRoleByUserId(String id);

    List<String> queryRoleNameByUserId(String id);

    Map<String, Object> queryAssignedRole(String userId);

    void assignRolesToUser(String userId, String[] roleIdList);
}
