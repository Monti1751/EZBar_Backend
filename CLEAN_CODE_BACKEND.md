# GUÍA DE CLEAN CODE - BACKEND JAVA (EZBar)

## Resumen Ejecutivo

Este documento establece los estándares de Clean Code aplicados específicamente al backend Java del proyecto EZBar. El objetivo es mantener un código:
- **Legible y mantenible**
- **Escalable y flexible**
- **Seguro y robusto**
- **Fácil de testear**

---

## 1. ESTRUCTURA DEL PROYECTO

### Estándar Java con Maven

```
src/main/java/
├── controllers/          # REST Controllers
├── models/              # Entidades JPA (ClasesBD)
├── repositories/        # Spring Data JPA Repositories
├── services/            # Lógica de negocio
├── dtos/                # Data Transfer Objects
├── exceptions/          # Excepciones personalizadas
├── config/              # Configuración (Security, CORS, etc.)
└── utils/               # Clases de utilidad

src/main/resources/
├── application.yml      # Configuración de Spring Boot
└── messages.properties  # Mensajes internacionalizados
```

### Convenciones de Nombres

| Elemento | Convención | Ejemplo |
|----------|-----------|---------|
| **Paquetes** | lowercase, punto separado | `com.ezbar.controllers` |
| **Clases** | PascalCase, sustantivos | `CategoriasRestController` |
| **Variables** | camelCase | `usuarioActivo` |
| **Métodos** | camelCase, verbos | `obtenerPorId()` |
| **Constantes** | UPPER_SNAKE_CASE | `MAX_PRODUCT_LENGTH` |
| **Interfaces** | I prefijo (opcional) | `IProductoService` |

---

## 2. ARQUITECTURA EN CAPAS (Separación de Responsabilidades)

### Patrón: Controller → Service → Repository

```java
// 1. CONTROLLER (Maneja HTTP)
@RestController
@RequestMapping("/categorias")
public class CategoriasRestController {
    private final CategoriasService service;
    
    @GetMapping("/{id}")
    public ResponseEntity<Categorias> obtenerPorId(@PathVariable Integer id) {
        // Delega lógica al service
        return ResponseEntity.ok(service.obtenerPorId(id));
    }
}

// 2. SERVICE (Lógica de negocio)
@Service
public class CategoriasService {
    private final CategoriasRepository repository;
    
    public Categorias obtenerPorId(Integer id) {
        // Contiene lógica de negocio
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
    }
}

// 3. REPOSITORY (Acceso a datos)
public interface CategoriasRepository extends JpaRepository<Categorias, Integer> {
    // Spring Data genera las consultas automáticamente
}
```

### Responsabilidades de cada capa

| Capa | Responsabilidad | Qué NO hace |
|------|-----------------|-----------|
| **Controller** | Manejo de HTTP, validación de entrada, mapeo de rutas | Lógica de negocio, acceso a BD |
| **Service** | Lógica de negocio, transacciones, orquestación | HTTP, queries SQL |
| **Repository** | Acceso a datos, queries | Lógica de negocio |

---

## 3. INYECCIÓN DE DEPENDENCIAS

### Preferencia: Constructor Injection

```java
// ✅ BIEN - Constructor injection (mejor)
@Service
public class ProductosService {
    private final ProductosRepository repository;
    private final CategoriasService categoriasService;
    
    @Autowired
    public ProductosService(
            ProductosRepository repository,
            CategoriasService categoriasService) {
        this.repository = repository;
        this.categoriasService = categoriasService;
    }
}

// ❌ EVITAR - Field injection (acoplamiento fuerte)
@Service
public class ProductosService {
    @Autowired
    private ProductosRepository repository; // Difícil de testear
}
```

**Razones:**
1. **Testeable**: Fácil pasar mocks en tests
2. **Visible**: Las dependencias son explícitas
3. **Inmutable**: Podemos usar `final`
4. **Desacoplado**: No depende de Spring en constructores

---

## 4. MANEJO DE EXCEPCIONES

### Excepciones Personalizadas

```java
// Crear excepciones específicas
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}

// Usar en el Service
@Service
public class CategoriasService {
    public Categorias obtenerPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new InvalidInputException("ID debe ser mayor a 0");
        }
        
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Categoría con ID " + id + " no encontrada"));
    }
}

// Manejo global en GlobalExceptionHandler
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(buildErrorResponse("error", "ERR_404", ex.getMessage()));
    }
}
```

### Reglas para excepciones

- ✅ Lanza excepciones específicas
- ✅ Maneja globalmente en `GlobalExceptionHandler`
- ✅ Loguea el contexto completo
- ✅ No expongas detalles internos al cliente
- ❌ No ignores excepciones (empty catch blocks)
- ❌ No lances genéricas `Exception.class`

---

## 5. DTOs (Data Transfer Objects)

### Por qué usar DTOs

Nunca devuelvas entidades JPA directamente al cliente.

```java
// ❌ MALO - Exponer entidad directamente
@GetMapping("/{id}")
public Categorias obtenerPorId(@PathVariable Integer id) {
    return repository.findById(id).orElse(null); // Expone ID interno, relaciones, etc.
}

// ✅ BIEN - Usar DTO
@GetMapping("/{id}")
public ResponseEntity<CategoriasDTO> obtenerPorId(@PathVariable Integer id) {
    Categorias categoria = service.obtenerPorId(id);
    return ResponseEntity.ok(mapper.toDTO(categoria));
}

// DTO
public class CategoriasDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    // No exponemos relaciones ni campos internos
}
```

---

## 6. VALIDACIONES

### Validación en múltiples niveles

```java
// NIVEL 1: Validación en Controller (validación de entrada)
@PostMapping
public ResponseEntity<ApiResponse<CategoriasDTO>> crear(
        @Valid @RequestBody CategoriasCreateDTO dto) {
    // Spring valida automáticamente con @Valid
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Categoría creada", service.crear(dto)));
}

// NIVEL 2: Validación en Service (lógica de negocio)
@Service
public class CategoriasService {
    public Categorias crear(CategoriasCreateDTO dto) {
        // Validar reglas de negocio
        if (repository.existsByNombre(dto.getNombre())) {
            throw new InvalidInputException("Categoría con ese nombre ya existe");
        }
        
        return repository.save(mapper.toEntity(dto));
    }
}

// DTO con validaciones
public class CategoriasCreateDTO {
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
}
```

---

## 7. TRANSACCIONES

### Usar @Transactional apropiadamente

```java
// ✅ En Service donde se modifica datos
@Service
public class PedidosService {
    
    @Transactional // Garantiza atomicidad (todo o nada)
    public Pedido crearPedido(PedidoCreateDTO dto) {
        // Si una operación falla, se hace rollback de todas
        Pedido pedido = new Pedido();
        repository.save(pedido);
        
        // Crear detalles del pedido
        for (DetallePedidoDTO detalle : dto.getDetalles()) {
            detalleRepository.save(mapper.toEntity(detalle));
        }
        
        return pedido;
    }
    
    // Read-only = mejor performance
    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos() {
        return repository.findAll();
    }
}
```

---

## 8. CONSTANTES Y CONFIGURACIÓN

### Centralizar constantes

```java
public class Constants {
    private Constants() {} // Prevenir instanciación
    
    public static class Messages {
        public static final String PRODUCT_NOT_FOUND = "Producto no encontrado";
        public static final String INVALID_PRICE = "El precio debe ser positivo";
    }
    
    public static class DefaultValues {
        public static final BigDecimal DEFAULT_IVA = new BigDecimal("21.00");
        public static final Integer DEFAULT_STOCK = 0;
    }
    
    public static class Routes {
        public static final String API_PREFIX = "/api/v1";
        public static final String PRODUCTOS = "/productos";
    }
}

// Uso
if (producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
    throw new InvalidInputException(Constants.Messages.INVALID_PRICE);
}
```

### Variables de entorno (NO hardcodear credenciales)

```yaml
# application.yml
spring:
  datasource:
    url: ${DB_URL:jdbc:mariadb://localhost:3306/ezbar}
    username: ${DB_USER:root}
    password: ${DB_PASSWORD:}
```

```java
// .env (en raíz del proyecto, NUNCA commitear)
DB_URL=jdbc:mariadb://localhost:3306/ezbar
DB_USER=root
DB_PASSWORD=password123
```

---

## 9. LOGGING

### Usar SLF4J + Logback (NO System.out.println)

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductosService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductosService.class);
    
    public Productos obtenerPorId(Integer id) {
        logger.debug("Obteniendo producto con ID: {}", id); // DEBUG
        
        try {
            return repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Producto con ID {} no encontrado", id); // WARN
                    return new ResourceNotFoundException("Producto no encontrado");
                });
        } catch (Exception e) {
            logger.error("Error al obtener producto con ID {}", id, e); // ERROR
            throw e;
        }
    }
}
```

**Niveles de logging:**
- **DEBUG**: Información de desarrollo (valores, flujos)
- **INFO**: Eventos importantes (usuario login, recurso creado)
- **WARN**: Situaciones anómalas pero recuperables
- **ERROR**: Errores que requieren atención inmediata

---

## 10. COMENTARIOS

### Guías para comentarios

```java
// ❌ MAL - Comentario obvio
public void crearProducto(Productos producto) {
    // Guarda el producto
    repository.save(producto);
}

// ✅ BIEN - Explica el POR QUÉ, no el QUÉ
public void crearProducto(Productos producto) {
    // Guardamos con flush() para generar el ID inmediatamente,
    // que es necesario para las operaciones siguientes
    repository.saveAndFlush(producto);
}

// ✅ BIEN - Javadoc para métodos públicos
/**
 * Obtiene un producto por su identificador.
 * 
 * @param id Identificador único del producto
 * @return El producto encontrado
 * @throws ResourceNotFoundException Si el producto no existe
 * @example GET /productos/42
 */
public Productos obtenerPorId(Integer id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
}
```

---

## 11. MEJORES PRÁCTICAS DE BASE DE DATOS

### Prepared Statements (Spring Data lo hace automáticamente)

```java
// ✅ BIEN - Spring Data genera queries seguras
public interface ProductosRepository extends JpaRepository<Productos, Integer> {
    List<Productos> findByCategoriaId(Integer categoriaId);
    List<Productos> findByNombreContainingIgnoreCase(String nombre);
}

// ❌ NUNCA - Concatenación de strings (SQL Injection)
String query = "SELECT * FROM productos WHERE nombre = '" + nombre + "'";
```

### Índices

```java
@Entity
@Table(name = "PRODUCTOS", indexes = {
    @Index(name = "idx_categoria_id", columnList = "categoria_id"),
    @Index(name = "idx_nombre", columnList = "nombre")
})
public class Productos {
    // Indexa columnas frecuentemente consultadas para mejor performance
}
```

### Connection Pooling (Spring Boot lo configura automáticamente)

```yaml
# application.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10      # Máximo de conexiones simultáneas
      minimum-idle: 5            # Mínimo de conexiones inactivas
      connection-timeout: 20000  # ms
```

---

## 12. TESTING

### Unit Tests (Testean lógica aislada)

```java
@ExtendWith(MockitoExtension.class)
public class CategoriasServiceTest {
    
    @Mock
    private CategoriasRepository repository;
    
    @InjectMocks
    private CategoriasService service;
    
    @Test
    public void testObtenerPorIdNoEncontrado() {
        // Arrange
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> service.obtenerPorId(id));
    }
}
```

### Integration Tests (Testean flujo completo)

```java
@SpringBootTest
public class CategoriasControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testObtenerCategoriasEndpoint() throws Exception {
        mockMvc.perform(get("/categorias"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
}
```

---

## 13. CHECKLIST DE CLEAN CODE

Antes de hacer commit, revisa:

- [ ] **Nombres claros**: ¿Variables, métodos y clases tienen nombres descriptivos?
- [ ] **Métodos pequeños**: ¿Cada método hace una cosa y la hace bien?
- [ ] **Sin duplicación**: ¿Hay código repetido que se puede refactorizar?
- [ ] **Manejo de errores**: ¿Se usan excepciones personalizadas y GlobalExceptionHandler?
- [ ] **DTOs**: ¿Se usan DTOs en lugar de exponer entidades JPA?
- [ ] **Transacciones**: ¿Se usa @Transactional correctamente?
- [ ] **Logging**: ¿Se usan logs en lugar de System.out?
- [ ] **Comentarios**: ¿Los comentarios explican el POR QUÉ, no el QUÉ?
- [ ] **Constantes**: ¿Los valores mágicos están centralizados?
- [ ] **Arquitectura**: ¿Se respeta Controller → Service → Repository?
- [ ] **Inyección**: ¿Se usa constructor injection?
- [ ] **Validaciones**: ¿Se valida entrada en Controller y lógica en Service?

---

## 14. EJEMPLO COMPLETO

### Creación de un nuevo Feature

```java
// 1. DTO (Entrada del cliente)
public class CrearProductoDTO {
    @NotNull
    @NotBlank
    @Size(min = 3, max = 100)
    private String nombre;
    
    @Min(0)
    private BigDecimal precio;
    
    private Integer categoriaId;
}

// 2. Service (Lógica de negocio)
@Service
public class ProductosService {
    
    private final ProductosRepository productosRepo;
    private final CategoriasService categoriasService;
    private static final Logger logger = LoggerFactory.getLogger(ProductosService.class);
    
    @Autowired
    public ProductosService(
            ProductosRepository productosRepo,
            CategoriasService categoriasService) {
        this.productosRepo = productosRepo;
        this.categoriasService = categoriasService;
    }
    
    @Transactional
    public Productos crearProducto(CrearProductoDTO dto) {
        logger.info("Creando nuevo producto: {}", dto.getNombre());
        
        // Validar que categoría existe
        Categorias categoria = categoriasService.obtenerPorId(dto.getCategoriaId());
        
        // Validar que no exista producto con ese nombre
        if (productosRepo.existsByNombreIgnoreCase(dto.getNombre())) {
            logger.warn("Intento de crear producto duplicado: {}", dto.getNombre());
            throw new InvalidInputException(
                "Ya existe un producto con el nombre: " + dto.getNombre());
        }
        
        // Crear y guardar
        Productos producto = new Productos();
        producto.setNombre(dto.getNombre());
        producto.setCategoria(categoria);
        producto.setPrecio(dto.getPrecio());
        producto.setActivo(true);
        
        Productos guardado = productosRepo.save(producto);
        logger.info("Producto creado exitosamente con ID: {}", guardado.getProducto_id());
        
        return guardado;
    }
}

// 3. Controller (HTTP)
@RestController
@RequestMapping("/productos")
public class ProductosRestController {
    
    private final ProductosService service;
    
    @Autowired
    public ProductosRestController(ProductosService service) {
        this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ProductosDTO>> crear(
            @Valid @RequestBody CrearProductoDTO dto) {
        
        Productos producto = service.crearProducto(dto);
        ProductosDTO respuesta = mapper.toDTO(producto);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ApiResponse<>(
                Constants.Messages.CREATED_SUCCESS,
                respuesta));
    }
}
```

---

## Conclusión

Siguiendo estos principios:
- El código es **más legible** y **mantenible**
- Los **bugs son más fáciles de encontrar**
- El **testing es más simple**
- La **escalabilidad es mayor**
- El **onboarding de nuevos desarrolladores es más rápido**

**Recuerda**: El código se lee 10 veces más de lo que se escribe. ¡Que sea hermoso!
