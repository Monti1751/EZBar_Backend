package Controladores;

import Repositorios.InventarioRepository;
import ClasesBD.Inventario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/inventario")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InventarioRestController {

    @Autowired
    private InventarioRepository repository;

    @GetMapping
    public List<Inventario> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Inventario obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Inventario crear(@RequestBody Inventario inventario) {
        return repository.save(inventario);
    }

    @PutMapping("/{id}")
    public Inventario actualizar(@PathVariable Integer id, @RequestBody Inventario inventario) {
        if (repository.existsById(id)) {
            inventario.setInventario_id(id);
            return repository.save(inventario);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
