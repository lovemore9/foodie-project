package com.imooc.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Controller层统一日志处理
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-09-23 10:50
 * </pre>
 */

@Slf4j
@Component
@Aspect
public class ControllerAop {

    @Pointcut("execution(* com.imooc.controller..*.*(..))")
    public void executeService() {

    }

    @Before(value = "executeService()")
    public void doBeforeAdvice() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest)requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        request.setAttribute("BEGIN_TIME", new Date().getTime());
    }

    @AfterReturning(value = "executeService()", returning = "keys")
    public void doAfterReturningAdvice(JoinPoint joinPoint, Object keys) {
        //获取requestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest)requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        request.setAttribute("RETURN_DATA", keys);

    }

}
