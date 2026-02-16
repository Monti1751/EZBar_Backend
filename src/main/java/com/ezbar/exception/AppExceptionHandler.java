package com.ezbar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ezbar.util.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global de excepciones.
 * Centraliza el manejo de errores en toda la aplicación.
 */
@RestControllerAdvice
public class AppExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

        @Autowired(required = false)
        private LoggerService loggerService;

        /**
         * Maneja excepciones de recurso no encontrado.
         * 
         * @param ex      Excepción
         * @param request Petición
         * @return Respuesta de error
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<?> handleResourceNotFound(
                        ResourceNotFoundException ex,
                        WebRequest request) {
                loggerService.warn("Recurso no encontrado: {}", ex.getMessage());

                Map<String, Object> body = new LinkedHashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("status", HttpStatus.NOT_FOUND.value());
                body.put("error", "Recurso No Encontrado");
                body.put("message", ex.getMessage());
                body.put("path", request.getDescription(false).replace("uri=", ""));

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(body);
        }

        /**
         * Maneja excepciones de validación.
         * 
         * @param ex      Excepción
         * @param request Petición
         * @return Respuesta de error
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<?> handleIllegalArgument(
                        IllegalArgumentException ex,
                        WebRequest request) {
                loggerService.warn("Argumento inválido: {}", ex.getMessage());

                Map<String, Object> body = new LinkedHashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("status", HttpStatus.BAD_REQUEST.value());
                body.put("error", "Argumento Inválido");
                body.put("message", ex.getMessage());
                body.put("path", request.getDescription(false).replace("uri=", ""));

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(body);
        }

        /**
         * Maneja todas las excepciones no capturadas.
         * 
         * @param ex      Excepción
         * @param request Petición
         * @return Respuesta de error
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<?> handleGlobalException(
                        Exception ex,
                        WebRequest request) {
                loggerService.error(
                                "Error no esperado: {}",
                                ex);

                Map<String, Object> body = new LinkedHashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                body.put("error", "Error Interno del Servidor");
                body.put("message", "Ocurrió un error inesperado. Por favor, contacte al administrador.");
                body.put("path", request.getDescription(false).replace("uri=", ""));

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(body);
        }
}
