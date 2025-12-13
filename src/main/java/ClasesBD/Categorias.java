package ClasesBD;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIAS")
public class Categorias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoria_id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    public Categorias() {
    }

    public Categorias(Integer categoria_id, String nombre, String descripcion) {
        this.categoria_id = categoria_id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(Integer categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}