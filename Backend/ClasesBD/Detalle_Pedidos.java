package EstructuraBD;

public class Detalle_Pedidos {
	private String detalle_id, pedido_id, producto_id, observaciones, estado_item;
	private short cantidad;
	
	public Detalle_Pedidos(String detalle_id, String pedido_id, String producto_id, String observaciones, String estado_item, short cantidad) {
		this.detalle_id = detalle_id;
		this.pedido_id = pedido_id;
		this.producto_id = producto_id;
		this.observaciones = observaciones;
		this.estado_item = estado_item;
		this.cantidad = cantidad;
	}
	
	public String getEstado_item() {
		return estado_item;
	}
	
	public void setEstado_item(String estado_item) {
		this.estado_item = estado_item;
	}
	
	public short getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(short cantidad) {
		this.cantidad = cantidad;
	}
	
	public String getProducto_id() {
		return producto_id;
	}
	
	public void setProducto_id(String producto_id) {
		this.producto_id = producto_id;
	}
	
	public String getPedido_id() {
		return pedido_id;
	}
	
	public void setPedido_id(String pedido_id) {
		this.pedido_id = pedido_id;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public String getDetalle_id() {
		return detalle_id;
	}
	
	public void setDetalle_id(String detalle_id) {
		this.detalle_id = detalle_id;
	}
}
