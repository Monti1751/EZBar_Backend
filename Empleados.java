package EstructuraBD;

public class Empleados {
    private String empleado_id, nombre_empleado, apellido_empleado, nombre_usuario, dni, puesto_id, password_hash, fecha_creacion;
    private boolean activo;

    public Empleados(String empleado_id, String nombre_empleado, String apellido_empleado, String nombre_usuario,
                     String dni, String puesto_id, String password_hash, String fecha_creacion, boolean activo) {
        this.empleado_id = empleado_id;
        this.nombre_empleado = nombre_empleado;
        this.apellido_empleado = apellido_empleado;
        this.nombre_usuario = nombre_usuario;
        this.dni = dni;
        this.puesto_id = puesto_id;
        this.password_hash = password_hash;
        this.fecha_creacion = fecha_creacion;
        this.activo = activo;
    }

    // Getters y Setters
    public String getEmpleado_id() { return empleado_id; }
    public void setEmpleado_id(String empleado_id) { this.empleado_id = empleado_id; }

    public String getNombre_empleado() { return nombre_empleado; }
    public void setNombre_empleado(String nombre_empleado) { this.nombre_empleado = nombre_empleado; }

    public String getApellido_empleado() { return apellido_empleado; }
    public void setApellido_empleado(String apellido_empleado) { this.apellido_empleado = apellido_empleado; }

    public String getNombre_usuario() { return nombre_usuario; }
    public void setNombre_usuario(String nombre_usuario) { this.nombre_usuario = nombre_usuario; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getPuesto_id() { return puesto_id; }
    public void setPuesto_id(String puesto_id) { this.puesto_id = puesto_id; }

    public String getPassword_hash() { return password_hash; }
    public void setPassword_hash(String password_hash) { this.password_hash = password_hash; }

    public String getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(String fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
