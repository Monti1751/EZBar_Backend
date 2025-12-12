package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.Pagos;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PagosRestController {

    private final PagosController pagosController;

    public PagosRestController() {
        this.pagosController = Main.getPagosController();
    }

    @GetMapping
    public List<Pagos> listarTodos() {
        return pagosController.listar();
    }

    @GetMapping("/{id}")
    public Pagos obtenerPorId(@PathVariable String id) {
        return pagosController.buscarPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Pagos pago) {
        pagosController.insertar(pago);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable String id, @RequestBody Pagos pago) {
        pagosController.actualizar(pago);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        pagosController.eliminar(id);
    }
}
