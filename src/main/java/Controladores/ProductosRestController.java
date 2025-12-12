package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.Productos;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductosRestController {

    private final ProductosController productosController;

    public ProductosRestController() {
        this.productosController = Main.getProductosController();
    }

    @GetMapping
    public List<Productos> listarTodos() {
        return productosController.listar();
    }

    @GetMapping("/{id}")
    public Productos obtenerPorId(@PathVariable String id) {
        return productosController.buscarPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Productos producto) {
        productosController.insertar(producto);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable String id, @RequestBody Productos producto) {
        productosController.actualizar(producto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        productosController.eliminar(id);
    }
}
