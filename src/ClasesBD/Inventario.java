package EstructuraBD;

public class Inventario {
    private String inventario_id, producto_id, empleado_id, fecha_movimiento, tipo_movimiento;
    private double cantidad;

    public Inventario(String inventario_id, String producto_id, String empleado_id, String fecha_movimiento, String tipo_movimiento, double cantidad) {
        this.inventario_id = inventario_id;
        this.producto_id = producto_id;
        this.empleado_id = empleado_id;
        this.fecha_movimiento = fecha_movimiento;
        this.cantidad = cantidad;
        this.tipo_movimiento = tipo_movimiento;
    }

    public String getInventario_id() { return inventario_id; }
    public void setInventario_id(String inventario_id) { this.inventario_id = inventario_id; }

    public String getProducto_id() { return producto_id; }
    public void setProducto_id(String producto_id) { this.producto_id = producto_id; }

    public String getEmpleado_id() { return empleado_id; }
    public void setEmpleado_id(String empleado_id) { this.empleado_id = empleado_id; }

    public String getFecha_movimiento() { return fecha_movimiento; }
    public void setFecha_movimiento(String fecha_movimiento) { this.fecha_movimiento = fecha_movimiento; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getTipo_movimiento() { return tipo_movimiento; }
    public void setTipo_movimiento(short movimiento) {
    	switch (movimiento) {
    	case 1 -> this.tipo_movimiento = "entrada";
    	case 2 -> this.tipo_movimiento = "salida";
    	default -> System.out.println("Error: tipo de movimiento no válido");}
    }
    
}