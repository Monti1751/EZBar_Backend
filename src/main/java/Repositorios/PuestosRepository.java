package Repositorios;

import ClasesBD.Puestos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuestosRepository extends JpaRepository<Puestos, Integer> {
}
