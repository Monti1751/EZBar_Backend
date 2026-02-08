package Controladores;

import ClasesBD.*;
import Repositorios.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PedidosRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PedidosRepository repository;
    @Mock
    private ProductosRepository productosRepository;
    @Mock
    private MesasRepository mesasRepository;
    @Mock
    private DetallePedidosRepository detallePedidosRepository;
    @Mock
    private EmpleadosRepository empleadosRepository;
    @Mock
    private PuestosRepository puestosRepository;

    @InjectMocks
    private PedidosRestController pedidosRestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pedidosRestController).build();
    }

    @Test
    void testListarTodos() throws Exception {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAgregarProductoAMesa_NuevoPedido() throws Exception {
        int mesaId = 1;
        int productoId = 10;

        // Mocks para datos existentes
        Mesas mesa = new Mesas(mesaId, 1, 4, "Terraza", "Mesa 1", Mesas.Estado.libre);
        Productos producto = new Productos();
        producto.setProducto_id(productoId);
        producto.setNombre("Cerveza");
        producto.setPrecio(new BigDecimal("3.50"));

        Empleados empleado = new Empleados();
        empleado.setEmpleado_id(1);
        empleado.setNombre_empleado("Camarero");

        when(mesasRepository.findById(mesaId)).thenReturn(Optional.of(mesa));
        when(productosRepository.findById(productoId)).thenReturn(Optional.of(producto));
        when(repository.findAll()).thenReturn(Collections.emptyList()); // No hay pedidos activos
        when(empleadosRepository.findById(1)).thenReturn(Optional.of(empleado));

        when(repository.save(any(Pedidos.class))).thenAnswer(invocation -> {
            Pedidos p = invocation.getArgument(0);
            if (p.getPedido_id() == null) {
                p.setPedido_id(100);
            }
            return p;
        });

        Map<String, Integer> payload = new HashMap<>();
        payload.put("productoId", productoId);

        mockMvc.perform(post("/pedidos/mesa/" + mesaId + "/agregar-producto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedido_id").value(100))
                .andExpect(jsonPath("$.total_pedido").value(3.5));

        // Verificar que se creó el detalle y se guardó el pedido
        verify(detallePedidosRepository, times(1)).save(any(DetallePedidos.class));
        verify(repository, atLeastOnce()).save(any(Pedidos.class));
    }

    @Test
    void testEliminarPedido() throws Exception {
        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isOk());

        verify(repository, times(1)).deleteById(1);
    }
}
