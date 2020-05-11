package com.atguigu.acl.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface IndexService {
    Map<String, Object> getUserInfo(String userName);

    List<JSONObject> getDynamicRouter(String userName);
}
