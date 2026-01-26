package Controladores;

import Repositorios.ProductosRepository;
import ClasesBD.Productos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar Productos
 * Provee metodos CRUD estándar accesibles vía HTTP.
 */
@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductosRestController {

    // Repositorio JPA para operaciones de base de datos sobre la tabla PRODUCTOS
    @Autowired
    private ProductosRepository repository;

    // Listar todos los productos (GET /productos)
    @GetMapping
    public List<Productos> listarTodos() {
        return repository.findAll();
    }

    // Obtener producto por ID (GET /productos/{id})
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public Productos obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    // Crear producto (POST /productos)
    @PostMapping
    @SuppressWarnings("null")
    public Productos crear(@RequestBody Productos producto) {
        return repository.save(producto);
    }

    // Actualizar producto (PUT /productos/{id})
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public Productos actualizar(@PathVariable Integer id, @RequestBody Productos producto) {
        if (repository.existsById(id)) {
            producto.setProducto_id(id);
            return repository.save(producto);
        }
        return null;
    }

    // Eliminar producto (DELETE /productos/{id})
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        repository.deleteById(id);
    }
}
