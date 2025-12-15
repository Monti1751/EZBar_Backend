package Controladores;

import Repositorios.PedidosRepository;
import ClasesBD.Pedidos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidosRestController {

    @Autowired
    private PedidosRepository repository;

    @GetMapping
    public List<Pedidos> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Pedidos obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Pedidos crear(@RequestBody Pedidos pedido) {
        return repository.save(pedido);
    }

    @PutMapping("/{id}")
    public Pedidos actualizar(@PathVariable Integer id, @RequestBody Pedidos pedido) {
        if (repository.existsById(id)) {
            pedido.setPedido_id(id);
            return repository.save(pedido);
        }
        return null;
    }

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

    @PostMapping("/mesa/{mesaId}/agregar-producto")
    public Pedidos agregarProductoAMesa(@PathVariable Integer mesaId,
            @RequestBody java.util.Map<String, Integer> payload) {
        Integer productoId = payload.get("productoId");

        // 1. Validar Mesa y Producto
        ClasesBD.Mesas mesa = mesasRepository.findById(mesaId)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        ClasesBD.Productos producto = productosRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // 2. Buscar pedido activo (no pagado ni cancelado)
        List<Pedidos> pedidosMesa = repository.findAll().stream()
                .filter(p -> p.getMesa().getMesa_id().equals(mesaId))
                .filter(p -> p.getEstado() != ClasesBD.Pedidos.Estado.pagado
                        && p.getEstado() != ClasesBD.Pedidos.Estado.cancelado)
                .collect(java.util.stream.Collectors.toList());

        Pedidos pedidoActual;

        if (pedidosMesa.isEmpty()) {
            // Crear nuevo pedido
            pedidoActual = new Pedidos();
            pedidoActual.setMesa(mesa);
            // Asignar Empleado por defecto (ID 1) o el primero que pille
            // Asignar Empleado: buscar ID 1, si no el primero de la lista, si no crear uno
            // nuevo
            ClasesBD.Empleados empleado = empleadosRepository.findById(1).orElse(null);
            if (empleado == null) {
                List<ClasesBD.Empleados> todos = empleadosRepository.findAll();
                if (!todos.isEmpty()) {
                    empleado = todos.get(0);
                } else {
                    // 1. Obtener o crear puesto
                    ClasesBD.Puestos puesto;
                    List<ClasesBD.Puestos> puestos = puestosRepository.findAll();
                    if (puestos.isEmpty()) {
                        puesto = new ClasesBD.Puestos();
                        puesto.setNombre_puesto("Camarero");
                        puesto = puestosRepository.save(puesto);
                    } else {
                        puesto = puestos.get(0);
                    }

                    // 2. Crear Empleado con todos los campos obligatorios
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
            pedidoActual.setNumero_comensales(1);
            pedidoActual.setTotal_pedido(java.math.BigDecimal.ZERO);
            pedidoActual = repository.save(pedidoActual);

            // Marcar mesa como ocupada si estaba libre
            if (mesa.getEstado() == ClasesBD.Mesas.Estado.libre) {
                mesa.setEstado(ClasesBD.Mesas.Estado.ocupada);
                mesasRepository.save(mesa);
            }
        } else {
            pedidoActual = pedidosMesa.get(0);
        }

        // 3. Crear Detalle
        ClasesBD.DetallePedidos detalle = new ClasesBD.DetallePedidos();
        detalle.setPedido(pedidoActual);
        detalle.setProducto(producto);
        detalle.setCantidad(java.math.BigDecimal.ONE); // Por ahora 1 unidad
        detalle.setPrecio_unitario(producto.getPrecio());
        detalle.setTotal_linea(producto.getPrecio()); // 1 * precio

        detallePedidosRepository.save(detalle);

        // 4. Actualizar Total Pedido
        java.math.BigDecimal nuevoTotal = pedidoActual.getTotal_pedido() == null
                ? producto.getPrecio()
                : pedidoActual.getTotal_pedido().add(producto.getPrecio());
        pedidoActual.setTotal_pedido(nuevoTotal);

        return repository.save(pedidoActual);
    }

    @GetMapping("/{id}/detalles")
    public List<ClasesBD.DetallePedidos> obtenerDetalles(@PathVariable Integer id) {
        Pedidos pedido = repository.findById(id).orElse(null);
        if (pedido == null)
            return java.util.Collections.emptyList();
        return detallePedidosRepository.findByPedido(pedido);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    @DeleteMapping("/detalles/{id}")
    public void eliminarDetalle(@PathVariable Integer id) {
        ClasesBD.DetallePedidos detalle = detallePedidosRepository.findById(id).orElse(null);
        if (detalle != null) {
            Pedidos pedido = detalle.getPedido();
            // Update order total
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
