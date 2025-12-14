package ClasesBD;

import jakarta.persistence.*;

@Entity
@Table(name = "ZONAS")
public class Zonas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer zona_id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public Zonas() {
    }

    public Zonas(Integer zona_id, String nombre) {
        this.zona_id = zona_id;
        this.nombre = nombre;
    }

    public Integer getZona_id() {
        return zona_id;
    }

    public void setZona_id(Integer zona_id) {
        this.zona_id = zona_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
