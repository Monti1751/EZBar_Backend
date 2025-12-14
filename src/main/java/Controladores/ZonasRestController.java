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
}
