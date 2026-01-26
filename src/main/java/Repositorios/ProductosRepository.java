package Repositorios;

import ClasesBD.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Productos.
 * Provee acceso a datos para la tabla PRODUCTOS.
 */
@Repository
public interface ProductosRepository extends JpaRepository<Productos, Integer> {
}
