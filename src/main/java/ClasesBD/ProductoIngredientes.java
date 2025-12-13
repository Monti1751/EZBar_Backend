package ClasesBD;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTO_INGREDIENTES")
@IdClass(ProductoIngredientesId.class)
public class ProductoIngredientes {

    @Id
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Productos producto;

    @Id
    @ManyToOne
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Productos ingrediente;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(length = 20)
    private String unidad_medida;

    public ProductoIngredientes() {
    }

    public ProductoIngredientes(Productos producto, Productos ingrediente, BigDecimal cantidad, String unidad_medida) {
        this.producto = producto;
        this.ingrediente = ingrediente;
        this.cantidad = cantidad;
        this.unidad_medida = unidad_medida;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public Productos getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Productos ingrediente) {
        this.ingrediente = ingrediente;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }
}