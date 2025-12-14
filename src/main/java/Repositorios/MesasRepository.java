package Repositorios;

import ClasesBD.Mesas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MesasRepository extends JpaRepository<Mesas, Integer> {
    @Query("SELECT DISTINCT m.ubicacion FROM Mesas m")
    List<String> findDistinctUbicacion();
}
