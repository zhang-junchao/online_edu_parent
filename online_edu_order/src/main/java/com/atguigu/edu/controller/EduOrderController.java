package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduOrder;
import com.atguigu.edu.service.EduOrderService;
import com.atguigu.edu.utils.JwtUtils;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author zhangjunchao
 * @since 2020-04-28
 */
@RestController
@RequestMapping("/order")
@CrossOrigin
public class EduOrderController {

    @Autowired
    private EduOrderService orderService;

    // 创建订单
    @GetMapping("createOrder/{courseId}")
    public RetVal createOrder(@PathVariable String courseId,
                              HttpServletRequest request){
        String userId= JwtUtils.getMemberIdByJwtToken(request);
        String orderNo = orderService.createOrder(userId, courseId);
        return RetVal.success().data("orderNo",orderNo);
    }

    // 根据订单Id查询订单详情
    @GetMapping("getOrderInfo/{orderNo}")
    public RetVal getOrderInfo(@PathVariable String orderNo){
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        EduOrder order = orderService.getOne(wrapper);
        return RetVal.success().data("order",order);

    }
    // 根据订单号生成二维码
    @GetMapping("createPayCode/{orderNo}")
    public RetVal createPayCode(@PathVariable String orderNo){
        Map<String, Object> payCode = orderService.createPayCode(orderNo);
        return RetVal.success().data(payCode);
    }
    // 根据订单号查询支付结果
    @GetMapping("queryPayState/{orderNo}")
    public RetVal queryPayState(@PathVariable String orderNo){
        Map<String, String> retMap = orderService.queryPayState(orderNo);
       if(retMap.get("trade_state").equals("SUCCESS")){
           orderService.updateOrderState(retMap);
           return RetVal.success().message("支付成功");
       }else {
           return RetVal.error().message("待支付");
       }

    }


}

