package org.creatorledger.common.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApplicationServiceLoggingAspect {

    @Around("@within(org.springframework.stereotype.Service)")
    public Object logServiceMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        final String methodName = joinPoint.getSignature().getName();
        final long startTime = System.currentTimeMillis();

        logger.debug("{} - START", methodName);

        try {
            final Object result = joinPoint.proceed();
            final long duration = System.currentTimeMillis() - startTime;
            logger.debug("{} - COMPLETED in {}ms", methodName, duration);
            return result;
        } catch (final Exception e) {
            final long duration = System.currentTimeMillis() - startTime;
            logger.error("{} - FAILED after {}ms: {}", methodName, duration, e.getMessage());
            throw e;
        }
    }
}
