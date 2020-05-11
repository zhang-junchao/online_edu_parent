package com.atguigu.acl.controller;


import com.atguigu.acl.entity.TPermission;
import com.atguigu.acl.service.TPermissionService;
import com.atguigu.response.RetVal;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
@RestController
@RequestMapping("/acl/permission")
@CrossOrigin
public class TPermissionController {
    @Autowired
    private TPermissionService permissionService;

    @ApiOperation(value = "1.回显角色已分配的权限")
    @GetMapping("queryAssignedPermission/{roleId}")
   public RetVal queryAssignedPermission(@PathVariable String roleId){
       List<TPermission> list= permissionService.queryAssignedPermission(roleId);
       return RetVal.success().data("permissionInfo",list);
   }
    @ApiOperation(value = "2.修改角色拥有的权限")
    @PostMapping("assignPermissionToRole")
    public RetVal assignPermissionToRole(String roleId,String[] permissionIdList){
        permissionService.assignPermissionToRole(roleId,permissionIdList);
        return RetVal.success();
    }










    //------------------------------分割线----------------------
    @ApiOperation(value = "递归删除菜单")
    //@PreAuthorize("hasAuthority('permission.remove')")
    @DeleteMapping("remove/{id}")
    public RetVal remove(@PathVariable String id) {
        permissionService.removeChildById(id);
        return RetVal.success();
    }
    @ApiOperation(value = "新增权限")
    @PreAuthorize("hasAuthority('permission.add')")
    @PostMapping("save")
    public RetVal save(@RequestBody TPermission permission) {
        permissionService.save(permission);
        return RetVal.success();
    }

    @ApiOperation(value = "修改权限")
    @PutMapping("update")
    public RetVal updateById(@RequestBody TPermission permission) {
        permissionService.updateById(permission);
        return RetVal.success();
    }

    @GetMapping
    public RetVal queryAllPermission() {
        List<TPermission> list =  permissionService.queryAllPermission();
        return RetVal.success().data("permissionInfo",list);
    }



}

