package com.ezbar.config;

import java.time.Duration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de la aplicación web.
 * Configura timeout, pool de conexiones y parámetros de Tomcat.
 */
@Configuration
public class WebConfig {

    /**
     * Configurar RestTemplate con timeout.
     * 
     * @return RestTemplate con timeouts configurados
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 segundos
        factory.setReadTimeout(10000); // 10 segundos
        return new RestTemplate(factory);
    }

    /**
     * Personalizar Tomcat para optimizar rendimiento.
     * 
     * @return WebServerFactoryCustomizer para Tomcat
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> {
            factory.setBaseDirectory(null);
            factory.addConnectorCustomizers(
                    connector -> {
                        connector.setProperty("acceptCount", "100");
                        connector.setProperty("maxConnections", "1000");
                        connector.setProperty("connectionTimeout", "20000");
                    });
        };
    }
}
