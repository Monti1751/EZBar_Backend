package Controladores;

import Repositorios.PuestosRepository;
import ClasesBD.Puestos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/puestos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PuestosRestController {

    @Autowired
    private PuestosRepository repository;

    @GetMapping
    public List<Puestos> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Puestos obtenerPorId(@PathVariable int id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    @SuppressWarnings("null")
    public Puestos crear(@RequestBody Puestos puesto) {
        return repository.save(puesto);
    }

    @PutMapping("/{id}")
    public Puestos actualizar(@PathVariable int id, @RequestBody Puestos puesto) {
        if (repository.existsById(id)) {
            puesto.setPuesto_id(id);
            return repository.save(puesto);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        repository.deleteById(id);
    }
}
