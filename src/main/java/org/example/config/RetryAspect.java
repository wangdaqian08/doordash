package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.annotation.RetryProcess;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class RetryAspect {
    private int maxRetryTimes = 3;

    @Pointcut("@annotation(org.example.annotation.RetryProcess)")
    public void retry() {
    }


    @Around("retry()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Class<?>[] parameterTypes = methodSignature.getMethod().getParameterTypes();
        Method method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), parameterTypes);

        this.maxRetryTimes = method.getAnnotation(RetryProcess.class).value();
        int attemps = 0;

        Object result = null;
        do {
            attemps++;
            try {
                result = pjp.proceed();
                return result;
            } catch (Exception e) {
                if (e instanceof ObjectOptimisticLockingFailureException || e instanceof StaleObjectStateException) {
                    log.warn("retrying.... times: {}", attemps);
                    if (attemps > maxRetryTimes) {
                        log.warn("retry exceed the max times: {}", maxRetryTimes);
                        throw e;
                    }
                }
            }
        } while (attemps < maxRetryTimes);
        return result;
    }

}
