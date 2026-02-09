package Controladores;

import ClasesBD.Zonas;
import ClasesBD.Mesas;
import Repositorios.ZonasRepository;
import Repositorios.MesasRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ZonasRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ZonasRepository zonasRepository;

    @Mock
    private MesasRepository mesasRepository;

    @InjectMocks
    private ZonasRestController zonasRestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(zonasRestController).build();
    }

    @Test
    void testListarZonas() throws Exception {
        Zonas zona1 = new Zonas(1, "Terraza");
        Zonas zona2 = new Zonas(2, "Salon");
        when(zonasRepository.findAll()).thenReturn(Arrays.asList(zona1, zona2));

        mockMvc.perform(get("/zonas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Terraza"))
                .andExpect(jsonPath("$[1].nombre").value("Salon"));

        verify(zonasRepository, times(1)).findAll();
    }

    @Test
    void testCrearZona() throws Exception {
        Map<String, String> payload = new HashMap<>();
        payload.put("nombre", "VIP");

        Zonas zonaGuardada = new Zonas(3, "VIP");
        when(zonasRepository.save(any(Zonas.class))).thenReturn(zonaGuardada);

        mockMvc.perform(post("/zonas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("VIP"));

        verify(zonasRepository, times(1)).save(any(Zonas.class));
    }

    @Test
    void testBorrarZona() throws Exception {
        mockMvc.perform(delete("/zonas/Terraza"))
                .andExpect(status().isOk());

        verify(zonasRepository, times(1)).deleteByNombre("Terraza");
    }

    @Test
    void testGuardarMesas() throws Exception {
        Map<String, List<Map<String, Object>>> payload = new HashMap<>();
        List<Map<String, Object>> mesasData = new ArrayList<>();
        Map<String, Object> mesa1 = new HashMap<>();
        mesa1.put("nombre", "Mesa 1");
        mesa1.put("numero_mesa", 1);
        mesa1.put("capacidad", 4);
        mesa1.put("estado", "libre");
        mesasData.add(mesa1);
        payload.put("mesas", mesasData);

        Mesas mesaGuardada = new Mesas(1, 1, 4, "Terraza", "Mesa 1", Mesas.Estado.libre);
        when(mesasRepository.save(any(Mesas.class))).thenReturn(mesaGuardada);

        mockMvc.perform(post("/zonas/Terraza/mesas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Mesa 1"))
                .andExpect(jsonPath("$[0].ubicacion").value("Terraza"));

        verify(mesasRepository, times(1)).save(any(Mesas.class));
    }
}
