package com.atguigu.acl.controller;


import com.atguigu.acl.entity.TUser;
import com.atguigu.acl.service.TRoleService;
import com.atguigu.acl.service.TUserService;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
@RestController
@RequestMapping("/acl/user")
@CrossOrigin
public class TUserController {

    @Autowired
    private TUserService userService;

    @Autowired
    private TRoleService roleService;

    @ApiOperation(value = "1.回显用户已分配的角色")
    @GetMapping("queryAssignedRole/{userId}")
    public RetVal queryAssignedRole(@PathVariable String userId){
        Map<String,Object> retMap= roleService.queryAssignedRole(userId);
        return RetVal.success().data(retMap);
    }

    @ApiOperation(value = "2.修改用户拥有的角色")
    @PostMapping("assignRolesToUser")
    public RetVal assignRolesToUser(String userId,String[] roleIdList){
        roleService.assignRolesToUser(userId,roleIdList);
        return RetVal.success();
    }










    
    //------------------------------分割线----------------------
    @ApiOperation(value = "获取管理用户分页列表")
    @GetMapping("{pageNum}/{pageSize}")
    public RetVal index(
            @PathVariable Long pageNum,
            @PathVariable Long pageSize,
            TUser user) {
        Page<TUser> userPage = new Page<>(pageNum, pageSize);
        QueryWrapper<TUser> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(user.getUsername())) {
            wrapper.like("username",user.getUsername());
        }
        userService.page(userPage, wrapper);
        return RetVal.success().data("items", userPage.getRecords()).data("total", userPage.getTotal());
    }

    @ApiOperation(value = "新增管理用户")
    @PostMapping("save")
    public RetVal save(@RequestBody TUser user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);
        return RetVal.success();
    }

    @ApiOperation(value = "修改管理用户")
    @PutMapping("update")
    public RetVal updateById(@RequestBody TUser user) {
        userService.updateById(user);
        return RetVal.success();
    }


    @ApiOperation(value = "删除管理用户")
    @DeleteMapping("remove/{id}")
    public RetVal remove(@PathVariable String id) {
        userService.removeById(id);
        return RetVal.success();
    }

    @ApiOperation(value = "获取管理用户")
    @GetMapping("get/{id}")
    public RetVal get(@PathVariable String id) {
        TUser user = userService.getById(id);
        return RetVal.success().data("item", user);
    }


    @ApiOperation(value = "根据id列表删除管理用户")
    @DeleteMapping("batchRemove")
    public RetVal batchRemove(@RequestBody List<String> idList) {
        userService.removeByIds(idList);
        return RetVal.success();
    }
}

