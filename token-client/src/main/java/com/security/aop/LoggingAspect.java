package com.security.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.security.controller..*(..)))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        logger.info("------------------------Enter into {}.{}---------------------------------",className,methodName);
        Map<String,Object> args = new HashMap<>();
        for (int i = 0; i < proceedingJoinPoint.getArgs().length; i++) {
            args.put(methodSignature.getParameterNames()[i],proceedingJoinPoint.getArgs()[i]);
        }
        logger.info("The method " + proceedingJoinPoint.getSignature().getName() + " begin, Args:" + args);
        final StopWatch stopWatch = new StopWatch();
        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();
        //Log method execution time
        logger.info("Execution time of {}.{} in:{} ms",className,methodName,stopWatch.getTotalTimeMillis());
        logger.info("------------------------END of {}.{}---------------------------------",className,methodName);
        return result;
    }
}

