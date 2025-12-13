package ClasesBD;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "PEDIDOS")
public class Pedidos {

    public enum Estado {
        pendiente, en_preparacion, listo, entregado, cancelado, pagado
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pedido_id;

    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesas mesa;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleados empleado;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('pendiente','en_preparacion','listo','entregado','cancelado','pagado') DEFAULT 'pendiente'")
    private Estado estado = Estado.pendiente;

    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer numero_comensales = 1;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0", precision = 10, scale = 2)
    private BigDecimal total_pedido = BigDecimal.ZERO;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fecha_hora_pedido;

    public Pedidos() {
    }

    public Pedidos(Integer pedido_id, Mesas mesa, Empleados empleado, Estado estado, Integer numero_comensales,
            String observaciones, BigDecimal total_pedido, Timestamp fecha_hora_pedido) {
        this.pedido_id = pedido_id;
        this.mesa = mesa;
        this.empleado = empleado;
        this.estado = estado;
        this.numero_comensales = numero_comensales;
        this.observaciones = observaciones;
        this.total_pedido = total_pedido;
        this.fecha_hora_pedido = fecha_hora_pedido;
    }

    public Integer getPedido_id() {
        return pedido_id;
    }

    public void setPedido_id(Integer pedido_id) {
        this.pedido_id = pedido_id;
    }

    public Mesas getMesa() {
        return mesa;
    }

    public void setMesa(Mesas mesa) {
        this.mesa = mesa;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Integer getNumero_comensales() {
        return numero_comensales;
    }

    public void setNumero_comensales(Integer numero_comensales) {
        this.numero_comensales = numero_comensales;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getTotal_pedido() {
        return total_pedido;
    }

    public void setTotal_pedido(BigDecimal total_pedido) {
        this.total_pedido = total_pedido;
    }

    public Timestamp getFecha_hora_pedido() {
        return fecha_hora_pedido;
    }

    public void setFecha_hora_pedido(Timestamp fecha_hora_pedido) {
        this.fecha_hora_pedido = fecha_hora_pedido;
    }
}