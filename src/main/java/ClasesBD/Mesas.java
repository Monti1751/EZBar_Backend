package ClasesBD;

import jakarta.persistence.*;

@Entity
@Table(name = "MESAS")
public class Mesas {

    public enum Estado {
        libre, ocupada, reservada
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mesa_id;

    @Column(nullable = false, unique = true)
    private Integer numero_mesa;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('libre','ocupada','reservada') DEFAULT 'libre'")
    private Estado estado = Estado.libre;

    public Mesas() {
    }

    public Mesas(Integer mesa_id, Integer numero_mesa, Integer capacidad, String ubicacion, Estado estado) {
        this.mesa_id = mesa_id;
        this.numero_mesa = numero_mesa;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.estado = estado;
    }

    public Integer getMesa_id() {
        return mesa_id;
    }

    public void setMesa_id(Integer mesa_id) {
        this.mesa_id = mesa_id;
    }

    public Integer getNumero_mesa() {
        return numero_mesa;
    }

    public void setNumero_mesa(Integer numero_mesa) {
        this.numero_mesa = numero_mesa;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
