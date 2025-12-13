package Controladores;

import Repositorios.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HealthCheckController {

    @Autowired
    private ProductosRepository productosRepository;

    @GetMapping
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "EZBar Backend API");
        response.put("version", "1.0.0");
        response.put("timestamp", System.currentTimeMillis());

        try {
            // Verifica que la base de datos está conectada intentando una consulta simple
            productosRepository.count();
            response.put("database", "CONNECTED");
        } catch (Exception e) {
            response.put("database", "DISCONNECTED");
            response.put("error", e.getMessage());
        }

        return response;
    }
}
