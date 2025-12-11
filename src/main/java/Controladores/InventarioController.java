package Controladores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ClasesBD.*;

public class InventarioController {

    // INSERTAR
    public void insertar(Inventario i) {
        String sql = "INSERT INTO inventario (inventario_id, producto_id, empleado_id, fecha_movimiento, tipo_movimiento, cantidad) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, i.getInventario_id());
            st.setString(2, i.getProducto_id());
            st.setString(3, i.getEmpleado_id());
            st.setString(4, i.getFecha_movimiento());
            st.setString(5, i.getTipo_movimiento());
            st.setDouble(6, i.getCantidad());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<Inventario> listar() {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Inventario(
                        rs.getString("inventario_id"),
                        rs.getString("producto_id"),
                        rs.getString("empleado_id"),
                        rs.getString("fecha_movimiento"),
                        rs.getString("tipo_movimiento"),
                        rs.getDouble("cantidad")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public Inventario buscarPorId(String inventarioId) {
        String sql = "SELECT * FROM inventario WHERE inventario_id = ?";
        Inventario i = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, inventarioId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                i = new Inventario(
                        rs.getString("inventario_id"),
                        rs.getString("producto_id"),
                        rs.getString("empleado_id"),
                        rs.getString("fecha_movimiento"),
                        rs.getString("tipo_movimiento"),
                        rs.getDouble("cantidad")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return i;
    }

    // ACTUALIZAR
    public void actualizar(Inventario i) {
        String sql = "UPDATE inventario SET producto_id = ?, empleado_id = ?, fecha_movimiento = ?, tipo_movimiento = ?, cantidad = ? WHERE inventario_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, i.getProducto_id());
            st.setString(2, i.getEmpleado_id());
            st.setString(3, i.getFecha_movimiento());
            st.setString(4, i.getTipo_movimiento());
            st.setDouble(5, i.getCantidad());
            st.setString(6, i.getInventario_id());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(String inventarioId) {
        String sql = "DELETE FROM inventario WHERE inventario_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, inventarioId);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS ADICIONALES ÚTILES

    // Listar movimientos por producto
    public List<Inventario> listarPorProducto(String productoId) {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE producto_id = ? ORDER BY fecha_movimiento DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, productoId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Inventario(
                        rs.getString("inventario_id"),
                        rs.getString("producto_id"),
                        rs.getString("empleado_id"),
                        rs.getString("fecha_movimiento"),
                        rs.getString("tipo_movimiento"),
                        rs.getDouble("cantidad")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar movimientos por tipo (entrada/salida)
    public List<Inventario> listarPorTipo(String tipoMovimiento) {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE tipo_movimiento = ? ORDER BY fecha_movimiento DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, tipoMovimiento);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Inventario(
                        rs.getString("inventario_id"),
                        rs.getString("producto_id"),
                        rs.getString("empleado_id"),
                        rs.getString("fecha_movimiento"),
                        rs.getString("tipo_movimiento"),
                        rs.getDouble("cantidad")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar movimientos por empleado
    public List<Inventario> listarPorEmpleado(String empleadoId) {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE empleado_id = ? ORDER BY fecha_movimiento DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, empleadoId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Inventario(
                        rs.getString("inventario_id"),
                        rs.getString("producto_id"),
                        rs.getString("empleado_id"),
                        rs.getString("fecha_movimiento"),
                        rs.getString("tipo_movimiento"),
                        rs.getDouble("cantidad")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar movimientos por rango de fechas
    public List<Inventario> listarPorRangoFechas(String fechaInicio, String fechaFin) {
        List<Inventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE fecha_movimiento BETWEEN ? AND ? ORDER BY fecha_movimiento DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, fechaInicio);
            st.setString(2, fechaFin);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Inventario(
                        rs.getString("inventario_id"),
                        rs.getString("producto_id"),
                        rs.getString("empleado_id"),
                        rs.getString("fecha_movimiento"),
                        rs.getString("tipo_movimiento"),
                        rs.getDouble("cantidad")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}