package com.ezbar.config;

import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;

/**
 * Configuración de caché usando Caffeine.
 * Proporciona manejo automático de caché con invalidación por tiempo.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configurar el gestor de caché con Caffeine.
     * 
     * @return CacheManager configurado
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "usuarios",
                "productos",
                "mesas",
                "pedidos",
                "categorias",
                "empleados");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());

        return cacheManager;
    }
}
