# PATRONES Y EJEMPLOS CLEAN CODE

## Ejemplos Prácticos de Mejoras Aplicadas

---

## ANTES vs DESPUÉS

### 1. INYECCIÓN DE DEPENDENCIAS

#### ❌ ANTES (Field Injection)
```java
@Service
public class ProductosService {
    @Autowired
    private ProductosRepository repository;  // Difícil de testear
    @Autowired
    private CategoriasRepository catRepository; // No explícito
    @Autowired
    private EmpleadosService empleadosService;
}
```

**Problemas:**
- Difícil de testear (requiere Spring)
- No es claro qué dependencias necesita
- Puede ser nulo si no se inicializa
- Acoplamiento con Spring

#### ✅ DESPUÉS (Constructor Injection)
```java
@Service
public class ProductosService {
    private final ProductosRepository repository;
    private final CategoriasRepository catRepository;
    private final EmpleadosService empleadosService;
    
    @Autowired
    public ProductosService(
            ProductosRepository repository,
            CategoriasRepository catRepository,
            EmpleadosService empleadosService) {
        this.repository = repository;
        this.catRepository = catRepository;
        this.empleadosService = empleadosService;
    }
}
```

**Beneficios:**
- Fácil de testear (pasar mocks)
- Explícito qué necesita
- Immutable (final)
- Independiente de Spring

**Unit Test Ejemplo:**
```java
@Test
public void testCrearProducto() {
    // Crear mocks
    ProductosRepository mockRepo = mock(ProductosRepository.class);
    CategoriasRepository mockCatRepo = mock(CategoriasRepository.class);
    
    // Inyectar manualmente
    ProductosService service = new ProductosService(mockRepo, mockCatRepo, null);
    
    // Testear
    service.crear(new ProductoCreateDTO());
}
```

---

### 2. MANEJO DE ERRORES

#### ❌ ANTES (Try-catch genérico)
```java
@GetMapping("/{id}")
public Productos obtenerPorId(@PathVariable Integer id) {
    try {
        return repository.findById(id).orElse(null); // Retorna null
    } catch (Exception e) {
        return null; // Enmasca error real
    }
}
```

**Problemas:**
- Retorna `null` (peligroso)
- No diferencia tipo de error
- Cliente no sabe qué pasó
- Codebase inconsistente

#### ✅ DESPUÉS (Manejo estructurado)
```java
@GetMapping("/{id}")
public ResponseEntity<Productos> obtenerPorId(@PathVariable Integer id) {
    try {
        Productos producto = service.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    } catch (ResourceNotFoundException e) {
        logger.warn("Producto {} no encontrado", id);
        return ResponseEntity.notFound().build();
    } catch (Exception e) {
        logger.error("Error obteniendo producto {}", id, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

// O mejor aún, delegar a GlobalExceptionHandler
@GetMapping("/{id}")
public ResponseEntity<Productos> obtenerPorId(@PathVariable Integer id) {
    return ResponseEntity.ok(service.obtenerPorId(id)); // Service lanza excepción
}
```

**GlobalExceptionHandler maneja:**
```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<Map<String, Object>> handleNotFound(
        ResourceNotFoundException ex, WebRequest request) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(buildErrorResponse("ERR_404", ex.getMessage()));
}
```

**Beneficios:**
- Códigos HTTP correctos
- Respuesta estructurada
- Logging centralizado
- Código más limpio

---

### 3. LÓGICA DE NEGOCIO EN CONTROLLERS

#### ❌ ANTES (Todo en Controller)
```java
@RestController
public class SetupController {
    @Autowired
    private CategoriasRepository categoriasRepo;
    @Autowired
    private ProductosRepository productosRepo;
    
    @GetMapping("/setup")
    public String setupData() {
        if (categoriasRepo.count() == 0) {
            Categorias bebidas = new Categorias(null, "Bebidas", "...");
            categoriasRepo.save(bebidas);
            
            productosRepo.save(new Productos(null, bebidas, "Coca", ...));
            productosRepo.save(new Productos(null, bebidas, "Cerveza", ...));
        }
        return "OK";
    }
}
```

**Problemas:**
- Controller hace demasiado
- No reutilizable
- Difícil de testear
- Sin transacciones

#### ✅ DESPUÉS (Separar en Service)
```java
// Controller - Solo HTTP
@RestController
@RequestMapping("/setup")
public class SetupController {
    private final SetupService setupService;
    
    @Autowired
    public SetupController(SetupService setupService) {
        this.setupService = setupService;
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> setupData() {
        String message = setupService.initializeDatabaseWithTestData();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        
        return ResponseEntity.ok(response);
    }
}

// Service - Lógica de negocio
@Service
public class SetupService {
    private final CategoriasRepository categoriasRepository;
    private final ProductosRepository productosRepository;
    
    @Transactional
    public String initializeDatabaseWithTestData() {
        initializeCategories();
        initializeProducts();
        return "Database initialized successfully";
    }
    
    private void initializeCategories() {
        if (categoriasRepository.count() == 0) {
            categoriasRepository.save(new Categorias(null, "Bebidas", "..."));
        }
    }
    
    private void initializeProducts() {
        // Crear productos
    }
}
```

**Beneficios:**
- Controller es simple
- Service reutilizable desde CLI, scheduler, etc.
- Transaccional automático
- Fácil de testear

---

### 4. RESPUESTAS DE API

#### ❌ ANTES (Inconsistente)
```java
@GetMapping
public List<Categorias> listar() {
    return repository.findAll(); // A veces lista, a veces null
}

@GetMapping("/{id}")
public Categorias obtener(@PathVariable Integer id) {
    return repository.findById(id).orElse(null); // Devuelve null
}

@PostMapping
public Categorias crear(@RequestBody Categorias c) {
    return repository.save(c); // Devuelve objeto
}

@DeleteMapping("/{id}")
public void eliminar(@PathVariable Integer id) {
    repository.deleteById(id); // Devuelve void
}
```

**Problemas:**
- Sin códigos HTTP
- Respuestas inconsistentes
- Cliente no sabe el estado
- Difícil de documentar

#### ✅ DESPUÉS (Estructurado)
```java
@GetMapping
public ResponseEntity<ApiResponse<List<Categorias>>> listar() {
    List<Categorias> categorias = repository.findAll();
    return ResponseEntity.ok(
        new ApiResponse<>("Categorías obtenidas", categorias));
}

@GetMapping("/{id}")
public ResponseEntity<ApiResponse<Categorias>> obtener(@PathVariable Integer id) {
    Categorias categoria = service.obtenerPorId(id); // Lanza excepción si no existe
    return ResponseEntity.ok(
        new ApiResponse<>("Categoría obtenida", categoria));
}

@PostMapping
public ResponseEntity<ApiResponse<Categorias>> crear(
        @Valid @RequestBody CategoriasCreateDTO dto) {
    Categorias categoria = service.crear(dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Categoría creada", categoria));
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
    service.eliminar(id);
    return ResponseEntity.noContent().build();
}
```

**Respuestas JSON:**
```json
// 200 OK - GET
{
  "status": "success",
  "message": "Categorías obtenidas",
  "data": [ ... ],
  "code": null,
  "timestamp": "2026-01-26T10:30:00"
}

// 201 CREATED - POST
{
  "status": "success",
  "message": "Categoría creada",
  "data": { "id": 1, "nombre": "Bebidas" },
  "code": null,
  "timestamp": "2026-01-26T10:31:00"
}

// 404 NOT FOUND
{
  "status": "error",
  "code": "ERR_404",
  "message": "Categoría no encontrada",
  "httpStatus": 404,
  "timestamp": "2026-01-26T10:32:00",
  "path": "/categorias/999"
}

// 204 NO CONTENT - DELETE
(Sin body)
```

**Beneficios:**
- Códigos HTTP correctos
- Estructura consistente
- Fácil de procesar
- Documentable con Swagger

---

### 5. CONSTANTES Y VALORES MÁGICOS

#### ❌ ANTES (Strings dispersos)
```java
if (usuario.getRol().equals("Administrador")) { ... }
if (usuario.getRol().equals("Mesero")) { ... }
if (usuario.getRol().equals("Cocinero")) { ... }

String zona = "Terraza";
String zona = "Interior";

if (producto.getPrecio().compareTo(new BigDecimal("0")) < 0) { ... }
```

**Problemas:**
- Errores de tipeo
- Cambiar valor = cambiar 5 lugares
- No documentado
- Difícil de testear

#### ✅ DESPUÉS (Centralizados)
```java
public class Constants {
    public static class Roles {
        public static final String ADMIN = "Administrador";
        public static final String WAITER = "Mesero";
        public static final String CHEF = "Cocinero";
    }
    
    public static class Zones {
        public static final String TERRAZA = "Terraza";
        public static final String INTERIOR = "Interior";
    }
    
    public static class Validation {
        public static final BigDecimal MIN_PRICE = BigDecimal.ZERO;
    }
}

// Uso
if (usuario.getRol().equals(Constants.Roles.ADMIN)) { ... }
if (usuario.getRol().equals(Constants.Roles.WAITER)) { ... }

String zona = Constants.Zones.TERRAZA;

if (producto.getPrecio().compareTo(Constants.Validation.MIN_PRICE) < 0) { ... }
```

**Beneficios:**
- Un único lugar para valores
- Refactorización fácil
- Auto-complete en IDE
- Documentado

---

### 6. TRANSACCIONES Y CONSISTENCIA

#### ❌ ANTES (Sin transacción)
```java
@PostMapping
public Pedido crear(@RequestBody PedidoCreateDTO dto) {
    // Si falla aquí, BD en estado inconsistente
    Pedido pedido = new Pedido();
    pedidoRepository.save(pedido);
    
    // Si falla aquí, pedido creado pero sin detalles
    for (DetallePedidoDTO det : dto.getDetalles()) {
        detalleRepository.save(new DetallePedido(pedido, det));
    }
    
    return pedido;
}
```

**Problema:** Si falla a mitad, BD inconsistente

#### ✅ DESPUÉS (Con @Transactional)
```java
@Service
public class PedidosService {
    
    @Transactional // O TODO SE GUARDA, O NADA SE GUARDA
    public Pedido crear(PedidoCreateDTO dto) {
        Pedido pedido = new Pedido();
        pedidoRepository.save(pedido);
        
        for (DetallePedidoDTO det : dto.getDetalles()) {
            detalleRepository.save(new DetallePedido(pedido, det));
        }
        
        return pedido; // Si llega aquí, todo se commitea
        // Si lanza excepción antes, todo se revierte
    }
    
    @Transactional(readOnly = true) // Mejor performance para consultas
    public List<Pedido> listarPendientes() {
        return pedidoRepository.findByEstado("Pendiente");
    }
}
```

**Beneficios:**
- BD siempre consistente
- Rollback automático en errores
- Performance optimizado (readOnly)

---

### 7. VALIDACIONES EN MÚLTIPLES CAPAS

#### Capa 1: DTO (Formato y restricciones)
```java
public class ProductoCreateDTO {
    @NotNull(message = "El nombre es requerido")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "Entre 3 y 100 caracteres")
    private String nombre;
    
    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.01", message = "Debe ser mayor a 0")
    private BigDecimal precio;
    
    @NotNull(message = "La categoría es requerida")
    private Integer categoriaId;
}
```

#### Capa 2: Controller (Validar DTO)
```java
@PostMapping
public ResponseEntity<ApiResponse<ProductoDTO>> crear(
        @Valid @RequestBody ProductoCreateDTO dto) { // @Valid activa validaciones
    
    Producto producto = service.crear(dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Producto creado", mapper.toDTO(producto)));
}
```

#### Capa 3: Service (Lógica de negocio)
```java
@Service
@Transactional
public class ProductosService {
    
    public Producto crear(ProductoCreateDTO dto) {
        // Validar que categoría existe
        Categorias categoria = categoriasRepository.findById(dto.getCategoriaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Categoría " + dto.getCategoriaId() + " no existe"));
        
        // Validar que nombre es único
        if (productosRepository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new InvalidInputException(
                "Ya existe producto con nombre: " + dto.getNombre());
        }
        
        // Crear producto
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCategoria(categoria);
        producto.setPrecio(dto.getPrecio());
        
        return productosRepository.save(producto);
    }
}
```

**Beneficios:**
- Validaciones claras en cada nivel
- DTO valida formato
- Service valida reglas de negocio
- Errores específicos al cliente

---

## CHECKLIST DE CONVERSIÓN

Para convertir un controller/service antiguo a Clean Code:

```
[ ] Cambiar a constructor injection
[ ] Crear Service si no existe
[ ] Agregar @Transactional donde corresponda
[ ] Usar ResponseEntity en lugar de retornar objeto directamente
[ ] Agregar códigos HTTP (201, 204, 404, etc.)
[ ] Lanzar excepciones específicas (ResourceNotFoundException, etc.)
[ ] Agregar validaciones en DTOs
[ ] Agregar Javadoc a métodos públicos
[ ] Agregar logging con Logger
[ ] Centralizar strings mágicos en Constants
[ ] Crear DTOs para entrada/salida
[ ] Agregar mappers (si usa MapStruct o similar)
[ ] Escribir tests unitarios
```

---

**Última actualización:** 26 de enero de 2026
