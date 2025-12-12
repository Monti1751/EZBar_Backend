package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HealthCheckController {

    @GetMapping
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "EZBar Backend API");
        response.put("version", "1.0.0");
        response.put("timestamp", System.currentTimeMillis());
        
        try {
            // Verifica que la base de datos está conectada
            Main.getProductosController().listar();
            response.put("database", "CONNECTED");
        } catch (Exception e) {
            response.put("database", "DISCONNECTED");
            response.put("error", e.getMessage());
        }
        
        return response;
    }
}
