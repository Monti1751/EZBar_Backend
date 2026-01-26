# MEJORAS INMEDIATAS - LISTA DE TAREAS

## Sprint Actual: Refactoring de Clean Code Backend

---

## 1. REFACTORIZAR CONTROLLERS (Prioridad: ALTA)

### ProductosRestController.java
**Estado:** ❌ Necesita refactorización
**Tareas:**
- [ ] Cambiar a constructor injection
- [ ] Crear ProductosService para lógica
- [ ] Usar ResponseEntity correctamente
- [ ] Agregar manejo de excepciones
- [ ] Validar entrada (nombre, precio > 0)
- [ ] Agregar Javadoc

**Referencia:** TEMPLATE_ProductosRestController.java

### MesasRestController.java
**Estado:** ❌ Necesita refactorización
**Tareas:**
- [ ] Constructor injection
- [ ] Crear MesasService
- [ ] ResponseEntity con códigos HTTP
- [ ] GlobalExceptionHandler
- [ ] Javadoc

### PedidosRestController.java
**Estado:** ❌ Necesita refactorización
**Tareas:**
- [ ] Constructor injection
- [ ] Crear PedidosService con @Transactional
- [ ] Crear DetallePedidoService
- [ ] Validar: pedido no vacío, categorías existen
- [ ] ResponseEntity

### Otros Controllers
**Estado:** ❌ Necesita refactorización
Aplicar el mismo patrón a:
- [ ] EmpleadosRestController
- [ ] ZonasRestController
- [ ] PuestosRestController
- [ ] InventarioRestController
- [ ] PagosRestController
- [ ] DetallePedidosRestController
- [ ] ProductoIngredientesRestController

---

## 2. CREAR SERVICE CLASSES (Prioridad: ALTA)

Para cada controller, crear su correspondiente Service:

### ProductosService.java
```java
@Service
public class ProductosService {
    @Transactional(readOnly = true)
    public List<Productos> listarTodos() { }
    
    @Transactional(readOnly = true)
    public Productos obtenerPorId(Integer id) { }
    
    @Transactional
    public Productos crear(Productos producto) { }
    
    @Transactional
    public Productos actualizar(Integer id, Productos producto) { }
    
    @Transactional
    public void eliminar(Integer id) { }
}
```

Repetir estructura para:
- [ ] MesasService
- [ ] PedidosService
- [ ] EmpleadosService
- [ ] ZonasService
- [ ] PuestosService
- [ ] InventarioService
- [ ] PagosService

---

## 3. CREAR DTOS (Prioridad: MEDIA)

Para cada entidad, crear DTOs para entrada y salida:

### ProductoCreateDTO.java
```java
public class ProductoCreateDTO {
    @NotNull @NotBlank @Size(min=3, max=100)
    private String nombre;
    
    @NotNull @Min(0)
    private BigDecimal precio;
    
    @NotNull
    private Integer categoriaId;
    
    @Size(max=500)
    private String descripcion;
    
    // getters/setters
}
```

### ProductoUpdateDTO.java
```java
public class ProductoUpdateDTO {
    @NotNull @NotBlank @Size(min=3, max=100)
    private String nombre;
    
    @Min(0)
    private BigDecimal precio;
    
    @Size(max=500)
    private String descripcion;
    
    // getters/setters
}
```

Crear para:
- [ ] Productos (Create, Update, Response)
- [ ] Mesas
- [ ] Pedidos
- [ ] Empleados
- [ ] Categorías

**Estructura de carpetas:**
```
src/main/java/
├── dtos/
│   ├── request/
│   │   ├── ProductoCreateDTO
│   │   ├── ProductoUpdateDTO
│   │   └── ...
│   └── response/
│       ├── ProductoResponseDTO
│       └── ...
```

---

## 4. AGREGAR VALIDACIONES (Prioridad: MEDIA)

### En DTOs con Bean Validation:
```java
@NotNull(message = "El nombre no puede ser nulo")
@NotBlank(message = "El nombre no puede estar vacío")
@Size(min = 3, max = 100, message = "Entre 3 y 100 caracteres")
private String nombre;

@NotNull(message = "El precio es requerido")
@DecimalMin(value = "0.0", inclusive = false, message = "Debe ser mayor a 0")
@DecimalMax(value = "99999.99", message = "Máximo 99999.99")
private BigDecimal precio;
```

### En Services (lógica de negocio):
```java
@Transactional
public Productos crear(ProductoCreateDTO dto) {
    // Validar que categoría existe
    Categorias categoria = categoriasRepository.findById(dto.getCategoriaId())
        .orElseThrow(() -> new ResourceNotFoundException("Categoría no existe"));
    
    // Validar que nombre es único
    if (productosRepository.existsByNombreIgnoreCase(dto.getNombre())) {
        throw new InvalidInputException("Producto con ese nombre ya existe");
    }
    
    // Crear producto
    Productos producto = new Productos();
    producto.setNombre(dto.getNombre());
    producto.setCategoria(categoria);
    producto.setPrecio(dto.getPrecio());
    
    return productosRepository.save(producto);
}
```

### En Controllers:
```java
@PostMapping
public ResponseEntity<ApiResponse<ProductoResponseDTO>> crear(
        @Valid @RequestBody ProductoCreateDTO dto) {
    Productos producto = productosService.crear(dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Producto creado", mapper.toDTO(producto)));
}
```

---

## 5. IMPLEMENTAR LOGGING (Prioridad: MEDIA)

### Agregar a todos los Services:
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductosService {
    private static final Logger logger = LoggerFactory.getLogger(ProductosService.class);
    
    public Productos obtenerPorId(Integer id) {
        logger.debug("Obteniendo producto con ID: {}", id);
        
        try {
            Productos producto = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Producto con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Producto no encontrado");
                });
            
            logger.info("Producto obtenido exitosamente: ID {}", id);
            return producto;
        } catch (Exception e) {
            logger.error("Error al obtener producto con ID {}", id, e);
            throw e;
        }
    }
}
```

### Configurar logback-spring.xml:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="LOG_FILE" value="logs/application.log"/>
    
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_FILE}</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_FILE}.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>10</maxHistory>
        </rollingPolicy>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

---

## 6. AGREGAR TESTS (Prioridad: BAJA)

### Unit Tests para Services:
```java
@ExtendWith(MockitoExtension.class)
public class ProductosServiceTest {
    
    @Mock
    private ProductosRepository repository;
    
    @InjectMocks
    private ProductosService service;
    
    @Test
    public void testObtenerPorIdExitoso() {
        // Arrange
        Productos producto = new Productos();
        producto.setProducto_id(1);
        producto.setNombre("Test");
        
        when(repository.findById(1)).thenReturn(Optional.of(producto));
        
        // Act
        Productos resultado = service.obtenerPorId(1);
        
        // Assert
        assertEquals("Test", resultado.getNombre());
        verify(repository).findById(1);
    }
    
    @Test
    public void testObtenerPorIdNoExiste() {
        when(repository.findById(999)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, 
            () -> service.obtenerPorId(999));
    }
}
```

### Integration Tests para Controllers:
```java
@SpringBootTest
public class ProductosControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testObtenerProductosEndpoint() throws Exception {
        mockMvc.perform(get("/productos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
}
```

---

## 7. ACTUALIZAR DOCUMENTATION (Prioridad: BAJA)

### Agregar Swagger/OpenAPI:
```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("EZBar API")
                .version("1.0.0")
                .description("API de Gestión de Restaurante"));
    }
}
```

### En Controllers:
```java
@GetMapping("/{id}")
@Operation(summary = "Obtener producto por ID", 
           description = "Devuelve los detalles de un producto específico")
@ApiResponse(responseCode = "200", description = "Producto encontrado")
@ApiResponse(responseCode = "404", description = "Producto no encontrado")
public ResponseEntity<Productos> obtenerPorId(
        @PathVariable 
        @Parameter(description = "ID del producto") 
        Integer id) {
    // ...
}
```

---

## 8. REVISAR SECURITY (Prioridad: BAJA)

- [ ] ¿Las credenciales están en application.yml o variables de entorno?
- [ ] ¿Se usan prepared statements? (Sí, Spring Data lo hace)
- [ ] ¿Hay validaciones de entrada?
- [ ] ¿Se loguean eventos sensibles?
- [ ] ¿Se implementará Spring Security en futuro?

---

## TIMELINE RECOMENDADO

### Semana 1-2
- [ ] Refactorizar 3 controllers principales
- [ ] Crear sus 3 services
- [ ] Implementar GlobalExceptionHandler

### Semana 3-4
- [ ] Refactorizar otros controllers
- [ ] Crear DTOs básicos
- [ ] Agregar validaciones

### Semana 5-6
- [ ] Implementar logging
- [ ] Agregar tests unitarios
- [ ] Code review y fixes

### Semana 7-8
- [ ] Integration tests
- [ ] Documentación Swagger
- [ ] Cleanup final

---

## CHECKLIST PARA CODE REVIEW

Antes de mergear cualquier PR:

- [ ] ¿Usa constructor injection?
- [ ] ¿La lógica está en Service, no en Controller?
- [ ] ¿Usa ResponseEntity correctamente?
- [ ] ¿Las excepciones son manejadas en GlobalExceptionHandler?
- [ ] ¿Hay Javadoc en métodos públicos?
- [ ] ¿Los nombres de variables/métodos son claros?
- [ ] ¿No hay código duplicado?
- [ ] ¿Se usan constantes en lugar de strings mágicos?
- [ ] ¿Hay validaciones de entrada?
- [ ] ¿Se usa logging en lugar de System.out?

---

## RECURSOS

- **CLEAN_CODE_BACKEND.md** - Guía completa de Clean Code
- **TEMPLATE_ProductosRestController.java** - Template para refactorizar
- **Constants.java** - Constantes centralizadas
- **GlobalExceptionHandler.java** - Manejo de excepciones
- **ApiResponse.java** - DTO para respuestas

---

**Última actualización:** 26 de enero de 2026
