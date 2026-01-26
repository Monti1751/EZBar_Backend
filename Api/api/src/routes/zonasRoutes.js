import express from 'express';
import {
    obtenerZonas,
    crearZona,
    obtenerZonaPorUbicacion,
    actualizarZona,
    eliminarZona,
    guardarMesasDeZona
} from '../controllers/zonasController.js';

const router = express.Router();

// GET /api/zonas - Listar todas las zonas
router.get('/', obtenerZonas);

// POST /api/zonas - Crear nueva zona
router.post('/', crearZona);

// GET /api/zonas/:ubicacion - Buscar zona por su nombre
router.get('/:ubicacion', obtenerZonaPorUbicacion);

// PUT /api/zonas/:zonaId - Renombrar zona
router.put('/:zonaId', actualizarZona);

// DELETE /api/zonas/:ubicacion - Eliminar zona (por nombre)
router.delete('/:ubicacion', eliminarZona);

// POST /api/zonas/:ubicacion/mesas - Guardar/Reemplazar todas las mesas de una zona (Sincronización masiva)
router.post('/:ubicacion/mesas', guardarMesasDeZona);

export default router;

