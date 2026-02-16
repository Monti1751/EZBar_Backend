package com.ezbar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Servicio centralizado de logging.
 * Proporciona métodos para registrar mensajes en diferentes niveles
 * (INFO, DEBUG, ERROR, WARN).
 */
@Component
public class LoggerService {

    private static final Logger infoLogger = LoggerFactory.getLogger("com.ezbar.info");

    private static final Logger debugLogger = LoggerFactory.getLogger("com.ezbar.debug");

    private static final Logger errorLogger = LoggerFactory.getLogger("com.ezbar.error");

    private static final Logger warnLogger = LoggerFactory.getLogger("com.ezbar.warn");

    /**
     * Registra un mensaje de nivel INFO.
     * 
     * @param message Mensaje a registrar
     * @param args    Argumentos para el mensaje
     */
    public void info(String message, Object... args) {
        infoLogger.info(message, args);
    }

    /**
     * Registra un mensaje de nivel DEBUG.
     * 
     * @param message Mensaje a registrar
     * @param args    Argumentos para el mensaje
     */
    public void debug(String message, Object... args) {
        debugLogger.debug(message, args);
    }

    /**
     * Registra un mensaje de nivel ERROR.
     * 
     * @param message   Mensaje a registrar
     * @param exception Excepción a registrar
     */
    public void error(String message, Exception exception) {
        errorLogger.error(message, exception);
    }

    /**
     * Registra un mensaje de nivel ERROR.
     * 
     * @param message Mensaje a registrar
     * @param args    Argumentos para el mensaje
     */
    public void error(String message, Object... args) {
        errorLogger.error(message, args);
    }

    /**
     * Registra un mensaje de nivel WARN.
     * 
     * @param message Mensaje a registrar
     * @param args    Argumentos para el mensaje
     */
    public void warn(String message, Object... args) {
        warnLogger.warn(message, args);
    }

    /**
     * Registra una acción realizada.
     * 
     * @param action   Acción realizada
     * @param details  Detalles de la acción
     * @param duration Duración en milisegundos
     */
    public void logAction(String action, String details, long duration) {
        info(
                "ACCIÓN [{}] - {}: {}ms",
                action,
                details,
                duration);
    }

    /**
     * Registra un error con contexto.
     * 
     * @param message   Mensaje
     * @param context   Contexto del error
     * @param exception Excepción
     */
    public void logError(
            String message,
            String context,
            Exception exception) {
        error("[{}] {}: {}", context, message, exception.getMessage());
    }
}
