import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriasController {

    // INSERTAR
    public void insertar(Categorias c) {
        String sql = "INSERT INTO categorias (categoria_id, nombre, descripcion) VALUES (?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, c.getCategoria_id());
            st.setString(2, c.getNombre());
            st.setString(3, c.getDescripcion());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<Categorias> listar() {
        List<Categorias> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Categorias(
                        rs.getString("categoria_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public Categorias buscarPorId(String categoriaId) {
        String sql = "SELECT * FROM categorias WHERE categoria_id = ?";
        Categorias c = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, categoriaId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                c = new Categorias(
                        rs.getString("categoria_id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    // ACTUALIZAR
    public void actualizar(Categorias c) {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE categoria_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, c.getNombre());
            st.setString(2, c.getDescripcion());
            st.setString(3, c.getCategoria_id());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(String categoriaId) {
        String sql = "DELETE FROM categorias WHERE categoria_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, categoriaId);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}