package Controladores;

import ClasesBD.Categorias;
import ClasesBD.Productos;
import Repositorios.CategoriasRepository;
import Repositorios.ProductosRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class ProductosRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductosRepository repository;
    @Mock
    private CategoriasRepository categoriasRepository;

    @InjectMocks
    private ProductosRestController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testActualizarProducto_ConImagenBlob() throws Exception {
        int id = 1;
        Productos existing = new Productos();
        existing.setProducto_id(id);
        existing.setNombre("Coca Cola");
        existing.setUrl_imagen("old_url");

        Productos updateData = new Productos();
        byte[] newImage = new byte[] { 1, 2, 3 };
        updateData.setImagenBlob(newImage);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(Productos.class))).thenAnswer(i -> i.getArguments()[0]);

        mockMvc.perform(put("/productos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imagenUrl").value(nullValue()))
                .andExpect(jsonPath("$.imagenBlob").exists());

        verify(repository).save(existing);
    }

    @Test
    void testActualizarProducto_ConCategoria() throws Exception {
        int id = 1;
        int catId = 5;
        Productos existing = new Productos();
        existing.setProducto_id(id);

        Categorias newCat = new Categorias(catId, "Nuevas", "desc");

        Productos updateData = new Productos();
        updateData.setCategoria(newCat);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(categoriasRepository.findById(catId)).thenReturn(Optional.of(newCat));
        when(repository.save(any(Productos.class))).thenAnswer(i -> i.getArguments()[0]);

        mockMvc.perform(put("/productos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk());

        verify(repository).save(existing);
        assert existing.getCategoria().equals(newCat);
    }
}
