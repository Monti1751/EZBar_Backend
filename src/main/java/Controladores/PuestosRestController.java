package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.Puestos;
import java.util.List;

@RestController
@RequestMapping("/api/puestos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PuestosRestController {

    private final PuestosController puestosController;

    public PuestosRestController() {
        this.puestosController = Main.getPuestosController();
    }

    @GetMapping
    public List<Puestos> listarTodos() {
        return puestosController.listar();
    }

    @GetMapping("/{id}")
    public Puestos obtenerPorId(@PathVariable String id) {
        return puestosController.buscarPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Puestos puesto) {
        puestosController.insertar(puesto);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable String id, @RequestBody Puestos puesto) {
        puestosController.actualizar(puesto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        puestosController.eliminar(id);
    }
}
