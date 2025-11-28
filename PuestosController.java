import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PuestosController {

    // INSERTAR
    public void insertar(Puestos p) {
        String sql = "INSERT INTO puestos (puesto_id, nombre_puesto) VALUES (?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getPuesto_id());
            st.setString(2, p.getNombre_puesto());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<Puestos> listar() {
        List<Puestos> lista = new ArrayList<>();
        String sql = "SELECT * FROM puestos";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Puestos(
                        rs.getString("puesto_id"),
                        rs.getString("nombre_puesto")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public Puestos buscarPorId(String puestoId) {
        String sql = "SELECT * FROM puestos WHERE puesto_id = ?";
        Puestos p = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, puestoId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                p = new Puestos(
                        rs.getString("puesto_id"),
                        rs.getString("nombre_puesto")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // ACTUALIZAR
    public void actualizar(Puestos p) {
        String sql = "UPDATE puestos SET nombre_puesto = ? WHERE puesto_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getNombre_puesto());
            st.setString(2, p.getPuesto_id());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(String puestoId) {
        String sql = "DELETE FROM puestos WHERE puesto_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, puestoId);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS ADICIONALES ÚTILES

    // Buscar puestos cuyo nombre coincida parcialmente
    public List<Puestos> buscarPorNombre(String nombre) {
        List<Puestos> lista = new ArrayList<>();
        String sql = "SELECT * FROM puestos WHERE nombre_puesto LIKE ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, "%" + nombre + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Puestos(
                        rs.getString("puesto_id"),
                        rs.getString("nombre_puesto")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Contar total de puestos registrados
    public int contarPuestos() {
        String sql = "SELECT COUNT(*) AS total FROM puestos";
        int total = 0;

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
}