package org.creatorledger.common.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryLoggingAspect {

    @Around("@within(org.springframework.stereotype.Repository)")
    public Object logRepositoryMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        final String methodName = joinPoint.getSignature().getName();

        logger.debug("Data access: {}", methodName);

        try {
            return joinPoint.proceed();
        } catch (final Exception e) {
            logger.error("Data access failed: {} - {}", methodName, e.getMessage());
            throw e;
        }
    }
}
