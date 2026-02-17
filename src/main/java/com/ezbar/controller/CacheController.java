package com.ezbar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ezbar.cache.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para gestionar cachés.
 * Proporciona endpoints para monitoreo y limpieza de caché.
 */
@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    private CacheService cacheService;

    /**
     * Obtiene estadísticas de un caché específico.
     * 
     * @param cacheName Nombre del caché
     * @return Estadísticas del caché
     */
    @GetMapping("/stats/{cacheName}")
    public ResponseEntity<?> getCacheStats(
            @PathVariable String cacheName) {
        logger.debug("Obteniendo estadísticas del caché: {}", cacheName);
        return ResponseEntity.ok(cacheService.getCacheStats(cacheName));
    }

    /**
     * Limpia un caché específico.
     * 
     * @param cacheName Nombre del caché
     * @return Mensaje de confirmación
     */
    @DeleteMapping("/{cacheName}")
    public ResponseEntity<?> clearCache(
            @PathVariable String cacheName) {
        logger.info("Limpiando caché: {}", cacheName);
        cacheService.clearCache(cacheName);
        return ResponseEntity.ok("Cache cleared: " + cacheName);
    }

    /**
     * Limpia todos los cachés.
     * 
     * @return Mensaje de confirmación
     */
    @DeleteMapping("/all")
    public ResponseEntity<?> clearAllCaches() {
        logger.info("Limpiando todos los cachés");
        cacheService.clearAllCaches();
        return ResponseEntity.ok("All caches cleared");
    }
}
