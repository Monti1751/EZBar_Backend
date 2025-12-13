package ClasesBD;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "INVENTARIO")
public class Inventario {

    public enum TipoMovimiento {
        entrada, salida
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventario_id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Productos producto;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleados empleado;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fecha_movimiento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo_movimiento;

    public Inventario() {
    }

    public Inventario(Integer inventario_id, Productos producto, Empleados empleado, Timestamp fecha_movimiento,
            BigDecimal cantidad, TipoMovimiento tipo_movimiento) {
        this.inventario_id = inventario_id;
        this.producto = producto;
        this.empleado = empleado;
        this.fecha_movimiento = fecha_movimiento;
        this.cantidad = cantidad;
        this.tipo_movimiento = tipo_movimiento;
    }

    public Integer getInventario_id() {
        return inventario_id;
    }

    public void setInventario_id(Integer inventario_id) {
        this.inventario_id = inventario_id;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Timestamp getFecha_movimiento() {
        return fecha_movimiento;
    }

    public void setFecha_movimiento(Timestamp fecha_movimiento) {
        this.fecha_movimiento = fecha_movimiento;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public TipoMovimiento getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(TipoMovimiento tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }
}