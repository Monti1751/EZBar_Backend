package ClasesBD;

public class Productos {
    private String producto_id, categoria_id, nombre, descripcion, unidad_medida, url_imagen;
    private double precio, costo, iva_porcentaje;
    private int stock_actual, stock_minimo;
    private boolean es_ingrediente, activo;

    public Productos(String producto_id, String categoria_id, String nombre, String descripcion, String unidad_medida, double precio, double costo, double iva_porcentaje, int stock_actual, int stock_minimo, boolean es_ingrediente, boolean activo, String url_imagen) {
        this.producto_id = producto_id;
        this.categoria_id = categoria_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.costo = costo;
        this.iva_porcentaje = iva_porcentaje;
        this.stock_actual = stock_actual;
        this.stock_minimo = stock_minimo;
        this.unidad_medida = unidad_medida;
        this.es_ingrediente = es_ingrediente;
        this.activo = activo;
        this.url_imagen = url_imagen;
    }

    public String getProducto_id() { return producto_id; }
    public void setProducto_id(String producto_id) { this.producto_id = producto_id; }

    public String getCategoria_id() { return categoria_id; }
    public void setCategoria_id(String categoria_id) { this.categoria_id = categoria_id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public double getIva_porcentaje() { return iva_porcentaje; }
    public void setIva_porcentaje(double iva_porcentaje) { this.iva_porcentaje = iva_porcentaje; }

    public int getStock_actual() { return stock_actual; }
    public void setStock_actual(int stock_actual) { this.stock_actual = stock_actual; }

    public int getStock_minimo() { return stock_minimo; }
    public void setStock_minimo(int stock_minimo) { this.stock_minimo = stock_minimo; }

    public String getUnidad_medida() { return unidad_medida; }
    public void setUnidad_medida(String unidad_medida) { this.unidad_medida = unidad_medida; }

    public boolean isEs_ingrediente() { return es_ingrediente; }
    public void setEs_ingrediente(boolean es_ingrediente) { this.es_ingrediente = es_ingrediente; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getUrl_imagen() { return url_imagen; }
    public void setUrl_imagen(String url_imagen) { this.url_imagen = url_imagen; }
}