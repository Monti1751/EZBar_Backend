package EstructuraBD;

public class Cliente {
	private String cliente_id, nombre_cliente;
	private short telefono_cliente;
	
	public Cliente (String cliente_id, String nombre_cliente, String email_cliente, short telefono_cliente) {
		this.cliente_id = cliente_id;
		this.nombre_cliente = nombre_cliente;
		this.telefono_cliente = telefono_cliente;
	}

	public String getCliente_id() {
		return cliente_id;
	}
	
	public void setCliente_id(String cliente_id) {
		this.cliente_id = cliente_id;
	}
	
	public String getNombreC() {
		return nombre_cliente;
	}
	
	public void setNombreC(String nombre_cliente) {
		this.nombre_cliente = nombre_cliente;
	}
	
	public short getTelefonoC() {
		return telefono_cliente;
	}
	
	public void setTelefonoC(short telefono_cliente) {
		this.telefono_cliente = telefono_cliente;
	}
}
