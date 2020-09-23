package com.imooc.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-03 20:29
 * </pre>
 */

@Slf4j
@Aspect
@Component
public class ServiceLogAspect {
    
    /**
     * AOP通知
     * 1.前置通知：在方法调用之前
     * 2.后置通知：方法正常调用之后执行
     * 3.环绕通知： 在方法调用之前和之后，都可以执行的通知
     * 4.异常通知：在方法发生异常，则通知
     * 5.最终通知：在方法调用之后（final）
     */

    //切面表达式 第一处 * 代表返回类型 所有返回类型 两个点 代表当前包下所有 倒数第二星号代表类名
    //*(..) * 代表类中方法名，(..) 代表方法中任何参数
    @Around("execution(* com.imooc.service.impl..*.*(..))")
    public Object recordTimeog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("========== 开始执行 {}.{}=========", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        
        //记录开始时间
        long begin = System.currentTimeMillis();
        //执行目标service
        Object proceed = joinPoint.proceed();
        //结束时间
        long end = System.currentTimeMillis();

        long takeTime = end - begin;

        log.info("========== 执行结束,耗时{} 毫秒=========", takeTime);

        return proceed;
    }

}
