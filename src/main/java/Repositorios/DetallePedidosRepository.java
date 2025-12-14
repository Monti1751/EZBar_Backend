package Repositorios;

import ClasesBD.DetallePedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidosRepository extends JpaRepository<DetallePedidos, Integer> {
    java.util.List<DetallePedidos> findByPedido(ClasesBD.Pedidos pedido);
}
