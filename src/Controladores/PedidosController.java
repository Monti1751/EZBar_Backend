import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidosController {

    // INSERTAR
    public void insertar(Pedidos p) {
        String sql = "INSERT INTO pedidos (pedido_id, mesa_id, empleado_id, estado, observaciones, fecha_hora_pedido, numero_comensales, total_pedido) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getPedido_id());
            st.setString(2, p.getMesa_id());
            st.setString(3, p.getEmpleado_id());
            st.setString(4, p.getEstado());
            st.setString(5, p.getObservaciones());
            st.setString(6, p.getFecha_hora_pedido());
            st.setInt(7, p.getNumero_comensales());
            st.setDouble(8, p.getTotal_pedido());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<Pedidos> listar() {
        List<Pedidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Pedidos(
                        rs.getString("pedido_id"),
                        rs.getString("mesa_id"),
                        rs.getString("empleado_id"),
                        rs.getString("estado"),
                        rs.getString("observaciones"),
                        rs.getString("fecha_hora_pedido"),
                        rs.getInt("numero_comensales"),
                        rs.getDouble("total_pedido")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public Pedidos buscarPorId(String pedidoId) {
        String sql = "SELECT * FROM pedidos WHERE pedido_id = ?";
        Pedidos p = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pedidoId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                p = new Pedidos(
                        rs.getString("pedido_id"),
                        rs.getString("mesa_id"),
                        rs.getString("empleado_id"),
                        rs.getString("estado"),
                        rs.getString("observaciones"),
                        rs.getString("fecha_hora_pedido"),
                        rs.getInt("numero_comensales"),
                        rs.getDouble("total_pedido")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // ACTUALIZAR
    public void actualizar(Pedidos p) {
        String sql = "UPDATE pedidos SET mesa_id = ?, empleado_id = ?, estado = ?, observaciones = ?, fecha_hora_pedido = ?, numero_comensales = ?, total_pedido = ? WHERE pedido_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getMesa_id());
            st.setString(2, p.getEmpleado_id());
            st.setString(3, p.getEstado());
            st.setString(4, p.getObservaciones());
            st.setString(5, p.getFecha_hora_pedido());
            st.setInt(6, p.getNumero_comensales());
            st.setDouble(7, p.getTotal_pedido());
            st.setString(8, p.getPedido_id());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(String pedidoId) {
        String sql = "DELETE FROM pedidos WHERE pedido_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pedidoId);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS ADICIONALES ÚTILES

    // Listar pedidos por estado
    public List<Pedidos> listarPorEstado(String estado) {
        List<Pedidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE estado = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, estado);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Pedidos(
                        rs.getString("pedido_id"),
                        rs.getString("mesa_id"),
                        rs.getString("empleado_id"),
                        rs.getString("estado"),
                        rs.getString("observaciones"),
                        rs.getString("fecha_hora_pedido"),
                        rs.getInt("numero_comensales"),
                        rs.getDouble("total_pedido")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar pedidos por mesa
    public List<Pedidos> listarPorMesa(String mesaId) {
        List<Pedidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE mesa_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, mesaId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Pedidos(
                        rs.getString("pedido_id"),
                        rs.getString("mesa_id"),
                        rs.getString("empleado_id"),
                        rs.getString("estado"),
                        rs.getString("observaciones"),
                        rs.getString("fecha_hora_pedido"),
                        rs.getInt("numero_comensales"),
                        rs.getDouble("total_pedido")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Actualizar estado del pedido
    public void actualizarEstado(String pedidoId, String nuevoEstado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE pedido_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, nuevoEstado);
            st.setString(2, pedidoId);

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Listar pedidos activos (no pagados ni cancelados)
    public List<Pedidos> listarActivos() {
        List<Pedidos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE estado NOT IN ('pagado', 'cancelado')";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Pedidos(
                        rs.getString("pedido_id"),
                        rs.getString("mesa_id"),
                        rs.getString("empleado_id"),
                        rs.getString("estado"),
                        rs.getString("observaciones"),
                        rs.getString("fecha_hora_pedido"),
                        rs.getInt("numero_comensales"),
                        rs.getDouble("total_pedido")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}