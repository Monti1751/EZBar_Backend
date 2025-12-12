package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.Pedidos;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidosRestController {

    private final PedidosController pedidosController;

    public PedidosRestController() {
        this.pedidosController = Main.getPedidosController();
    }

    @GetMapping
    public List<Pedidos> listarTodos() {
        return pedidosController.listar();
    }

    @GetMapping("/{id}")
    public Pedidos obtenerPorId(@PathVariable String id) {
        return pedidosController.buscarPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Pedidos pedido) {
        pedidosController.insertar(pedido);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable String id, @RequestBody Pedidos pedido) {
        pedidosController.actualizar(pedido);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        pedidosController.eliminar(id);
    }
}
