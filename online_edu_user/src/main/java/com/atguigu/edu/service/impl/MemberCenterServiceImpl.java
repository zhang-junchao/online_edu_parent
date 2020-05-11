package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.MemberCenter;
import com.atguigu.edu.mapper.MemberCenterMapper;
import com.atguigu.edu.service.MemberCenterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2020-04-20
 */
@Service
public class MemberCenterServiceImpl extends ServiceImpl<MemberCenterMapper, MemberCenter> implements MemberCenterService {

    @Override
    public Integer queryRegisterNum(String day) {
        return baseMapper.queryRegisterNum(day);
    }

    @Override
    public MemberCenter queryUserByOpenId(String openid) {
        QueryWrapper<MemberCenter> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        return baseMapper.selectOne(wrapper);
    }
}
