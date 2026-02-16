package com.ezbar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ezbar.util.MemoryMonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para monitoreo de memoria.
 * Proporciona endpoints para verificar el estado de memoria y recursos.
 */
@RestController
@RequestMapping("/api/memory")
public class MemoryMonitoringController {

    private static final Logger logger = LoggerFactory.getLogger(MemoryMonitoringController.class);

    @Autowired
    private MemoryMonitoringService memoryService;

    /**
     * Obtiene información actual de memoria.
     * 
     * @return Información de memoria
     */
    @GetMapping("/info")
    public ResponseEntity<?> getMemoryInfo() {
        logger.debug("Obteniendo información de memoria");
        return ResponseEntity.ok(memoryService.getMemoryInfo());
    }

    /**
     * Obtiene estado de salud de la memoria.
     * 
     * @return Estado de salud
     */
    @GetMapping("/health")
    public ResponseEntity<?> getMemoryHealth() {
        logger.debug("Comprobando salud de memoria");
        boolean healthy = memoryService.isMemoryHealthy();
        return ResponseEntity.ok(
                "{ \"memory_healthy\": " + healthy + " }");
    }

    /**
     * Obtiene información del sistema.
     * 
     * @return Información del sistema
     */
    @GetMapping("/system-info")
    public ResponseEntity<?> getSystemInfo() {
        logger.debug("Obteniendo información del sistema");
        return ResponseEntity.ok(memoryService.getSystemInfo());
    }

    /**
     * Ejecuta recolección de basura manual.
     * 
     * @return Confirmación
     */
    @PostMapping("/gc")
    public ResponseEntity<?> triggerGarbageCollection() {
        logger.info("Ejecutando recolección de basura");
        memoryService.triggerGarbageCollection();
        return ResponseEntity.ok("Garbage collection triggered");
    }
}
