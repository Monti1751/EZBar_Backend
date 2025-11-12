package EstructuraBD;

public class Pagos {
	private String pago_id, pedido_id, metodo_pago, fecha_hora_pago, empleado_id;
	private double monto, propina;
	
	public Pagos (String pago_id, String pedido_id, String metodo_pago, String fecha_hora_pago, String empleado_id, double monto, double propina) {
		this.pago_id = pago_id;
		this.pedido_id = pedido_id;
		this.metodo_pago = metodo_pago;
		this.fecha_hora_pago = fecha_hora_pago;
		this.empleado_id = empleado_id;
		this.monto = monto;
		this.propina = propina;
	}
	
	public String getPedido_id() {
		return pedido_id;
	}
	
	public void setPedido_id(String pedido_id) {
		this.pedido_id = pedido_id;
	}
	
	public double getPropina() {
		return propina;
	}
	
	public void setPropina(double propina) {
		this.propina = propina;
	}
	
	public double getMonto() {
		return monto;
	}
	
	public void setMonto(double monto) {
		this.monto = monto;
	}
	
	public String getFecha_hora_pago() {
		return fecha_hora_pago;
	}
	
	public void setFecha_hora_pago(String fecha_hora_pago) {
		this.fecha_hora_pago = fecha_hora_pago;
	}
	
	public String getMetodo_pago() {
		return metodo_pago;
	}
	
	public void setMetodo_pago(String metodo_pago) {
		this.metodo_pago = metodo_pago;
	}
	
	public String getPago_id() {
		return pago_id;
	}
	
	public void setPago_id(String pago_id) {
		this.pago_id = pago_id;
	}
	
	public String getEmpleado_id() {
		return empleado_id;
	}
	
	public void setEmpleado_id(String empleado_id) {
		this.empleado_id = empleado_id;
	}
}
