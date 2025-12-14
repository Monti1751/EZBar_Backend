package Repositorios;

import ClasesBD.Zonas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonasRepository extends JpaRepository<Zonas, Integer> {
    java.util.Optional<Zonas> findByNombre(String nombre);

    @org.springframework.transaction.annotation.Transactional
    void deleteByNombre(String nombre);
}
