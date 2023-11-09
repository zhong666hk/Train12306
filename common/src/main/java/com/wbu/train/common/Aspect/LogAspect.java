package com.wbu.train.common.Aspect;

import cn.hutool.core.util.RandomUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    public static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Pointcut("@annotation(com.wbu.train.common.Aspect.annotation.LogAnnotation)")
    public void pointcut(){}

    @Around("pointcut()")
    public Object AroundAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        // 设置日志的自定义参数--流水号
        MDC.put("LOG_ID",System.currentTimeMillis()+ RandomUtil.randomString(3));
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        logger.info("方法名为{}",signature.getName());
        logger.info("方法参数类型为{}",signature.getParameterTypes());
        logger.info("方法参数{}",signature.getParameterNames());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        logger.info("方法返回值为{}",result);
        logger.info("方法执行耗时{}",end-start);
        return result;
    }
}
