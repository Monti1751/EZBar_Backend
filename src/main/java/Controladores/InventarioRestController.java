package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.Inventario;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InventarioRestController {

    private final InventarioController inventarioController;

    public InventarioRestController() {
        this.inventarioController = Main.getInventarioController();
    }

    @GetMapping
    public List<Inventario> listarTodos() {
        return inventarioController.listar();
    }

    @GetMapping("/{id}")
    public Inventario obtenerPorId(@PathVariable String id) {
        return inventarioController.buscarPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Inventario inventario) {
        inventarioController.insertar(inventario);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable String id, @RequestBody Inventario inventario) {
        inventarioController.actualizar(inventario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        inventarioController.eliminar(id);
    }
}
