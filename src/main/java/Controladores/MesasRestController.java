package Controladores;

import Repositorios.MesasRepository;
import ClasesBD.Mesas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar Mesas
 */
@RestController
@RequestMapping("/mesas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MesasRestController {

    @Autowired
    private MesasRepository repository;

    // Obtener todas las mesas (GET /mesas)
    @GetMapping
    public List<Mesas> listarTodos() {
        return repository.findAll();
    }

    // Obtener mesa por ID (GET /mesas/{id})
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public Mesas obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    // Crear mesa (POST /mesas)
    @PostMapping
    @SuppressWarnings("null")
    public Mesas crear(@RequestBody Mesas mesa) {
        return repository.save(mesa);
    }

    // Actualizar mesa (PUT /mesas/{id})
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public Mesas actualizar(@PathVariable Integer id, @RequestBody Mesas mesa) {
        if (repository.existsById(id)) {
            mesa.setMesa_id(id);
            return repository.save(mesa);
        }
        return null;
    }

    // Eliminar mesa (DELETE /mesas/{id})
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        repository.deleteById(id);
    }
}
