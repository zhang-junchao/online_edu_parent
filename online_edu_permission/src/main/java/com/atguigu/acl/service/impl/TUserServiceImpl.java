package com.atguigu.acl.service.impl;

import com.atguigu.acl.entity.TUser;
import com.atguigu.acl.mapper.TUserMapper;
import com.atguigu.acl.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-03-22
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

}
