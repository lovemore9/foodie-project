package com.imooc.interceptor;

import com.alibaba.fastjson.JSON;
import com.imooc.pojo.AccessLog;
import com.imooc.service.system.AccessLogBusService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-23 11:29
 * </pre>
 */
@Slf4j
public class CommonInterceptor implements HandlerInterceptor {

    @Autowired
    private AccessLogBusService accessLogBusService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put("trice_id", UUID.randomUUID().toString().replace("-", ""));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        String url = request.getServletPath();
//        String requestParams = HttpUtil.getRequestParams(request);
        AccessLog accessLog = accessLogBusService.generateAccessLogPojo(request, response, ex, null);
        log.info(JSON.toJSONString(accessLog));
    }
}