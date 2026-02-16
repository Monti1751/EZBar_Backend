package com.ezbar.interceptor;

import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor para manejar logs y tracking de peticiones.
 * Agrega un ID único a cada petición para traceabilidad.
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private static final String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String requestId = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, requestId);

        logger.debug(
                "Iniciando petición [{}] {} {} desde {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());

        request.setAttribute(REQUEST_ID, requestId);
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {

        String requestId = (String) request.getAttribute(REQUEST_ID);

        if (ex != null) {
            logger.error(
                    "Error en petición [{}] {}: {}",
                    requestId,
                    request.getRequestURI(),
                    ex.getMessage(),
                    ex);
        } else {
            logger.debug(
                    "Petición completada [{}] {} con status {}",
                    requestId,
                    request.getRequestURI(),
                    response.getStatus());
        }

        MDC.remove(REQUEST_ID);
    }
}
