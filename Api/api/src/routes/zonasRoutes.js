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

router.get('/', obtenerZonas);           // GET /api/zonas
router.post('/', crearZona);             // POST /api/zonas
router.get('/:ubicacion', obtenerZonaPorUbicacion); // GET /api/zonas/:ubicacion
router.put('/:zonaId', actualizarZona);  // PUT /api/zonas/:zonaId
router.delete('/:ubicacion', eliminarZona); // DELETE /api/zonas/:ubicacion
router.post('/:ubicacion/mesas', guardarMesasDeZona); // POST /api/zonas/:zonaName/mesas

export default router;

