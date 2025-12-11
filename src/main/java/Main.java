import java.sql.Connection;
import Controladores.*;
import ClasesBD.*;

public class Main {

    // Controladores expuestos para que la API los use directamente
    public static ProductosController productosController;
    public static CategoriasController categoriasController;
    public static DetallePedidosController detallePedidosController;
    public static EmpleadosController empleadosController;
    public static InventarioController inventarioController;
    public static MesaController mesaController;
    public static PagosController pagosController;
    public static PedidosController pedidosController;
    public static ProductoIngredientesController productoIngredientesController;
    public static PuestosController puestosController;

    // Inicializa los controladores y comprueba la conexión a la BD
    public static void init() {
        productosController = new ProductosController();
        categoriasController = new CategoriasController();
        detallePedidosController = new DetallePedidosController();
        empleadosController = new EmpleadosController();
        inventarioController = new InventarioController();
        mesaController = new MesaController();
        pagosController = new PagosController();
        pedidosController = new PedidosController();
        productoIngredientesController = new ProductoIngredientesController();
        puestosController = new PuestosController();

        System.out.println("Inicializando Main y comprobando conexión a la base de datos...");

        try (Connection con = Database.getConnection()) {
            if (con != null && !con.isClosed()) {
                System.out.println("Conexión a la base de datos: OK");
            } else {
                System.err.println("No se pudo establecer la conexión a la base de datos (null o cerrada).");
            }
        } catch (Exception e) {
            System.err.println("Excepción comprobando la conexión a la BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getters (opcionales) para uso por la API
    public static ProductosController getProductosController() { return productosController; }
    public static CategoriasController getCategoriasController() { return categoriasController; }
    public static DetallePedidosController getDetallePedidosController() { return detallePedidosController; }
    public static EmpleadosController getEmpleadosController() { return empleadosController; }
    public static InventarioController getInventarioController() { return inventarioController; }
    public static MesaController getMesaController() { return mesaController; }
    public static PagosController getPagosController() { return pagosController; }
    public static PedidosController getPedidosController() { return pedidosController; }
    public static ProductoIngredientesController getProductoIngredientesController() { return productoIngredientesController; }
    public static PuestosController getPuestosController() { return puestosController; }

    // Método main para ejecutar localmente si se desea
    public static void main(String[] args) {
        init();
        System.out.println("Main listo. La API puede acceder a los controladores a través de Main.get...()");
    }
}
