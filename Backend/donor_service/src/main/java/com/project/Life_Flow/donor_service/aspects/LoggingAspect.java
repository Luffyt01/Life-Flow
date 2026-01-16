package com.project.Life_Flow.donor_service.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // 1. Log before any method in the 'services' package runs
    @Before("execution(* com.project.Life_Flow.donor_service.services..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Enter: {}() with argument[s] = {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    // 2. Log after any method returns successfully
    @AfterReturning(pointcut = "execution(* com.project.Life_Flow.donor_service.services..*(..))", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("Exit: {}() with result = {}",
                joinPoint.getSignature().getName(),
                result);
    }

    // 3. Log if any method throws an exception
    @AfterThrowing(pointcut = "execution(* com.project.Life_Flow.donor_service.services..*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("Exception in {}() with cause = {}",
                joinPoint.getSignature().getName(),
                error.getMessage()); // Use error.getMessage() to avoid huge stack traces in 'error' logs, or pass 'error' to see full trace
    }
}