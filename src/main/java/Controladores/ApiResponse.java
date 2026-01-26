package Controladores;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

/**
 * Objeto de Transferencia de Datos (DTO) para respuestas de API.
 * 
 * Razones para usar DTOs:
 * 1. Separación: No exponemos las entidades JPA directamente al cliente
 * 2. Flexibilidad: Podemos devolver datos diferentes de los que almacenamos
 * 3. Validación: Podemos validar datos antes de convertir a entidad
 * 4. Seguridad: No exponemos campos internos sensibles
 * 5. Versionado: Podemos tener versiones diferentes de DTOs para diferentes clientes
 * 
 * Principio SOLID aplicado: Interface Segregation (exponemos solo lo necesario)
 * 
 * @param <T> Tipo genérico de datos que contiene la respuesta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private String status; // "success", "error"
    private String message; // Mensaje descriptivo
    private T data; // Los datos reales (puede ser null en errores)
    private String code; // Código de error o éxito (ej: "ERR_404", "SUCCESS_201")
    private LocalDateTime timestamp; // Cuándo ocurrió la respuesta

    /**
     * Constructor para respuesta exitosa sin datos.
     * 
     * @param message Mensaje de éxito
     */
    public ApiResponse(String message) {
        this.status = "success";
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor para respuesta exitosa con datos.
     * 
     * @param message Mensaje de éxito
     * @param data Datos a devolver
     */
    public ApiResponse(String message, T data) {
        this.status = "success";
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor completo para respuestas detalladas.
     * 
     * @param status Estado de la respuesta ("success" o "error")
     * @param code Código personalizado
     * @param message Mensaje descriptivo
     * @param data Datos (puede ser null)
     */
    public ApiResponse(String status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Getters y Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
