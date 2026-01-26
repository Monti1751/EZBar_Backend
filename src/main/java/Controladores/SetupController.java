package Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para inicializar datos de prueba en la base de datos.
 * 
 * Responsabilidad: Exponer un endpoint HTTP para el setup inicial.
 * La lógica de negocio está delegada a SetupService.
 * 
 * @RestController Maneja peticiones HTTP y devuelve datos en JSON.
 * @RequestMapping Define la URL base: /setup
 */
@RestController
@RequestMapping("/setup")
public class SetupController {

    private final SetupService setupService;

    /**
     * Constructor con inyección de dependencia por constructor.
     * Preferible a @Autowired field injection (mejor para testing y desacoplamiento).
     * 
     * @param setupService Servicio de setup delegado
     */
    @Autowired
    public SetupController(SetupService setupService) {
        this.setupService = setupService;
    }

    /**
     * Endpoint GET para inicializar la base de datos con datos de prueba.
     * 
     * @return ResponseEntity con estado HTTP y mapa de respuesta
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> setupData() {
        try {
            String message = setupService.initializeDatabaseWithTestData();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", message);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error initializing database: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
