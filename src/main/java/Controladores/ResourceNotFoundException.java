package Controladores;

/**
 * Excepción personalizada para recursos no encontrados.
 * 
 * Beneficios de las excepciones personalizadas:
 * 1. Claridad: El nombre dice exactamente qué pasó
 * 2. Especificidad: Podemos diferenciar errores 404 de otros errores
 * 3. Tratamiento: GlobalExceptionHandler puede manejar esto específicamente
 * 4. Documentación: Los desarrolladores saben cuándo lanzarla
 * 
 * Ejemplo de uso:
 * {@code
 * if (categoria == null) {
 *     throw new ResourceNotFoundException("Categoría con ID " + id + " no encontrada");
 * }
 * }
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor simple con mensaje.
     * 
     * @param message Mensaje descriptivo del error
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa (excepción raíz).
     * 
     * Útil cuando queremos preservar la excepción original para debugging.
     * 
     * @param message Mensaje descriptivo del error
     * @param cause Excepción que causó este error
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
