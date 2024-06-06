package org.biwaby.studytracker.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Aspect
public class ControllerLogAspect {
    @Pointcut("execution(public * org.biwaby.studytracker.controllers.*.*(..))")
    public void callControllerPointcut() {}

    @Before("callControllerPointcut()")
    public void beforeCallController(JoinPoint joinPoint) {
        List<String> args = Arrays.stream(joinPoint.getArgs()).map(Object::toString).toList();
        log.info("\u001B[33m[CALL CONTROLLER]: method {} with args {}\u001B[0m", joinPoint.getSignature().getName(), args);
    }

    @AfterReturning(value = "callControllerPointcut()", returning = "object")
    public void afterCallController(JoinPoint joinPoint, ResponseEntity<?> object) {
        log.info("\u001B[33m[CONTROLLER]: controller {} returned status code {}\u001B[0m", joinPoint.getSignature().getName(), object.getStatusCode());
    }
}
