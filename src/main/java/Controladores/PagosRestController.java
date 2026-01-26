package Controladores;

import Repositorios.PagosRepository;
import ClasesBD.Pagos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pagos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PagosRestController {

    @Autowired
    private PagosRepository repository;

    @GetMapping
    public List<Pagos> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Pagos obtenerPorId(@PathVariable int id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    @SuppressWarnings("null")
    public Pagos crear(@RequestBody Pagos pago) {
        return repository.save(pago);
    }

    @PutMapping("/{id}")
    public Pagos actualizar(@PathVariable int id, @RequestBody Pagos pago) {
        if (repository.existsById(id)) {
            pago.setPago_id(id);
            return repository.save(pago);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        repository.deleteById(id);
    }
}
