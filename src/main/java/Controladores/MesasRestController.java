package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.Mesas;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MesasRestController {

    private final MesaController mesaController;

    public MesasRestController() {
        this.mesaController = Main.getMesaController();
    }

    @GetMapping("/{id}")
    public String obtenerEstado(@PathVariable String id) {
        return mesaController.getEstadoMesa(id);
    }

    @PutMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable String id, @RequestParam String nuevoEstado) {
        return mesaController.cambiarEstadoMesa(id, nuevoEstado);
    }
}
