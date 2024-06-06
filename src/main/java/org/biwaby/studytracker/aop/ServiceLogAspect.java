package org.biwaby.studytracker.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@Slf4j
public class ServiceLogAspect {

    @Pointcut("execution(public * org.biwaby.studytracker.services.implementations.*.*(..))")
    public void servicePointcut() {}

    @Before("servicePointcut()")
    public void beforeCallService(JoinPoint joinPoint) {
        List<String> args = Arrays.stream(joinPoint.getArgs()).map(Object::toString).toList();
        log.info("\u001B[32m[CALL SERVICE]: called method {} with args {}\u001B[0m", joinPoint.getSignature().getName(), args);
    }

    @AfterReturning(value = "servicePointcut()", returning = "object")
    public void afterCallService(JoinPoint joinPoint, Object object) {
        log.info("\u001B[32m[SERVICE]: method {} returned {}\u001B[0m", joinPoint.getSignature().getName(), object);
    }
}
