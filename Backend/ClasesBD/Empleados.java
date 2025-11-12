package EstructuraBD;

public class Empleados {
	private String empleado_id, nombre_empleado, apellido_empleado, dni, email_empleado, puesto, password, fecha_creacion;
	private long telefono_empleado;
	private boolean activo;
	
	public Empleados(String empleado_id, String nombre, String apellido, String dni, String email, String puesto, String password, String fecha_creacion, long telefono, boolean activo) {
		this.empleado_id = empleado_id;
		this.nombre_empleado = nombre;
		this.apellido_empleado = apellido;
		this.dni = dni;
		this.email_empleado = email;
		this.puesto = puesto;
		this.password = password;
		this.fecha_creacion = fecha_creacion;
		this.telefono_empleado = telefono;
		this.activo = activo;
	}
	
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public long getTelefonoE() {
		return telefono_empleado;
	}
	
	public void setTelefonoE(long telefono) {
		this.telefono_empleado = telefono;
	}
	
	public String getNombreE() {
		return nombre_empleado;
	}
	
	public void setNombreE(String nombre) {
		this.nombre_empleado = nombre;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPuesto() {
		return puesto;
	}
	
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	
	public String getEmpleado_id() {
		return empleado_id;
	}
	public void setEmpleado_id(String empleado_id) {
		this.empleado_id = empleado_id;
	}
	
	public String getEmail() {
		return email_empleado;
	}
	
	public void setEmail(String email) {
		this.email_empleado = email;
	}
	
	public String getFecha_creacion() {
		return fecha_creacion;
	}
	
	public void setFecha_creacion(String fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	
	public String getApellidoE() {
		return apellido_empleado;
	}
	
	public void setApellidoE(String apellido) {
		this.apellido_empleado = apellido;
	}
	
	public boolean isActivo() {
		return activo;
	}
	
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}
