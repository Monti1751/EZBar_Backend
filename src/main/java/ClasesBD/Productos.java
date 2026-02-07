package ClasesBD;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTOS")
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("producto_id")
    private Integer producto_id;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categorias categoria;

    @Column(nullable = false, length = 100)
    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("descripcion")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    @JsonProperty("precio")
    private BigDecimal precio;

    @Column(precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name = "iva_porcentaje", precision = 5, scale = 2) // Default 0 in DB
    private BigDecimal iva_porcentaje = BigDecimal.ZERO;

    @Column(name = "stock_actual")
    private Integer stock_actual = 0;

    @Column(name = "stock_minimo")
    private Integer stock_minimo = 0;

    @Column(length = 20)
    private String unidad_medida;

    @Column(name = "es_ingrediente")
    private boolean es_ingrediente = false;

    private boolean activo = true;

    @JsonProperty("imagenUrl")
    private String url_imagen;

    @Lob
    @Column(name = "imagen", columnDefinition = "LONGBLOB")
    @JsonProperty("imagenBlob")
    private byte[] imagenBlob;

    public Productos() {
    }

    public Productos(Integer producto_id, Categorias categoria, String nombre, String descripcion, BigDecimal precio,
            BigDecimal costo, BigDecimal iva_porcentaje, Integer stock_actual, Integer stock_minimo,
            String unidad_medida, boolean es_ingrediente, boolean activo, String url_imagen) {
        this.producto_id = producto_id;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.costo = costo;
        this.iva_porcentaje = iva_porcentaje;
        this.stock_actual = stock_actual;
        this.stock_minimo = stock_minimo;
        this.unidad_medida = unidad_medida;
        this.es_ingrediente = es_ingrediente;
        this.activo = activo;
        this.url_imagen = url_imagen;
    }

    // Getters and Setters
    public Integer getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Integer producto_id) {
        this.producto_id = producto_id;
    }

    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public BigDecimal getIva_porcentaje() {
        return iva_porcentaje;
    }

    public void setIva_porcentaje(BigDecimal iva_porcentaje) {
        this.iva_porcentaje = iva_porcentaje;
    }

    public Integer getStock_actual() {
        return stock_actual;
    }

    public void setStock_actual(Integer stock_actual) {
        this.stock_actual = stock_actual;
    }

    public Integer getStock_minimo() {
        return stock_minimo;
    }

    public void setStock_minimo(Integer stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public boolean isEs_ingrediente() {
        return es_ingrediente;
    }

    public void setEs_ingrediente(boolean es_ingrediente) {
        this.es_ingrediente = es_ingrediente;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public byte[] getImagenBlob() {
        return imagenBlob;
    }

    public void setImagenBlob(byte[] imagenBlob) {
        this.imagenBlob = imagenBlob;
    }
}