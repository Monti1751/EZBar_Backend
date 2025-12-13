package Controladores;

import Repositorios.ProductosRepository;
import ClasesBD.Productos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductosRestController {

    @Autowired
    private ProductosRepository repository;

    @GetMapping
    public List<Productos> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Productos obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Productos crear(@RequestBody Productos producto) {
        return repository.save(producto);
    }

    @PutMapping("/{id}")
    public Productos actualizar(@PathVariable Integer id, @RequestBody Productos producto) {
        if (repository.existsById(id)) {
            producto.setProducto_id(id);
            return repository.save(producto);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
