package com.imooc.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: wangyujin
 * @Description: common 统一日志处理
 * @Date: 2020/8/30 12:16
 */
@Component
//@Aspect
public class CommonAop {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.imooc.utils..*.*(..))")
    public void executeService() {
    }

    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint) {

        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        logger.info(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName()+" : url = "+request.getRequestURI());
        logger.info(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName()+" : method = "+request.getMethod());
        logger.info(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName()+" : args = "+ JSON.toJSON(joinPoint.getArgs()).toString());
    }

    /**
     * 后置返回通知
     * 这里需要注意的是:
     *      如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     *      如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     * @param joinPoint
     * @param keys
     */
    @AfterReturning(value = "executeService()",returning = "keys")
    public void doAfterReturningAdvice1(JoinPoint joinPoint,Object keys){
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (keys==null){
            logger.info(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName()+" :return="+keys);
        }else {
            logger.info(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName()+" :return="+keys.toString());
        }

    }
}
