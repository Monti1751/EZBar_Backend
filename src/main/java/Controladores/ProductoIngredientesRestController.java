package Controladores;

import Repositorios.ProductoIngredientesRepository;
import ClasesBD.ProductoIngredientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/producto_ingredientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductoIngredientesRestController {

    @Autowired
    private ProductoIngredientesRepository repository;

    @GetMapping
    public List<ProductoIngredientes> listarTodos() {
        return repository.findAll();
    }

    // Composite key handling via params or body is trickier, omitting simple GET by
    // ID for now or using params
    // For simplicity, just listing and creation

    @PostMapping
    @NonNull
    public ProductoIngredientes crear(@RequestBody @NonNull ProductoIngredientes relacion) {
        return repository.save(relacion);
    }

    // Delete needs composite ID logic, skipping for now to prioritize compilation
}
