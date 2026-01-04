package org.creatorledger.common.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

    @AfterThrowing(
            pointcut = "within(org.creatorledger..*)",
            throwing = "exception"
    )
    public void logException(final JoinPoint joinPoint, final Throwable exception) {
        final String className = joinPoint.getSignature().getDeclaringTypeName();
        final String methodName = joinPoint.getSignature().getName();

        logger.error(
                "Exception in {}.{}: {}",
                className,
                methodName,
                exception.getMessage(),
                exception
        );
    }
}
