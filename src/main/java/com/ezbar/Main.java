package com.ezbar;

import java.sql.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import Controladores.*;
import ClasesBD.*;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ezbar"})
public class Main {

    // Controladores expuestos para que los RestControllers los usen
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

    // Getters para uso por los RestControllers
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

    // Método main para ejecutar como aplicación Spring Boot
    public static void main(String[] args) {
        init();
        SpringApplication.run(Main.class, args);
        System.out.println("✓ API REST disponible en http://localhost:8080");
    }
}
