package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.ProductoIngredientes;
import java.util.List;

@RestController
@RequestMapping("/api/productoingredientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductoIngredientesRestController {

    private final ProductoIngredientesController productoIngredientesController;

    public ProductoIngredientesRestController() {
        this.productoIngredientesController = Main.getProductoIngredientesController();
    }

    @GetMapping
    public List<ProductoIngredientes> listarTodos() {
        return productoIngredientesController.listar();
    }

    @GetMapping("/buscar")
    public ProductoIngredientes obtenerPorIds(@RequestParam String productoId, @RequestParam String ingredienteId) {
        return productoIngredientesController.buscar(productoId, ingredienteId);
    }

    @PostMapping
    public void crear(@RequestBody ProductoIngredientes productoIngrediente) {
        productoIngredientesController.insertar(productoIngrediente);
    }

    @PutMapping
    public void actualizar(@RequestBody ProductoIngredientes productoIngrediente) {
        productoIngredientesController.actualizar(productoIngrediente);
    }

    @DeleteMapping
    public void eliminar(@RequestParam String productoId, @RequestParam String ingredienteId) {
        productoIngredientesController.eliminar(productoId, ingredienteId);
    }
}
