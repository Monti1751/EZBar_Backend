package Controladores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ClasesBD.*;

public class EmpleadosController {

    // INSERTAR
    public void insertar(Empleados p) {
        String sql = "INSERT INTO empleados (empleado_id, nombre_empleado, apellido_empleado, nombre_usuario, dni, puesto_id, password_hash, fecha_creacion, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getEmpleado_id());
            st.setString(2, p.getNombre_empleado());
            st.setString(3, p.getApellido_empleado());
            st.setString(4, p.getNombre_usuario());
            st.setString(5, p.getDni());
            st.setString(6, p.getPuesto_id());
            st.setString(7, p.getPassword_hash());
            st.setString(8, p.getFecha_creacion());
            st.setBoolean(9, p.isActivo());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LISTAR TODOS
    public List<Empleados> listar() {
        List<Empleados> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados";

        try (Connection con = Database.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Empleados(
                        rs.getString("empleado_id"),
                        rs.getString("nombre_empleado"),
                        rs.getString("apellido_empleado"),
                        rs.getString("nombre_usuario"),
                        rs.getString("dni"),
                        rs.getString("puesto_id"),
                        rs.getString("password_hash"),
                        rs.getString("fecha_creacion"),
                        rs.getBoolean("activo")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // BUSCAR POR ID
    public Empleados buscarPorId(String empleadoId) {
        String sql = "SELECT * FROM empleados WHERE empleado_id = ?";
        Empleados p = null;

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, empleadoId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                p = new Empleados(
                        rs.getString("empleado_id"),
                        rs.getString("nombre_empleado"),
                        rs.getString("apellido_empleado"),
                        rs.getString("nombre_usuario"),
                        rs.getString("dni"),
                        rs.getString("puesto_id"),
                        rs.getString("password_hash"),
                        rs.getString("fecha_creacion"),
                        rs.getBoolean("activo")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // ACTUALIZAR
    public void actualizar(Empleados p) {
        String sql = "UPDATE empleados SET nombre_empleado = ?, apellido_empleado = ?, nombre_usuario = ?, dni = ?, puesto_id = ?, password_hash = ?, fecha_creacion = ?, activo = ? WHERE empleado_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getNombre_empleado());
            st.setString(2, p.getApellido_empleado());
            st.setString(3, p.getNombre_usuario());
            st.setString(4, p.getDni());
            st.setString(5, p.getPuesto_id());
            st.setString(6, p.getPassword_hash());
            st.setString(7, p.getFecha_creacion());
            st.setBoolean(8, p.isActivo());
            st.setString(9, p.getEmpleado_id());

            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ELIMINAR
    public void eliminar(String empleadoId) {
        String sql = "DELETE FROM empleados WHERE empleado_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, empleadoId);
            st.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
