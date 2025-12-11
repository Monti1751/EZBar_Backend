package Controladores;

import java.util.HashMap;
import java.util.Map;
import ClasesBD.*;

/**
 * Controlador simple para manejar mesas sin usar Spring.
 * Simula un API, pero funciona en un proyecto Java normal.
 */
public class MesaController {

    // Simulación de base de datos en memoria
    private Map<String, Mesas> mesas = new HashMap<>();

    public MesaController() {
        // Crea una mesa de ejemplo
        Mesas m1 = new Mesas("1", "Terraza", "libre", (short) 1, (short) 4, true);
        mesas.put(m1.getMesa_id(), m1);
    }

    // Devuelve el estado actual de una mesa
    public String getEstadoMesa(String id) {
        Mesas mesa = mesas.get(id);
        if (mesa != null) {
            return mesa.getEstado();
        } else {
            return "Mesa no encontrada";
        }
    }

    // Cambia el estado de una mesa
    public String cambiarEstadoMesa(String id, String nuevoEstado) {
        Mesas mesa = mesas.get(id);
        if (mesa == null) {
            return "Mesa no encontrada";
        }

        short codigo;

        switch (nuevoEstado.toLowerCase()) {
            case "libre" -> codigo = 1;
            case "reservado" -> codigo = 2;
            case "ocupado" -> codigo = 3;
            default -> {
                return "Estado inválido";
            }
        }

        mesa.setEstado(codigo);
        return "Estado de la mesa " + id + " cambiado a: " + mesa.getEstado();
    }

    // Método de prueba para ejecutar desde consola
    public static void main(String[] args) {
        MesaController controlador = new MesaController();

        System.out.println(controlador.getEstadoMesa("1"));
        System.out.println(controlador.cambiarEstadoMesa("1", "ocupado"));
        System.out.println(controlador.getEstadoMesa("1"));
        System.out.println(controlador.cambiarEstadoMesa("2", "reservado"));
    }
}