package Controladores;

import Repositorios.PedidosRepository;
import ClasesBD.Pedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para gestionar Pedidos
 * Maneja la creación de pedidos, agregar productos a mesas y listar pedidos.
 */
@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidosRestController {

    @Autowired
    private PedidosRepository repository;

    // Listar todos los pedidos (GET /pedidos)
    @GetMapping
    public List<Pedidos> listarTodos() {
        return repository.findAll();
    }

    // Obtener pedido por ID (GET /pedidos/{id})
    @GetMapping("/{id}")
    public Pedidos obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    // Crear un pedido manualmente (POST /pedidos)
    @PostMapping
    public Pedidos crear(@RequestBody Pedidos pedido) {
        return repository.save(pedido);
    }

    // Actualizar un pedido (PUT /pedidos/{id})
    @PutMapping("/{id}")
    public Pedidos actualizar(@PathVariable Integer id, @RequestBody Pedidos pedido) {
        if (repository.existsById(id)) {
            pedido.setPedido_id(id);
            return repository.save(pedido);
        }
        return null;
    }

    // Inyección de repositorios necesarios para la lógica compleja de pedidos
    @Autowired
    private Repositorios.ProductosRepository productosRepository;
    @Autowired
    private Repositorios.MesasRepository mesasRepository;
    @Autowired
    private Repositorios.DetallePedidosRepository detallePedidosRepository;
    @Autowired
    private Repositorios.EmpleadosRepository empleadosRepository;
    @Autowired
    private Repositorios.PuestosRepository puestosRepository;

    /**
     * POST /pedidos/mesa/{mesaId}/agregar-producto
     * Método principal para la operativa diaria: Agrega un producto a la mesa
     * indicada.
     * Si la mesa no tiene pedido abierto, crea uno nuevo automáticamente.
     */
    @PostMapping("/mesa/{mesaId}/agregar-producto")
    public Pedidos agregarProductoAMesa(@PathVariable Integer mesaId,
            @RequestBody java.util.Map<String, Integer> payload) {
        Integer productoId = payload.get("productoId");

        // 1. Validar que la Mesa y el Producto existan en la BD
        ClasesBD.Mesas mesa = mesasRepository.findById(mesaId)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        ClasesBD.Productos producto = productosRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // 2. Buscar si hay algún pedido ACTIVO en esa mesa (PENDIENTE DE PAGO)
        // Filtramos en memoria (se podría optimizar con una query JPQL custom)
        List<Pedidos> pedidosMesa = repository.findAll().stream()
                .filter(p -> p.getMesa().getMesa_id().equals(mesaId))
                .filter(p -> p.getEstado() != ClasesBD.Pedidos.Estado.pagado
                        && p.getEstado() != ClasesBD.Pedidos.Estado.cancelado)
                .collect(java.util.stream.Collectors.toList());

        Pedidos pedidoActual;

        if (pedidosMesa.isEmpty()) {
            // Caso: NO hay pedido abierto -> Crear uno nuevo
            pedidoActual = new Pedidos();
            pedidoActual.setMesa(mesa);

            // Asignar Empleado: intentamos buscar uno por defecto (ID 1)
            // Si no existe, buscamos cualquiera, y si no hay ninguno, creamos un empleado
            // "dummy"
            ClasesBD.Empleados empleado = empleadosRepository.findById(1).orElse(null);
            if (empleado == null) {
                List<ClasesBD.Empleados> todos = empleadosRepository.findAll();
                if (!todos.isEmpty()) {
                    empleado = todos.get(0);
                } else {
                    // Lógica de fallback para entornos vacíos: crear puesto y empleado por defecto
                    ClasesBD.Puestos puesto;
                    List<ClasesBD.Puestos> puestos = puestosRepository.findAll();
                    if (puestos.isEmpty()) {
                        puesto = new ClasesBD.Puestos();
                        puesto.setNombre_puesto("Camarero");
                        puesto = puestosRepository.save(puesto);
                    } else {
                        puesto = puestos.get(0);
                    }

                    empleado = new ClasesBD.Empleados();
                    empleado.setNombre_empleado("Camarero");
                    empleado.setApellido_empleado("Default");
                    empleado.setNombre_usuario("camarero_def"); // unique
                    empleado.setDni("00000000X"); // unique
                    empleado.setPassword_hash("1234");
                    empleado.setActivo(true);
                    empleado.setPuesto(puesto);

                    empleado = empleadosRepository.save(empleado);
                }
            }
            pedidoActual.setEmpleado(empleado);
            pedidoActual.setEstado(ClasesBD.Pedidos.Estado.pendiente);
            pedidoActual.setNumero_comensales(1); // Valor por defecto
            pedidoActual.setTotal_pedido(java.math.BigDecimal.ZERO);
            pedidoActual = repository.save(pedidoActual);

            // Marcar mesa como ocupada automáticamente
            if (mesa.getEstado() == ClasesBD.Mesas.Estado.libre) {
                mesa.setEstado(ClasesBD.Mesas.Estado.ocupada);
                mesasRepository.save(mesa);
            }
        } else {
            // Caso: YA existe pedido abierto -> Usamos el primero encontrado
            pedidoActual = pedidosMesa.get(0);
        }

        // 3. Crear el detalle del pedido (añadir el producto)
        ClasesBD.DetallePedidos detalle = new ClasesBD.DetallePedidos();
        detalle.setPedido(pedidoActual);
        detalle.setProducto(producto);
        detalle.setCantidad(java.math.BigDecimal.ONE); // Añadimos 1 unidad
        detalle.setPrecio_unitario(producto.getPrecio());
        detalle.setTotal_linea(producto.getPrecio()); // Total linea = 1 * precio

        detallePedidosRepository.save(detalle);

        // 4. Actualizar el Total acumulado del Pedido
        java.math.BigDecimal nuevoTotal = pedidoActual.getTotal_pedido() == null
                ? producto.getPrecio()
                : pedidoActual.getTotal_pedido().add(producto.getPrecio());
        pedidoActual.setTotal_pedido(nuevoTotal);

        return repository.save(pedidoActual); // Devolver el pedido actualizado
    }

    // Obtener detalles (productos) de un pedido específico
    @GetMapping("/{id}/detalles")
    public List<ClasesBD.DetallePedidos> obtenerDetalles(@PathVariable Integer id) {
        Pedidos pedido = repository.findById(id).orElse(null);
        if (pedido == null)
            return java.util.Collections.emptyList();
        return detallePedidosRepository.findByPedido(pedido);
    }

    // Eliminar un pedido
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    // Eliminar una línea de detalle (producto de un pedido) y recalcular total
    @DeleteMapping("/detalles/{id}")
    public void eliminarDetalle(@PathVariable Integer id) {
        ClasesBD.DetallePedidos detalle = detallePedidosRepository.findById(id).orElse(null);
        if (detalle != null) {
            Pedidos pedido = detalle.getPedido();
            // Restar el importe de la línea del total del pedido
            if (pedido != null) {
                java.math.BigDecimal total = pedido.getTotal_pedido();
                if (total != null && detalle.getTotal_linea() != null) {
                    pedido.setTotal_pedido(total.subtract(detalle.getTotal_linea()));
                    repository.save(pedido);
                }
            }
            detallePedidosRepository.delete(detalle);
        }
    }
}
