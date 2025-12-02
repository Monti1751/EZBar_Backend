import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoIngredientesController {

    // INSERTAR
    public void insertar(ProductoIngredientes pi) {
        String sql = "INSERT INTO producto_ingredientes (producto_id, ingrediente_id, unidad_medida, cantidad) VALUES (?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pi.getProducto_id());
            st.setString(2, pi.getIngrediente_id());
            st.setString(3, pi.getUnidad_medida());
            st.setDouble(4, pi.getCantidad());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<ProductoIngredientes> listar() {
        List<ProductoIngredientes> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto_ingredientes";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new ProductoIngredientes(
                        rs.getString("producto_id"),
                        rs.getString("ingrediente_id"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("cantidad")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR PRODUCTO E INGREDIENTE (clave compuesta)
    public ProductoIngredientes buscar(String productoId, String ingredienteId) {
        String sql = "SELECT * FROM producto_ingredientes WHERE producto_id = ? AND ingrediente_id = ?";
        ProductoIngredientes pi = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, productoId);
            st.setString(2, ingredienteId);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                pi = new ProductoIngredientes(
                        rs.getString("producto_id"),
                        rs.getString("ingrediente_id"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("cantidad")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    // ACTUALIZAR
    public void actualizar(ProductoIngredientes pi) {
        String sql = "UPDATE producto_ingredientes SET unidad_medida = ?, cantidad = ? WHERE producto_id = ? AND ingrediente_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pi.getUnidad_medida());
            st.setDouble(2, pi.getCantidad());
            st.setString(3, pi.getProducto_id());
            st.setString(4, pi.getIngrediente_id());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(String productoId, String ingredienteId) {
        String sql = "DELETE FROM producto_ingredientes WHERE producto_id = ? AND ingrediente_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, productoId);
            st.setString(2, ingredienteId);

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS ADICIONALES ÚTILES

    // Listar ingredientes por producto
    public List<ProductoIngredientes> listarPorProducto(String productoId) {
        List<ProductoIngredientes> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto_ingredientes WHERE producto_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, productoId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new ProductoIngredientes(
                        rs.getString("producto_id"),
                        rs.getString("ingrediente_id"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("cantidad")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar productos donde se usa un ingrediente
    public List<ProductoIngredientes> listarPorIngrediente(String ingredienteId) {
        List<ProductoIngredientes> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto_ingredientes WHERE ingrediente_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, ingredienteId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new ProductoIngredientes(
                        rs.getString("producto_id"),
                        rs.getString("ingrediente_id"),
                        rs.getString("unidad_medida"),
                        rs.getDouble("cantidad")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Calcular cantidad total usada de un ingrediente (en todos los productos)
    public double calcularUsoIngrediente(String ingredienteId) {
        String sql = "SELECT SUM(cantidad) AS total FROM producto_ingredientes WHERE ingrediente_id = ?";
        double total = 0.0;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, ingredienteId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
}