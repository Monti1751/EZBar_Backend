package Controladores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ClasesBD.*;

public class ProductosController {

    // INSERTAR
    public void insertar(Productos p) {
        String sql = "INSERT INTO productos (producto_id, categoria_id, nombre, descripcion, unidad_medida, precio, costo, iva_porcentaje, stock_actual, stock_minimo, es_ingrediente, activo, url_imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getProducto_id());
            st.setString(2, p.getCategoria_id());
            st.setString(3, p.getNombre());
            st.setString(4, p.getDescripcion());
            st.setString(5, p.getUnidad_medida());
            st.setDouble(6, p.getPrecio());
            st.setDouble(7, p.getCosto());
            st.setDouble(8, p.getIva_porcentaje());
            st.setInt(9, p.getStock_actual());
            st.setInt(10, p.getStock_minimo());
            st.setBoolean(11, p.isEs_ingrediente());
            st.setBoolean(12, p.isActivo());
            st.setString(13, p.getUrl_imagen());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<Productos> listar() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Productos(
                        rs.getString("producto_id"),
                        rs.getString("categoria_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getDouble("costo"),
                        rs.getDouble("iva_porcentaje"),
                        rs.getInt("stock_actual"),
                        rs.getInt("stock_minimo"),
                        rs.getBoolean("es_ingrediente"),
                        rs.getBoolean("activo"),
                        rs.getString("url_imagen")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public Productos buscarPorId(String productoId) {
        String sql = "SELECT * FROM productos WHERE producto_id = ?";
        Productos p = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, productoId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                p = new Productos(
                        rs.getString("producto_id"),
                        rs.getString("categoria_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getDouble("costo"),
                        rs.getDouble("iva_porcentaje"),
                        rs.getInt("stock_actual"),
                        rs.getInt("stock_minimo"),
                        rs.getBoolean("es_ingrediente"),
                        rs.getBoolean("activo"),
                        rs.getString("url_imagen")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // ACTUALIZAR
    public void actualizar(Productos p) {
        String sql = "UPDATE productos SET categoria_id = ?, nombre = ?, descripcion = ?, unidad_medida = ?, precio = ?, costo = ?, iva_porcentaje = ?, stock_actual = ?, stock_minimo = ?, es_ingrediente = ?, activo = ?, url_imagen = ? WHERE producto_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getCategoria_id());
            st.setString(2, p.getNombre());
            st.setString(3, p.getDescripcion());
            st.setString(4, p.getUnidad_medida());
            st.setDouble(5, p.getPrecio());
            st.setDouble(6, p.getCosto());
            st.setDouble(7, p.getIva_porcentaje());
            st.setInt(8, p.getStock_actual());
            st.setInt(9, p.getStock_minimo());
            st.setBoolean(10, p.isEs_ingrediente());
            st.setBoolean(11, p.isActivo());
            st.setString(12, p.getUrl_imagen());
            st.setString(13, p.getProducto_id());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(String productoId) {
        String sql = "DELETE FROM productos WHERE producto_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, productoId);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS ADICIONALES ÚTILES

    // Listar productos activos
    public List<Productos> listarActivos() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE activo = true";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Productos(
                        rs.getString("producto_id"),
                        rs.getString("categoria_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getDouble("costo"),
                        rs.getDouble("iva_porcentaje"),
                        rs.getInt("stock_actual"),
                        rs.getInt("stock_minimo"),
                        rs.getBoolean("es_ingrediente"),
                        rs.getBoolean("activo"),
                        rs.getString("url_imagen")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar productos por categoría
    public List<Productos> listarPorCategoria(String categoriaId) {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, categoriaId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Productos(
                        rs.getString("producto_id"),
                        rs.getString("categoria_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getDouble("costo"),
                        rs.getDouble("iva_porcentaje"),
                        rs.getInt("stock_actual"),
                        rs.getInt("stock_minimo"),
                        rs.getBoolean("es_ingrediente"),
                        rs.getBoolean("activo"),
                        rs.getString("url_imagen")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar productos con stock bajo
    public List<Productos> listarStockBajo() {
        List<Productos> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE stock_actual <= stock_minimo";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Productos(
                        rs.getString("producto_id"),
                        rs.getString("categoria_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("precio"),
                        rs.getDouble("costo"),
                        rs.getDouble("iva_porcentaje"),
                        rs.getInt("stock_actual"),
                        rs.getInt("stock_minimo"),
                        rs.getBoolean("es_ingrediente"),
                        rs.getBoolean("activo"),
                        rs.getString("url_imagen")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
