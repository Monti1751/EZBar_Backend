package ClasesBD;

public class Pedidos {
    private String pedido_id, mesa_id, empleado_id, estado, observaciones, fecha_hora_pedido;
    private int numero_comensales;
    private double total_pedido;

    public Pedidos(String pedido_id, String mesa_id, String empleado_id, String estado, String observaciones, String fecha_hora_pedido, int numero_comensales,  double total_pedido) {
        this.pedido_id = pedido_id;
        this.mesa_id = mesa_id;
        this.empleado_id = empleado_id;
        this.estado = estado;
        this.numero_comensales = numero_comensales;
        this.observaciones = observaciones;
        this.total_pedido = total_pedido;
        this.fecha_hora_pedido = fecha_hora_pedido;
    }

    // Método especial para asignar estado por código
    public void setEstado(short codigo) {
        switch (codigo) {
            case 1 -> this.estado = "pendiente";
            case 2 -> this.estado = "en_preparacion";
            case 3 -> this.estado = "listo";
            case 4 -> this.estado = "entregado";
            case 5 -> this.estado = "cancelado";
            case 6 -> this.estado = "pagado";
            default -> System.out.println("Error estado pedido");
        }
    }

    public String getPedido_id() { return pedido_id; }
    public void setPedido_id(String pedido_id) { this.pedido_id = pedido_id; }

    public String getMesa_id() { return mesa_id; }
    public void setMesa_id(String mesa_id) { this.mesa_id = mesa_id; }

    public String getEmpleado_id() { return empleado_id; }
    public void setEmpleado_id(String empleado_id) { this.empleado_id = empleado_id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getNumero_comensales() { return numero_comensales; }
    public void setNumero_comensales(int numero_comensales) { this.numero_comensales = numero_comensales; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public double getTotal_pedido() { return total_pedido; }
    public void setTotal_pedido(double total_pedido) { this.total_pedido = total_pedido; }

    public String getFecha_hora_pedido() { return fecha_hora_pedido; }
    public void setFecha_hora_pedido(String fecha_hora_pedido) { this.fecha_hora_pedido = fecha_hora_pedido; }
}