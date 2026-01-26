# QUICK START - CLEAN CODE BACKEND

## Para empezar rápidamente

---

## 📖 LEE ESTO PRIMERO (5 minutos)

### Arquitectura en Capas (El concepto clave)
```
🌐 CONTROLLER → HTTP + validación
    ↓
💼 SERVICE → Lógica de negocio + transacciones
    ↓
📦 REPOSITORY → Acceso a datos (Spring Data JPA)
    ↓
❌ GLOBAL EXCEPTION HANDLER → Errores centralizados
```

**En una sola línea:** El Controller llama al Service, el Service llama al Repository, todos los errores van a GlobalExceptionHandler.

---

## ✨ LOS 5 CAMBIOS PRINCIPALES

### 1. Constructor Injection
```java
// ❌ VIEJO
@Autowired
private Repository repo;

// ✅ NUEVO
private final Repository repo;

@Autowired
public MyService(Repository repo) {
    this.repo = repo;
}
```

### 2. ResponseEntity (HTTP codes)
```java
// ❌ VIEJO
public Objeto obtener() {
    return repo.findById(...).orElse(null);
}

// ✅ NUEVO
public ResponseEntity<Objeto> obtener(@PathVariable int id) {
    return repo.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
}
```

### 3. Excepciones Específicas
```java
// ❌ VIEJO
catch (Exception e) { return null; }

// ✅ NUEVO
if (!exists) {
    throw new ResourceNotFoundException("No encontrado");
}
```

### 4. Constantes Centralizadas
```java
// ❌ VIEJO
if (rol.equals("admin")) { }

// ✅ NUEVO
if (rol.equals(Constants.Roles.ADMIN)) { }
```

### 5. Lógica en Service
```java
// ❌ VIEJO: En Controller
@PostMapping
public Objeto crear(Objeto obj) {
    validate(obj);
    save(obj);
    return obj;
}

// ✅ NUEVO: En Service
@PostMapping
public Objeto crear(Objeto obj) {
    return service.crear(obj); // Todo en service
}
```

---

## 🔍 EJEMPLOS RÁPIDOS

### Ejemplo 1: Obtener por ID
```java
@RestController
@RequestMapping("/categorias")
public class CategoriasController {
    
    private final CategoriasService service;
    
    @Autowired
    public CategoriasController(CategoriasService service) {
        this.service = service;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Categorias> obtenerPorId(@PathVariable Integer id) {
        try {
            Categorias categoria = service.obtenerPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
```

### Ejemplo 2: Crear
```java
@PostMapping
public ResponseEntity<Categorias> crear(@RequestBody Categorias categoria) {
    if (categoria.getNombre() == null || categoria.getNombre().isEmpty()) {
        return ResponseEntity.badRequest().build();
    }
    
    Categorias creada = service.crear(categoria);
    return ResponseEntity.status(HttpStatus.CREATED).body(creada);
}
```

### Ejemplo 3: Service
```java
@Service
public class CategoriasService {
    
    private final CategoriasRepository repository;
    
    @Autowired
    public CategoriasService(CategoriasRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public Categorias crear(Categorias categoria) {
        // Lógica de negocio aquí
        return repository.save(categoria);
    }
    
    public Categorias obtenerPorId(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> 
                new ResourceNotFoundException("Categoría no encontrada"));
    }
}
```

---

## 📚 ARCHIVOS A CONSULTAR

### Si necesitas...

**Refactorizar un controller:** 
→ Ve a `TEMPLATE_ProductosRestController.java`

**Entender el patrón:** 
→ Lee `PATRONES_EJEMPLOS.md` (antes vs después)

**Hacer un code review:** 
→ Usa checklist en `TAREAS_MEJORAS.md`

**Crear un nuevo feature:** 
→ Sigue ejemplo en `CLEAN_CODE_BACKEND.md` sección 14

**Ver qué cambió:** 
→ Consulta `CAMBIOS_CLEAN_CODE.md`

**Tienes una pregunta:** 
→ Busca en `CLEAN_CODE_BACKEND.md`

---

## ⚡ REGLAS DE ORO

1. **Controller: Solo HTTP**
   - Valida entrada
   - Llama a service
   - Devuelve ResponseEntity con código HTTP
   - NUNCA hace queries de BD

2. **Service: Lógica de negocio**
   - Validación de reglas de negocio
   - @Transactional
   - Llama a repository
   - Lanza excepciones específicas

3. **Repository: Solo datos**
   - Spring Data JPA
   - Nunca tiene lógica
   - Nunca tiene HTTP

4. **Excepciones: Específicas**
   - ResourceNotFoundException (404)
   - InvalidInputException (400)
   - Lanzadas en Service
   - Manejadas en GlobalExceptionHandler

5. **Constantes: Centralizadas**
   - Constants.java
   - Un lugar para buscar
   - Fácil cambiar valores

---

## ✅ CHECKLIST MINI

Antes de hacer commit a tu refactoring:

- [ ] ¿El controller tiene solo HTTP?
- [ ] ¿Hay un service con la lógica?
- [ ] ¿Usa constructor injection?
- [ ] ¿ResponseEntity tiene código HTTP?
- [ ] ¿Las excepciones son específicas?
- [ ] ¿Hay @Transactional en service?
- [ ] ¿Los nombres son claros?
- [ ] ¿Hay Javadoc?

Si respondiste SÍ a todo: ✅ Ready to commit!

---

## 🎯 PRÓXIMO PASO

1. Lee `PATRONES_EJEMPLOS.md` (10 minutos)
2. Abre `TEMPLATE_ProductosRestController.java`
3. Refactoriza `ProductosRestController.java` usando el template
4. Crea `ProductosService.java`
5. Haz test manual: GET /productos

**Tiempo estimado:** 30 minutos para un controller simple

---

## 🆘 AYUDA RÁPIDA

**P: ¿Dónde pongo la validación?**
A: Controller (validación de entrada) + Service (validación de lógica)

**P: ¿Qué código HTTP devuelvo?**
A: 200 (GET, PUT), 201 (POST), 204 (DELETE), 404 (no encontrado), 400 (entrada inválida)

**P: ¿Cómo manejo errores?**
A: Lanza ResourceNotFoundException en service, GlobalExceptionHandler lo maneja

**P: ¿Qué es un DTO?**
A: Objeto para transferir datos entre cliente y servidor, no expongas entidades JPA

**P: ¿@Transactional dónde?**
A: En Service, en métodos que modifican datos (create, update, delete)

**P: ¿@Autowired dónde?**
A: NUNCA en field injection. Solo en constructor con parámetro @Autowired

---

## 📞 REFERENCIAS RÁPIDAS

```java
// HTTP Codes
ResponseEntity.ok(data);                          // 200
ResponseEntity.status(HttpStatus.CREATED).body(); // 201
ResponseEntity.noContent().build();               // 204
ResponseEntity.notFound().build();                // 404
ResponseEntity.badRequest().build();              // 400

// Constructor Injection
@Autowired
public MyService(Repository repo) {
    this.repo = repo;
}

// Exception
throw new ResourceNotFoundException("Mensaje");

// Transactional
@Transactional
public void metodo() { }

// Logging (futuro)
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
logger.info("Mensaje");
```

---

## 🚀 ROADMAP PERSONAL

### Hoy
- [ ] Lee este documento
- [ ] Entiende los 5 cambios

### Mañana
- [ ] Refactoriza 1 controller
- [ ] Crea su service

### Esta semana
- [ ] Refactoriza 3-4 controllers más
- [ ] Crea tests básicos

### Este mes
- [ ] Todos los controllers refactorizados
- [ ] DTOs completos
- [ ] Logging implementado

---

**Tiempo para leer:** 10 minutos
**Tiempo para dominar:** 1-2 sprints
**Tiempo para enseñar a otros:** 30 minutos

---

*Simplifica, refactoriza, disfruta código limpio.* 🎯
