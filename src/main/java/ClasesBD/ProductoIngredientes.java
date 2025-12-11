package ClasesBD;

public class ProductoIngredientes {
    private String producto_id, ingrediente_id, unidad_medida;
    private double cantidad;

    public ProductoIngredientes(String producto_id, String ingrediente_id, String unidad_medida, double cantidad) {
        this.producto_id = producto_id;
        this.ingrediente_id = ingrediente_id;
        this.cantidad = cantidad;
        this.unidad_medida = unidad_medida;
    }

    public String getProducto_id() { return producto_id; }
    public void setProducto_id(String producto_id) { this.producto_id = producto_id; }

    public String getIngrediente_id() { return ingrediente_id; }
    public void setIngrediente_id(String ingrediente_id) { this.ingrediente_id = ingrediente_id; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getUnidad_medida() { return unidad_medida; }
    public void setUnidad_medida(String unidad_medida) { this.unidad_medida = unidad_medida; }
}