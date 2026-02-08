package Controladores;

import ClasesBD.Categorias;
import Repositorios.CategoriasRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoriasRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoriasRepository repository;

    @InjectMocks
    private CategoriasRestController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testListarTodos() throws Exception {
        Categorias cat1 = new Categorias(1, "Bebidas", "Todo tipo de refrescos");
        when(repository.findAll()).thenReturn(Arrays.asList(cat1));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Bebidas"));
    }

    @Test
    void testCrear() throws Exception {
        Categorias saving = new Categorias(null, "Comida", "Platos principales");
        Categorias saved = new Categorias(1, "Comida", "Platos principales");
        when(repository.save(any(Categorias.class))).thenReturn(saved);

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saving)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria_id").value(1));
    }

    @Test
    void testActualizar() throws Exception {
        Categorias cat = new Categorias(1, "Postres", "Deliciosos postres");
        when(repository.existsById(1)).thenReturn(true);
        when(repository.save(any(Categorias.class))).thenReturn(cat);

        mockMvc.perform(put("/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Postres"));
    }

    @Test
    void testEliminar() throws Exception {
        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isOk());
        verify(repository, times(1)).deleteById(1);
    }
}
