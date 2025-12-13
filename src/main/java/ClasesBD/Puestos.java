package ClasesBD;

import jakarta.persistence.*;

@Entity
@Table(name = "PUESTOS")
public class Puestos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer puesto_id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre_puesto;

    public Puestos() {
    }

    public Puestos(Integer puesto_id, String nombre_puesto) {
        this.puesto_id = puesto_id;
        this.nombre_puesto = nombre_puesto;
    }

    public Integer getPuesto_id() {
        return puesto_id;
    }

    public void setPuesto_id(Integer puesto_id) {
        this.puesto_id = puesto_id;
    }

    public String getNombre_puesto() {
        return nombre_puesto;
    }

    public void setNombre_puesto(String nombre_puesto) {
        this.nombre_puesto = nombre_puesto;
    }
}