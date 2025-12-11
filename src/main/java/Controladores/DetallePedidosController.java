package Controladores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ClasesBD.*;

public class DetallePedidosController {

    // INSERTAR
    public void insertar(DetallePedidos d) {
        String sql = "INSERT INTO detalle_pedidos (detalle_id, pedido_id, producto_id, cantidad, precio_unitario, total_linea) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, d.getDetalle_id());
            st.setString(2, d.getPedido_id());
            st.setString(3, d.getProducto_id());
            st.setDouble(4, d.getCantidad());
            st.setDouble(5, d.getPrecio_unitario());
            st.setDouble(6, d.getTotal_linea());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<DetallePedidos> listar() {
        List<DetallePedidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedidos";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new DetallePedidos(
                        rs.getString("detalle_id"),
                        rs.getString("pedido_id"),
                        rs.getString("producto_id"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("total_linea")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public DetallePedidos buscarPorId(String detalleId) {
        String sql = "SELECT * FROM detalle_pedidos WHERE detalle_id = ?";
        DetallePedidos d = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, detalleId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                d = new DetallePedidos(
                        rs.getString("detalle_id"),
                        rs.getString("pedido_id"),
                        rs.getString("producto_id"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("total_linea")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    // ACTUALIZAR
    public void actualizar(DetallePedidos d) {
        String sql = "UPDATE detalle_pedidos SET pedido_id = ?, producto_id = ?, cantidad = ?, precio_unitario = ?, total_linea = ? WHERE detalle_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, d.getPedido_id());
            st.setString(2, d.getProducto_id());
            st.setDouble(3, d.getCantidad());
            st.setDouble(4, d.getPrecio_unitario());
            st.setDouble(5, d.getTotal_linea());
            st.setString(6, d.getDetalle_id());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(String detalleId) {
        String sql = "DELETE FROM detalle_pedidos WHERE detalle_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, detalleId);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS ADICIONALES ÚTILES

    // Listar detalles por pedido
    public List<DetallePedidos> listarPorPedido(String pedidoId) {
        List<DetallePedidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedidos WHERE pedido_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pedidoId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new DetallePedidos(
                        rs.getString("detalle_id"),
                        rs.getString("pedido_id"),
                        rs.getString("producto_id"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("total_linea")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Calcular total de un pedido
    public double calcularTotalPedido(String pedidoId) {
        String sql = "SELECT SUM(total_linea) AS total FROM detalle_pedidos WHERE pedido_id = ?";
        double total = 0.0;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pedidoId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    // Eliminar todos los detalles de un pedido
    public void eliminarPorPedido(String pedidoId) {
        String sql = "DELETE FROM detalle_pedidos WHERE pedido_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pedidoId);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}