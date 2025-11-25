package EstructuraBD;

public class Puestos {
    private String puesto_id, nombre_puesto;

    public Puestos(String puesto_id, String nombre_puesto) {
        this.puesto_id = puesto_id;
        this.nombre_puesto = nombre_puesto;
    }

    public String getPuesto_id() { return puesto_id; }
    public void setPuesto_id(String puesto_id) { this.puesto_id = puesto_id; }

    public String getNombre_puesto() { return nombre_puesto; }
    public void setNombre_puesto(String nombre_puesto) { this.nombre_puesto = nombre_puesto; }
}