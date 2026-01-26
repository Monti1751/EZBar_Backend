package Controladores;

import Repositorios.EmpleadosRepository;
import ClasesBD.Empleados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/empleados")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EmpleadosRestController {

    @Autowired
    private EmpleadosRepository repository;

    @GetMapping
    public List<Empleados> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Empleados obtenerPorId(@PathVariable int id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    @SuppressWarnings("null")
    public Empleados crear(@RequestBody Empleados empleado) {
        return repository.save(empleado);
    }

    @PutMapping("/{id}")
    public Empleados actualizar(@PathVariable int id, @RequestBody Empleados empleado) {
        if (repository.existsById(id)) {
            empleado.setEmpleado_id(id);
            return repository.save(empleado);
        }
        return null; // Should return 404
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        repository.deleteById(id);
    }
}
