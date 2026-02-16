package com.ezbar.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.benmanes.caffeine.cache.Cache;

/**
 * Servicio de gestión de caché.
 * Centraliza las operaciones de caché de la aplicación.
 */
@Service
public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    private CacheManager cacheManager;

    /**
     * Obtiene un valor del caché.
     * 
     * @param cacheName Nombre del caché
     * @param key       Clave del elemento
     * @return Valor en caché o null
     */
    public Object getFromCache(String cacheName, String key) {
        if (cacheManager.getCache(cacheName) != null) {
            return cacheManager.getCache(cacheName).get(key);
        }
        return null;
    }

    /**
     * Agrega un valor al caché.
     * 
     * @param cacheName Nombre del caché
     * @param key       Clave del elemento
     * @param value     Valor a cachear
     */
    public void putInCache(String cacheName, String key, Object value) {
        if (cacheManager.getCache(cacheName) != null) {
            cacheManager.getCache(cacheName).put(key, value);
            logger.debug("Valor agregado al caché [{}:{}]", cacheName, key);
        }
    }

    /**
     * Elimina un valor del caché.
     * 
     * @param cacheName Nombre del caché
     * @param key       Clave del elemento
     */
    public void removeFromCache(String cacheName, String key) {
        if (cacheManager.getCache(cacheName) != null) {
            cacheManager.getCache(cacheName).evict(key);
            logger.debug("Valor eliminado del caché [{}:{}]", cacheName, key);
        }
    }

    /**
     * Limpia todos los valores de un caché.
     * 
     * @param cacheName Nombre del caché
     */
    public void clearCache(String cacheName) {
        if (cacheManager.getCache(cacheName) != null) {
            cacheManager.getCache(cacheName).clear();
            logger.debug("Caché limpiado [{}]", cacheName);
        }
    }

    /**
     * Limpia todos los cachés.
     */
    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            if (cacheManager.getCache(name) != null) {
                cacheManager.getCache(name).clear();
            }
        });
        logger.info("Todos los cachés han sido limpiados");
    }

    /**
     * Obtiene estadísticas del caché Caffeine.
     * 
     * @param cacheName Nombre del caché
     * @return Estadísticas en String
     */
    public String getCacheStats(String cacheName) {
        if (cacheManager.getCache(cacheName) != null) {
            var cache = cacheManager.getCache(cacheName).getNativeCache();
            if (cache instanceof Cache) {
                return ((Cache<?, ?>) cache).stats().toString();
            }
        }
        return "Cache not found";
    }
}
