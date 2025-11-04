package EstructuraBD;

public class Categoria_Productos {
	private String categoria_id, nombre;
	private boolean activa;
	
	public Categoria_Productos (String categoria_id, String nombre, boolean activa) {
		this.activa = activa;
		this.categoria_id = categoria_id;
		this.nombre = nombre;
	}
	
	public String getCategoria_id() {
		return categoria_id;
	}
	
	public void setCategoria_id(String categoria_id) {
		this.categoria_id = categoria_id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public boolean isActiva() {
		return activa;
	}
	
	public void setActiva(boolean activa) {
		this.activa = activa;
	}
}
