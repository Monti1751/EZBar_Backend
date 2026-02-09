package Controladores;

import ClasesBD.Pagos;
import Repositorios.PagosRepository;
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
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PagosRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PagosRepository repository;

    @InjectMocks
    private PagosRestController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testListarTodos() throws Exception {
        Pagos pago = new Pagos();
        pago.setPago_id(1);
        pago.setMonto(new BigDecimal("50.00"));
        when(repository.findAll()).thenReturn(Arrays.asList(pago));

        mockMvc.perform(get("/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pago_id").value(1))
                .andExpect(jsonPath("$[0].monto").value(50.00));
    }

    @Test
    void testCrear() throws Exception {
        Pagos pago = new Pagos();
        pago.setMonto(new BigDecimal("25.00"));

        Pagos savedPago = new Pagos();
        savedPago.setPago_id(1);
        savedPago.setMonto(new BigDecimal("25.00"));

        when(repository.save(any(Pagos.class))).thenReturn(savedPago);

        mockMvc.perform(post("/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pago)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pago_id").value(1));
    }

    @Test
    void testEliminar() throws Exception {
        mockMvc.perform(delete("/pagos/1"))
                .andExpect(status().isOk());
        verify(repository, times(1)).deleteById(1);
    }
}
