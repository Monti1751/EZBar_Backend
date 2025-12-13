package ClasesBD;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_PEDIDOS")
public class DetallePedidos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detalle_id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedidos pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Productos producto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad = BigDecimal.ONE;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_unitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total_linea;

    public DetallePedidos() {
    }

    public DetallePedidos(Integer detalle_id, Pedidos pedido, Productos producto, BigDecimal cantidad,
            BigDecimal precio_unitario, BigDecimal total_linea) {
        this.detalle_id = detalle_id;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.total_linea = total_linea;
    }

    public Integer getDetalle_id() {
        return detalle_id;
    }

    public void setDetalle_id(Integer detalle_id) {
        this.detalle_id = detalle_id;
    }

    public Pedidos getPedido() {
        return pedido;
    }

    public void setPedido(Pedidos pedido) {
        this.pedido = pedido;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public BigDecimal getTotal_linea() {
        return total_linea;
    }

    public void setTotal_linea(BigDecimal total_linea) {
        this.total_linea = total_linea;
    }
}