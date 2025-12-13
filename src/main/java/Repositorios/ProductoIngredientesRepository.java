package Repositorios;

import ClasesBD.ProductoIngredientes;
import ClasesBD.ProductoIngredientesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoIngredientesRepository extends JpaRepository<ProductoIngredientes, ProductoIngredientesId> {
}
