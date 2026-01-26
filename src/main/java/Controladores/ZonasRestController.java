package Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Controlador REST para gestionar Zonas y sus mesas asociadas
 */
@RestController
@RequestMapping("/zonas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ZonasRestController {

    @Autowired
    private Repositorios.ZonasRepository repository;

    @Autowired
    private Repositorios.MesasRepository mesasRepository;

    // Listar zonas simples (solo nombres para el frontend básico)
    @GetMapping
    public List<Map<String, String>> listarZonas() {
        List<ClasesBD.Zonas> zonasBD = repository.findAll();
        List<Map<String, String>> zonas = new ArrayList<>();

        for (ClasesBD.Zonas zonaBD : zonasBD) {
            Map<String, String> zona = new HashMap<>();
            zona.put("nombre", zonaBD.getNombre());
            zonas.add(zona);
        }

        return zonas;
    }

    // Crear una nueva zona
    @PostMapping
    public ClasesBD.Zonas crearZona(@RequestBody Map<String, String> payload) {
        String nombre = payload.get("nombre");
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de la zona es obligatorio");
        }
        return repository.save(new ClasesBD.Zonas(null, nombre));
    }

    // Borrar zona por nombre
    @DeleteMapping("/{nombre}")
    public void borrarZona(@PathVariable String nombre) {
        repository.deleteByNombre(nombre);
    }

    /**
     * Guardar/Actualizar todas las mesas de una zona específica.
     * Recibe un JSON complejo con una lista de mesas.
     * POST /zonas/{nombre}/mesas
     */
    @PostMapping("/{nombre}/mesas")
    public List<ClasesBD.Mesas> guardarMesas(@PathVariable String nombre,
            @RequestBody Map<String, List<Map<String, Object>>> payload) {
        List<Map<String, Object>> mesasData = payload.get("mesas");
        List<ClasesBD.Mesas> savedMesas = new ArrayList<>();

        if (mesasData != null) {
            for (Map<String, Object> mesaData : mesasData) {
                ClasesBD.Mesas mesa = new ClasesBD.Mesas();

                // Manejo de ID:
                // El Frontend puede enviar IDs temporales (strings largos/timestamps) para
                // nuevas mesas.
                // El Backend debe detectar si es un ID válido (int) o uno temporal para crear
                // una nueva entrada.
                Object idObj = mesaData.get("id");
                if (idObj instanceof Integer) {
                    mesa.setMesa_id((Integer) idObj);
                } else if (idObj instanceof String) {
                    try {
                        // Intentar parsear a entero. Si es muy grande (timestamp), fallará y se creará
                        // nueva (catch)
                        Integer parsedId = Integer.parseInt((String) idObj);
                        mesa.setMesa_id(parsedId);
                    } catch (NumberFormatException e) {
                        // ID inválido o temporal -> Se crea como nueva mesa (ID null)
                    }
                }

                // Asignar resto de datos
                mesa.setNombre((String) mesaData.get("nombre"));
                mesa.setUbicacion(nombre); // Forzar la zona actual

                Object numeroObj = mesaData.get("numero_mesa");
                if (numeroObj instanceof Integer)
                    mesa.setNumero_mesa((Integer) numeroObj);

                Object capacidadObj = mesaData.get("capacidad");
                if (capacidadObj instanceof Integer)
                    mesa.setCapacidad((Integer) capacidadObj);

                // Parsear estado (enum)
                String estadoStr = (String) mesaData.get("estado");
                if (estadoStr != null) {
                    try {
                        mesa.setEstado(ClasesBD.Mesas.Estado.valueOf(estadoStr.toLowerCase()));
                    } catch (IllegalArgumentException e) {
                        mesa.setEstado(ClasesBD.Mesas.Estado.libre); // Default
                    }
                }

                savedMesas.add(mesasRepository.save(mesa));
            }
        }
        return savedMesas;
    }
}
