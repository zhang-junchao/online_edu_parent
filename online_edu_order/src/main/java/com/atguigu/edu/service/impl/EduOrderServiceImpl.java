package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.EduOrder;
import com.atguigu.edu.entity.EduPayLog;
import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.mapper.EduOrderMapper;
import com.atguigu.edu.service.CourseServiceFeign;
import com.atguigu.edu.service.EduOrderService;
import com.atguigu.edu.service.EduPayLogService;
import com.atguigu.edu.service.TeacherServiceFeign;
import com.atguigu.edu.utils.EduException;
import com.atguigu.edu.utils.HttpClient;
import com.atguigu.edu.utils.OrderNoUtil;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author zhangjunchao
 * @since 2020-04-28
 */
@Service
public class EduOrderServiceImpl extends ServiceImpl<EduOrderMapper, EduOrder> implements EduOrderService {

    @Autowired
    private CourseServiceFeign courseServiceFeign;

    @Autowired
    private TeacherServiceFeign teacherServiceFeign;

    @Override
    public String createOrder(String userId, String courseId) {
        // 生成随机用户订单号
        String orderNo = OrderNoUtil.getOrderNo();
        RetVal courseInfo = courseServiceFeign.getCourseById(courseId);
        LinkedHashMap<String, String> course = (LinkedHashMap<String, String>) courseInfo.getData().get("courseInfo");

        System.out.println(course.get("title"));
        //远程调用service course 服务
        //远程调用user 服务
        //将生成的订单数据插入数据库
        EduOrder order = new EduOrder();
        order.setOrderNo(orderNo);
        order.setCourseId(courseId);
        order.setCourseTitle(course.get("title"));
        order.setCourseCover(course.get("cover"));

        String teacherId = course.get("teacherId");
        RetVal teacherInfo = teacherServiceFeign.queryTeacherById(teacherId);
        LinkedHashMap<String, String> teacher = (LinkedHashMap<String, String>) teacherInfo.getData().get("teacher");
        order.setTeacherName(teacher.get("name"));
        order.setNickName("Test_VIP");
        order.setMobile("17791601260");
        order.setTotalFee(new BigDecimal(0.01));
        order.setPayType(1);
        order.setStatus(0);
        baseMapper.insert(order);
        return orderNo;
    }

    @Value("${wx.pay.app_id}")
    private String WX_PAY_APP_ID;
    @Value("${wx.pay.mch_id}")
    private String WX_PAY_MCH_ID;
    @Value("${wx.pay.spbill_create_ip}")
    private String WX_PAY_IP;
    @Value("${wx.pay.notify_url}")
    private String WX_PAY_NOTIFY_URL;
    @Value("${wx.pay.xml_key}")
    private String WX_PAY_XML_KEY;

    @Override
    public Map<String, Object> createPayCode(String orderNo) {
        try { //根据订单id 查询订单详细信息、
            QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            EduOrder order = baseMapper.selectOne(wrapper);
            // 封装请求的参数
            if (order == null) {
                new EduException(20001, "订单失效");
            }
            //b.封装请求微信接口的参数
            Map<String, String> requestParam = new HashMap<>();
            //公众账号ID
            requestParam.put("appid", WX_PAY_APP_ID);
            //商户号
            requestParam.put("mch_id", WX_PAY_MCH_ID);
            //随机字符串
            requestParam.put("nonce_str", WXPayUtil.generateNonceStr());
            //商品描述
            requestParam.put("body", order.getCourseTitle());
            //商户订单号
            requestParam.put("out_trade_no", orderNo);
            //商品总金额 转换价格为分
            String totalFee = order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "";
            requestParam.put("total_fee", totalFee);
            //终端ip 微服务所部署机器的ip地址
            requestParam.put("spbill_create_ip", WX_PAY_IP);
            //支付成功之后通知地址
            requestParam.put("notify_url", WX_PAY_NOTIFY_URL);
            //交易类型
            requestParam.put("trade_type", "NATIVE");
            //c.利用httpclient模拟发起请求 并且设置上面的参数
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // 生成带有 sign 的 XML 格式字符串
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(requestParam, WX_PAY_XML_KEY));
            // 是否带有https
            httpClient.setHttps(true);
            // httpClient 模拟请求
            httpClient.post();

            // 发起请求之后返回结果
            String content = httpClient.getContent();
            //将返回结果转换为 一个 map 数据结构 
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(content);
            // 将结果封装到一个新的map集合，并返回
            Map<String, Object> retMap = new HashMap<>();
            //二维码url
            retMap.put("code_url", xmlToMap.get("code_url"));
            //订单编号
            retMap.put("out_trade_no", orderNo);
            //订单总金额
            retMap.put("total_fee", order.getTotalFee());
            retMap.put("course_id", order.getCourseId());
            System.out.println("----------------------------------------------");
            System.out.println(retMap);

            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> queryPayState(String orderNo) {
        try {
            //1.封装请求微信接口的参数
            Map<String, String> requestParam = new HashMap<>();
            //公众账号ID
            requestParam.put("appid", WX_PAY_APP_ID);
            //商户号
            requestParam.put("mch_id", WX_PAY_MCH_ID);
            //商户订单号
            requestParam.put("out_trade_no", orderNo);
            //随机字符串
            requestParam.put("nonce_str", WXPayUtil.generateNonceStr());

            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");

            httpClient.setXmlParam(WXPayUtil.generateSignedXml(requestParam, WX_PAY_XML_KEY));
            httpClient.setHttps(true);
            httpClient.post();

            String content = httpClient.getContent();
            Map<String, String> retMap = WXPayUtil.xmlToMap(content);
            return retMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null ;
    }

    @Autowired
    private EduPayLogService payLogService;
    @Override
    public void updateOrderState(Map<String,String>retMap) {
        // 修改订单号
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", retMap.get("out_trade_no"));
        EduOrder order = baseMapper.selectOne(wrapper);
        order.setStatus(1);
        baseMapper.updateById(order);
        //b.向pay_log表里面添加一条数据

        QueryWrapper<EduPayLog> payLogWrapper = new QueryWrapper<>();
        wrapper.eq("order_no", order.getOrderNo());
        EduPayLog existPaylog = payLogService.getOne(payLogWrapper);
        if(existPaylog==null) {
            EduPayLog payLog = new EduPayLog();
            payLog.setOrderNo(order.getOrderNo());
            payLog.setPayTime(new Date());
            payLog.setTotalFee(order.getTotalFee());
            //交易流水号
            payLog.setTransactionId(retMap.get("transaction_id"));
            //交易状态
            payLog.setTradeState(retMap.get("trade_state"));
            payLog.setPayType(1);
            payLogService.save(payLog);
        }
    }


}
