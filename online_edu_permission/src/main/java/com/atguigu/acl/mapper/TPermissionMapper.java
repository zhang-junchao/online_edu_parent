package com.atguigu.acl.mapper;

import com.atguigu.acl.entity.TPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
public interface TPermissionMapper extends BaseMapper<TPermission> {

    List<TPermission> queryPermissionByUserId(@Param("type") Integer type,@Param("userId") String userId);
}
