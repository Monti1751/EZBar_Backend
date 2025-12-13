package ClasesBD;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "PAGOS")
public class Pagos {

    public enum MetodoPago {
        efectivo, tarjeta, transferencia
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pago_id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedidos pedido;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleados empleado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodo_pago;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(precision = 10, scale = 2) // Default 0
    private BigDecimal propina = BigDecimal.ZERO;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fecha_hora_pago;

    public Pagos() {
    }

    public Pagos(Integer pago_id, Pedidos pedido, Empleados empleado, MetodoPago metodo_pago, BigDecimal monto,
            BigDecimal propina, Timestamp fecha_hora_pago) {
        this.pago_id = pago_id;
        this.pedido = pedido;
        this.empleado = empleado;
        this.metodo_pago = metodo_pago;
        this.monto = monto;
        this.propina = propina;
        this.fecha_hora_pago = fecha_hora_pago;
    }

    public Integer getPago_id() {
        return pago_id;
    }

    public void setPago_id(Integer pago_id) {
        this.pago_id = pago_id;
    }

    public Pedidos getPedido() {
        return pedido;
    }

    public void setPedido(Pedidos pedido) {
        this.pedido = pedido;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public MetodoPago getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(MetodoPago metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getPropina() {
        return propina;
    }

    public void setPropina(BigDecimal propina) {
        this.propina = propina;
    }

    public Timestamp getFecha_hora_pago() {
        return fecha_hora_pago;
    }

    public void setFecha_hora_pago(Timestamp fecha_hora_pago) {
        this.fecha_hora_pago = fecha_hora_pago;
    }
}