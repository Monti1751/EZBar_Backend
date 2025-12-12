package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.Categorias;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriasRestController {

    private final CategoriasController categoriasController;

    public CategoriasRestController() {
        this.categoriasController = Main.getCategoriasController();
    }

    @GetMapping
    public List<Categorias> listarTodos() {
        return categoriasController.listar();
    }

    @GetMapping("/{id}")
    public Categorias obtenerPorId(@PathVariable String id) {
        return categoriasController.buscarPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Categorias categoria) {
        categoriasController.insertar(categoria);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable String id, @RequestBody Categorias categoria) {
        categoriasController.actualizar(categoria);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        categoriasController.eliminar(id);
    }
}
