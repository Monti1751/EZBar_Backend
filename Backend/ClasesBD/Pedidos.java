package EstructuraBD;

public class Pedidos {
	private String pedido_id, mesa_id, empleado_id, estado, fecha_hora_apertura, fecha_hora_cierre, observaciones;
	private short numero_comensales;
	private double total_pedido;
	
    public Pedidos(String pedido_id, String mesa_id, String empleado_id, String estado, String fecha_hora_apertura, String fecha_hora_cierre, String observaciones, short numero_comensales, double total_pedido) {
    	this.pedido_id = pedido_id;
    	this.mesa_id = mesa_id;
    	this.empleado_id = empleado_id;
    	this.estado = estado;
    	this.fecha_hora_apertura = fecha_hora_apertura;
    	this.fecha_hora_cierre = fecha_hora_cierre;
    	this.observaciones = observaciones;
    	this.numero_comensales = numero_comensales;
    	this.total_pedido = total_pedido;
    }

	
	public double getTotal_pedido() {
		return total_pedido;
	}
	
	public void setTotal_pedido(double total_pedido) {
		this.total_pedido = total_pedido;
	}
	
	public String getMesa_id() {
		return mesa_id;
	}
	
	public void setMesa_id(String mesa_id) {
		this.mesa_id = mesa_id;
	}
	
	public String getPedido_id() {
		return pedido_id;
	}
	
	public void setPedido_id(String pedido_id) {
		this.pedido_id = pedido_id;
	}
	
	public String getEmpleado_id() {
		return empleado_id;
	}
	
	public void setEmpleado_id(String empleado_id) {
		this.empleado_id = empleado_id;
	}
	
	public String getFecha_hora_cierre() {
		return fecha_hora_cierre;
	}
	
	public void setFecha_hora_cierre(String fecha_hora_cierre) {
		this.fecha_hora_cierre = fecha_hora_cierre;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getFecha_hora_apertura() {
		return fecha_hora_apertura;
	}
	
	public void setFecha_hora_apertura(String fecha_hora_apertura) {
		this.fecha_hora_apertura = fecha_hora_apertura;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public short getNumero_comensales() {
		return numero_comensales;
	}
	
	public void setNumero_comensales(short numero_comensales) {
		this.numero_comensales = numero_comensales;
	}
}
