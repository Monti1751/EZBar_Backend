package Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/zonas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ZonasRestController {

    @Autowired
    private Repositorios.ZonasRepository repository;

    @GetMapping
    public List<Map<String, String>> listarZonas() {
        // Fetch directly from ZONAS table
        List<ClasesBD.Zonas> zonasBD = repository.findAll();
        List<Map<String, String>> zonas = new ArrayList<>();

        for (ClasesBD.Zonas zonaBD : zonasBD) {
            Map<String, String> zona = new HashMap<>();
            zona.put("nombre", zonaBD.getNombre());
            zonas.add(zona);
        }

        return zonas;
    }

    @PostMapping
    public ClasesBD.Zonas crearZona(@RequestBody Map<String, String> payload) {
        String nombre = payload.get("nombre");
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre de la zona es obligatorio");
        }
        return repository.save(new ClasesBD.Zonas(null, nombre));
    }

    @DeleteMapping("/{nombre}")
    public void borrarZona(@PathVariable String nombre) {
        repository.deleteByNombre(nombre);
    }

    @Autowired
    private Repositorios.MesasRepository mesasRepository;

    @PostMapping("/{nombre}/mesas")
    public List<ClasesBD.Mesas> guardarMesas(@PathVariable String nombre,
            @RequestBody Map<String, List<Map<String, Object>>> payload) {
        List<Map<String, Object>> mesasData = payload.get("mesas");
        List<ClasesBD.Mesas> savedMesas = new ArrayList<>();

        if (mesasData != null) {
            for (Map<String, Object> mesaData : mesasData) {
                ClasesBD.Mesas mesa = new ClasesBD.Mesas();

                // Handle ID
                Object idObj = mesaData.get("id");
                if (idObj instanceof Integer) {
                    mesa.setMesa_id((Integer) idObj);
                } else if (idObj instanceof String) {
                    // If string ID (e.g. timestamp from frontend), we ignore it to let DB generate
                    // new ID
                    // EXCEPT if it parses to a small int (legacy), but safer to treat text IDs as
                    // new if not matching DB type
                    try {
                        Integer parsedId = Integer.parseInt((String) idObj);
                        // We could check if it exists, but for now assuming if it parses it might be
                        // real.
                        // However, timestamps are too big for Integer, so likely they throw or are new.
                        // System timestamps (milliseconds) overflow Integer.MAX_VALUE usually.
                        // So catching exception is good.
                        mesa.setMesa_id(parsedId);
                    } catch (NumberFormatException e) {
                        // New mesa, auto-generate ID
                    }
                }

                // Handle other fields
                mesa.setNombre((String) mesaData.get("nombre"));
                mesa.setUbicacion(nombre); // Force zone name

                Object numeroObj = mesaData.get("numero_mesa");
                if (numeroObj instanceof Integer)
                    mesa.setNumero_mesa((Integer) numeroObj);

                Object capacidadObj = mesaData.get("capacidad");
                if (capacidadObj instanceof Integer)
                    mesa.setCapacidad((Integer) capacidadObj);

                String estadoStr = (String) mesaData.get("estado");
                if (estadoStr != null) {
                    try {
                        mesa.setEstado(ClasesBD.Mesas.Estado.valueOf(estadoStr.toLowerCase()));
                    } catch (IllegalArgumentException e) {
                        mesa.setEstado(ClasesBD.Mesas.Estado.libre);
                    }
                }

                savedMesas.add(mesasRepository.save(mesa));
            }
        }
        return savedMesas;
    }
}
