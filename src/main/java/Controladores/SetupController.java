package Controladores;

import Repositorios.CategoriasRepository;
import Repositorios.MesasRepository;
import Repositorios.ProductosRepository;
import ClasesBD.Categorias;
import ClasesBD.Mesas;
import ClasesBD.Productos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

@RestController
@RequestMapping("/setup")
public class SetupController {

    @Autowired
    private MesasRepository mesasRepo;
    @Autowired
    private CategoriasRepository categoriasRepo;
    @Autowired
    private ProductosRepository productosRepo;
    @Autowired
    private Repositorios.ZonasRepository zonasRepo;
    @Autowired
    private Repositorios.PuestosRepository puestosRepo;
    @Autowired
    private Repositorios.EmpleadosRepository empleadosRepo;

    @GetMapping
    public String setupData() {
        try {
            // 1. Categorias
            if (categoriasRepo.count() == 0) {
                Categorias bebidas = new Categorias(null, "Bebidas", "Refrescos y alcohol");
                Categorias tapas = new Categorias(null, "Tapas", "Para picar");
                categoriasRepo.save(bebidas);
                categoriasRepo.save(tapas);

                // 2. Productos
                productosRepo.save(new Productos(null, bebidas, "Coca Cola", "Refresco", new BigDecimal("2.50"), null,
                        null, 100, 10, "unid", false, true, null));
                productosRepo.save(new Productos(null, bebidas, "Cerveza", "Caña", new BigDecimal("1.80"), null, null,
                        200, 20, "unid", false, true, null));
                productosRepo.save(new Productos(null, tapas, "Patatas Bravas", "Picantes", new BigDecimal("4.50"),
                        null, null, 50, 5, "racion", false, true, null));
            }

            // 3. Zonas (Ensure specifc zones exist)
            ensureZona("Terraza");
            ensureZona("Interior");
            zonasRepo.flush();

            // 4. Puestos y Empleados (Fix for 500 error)
            if (puestosRepo.count() == 0) {
                ClasesBD.Puestos puestoAdmin = new ClasesBD.Puestos(null, "Administrador");
                puestosRepo.save(puestoAdmin);

                // Empleado Admin (dummy password for now)
                ClasesBD.Empleados admin = new ClasesBD.Empleados(
                        null,
                        "Admin",
                        "User",
                        "admin",
                        "00000000X",
                        puestoAdmin,
                        "hashed_password",
                        null,
                        true);
                empleadosRepo.save(admin);
            }

            // 5. Mesas (Disabled to avoid FK issues. User adds manually.)
            /*
             * if (mesasRepo.count() == 0) { ... }
             */

            return "Database seeded successfully! (Categories, Products, Zones, Puestos, Empleados)";
        } catch (Exception e) {
            return "Error seeding database: " + e.getMessage();
        }
    }

    private void ensureZona(String nombre) {
        if (zonasRepo.findByNombre(nombre).isEmpty()) {
            zonasRepo.save(new ClasesBD.Zonas(null, nombre));
        }
    }
}
