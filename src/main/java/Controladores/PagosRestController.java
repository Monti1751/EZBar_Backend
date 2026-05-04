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
    
    @Autowired
    private Repositorios.EmpleadosRepository empleadosRepository;
    
    @Autowired
    private Repositorios.PedidosRepository pedidosRepository;

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
        // Asegurar que hay un empleado válido
        if (pago.getEmpleado() != null && pago.getEmpleado().getEmpleado_id() != null) {
            ClasesBD.Empleados emp = empleadosRepository.findById(pago.getEmpleado().getEmpleado_id()).orElse(null);
            if (emp == null) {
                List<ClasesBD.Empleados> todos = empleadosRepository.findAll();
                if (!todos.isEmpty()) pago.setEmpleado(todos.get(0));
            } else {
                pago.setEmpleado(emp);
            }
        } else {
            List<ClasesBD.Empleados> todos = empleadosRepository.findAll();
            if (!todos.isEmpty()) pago.setEmpleado(todos.get(0));
        }

        Pagos nuevoPago = repository.save(pago);
        
        // Actualizar automáticamente el pedido a estado "pagado"
        if (nuevoPago.getPedido() != null && nuevoPago.getPedido().getPedido_id() != null) {
            ClasesBD.Pedidos ped = pedidosRepository.findById(nuevoPago.getPedido().getPedido_id()).orElse(null);
            if (ped != null) {
                ped.setEstado(ClasesBD.Pedidos.Estado.pagado);
                pedidosRepository.save(ped);
            }
        }
        
        return nuevoPago;
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
