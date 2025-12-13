package ClasesBD;

import jakarta.persistence.*;
import java.sql.Timestamp; // Using Timestamp as requested/fixed in SQL

@Entity
@Table(name = "EMPLEADOS")
public class Empleados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empleado_id;

    @Column(nullable = false, length = 50)
    private String nombre_empleado;

    @Column(nullable = false, length = 50)
    private String apellido_empleado;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre_usuario;

    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    @ManyToOne
    @JoinColumn(name = "puesto_id", nullable = false)
    private Puestos puesto;

    @Column(nullable = false)
    private String password_hash;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fecha_creacion;

    @Column(nullable = false) // active boolean
    private boolean activo = true;

    public Empleados() {
    }

    public Empleados(Integer empleado_id, String nombre_empleado, String apellido_empleado, String nombre_usuario,
            String dni, Puestos puesto, String password_hash, Timestamp fecha_creacion, boolean activo) {
        this.empleado_id = empleado_id;
        this.nombre_empleado = nombre_empleado;
        this.apellido_empleado = apellido_empleado;
        this.nombre_usuario = nombre_usuario;
        this.dni = dni;
        this.puesto = puesto;
        this.password_hash = password_hash;
        this.fecha_creacion = fecha_creacion;
        this.activo = activo;
    }

    public Integer getEmpleado_id() {
        return empleado_id;
    }

    public void setEmpleado_id(Integer empleado_id) {
        this.empleado_id = empleado_id;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public String getApellido_empleado() {
        return apellido_empleado;
    }

    public void setApellido_empleado(String apellido_empleado) {
        this.apellido_empleado = apellido_empleado;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Puestos getPuesto() {
        return puesto;
    }

    public void setPuesto(Puestos puesto) {
        this.puesto = puesto;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
