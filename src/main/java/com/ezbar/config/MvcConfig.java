package com.ezbar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ezbar.interceptor.LoggingInterceptor;
import com.ezbar.interceptor.TimeoutInterceptor;

/**
 * Configuración MVC.
 * Registra interceptores y configura componentes web.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Autowired
    private TimeoutInterceptor timeoutInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeoutInterceptor)
                .addPathPatterns("/api/**");

        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**");
    }
}
