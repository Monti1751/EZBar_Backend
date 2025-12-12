package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.DetallePedidos;
import java.util.List;

@RestController
@RequestMapping("/api/detallepedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DetallePedidosRestController {

    private final DetallePedidosController detallePedidosController;

    public DetallePedidosRestController() {
        this.detallePedidosController = Main.getDetallePedidosController();
    }

    @GetMapping
    public List<DetallePedidos> listarTodos() {
        return detallePedidosController.listar();
    }

    @GetMapping("/{id}")
    public DetallePedidos obtenerPorId(@PathVariable String id) {
        return detallePedidosController.buscarPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody DetallePedidos detalle) {
        detallePedidosController.insertar(detalle);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable String id, @RequestBody DetallePedidos detalle) {
        detallePedidosController.actualizar(detalle);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        detallePedidosController.eliminar(id);
    }
}
