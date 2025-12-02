package EstructuraBD;

public class Categorias {
    private String categoria_id, nombre, descripcion;

    public Categorias(String categoria_id, String nombre, String descripcion) {
        this.categoria_id = categoria_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getCategoria_id() { return categoria_id; }
    public void setCategoria_id(String categoria_id) { this.categoria_id = categoria_id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}