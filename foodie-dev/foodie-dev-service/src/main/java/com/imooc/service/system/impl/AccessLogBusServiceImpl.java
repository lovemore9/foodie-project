package com.imooc.service.system.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.imooc.pojo.AccessLog;
import com.imooc.service.system.AccessLogBusService;
import com.imooc.utils.HttpUtil;
import com.imooc.utils.JsonUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-23 15:14
 * </pre>
 */

@Service
public class AccessLogBusServiceImpl implements AccessLogBusService {
    @Override
    public AccessLog generateAccessLogPojo(HttpServletRequest request, HttpServletResponse response, Exception exception, String token) {

        String requestParams = HttpUtil.getRequestParams(request);

        JSONObject paramsDataJsonObject = null;
        String userId = null;
        if (!StringUtil.isEmpty(requestParams)) {
            paramsDataJsonObject = JSONObject.parseObject(requestParams);
            if (paramsDataJsonObject != null) {
                userId = paramsDataJsonObject.getString("userId");
            }
        }


        JSONObject returnDataJsonObject = null;
        Object returnData = request.getAttribute("RETURN_DATA");
        returnDataJsonObject = JSONObject.parseObject(JsonUtils.objectToJson(returnData));

        Long beginTime = (Long)request.getAttribute("BEGIN_TIME");
        Long endTime = new Date().getTime();
        Long consumingTime = endTime - beginTime;

        AccessLog accessLog = AccessLog.builder()
                .beginTime(beginTime)
                .endTime(endTime)
                .consumingTime(consumingTime)
                .loginId(UUID.randomUUID().toString())
                .requestContentType(request.getContentType())
                .method(request.getMethod())
                .returnStatusCode(response.getStatus())
                .Status(exception != null ? Short.valueOf("0") : Short.valueOf("1"))
                .exceptionMsg(exception != null ? exception.getMessage() : null)
                .createTime(endTime)
                .url(request.getServletPath())
                .paramsData(paramsDataJsonObject)
                .returnData(returnDataJsonObject)
                .userId(userId)
                .build();

        return accessLog;
    }
}
