package Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador Global de Excepciones.
 * 
 * Responsabilidad: Centralizar el manejo de excepciones en toda la aplicación.
 * Esto mejora:
 * - Consistencia: Todas las excepciones devuelven el mismo formato de respuesta
 * - Mantenibilidad: Cambios en formato de error afectan un único lugar
 * - Seguridad: No exponemos detalles internos sensibles al cliente
 * - Logging: Podemos loguear todas las excepciones en un lugar
 * 
 * Principios SOLID:
 * - Single Responsibility: Solo maneja excepciones
 * - Centralization: Un único lugar para toda la lógica de manejo de errores
 * 
 * @RestControllerAdvice Aplica este manejador globalmente a todos los
 *                       controllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones genéricas no capturadas.
     * 
     * Esta es la red de seguridad para cualquier excepción no anticipada.
     * Loguea y devuelve una respuesta estructurada al cliente.
     * 
     * @param exception La excepción no manejada
     * @param request   Los detalles de la petición
     * @return ResponseEntity con error estructurado y código 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception exception,
            WebRequest request) {

        // Log de la excepción para debugging (en producción usar logger)
        System.err.println("Unhandled exception: " + exception.getMessage());
        exception.printStackTrace();

        Map<String, Object> errorResponse = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                Constants.ErrorCodes.DATABASE_ERROR_CODE,
                "An unexpected error occurred. Please try again later.",
                request);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja excepciones de tipo argumento inválido (ej: parámetro de URL no
     * convertible).
     * 
     * Ejemplo: GET /categorias/abc cuando espera un número
     * 
     * @param exception Excepción de tipo de argumento
     * @param request   Los detalles de la petición
     * @return ResponseEntity con error estructurado y código 400
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            WebRequest request) {

        Class<?> requiredType = exception.getRequiredType();
        String typeName = requiredType != null
                ? requiredType.getSimpleName()
                : "unknown";
        String message = String.format(
                "Invalid parameter '%s': expected type %s but got %s",
                exception.getName(),
                typeName,
                exception.getValue());

        Map<String, Object> errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                Constants.ErrorCodes.INVALID_INPUT_CODE,
                message,
                request);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de recursos no encontrados.
     * 
     * Esta excepción debería ser lanzada cuando se intenta acceder a un recurso
     * que no existe.
     * 
     * @param exception Excepción de recurso no encontrado
     * @param request   Los detalles de la petición
     * @return ResponseEntity con error estructurado y código 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFoundException exception,
            WebRequest request) {

        Map<String, Object> errorResponse = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                Constants.ErrorCodes.NOT_FOUND_CODE,
                exception.getMessage(),
                request);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Constructor de respuesta de error estructurada.
     * 
     * Esto centraliza el formato de todas las respuestas de error.
     * Si necesitas cambiar la estructura de errores, haces en un solo lugar.
     * 
     * @param status    Código HTTP de estado
     * @param errorCode Código de error personalizado (ej: ERR_404)
     * @param message   Mensaje descriptivo para el cliente
     * @param request   Objeto de petición (para incluir ruta si es necesario)
     * @return Mapa con la estructura del error
     */
    private Map<String, Object> buildErrorResponse(
            HttpStatus status,
            String errorCode,
            String message,
            WebRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("code", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("httpStatus", status.value());
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));

        return errorResponse;
    }
}
