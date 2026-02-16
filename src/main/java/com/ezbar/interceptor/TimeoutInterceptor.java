package com.ezbar.interceptor;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor para controlar timeout de peticiones.
 */
@Component
public class TimeoutInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TimeoutInterceptor.class);

    @Value("${ezbar.request.timeout-ms:30000}")
    private long requestTimeoutMs;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        long startTime = System.currentTimeMillis();
        request.setAttribute("_start_time", startTime);
        request.setAttribute(
                "_timeout_ms",
                requestTimeoutMs);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {

        long startTime = (long) request.getAttribute("_start_time");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (duration > requestTimeoutMs) {
            logger.warn(
                    "Timeout en petición {}: {}ms (máximo: {}ms)",
                    request.getRequestURI(),
                    duration,
                    requestTimeoutMs);
        } else {
            logger.debug(
                    "Petición {} completada en {}ms",
                    request.getRequestURI(),
                    duration);
        }
    }
}
