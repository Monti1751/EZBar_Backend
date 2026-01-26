package Repositorios;

import ClasesBD.Pedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Pedidos.
 * Extiende JpaRepository para heredar operaciones CRUD estándar (save,
 * findById, delete, etc.)
 */
@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Integer> {
}
