package EstructuraBD;

public class Mesas {
	private String mesa_id, ubicacion, estado;
	private short numero_mesa, capacidad;
	private boolean activa;
	
	public Mesas(String mesa_id, String ubicacion, String estado, short numero_mesa, short capacidad, boolean activa) {
		this.mesa_id = mesa_id;
		this.ubicacion = ubicacion;
		this.estado = estado;
		this.numero_mesa = numero_mesa;
		this.capacidad = capacidad;
		this.activa = activa;
		
	}
	
	public String getUbicacion() {
		return ubicacion;
	}
	
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	
	public String getMesa_id() {
		return mesa_id;
	}
	
	public void setMesa_id(String mesa_id) {
		this.mesa_id = mesa_id;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(short disposicion) {
		
		// 1 = libre, 2 = reservado, 3 = ocupado
		if (disposicion == 1) {
			this.estado = "libre";	
		}
		
		else if(disposicion == 2) {
			this.estado = "reservado";
		}
		
		else if(disposicion == 3) {
			this.estado = "ocupado";
		}
		
		else {System.out.println("Error estado mesa");}
	}
	
	public boolean isActiva() {
		return activa;
	}
	
	public void setActiva(boolean activa) {
		this.activa = activa;
	}
	
	public short getNumero_mesa() {
		return numero_mesa;
	}
	
	public void setNumero_mesa(short numero_mesa) {
		this.numero_mesa = numero_mesa;
	}
	
	public short getCapacidad() {
		return capacidad;
	}
	
	public void setCapacidad(short capacidad) {
		this.capacidad = capacidad;
	}
}
