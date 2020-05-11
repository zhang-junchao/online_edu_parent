package com.atguigu.acl.mapper;

import com.atguigu.acl.entity.TRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
public interface TRoleMapper extends BaseMapper<TRole> {

    List<String> queryRoleNameByUserId(@Param("userId") String userId);


    List<TRole> queryRoleByUserId(@Param("userId") String userId);
}
