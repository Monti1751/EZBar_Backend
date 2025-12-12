package Controladores;

import org.springframework.web.bind.annotation.*;
import com.ezbar.Main;
import ClasesBD.Empleados;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EmpleadosRestController {

    private final EmpleadosController empleadosController;

    public EmpleadosRestController() {
        this.empleadosController = Main.getEmpleadosController();
    }

    @GetMapping
    public List<Empleados> listarTodos() {
        return empleadosController.listar();
    }

    @GetMapping("/{id}")
    public Empleados obtenerPorId(@PathVariable String id) {
        return empleadosController.buscarPorId(id);
    }

    @PostMapping
    public void crear(@RequestBody Empleados empleado) {
        empleadosController.insertar(empleado);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable String id, @RequestBody Empleados empleado) {
        empleadosController.actualizar(empleado);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        empleadosController.eliminar(id);
    }
}
