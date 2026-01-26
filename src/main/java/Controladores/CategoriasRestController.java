package Controladores;

import Repositorios.CategoriasRepository;
import ClasesBD.Categorias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar Categorías
 * 
 * @RestController: Indica que esta clase maneja peticiones HTTP y devuelve
 *                  datos (JSON) directamente.
 * @RequestMapping: Define la URL base para este controlador (/categorias).
 * @CrossOrigin: Permite peticiones desde cualquier origen (útil para desarrollo
 *               con Flutter/Web).
 */
@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriasRestController {

    // Inyección de dependencia del Repositorio para acceder a la base de datos
    @Autowired
    private CategoriasRepository repository;

    // Obtener todas las categorías (GET /categorias)
    @GetMapping
    public List<Categorias> listarTodos() {
        return repository.findAll();
    }

    // Obtener una categoría por ID (GET /categorias/{id})
    @GetMapping("/{id}")
    public Categorias obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    // Crear una nueva categoría (POST /categorias)
    // El objeto Categorias se construye automáticamente desde el JSON del cuerpo de
    // la petición
    @PostMapping
    @SuppressWarnings("null")
    public Categorias crear(@RequestBody Categorias categoria) {
        return repository.save(categoria);
    }

    // Actualizar una categoría existente (PUT /categorias/{id})
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public Categorias actualizar(@PathVariable int id, @RequestBody Categorias categoria) {
        if (repository.existsById(id)) {
            categoria.setCategoria_id(id); // Asegurar que el ID sea el correcto
            return repository.save(categoria);
        }
        return null; // O manejar error 404
    }

    // Eliminar una categoría (DELETE /categorias/{id})
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        repository.deleteById(id);
    }
}
