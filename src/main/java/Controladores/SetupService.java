package Controladores;

import Repositorios.CategoriasRepository;
import Repositorios.ProductosRepository;
import Repositorios.ZonasRepository;
import Repositorios.PuestosRepository;
import Repositorios.EmpleadosRepository;
import ClasesBD.Categorias;
import ClasesBD.Productos;
import ClasesBD.Zonas;
import ClasesBD.Puestos;
import ClasesBD.Empleados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

/**
 * Servicio de inicialización de datos.
 * 
 * Responsabilidad: Contener toda la lógica de negocio para popular la base de
 * datos
 * con datos de prueba iniciales. Esto separa la lógica de negocio del
 * controlador REST.
 * 
 * Beneficios de esta arquitectura:
 * - Single Responsibility: El controller solo maneja HTTP, el service maneja
 * lógica
 * - Reutilizable: El service puede ser llamado desde otros contextos (CLI,
 * scheduling, etc.)
 * - Testeable: Es fácil hacer unit tests de la lógica sin involucrar HTTP
 * - Transaccional: @Transactional asegura consistencia de la base de datos
 * 
 * @Service Indica que es un componente de lógica de negocio de Spring
 */
@Service
public class SetupService {

    private final CategoriasRepository categoriasRepository;
    private final ProductosRepository productosRepository;
    private final ZonasRepository zonasRepository;
    private final PuestosRepository puestosRepository;
    private final EmpleadosRepository empleadosRepository;

    /**
     * Constructor con inyección de dependencias por constructor.
     * Preferible a @Autowired field injection para testing y desacoplamiento.
     * 
     * @param categoriasRepository Repositorio de categorías
     * @param productosRepository  Repositorio de productos
     * @param zonasRepository      Repositorio de zonas
     * @param puestosRepository    Repositorio de puestos
     * @param empleadosRepository  Repositorio de empleados
     */
    @Autowired
    public SetupService(
            CategoriasRepository categoriasRepository,
            ProductosRepository productosRepository,
            ZonasRepository zonasRepository,
            PuestosRepository puestosRepository,
            EmpleadosRepository empleadosRepository) {

        this.categoriasRepository = categoriasRepository;
        this.productosRepository = productosRepository;
        this.zonasRepository = zonasRepository;
        this.puestosRepository = puestosRepository;
        this.empleadosRepository = empleadosRepository;
    }

    /**
     * Inicializa la base de datos con datos de prueba.
     * 
     * La anotación @Transactional garantiza que:
     * - O se ejecutan TODAS las operaciones o NINGUNA
     * - Si ocurre una excepción, se revierte todo (rollback)
     * 
     * El orden es importante debido a las dependencias:
     * 1. Categorías (sin dependencias)
     * 2. Productos (dependen de categorías)
     * 3. Zonas (sin dependencias)
     * 4. Puestos (sin dependencias)
     * 5. Empleados (dependen de puestos)
     * 
     * @return Mensaje de éxito describiendo qué fue inicializado
     * @throws Exception Si ocurre algún error durante la inicialización
     */
    @Transactional
    public String initializeDatabaseWithTestData() throws Exception {
        initializeCategories();
        initializeProducts();
        initializeZones();
        initializePositionsAndEmployees();

        return "Database seeded successfully! (Categories, Products, Zones, Positions, Employees)";
    }

    /**
     * Inicializa las categorías de productos si la tabla está vacía.
     * 
     * Ejemplo de datos: Bebidas, Tapas, Comidas, etc.
     */
    private void initializeCategories() {
        if (categoriasRepository.count() == 0) {
            Categorias beverages = new Categorias(null, "Bebidas", "Refrescos y alcohol");
            Categorias appetizers = new Categorias(null, "Tapas", "Para picar");

            categoriasRepository.save(beverages);
            categoriasRepository.save(appetizers);
        }
    }

    /**
     * Inicializa los productos si la tabla está vacía.
     * 
     * Los productos dependen de que las categorías existan previamente.
     */
    @SuppressWarnings("null")
    private void initializeProducts() {
        if (productosRepository.count() == 0) {
            Categorias beverages = categoriasRepository.findAll().stream()
                    .filter(c -> "Bebidas".equals(c.getNombre()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Beverages category not found"));

            Categorias appetizers = categoriasRepository.findAll().stream()
                    .filter(c -> "Tapas".equals(c.getNombre()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Appetizers category not found"));

            Productos cocaCola = createProduct(beverages, "Coca Cola", "Refresco",
                    new BigDecimal("2.50"), 100, 10, "unid");
            Productos beer = createProduct(beverages, "Cerveza", "Caña",
                    new BigDecimal("1.80"), 200, 20, "unid");
            Productos frenchFries = createProduct(appetizers, "Patatas Bravas", "Picantes",
                    new BigDecimal("4.50"), 50, 5, "racion");

            productosRepository.save(cocaCola);
            productosRepository.save(beer);
            productosRepository.save(frenchFries);
        }
    }

    /**
     * Inicializa las zonas (áreas del restaurante).
     * 
     * @see #ensureZoneExists(String)
     */
    private void initializeZones() {
        ensureZoneExists("Terraza");
        ensureZoneExists("Interior");
    }

    /**
     * Inicializa los puestos de trabajo y el empleado administrador.
     * 
     * Los empleados dependen de que los puestos existan previamente.
     */
    private void initializePositionsAndEmployees() {
        if (puestosRepository.count() == 0) {
            Puestos adminPosition = new Puestos(null, "Administrador");
            puestosRepository.save(adminPosition);

            Empleados adminUser = new Empleados(
                    null,
                    "Admin",
                    "User",
                    "admin",
                    "00000000X",
                    adminPosition,
                    "hashed_password_placeholder", // En producción, usar hash seguro
                    null,
                    true);

            empleadosRepository.save(adminUser);
        }
    }

    /**
     * Helper: Asegura que una zona con el nombre dado existe en la base de datos.
     * Si no existe, la crea automáticamente.
     * 
     * @param nombreZona Nombre de la zona a crear (ej: "Terraza", "Interior")
     */
    private void ensureZoneExists(String nombreZona) {
        if (zonasRepository.findByNombre(nombreZona).isEmpty()) {
            Zonas newZone = new Zonas(null, nombreZona);
            zonasRepository.save(newZone);
        }
    }

    /**
     * Helper: Factory method para crear un Producto con valores por defecto.
     * Esto centraliza la lógica de creación y facilita cambios futuros.
     * 
     * @param categoria    Categoría del producto
     * @param nombre       Nombre del producto
     * @param descripcion  Descripción breve
     * @param precio       Precio de venta
     * @param stockActual  Stock inicial
     * @param stockMinimo  Nivel mínimo de stock
     * @param unidadMedida Unidad (ej: "unid", "gramos", "ml")
     * @return Producto listo para guardar
     */
    private Productos createProduct(
            Categorias categoria,
            String nombre,
            String descripcion,
            BigDecimal precio,
            Integer stockActual,
            Integer stockMinimo,
            String unidadMedida) {

        return new Productos(
                null, // ID será generado por la BD
                categoria,
                nombre,
                descripcion,
                precio,
                null, // costo (opcional)
                BigDecimal.ZERO, // IVA por defecto 0%
                stockActual,
                stockMinimo,
                unidadMedida,
                false, // no es ingrediente
                true, // está activo
                null); // sin imagen
    }
}
