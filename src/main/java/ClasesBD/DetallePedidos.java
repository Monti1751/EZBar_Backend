package ClasesBD;

public class DetallePedidos {
    private String detalle_id, pedido_id, producto_id;
    private double cantidad, precio_unitario, total_linea;

    public DetallePedidos(String detalle_id, String pedido_id, String producto_id, double cantidad, double precio_unitario, double total_linea) {
        this.detalle_id = detalle_id;
        this.pedido_id = pedido_id;
        this.producto_id = producto_id;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.total_linea = total_linea;
    }

    public String getDetalle_id() { return detalle_id; }
    public void setDetalle_id(String detalle_id) { this.detalle_id = detalle_id; }

    public String getPedido_id() { return pedido_id; }
    public void setPedido_id(String pedido_id) { this.pedido_id = pedido_id; }

    public String getProducto_id() { return producto_id; }
    public void setProducto_id(String producto_id) { this.producto_id = producto_id; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getPrecio_unitario() { return precio_unitario; }
    public void setPrecio_unitario(double precio_unitario) { this.precio_unitario = precio_unitario; }

    public double getTotal_linea() { return total_linea; }
    public void setTotal_linea(double total_linea) { this.total_linea = total_linea; }
}