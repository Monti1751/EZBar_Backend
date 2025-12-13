package Controladores;

import Repositorios.PedidosRepository;
import ClasesBD.Pedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidosRestController {

    @Autowired
    private PedidosRepository repository;

    @GetMapping
    public List<Pedidos> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Pedidos obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Pedidos crear(@RequestBody Pedidos pedido) {
        return repository.save(pedido);
    }

    @PutMapping("/{id}")
    public Pedidos actualizar(@PathVariable Integer id, @RequestBody Pedidos pedido) {
        if (repository.existsById(id)) {
            pedido.setPedido_id(id);
            return repository.save(pedido);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
