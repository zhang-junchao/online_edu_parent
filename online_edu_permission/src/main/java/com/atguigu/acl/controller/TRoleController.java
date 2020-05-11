package com.atguigu.acl.controller;


import com.atguigu.acl.entity.TRole;
import com.atguigu.acl.service.TRoleService;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
@RestController
@RequestMapping("/acl/role")
@CrossOrigin
public class TRoleController {
    @Autowired
    private TRoleService roleService;

    @ApiOperation(value = "获取角色分页列表")
    @GetMapping("{pageNum}/{pageSize}")
    public RetVal index(
            @PathVariable Long pageNum,
            @PathVariable Long pageSize,
            TRole role) {
        Page<TRole> pageParam = new Page<>(pageNum, pageSize);
        QueryWrapper<TRole> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(role.getRoleName())) {
            wrapper.like("role_name",role.getRoleName());
        }
        roleService.page(pageParam,wrapper);
        return RetVal.success().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
    }

    @ApiOperation(value = "获取角色")
    @GetMapping("get/{id}")
    public RetVal get(@PathVariable String id) {
        TRole role = roleService.getById(id);
        return RetVal.success().data("item", role);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping("save")
    public RetVal save(@RequestBody TRole role) {
        roleService.save(role);
        return RetVal.success();
    }

    @ApiOperation(value = "修改角色")
    @PutMapping("update")
    public RetVal updateById(@RequestBody TRole role) {
        roleService.updateById(role);
        return RetVal.success();
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("remove/{id}")
    public RetVal remove(@PathVariable String id) {
        roleService.removeById(id);
        return RetVal.success();
    }

    @ApiOperation(value = "根据id列表删除角色")
    @DeleteMapping("batchRemove")
    public RetVal batchRemove(@RequestBody List<String> idList) {
        roleService.removeByIds(idList);
        return RetVal.success();
    }





}

