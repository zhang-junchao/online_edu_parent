package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author zhangjunchao
 * @since 2020-04-28
 */
public interface EduOrderService extends IService<EduOrder> {

    String createOrder(String userId, String courseId);

    Map<String, Object> createPayCode(String orderNo);

    Map<String, String> queryPayState(String orderNo);

    void updateOrderState(Map<String,String> retMap);
}
