package Controladores;

import Repositorios.DetallePedidosRepository;
import ClasesBD.DetallePedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/detalle_pedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DetallePedidosRestController {

    @Autowired
    private DetallePedidosRepository repository;

    @GetMapping
    public List<DetallePedidos> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public DetallePedidos obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public DetallePedidos crear(@RequestBody DetallePedidos detalle) {
        return repository.save(detalle);
    }

    @PutMapping("/{id}")
    public DetallePedidos actualizar(@PathVariable Integer id, @RequestBody DetallePedidos detalle) {
        if (repository.existsById(id)) {
            detalle.setDetalle_id(id);
            return repository.save(detalle);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
