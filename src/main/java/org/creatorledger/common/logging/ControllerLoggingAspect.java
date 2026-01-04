package org.creatorledger.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logControllerMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
        final HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return joinPoint.proceed();
        }

        final String httpMethod = request.getMethod();
        final String uri = request.getRequestURI();
        final long startTime = System.currentTimeMillis();

        logger.info("[{} {}] Request received", httpMethod, uri);

        try {
            final Object result = joinPoint.proceed();
            final long duration = System.currentTimeMillis() - startTime;
            final int statusCode = extractStatusCode(result);
            logger.info("[{} {}] Response {} in {}ms", httpMethod, uri, statusCode, duration);
            return result;
        } catch (final Exception e) {
            final long duration = System.currentTimeMillis() - startTime;
            logger.error("[{} {}] Failed after {}ms: {}", httpMethod, uri, duration, e.getMessage());
            throw e;
        }
    }

    private HttpServletRequest getCurrentRequest() {
        final ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private int extractStatusCode(final Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            return responseEntity.getStatusCode().value();
        }
        return 200;
    }
}
