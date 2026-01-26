package Repositorios;

import ClasesBD.Mesas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio JPA para la entidad Mesas.
 * Permite realizar operaciones CRUD directas contra la base de datos
 * automáticamente.
 */
@Repository
public interface MesasRepository extends JpaRepository<Mesas, Integer> {

    // Consulta personalizada JPQL para obtener ubicaciones únicas de las mesas
    // SQL Equivalente: SELECT DISTINCT ubicacion FROM MESAS
    @Query("SELECT DISTINCT m.ubicacion FROM Mesas m")
    List<String> findDistinctUbicacion();
}
