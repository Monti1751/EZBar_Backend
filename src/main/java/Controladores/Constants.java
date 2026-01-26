package Controladores;

/**
 * Clase centralizada para constantes de la aplicación.
 * 
 * Razones para esta clase:
 * 1. DRY (Don't Repeat Yourself): Evita hardcodear valores mágicos
 * 2. Single Source of Truth: Un único lugar para cambiar valores
 * 3. Fácil mantenimiento: Si cambia un mensaje o valor, se cambia en un solo lugar
 * 4. Reutilizable: Puede usarse en tests, logs, documentación, etc.
 * 
 * Principios SOLID aplicados:
 * - Centralización de datos (siguiendo patrón de constantes)
 * - Facilita cambios futuros sin afectar toda la codebase
 */
public class Constants {
    
    // Prevenir instanciación de esta clase de utilidad
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }

    /**
     * Mensajes de respuesta de la API.
     */
    public static class Messages {
        public static final String DATABASE_SEED_SUCCESS = 
                "Database seeded successfully! (Categories, Products, Zones, Positions, Employees)";
        
        public static final String DATABASE_SEED_ERROR = 
                "Error initializing database: ";
        
        public static final String INVALID_INPUT = 
                "Invalid input provided";
        
        public static final String NOT_FOUND = 
                "Resource not found";
        
        public static final String CREATED_SUCCESS = 
                "Resource created successfully";
        
        public static final String UPDATED_SUCCESS = 
                "Resource updated successfully";
        
        public static final String DELETED_SUCCESS = 
                "Resource deleted successfully";
    }

    /**
     * Rutas (endpoints) de la API.
     */
    public static class Routes {
        public static final String SETUP = "/setup";
        public static final String CATEGORIAS = "/categorias";
        public static final String PRODUCTOS = "/productos";
        public static final String MESAS = "/mesas";
        public static final String EMPLEADOS = "/empleados";
        public static final String ZONAS = "/zonas";
        public static final String PUESTOS = "/puestos";
        public static final String PEDIDOS = "/pedidos";
    }

    /**
     * Datos iniciales de prueba.
     */
    public static class DefaultTestData {
        public static final String ADMIN_USERNAME = "admin";
        public static final String ADMIN_PASSWORD_PLACEHOLDER = "hashed_password_placeholder";
        public static final String ADMIN_POSITION = "Administrador";
        public static final String ADMIN_DNI = "00000000X";
        
        public static final String ZONE_TERRAZA = "Terraza";
        public static final String ZONE_INTERIOR = "Interior";
        
        public static final String CATEGORY_BEVERAGES = "Bebidas";
        public static final String CATEGORY_APPETIZERS = "Tapas";
        
        public static final String UNIT_PIECE = "unid";
        public static final String UNIT_PORTION = "racion";
    }

    /**
     * Configuración de validaciones.
     */
    public static class Validation {
        public static final int MAX_CATEGORY_NAME_LENGTH = 100;
        public static final int MAX_PRODUCT_NAME_LENGTH = 100;
        public static final int MAX_DESCRIPTION_LENGTH = 1000;
        public static final int MIN_PRICE_VALUE = 0;
    }

    /**
     * Códigos de error personalizados (útiles para logging y debugging).
     */
    public static class ErrorCodes {
        public static final String INVALID_INPUT_CODE = "ERR_001";
        public static final String NOT_FOUND_CODE = "ERR_404";
        public static final String DATABASE_ERROR_CODE = "ERR_500";
        public static final String CONSTRAINT_VIOLATION_CODE = "ERR_409";
    }
}
