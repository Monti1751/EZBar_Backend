package ClasesBD;

import java.io.Serializable;
import java.util.Objects;

public class ProductoIngredientesId implements Serializable {
    private Integer producto;
    private Integer ingrediente;

    public ProductoIngredientesId() {
    }

    public ProductoIngredientesId(Integer producto, Integer ingrediente) {
        this.producto = producto;
        this.ingrediente = ingrediente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductoIngredientesId that = (ProductoIngredientesId) o;
        return Objects.equals(producto, that.producto) && Objects.equals(ingrediente, that.ingrediente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto, ingrediente);
    }
}
