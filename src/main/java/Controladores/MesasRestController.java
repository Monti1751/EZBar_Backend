package Controladores;

import Repositorios.MesasRepository;
import ClasesBD.Mesas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/mesas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MesasRestController {

    @Autowired
    private MesasRepository repository;

    @GetMapping
    public List<Mesas> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Mesas obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Mesas crear(@RequestBody Mesas mesa) {
        return repository.save(mesa);
    }

    @PutMapping("/{id}")
    public Mesas actualizar(@PathVariable Integer id, @RequestBody Mesas mesa) {
        if (repository.existsById(id)) {
            mesa.setMesa_id(id);
            return repository.save(mesa);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
