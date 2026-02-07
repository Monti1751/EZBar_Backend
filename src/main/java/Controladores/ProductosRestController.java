package Controladores;

import Repositorios.ProductosRepository;
import Repositorios.CategoriasRepository;
import ClasesBD.Productos;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar Productos
 * Provee metodos CRUD estándar accesibles vía HTTP.
 */
@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductosRestController {

    // Repositorio JPA para operaciones de base de datos sobre la tabla PRODUCTOS
    @Autowired
    private ProductosRepository repository;

    @Autowired
    private CategoriasRepository categoriasRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductosRestController.class);

    @PostConstruct
    public void init() {
        logger.info("=================================================");
        logger.info("🚀 EZBAR BACKEND REBUILT SUCCESSFULLY - VERSION 2");
        logger.info("=================================================");
    }

    // Listar todos los productos (GET /productos)
    @GetMapping
    public List<Productos> listarTodos() {
        return repository.findAll();
    }

    // Obtener producto por ID (GET /productos/{id})
    @GetMapping("/{id}")
    @SuppressWarnings("null")
    public Productos obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    // Crear producto (POST /productos)
    @PostMapping
    @SuppressWarnings("null")
    public Productos crear(@RequestBody Productos producto) {
        return repository.save(producto);
    }

    // Actualizar producto (PUT /productos/{id})
    @PutMapping("/{id}")
    @SuppressWarnings("null")
    public Productos actualizar(@PathVariable Integer id, @RequestBody Productos producto) {
        logger.info("DEBUG: Recibida petición PUT para producto ID: {}", id);

        return repository.findById(id).map(existing -> {
            logger.debug("DEBUG: Producto {} encontrado. Actualizando...", id);
            if (producto.getNombre() != null)
                existing.setNombre(producto.getNombre());
            if (producto.getPrecio() != null)
                existing.setPrecio(producto.getPrecio());
            if (producto.getCategoria() != null && producto.getCategoria().getCategoria_id() != null) {
                categoriasRepository.findById(producto.getCategoria().getCategoria_id())
                        .ifPresent(existing::setCategoria);
            }
            if (producto.getImagenBlob() != null && producto.getImagenBlob().length > 0) {
                existing.setImagenBlob(producto.getImagenBlob());
                existing.setUrl_imagen(null);
                logger.info("DEBUG: Recibida nueva imagen ({} bytes) para producto {}", producto.getImagenBlob().length,
                        id);
            } else if (producto.getUrl_imagen() != null) {
                existing.setUrl_imagen(producto.getUrl_imagen());
            }

            if (producto.getDescripcion() != null)
                existing.setDescripcion(producto.getDescripcion());
            if (producto.getCosto() != null)
                existing.setCosto(producto.getCosto());
            if (producto.getIva_porcentaje() != null)
                existing.setIva_porcentaje(producto.getIva_porcentaje());
            if (producto.getStock_actual() != null)
                existing.setStock_actual(producto.getStock_actual());
            if (producto.getStock_minimo() != null)
                existing.setStock_minimo(producto.getStock_minimo());
            if (producto.getUnidad_medida() != null)
                existing.setUnidad_medida(producto.getUnidad_medida());

            Productos guardado = repository.save(existing);
            logger.info("DEBUG: Producto {} guardado exitosamente con {} bytes de imagen", id,
                    (guardado.getImagenBlob() != null ? guardado.getImagenBlob().length : 0));
            return guardado;
        }).orElseGet(() -> {
            System.out.println("DEBUG: ERROR - Producto con ID " + id + " no encontrado");
            return null;
        });
    }

    // Eliminar producto (DELETE /productos/{id})
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        repository.deleteById(id);
    }
}
