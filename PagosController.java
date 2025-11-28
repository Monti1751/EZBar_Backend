import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagosController {

    // INSERTAR
    public void insertar(Pagos p) {
        String sql = "INSERT INTO pagos (pago_id, pedido_id, empleado_id, metodo_pago, fecha_hora_pago, monto, propina) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getPago_id());
            st.setString(2, p.getPedido_id());
            st.setString(3, p.getEmpleado_id());
            st.setString(4, p.getMetodo_pago());
            st.setString(5, p.getFecha_hora_pago());
            st.setDouble(6, p.getMonto());
            st.setDouble(7, p.getPropina());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<Pagos> listar() {
        List<Pagos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Pagos(
                        rs.getString("pago_id"),
                        rs.getString("pedido_id"),
                        rs.getString("empleado_id"),
                        rs.getString("metodo_pago"),
                        rs.getString("fecha_hora_pago"),
                        rs.getDouble("monto"),
                        rs.getDouble("propina")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public Pagos buscarPorId(String pagoId) {
        String sql = "SELECT * FROM pagos WHERE pago_id = ?";
        Pagos p = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pagoId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                p = new Pagos(
                        rs.getString("pago_id"),
                        rs.getString("pedido_id"),
                        rs.getString("empleado_id"),
                        rs.getString("metodo_pago"),
                        rs.getString("fecha_hora_pago"),
                        rs.getDouble("monto"),
                        rs.getDouble("propina")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // ACTUALIZAR
    public void actualizar(Pagos p) {
        String sql = "UPDATE pagos SET pedido_id = ?, empleado_id = ?, metodo_pago = ?, fecha_hora_pago = ?, monto = ?, propina = ? WHERE pago_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getPedido_id());
            st.setString(2, p.getEmpleado_id());
            st.setString(3, p.getMetodo_pago());
            st.setString(4, p.getFecha_hora_pago());
            st.setDouble(5, p.getMonto());
            st.setDouble(6, p.getPropina());
            st.setString(7, p.getPago_id());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(String pagoId) {
        String sql = "DELETE FROM pagos WHERE pago_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pagoId);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS ADICIONALES ÚTILES

    // Buscar pago por pedido
    public Pagos buscarPorPedido(String pedidoId) {
        String sql = "SELECT * FROM pagos WHERE pedido_id = ?";
        Pagos p = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, pedidoId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                p = new Pagos(
                        rs.getString("pago_id"),
                        rs.getString("pedido_id"),
                        rs.getString("empleado_id"),
                        rs.getString("metodo_pago"),
                        rs.getString("fecha_hora_pago"),
                        rs.getDouble("monto"),
                        rs.getDouble("propina")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // Listar pagos por método de pago
    public List<Pagos> listarPorMetodoPago(String metodoPago) {
        List<Pagos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE metodo_pago = ? ORDER BY fecha_hora_pago DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, metodoPago);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Pagos(
                        rs.getString("pago_id"),
                        rs.getString("pedido_id"),
                        rs.getString("empleado_id"),
                        rs.getString("metodo_pago"),
                        rs.getString("fecha_hora_pago"),
                        rs.getDouble("monto"),
                        rs.getDouble("propina")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar pagos por empleado
    public List<Pagos> listarPorEmpleado(String empleadoId) {
        List<Pagos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE empleado_id = ? ORDER BY fecha_hora_pago DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, empleadoId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Pagos(
                        rs.getString("pago_id"),
                        rs.getString("pedido_id"),
                        rs.getString("empleado_id"),
                        rs.getString("metodo_pago"),
                        rs.getString("fecha_hora_pago"),
                        rs.getDouble("monto"),
                        rs.getDouble("propina")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Listar pagos por rango de fechas
    public List<Pagos> listarPorRangoFechas(String fechaInicio, String fechaFin) {
        List<Pagos> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagos WHERE fecha_hora_pago BETWEEN ? AND ? ORDER BY fecha_hora_pago DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, fechaInicio);
            st.setString(2, fechaFin);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                lista.add(new Pagos(
                        rs.getString("pago_id"),
                        rs.getString("pedido_id"),
                        rs.getString("empleado_id"),
                        rs.getString("metodo_pago"),
                        rs.getString("fecha_hora_pago"),
                        rs.getDouble("monto"),
                        rs.getDouble("propina")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Calcular total de ventas por rango de fechas
    public double calcularTotalVentas(String fechaInicio, String fechaFin) {
        String sql = "SELECT SUM(monto) AS total FROM pagos WHERE fecha_hora_pago BETWEEN ? AND ?";
        double total = 0.0;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, fechaInicio);
            st.setString(2, fechaFin);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    // Calcular total de propinas por rango de fechas
    public double calcularTotalPropinas(String fechaInicio, String fechaFin) {
        String sql = "SELECT SUM(propina) AS total FROM pagos WHERE fecha_hora_pago BETWEEN ? AND ?";
        double total = 0.0;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, fechaInicio);
            st.setString(2, fechaFin);
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