package Controladores;

import Repositorios.CategoriasRepository;
import ClasesBD.Categorias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriasRestController {

    @Autowired
    private CategoriasRepository repository;

    @GetMapping
    public List<Categorias> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Categorias obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Categorias crear(@RequestBody Categorias categoria) {
        return repository.save(categoria);
    }

    @PutMapping("/{id}")
    public Categorias actualizar(@PathVariable Integer id, @RequestBody Categorias categoria) {
        if (repository.existsById(id)) {
            categoria.setCategoria_id(id);
            return repository.save(categoria);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
