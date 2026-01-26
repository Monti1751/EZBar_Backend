import express from 'express';
import * as mesasController from '../controllers/mesasController.js';

const router = express.Router();

// --- Rutas publicas (protegidas por middleware de auth global si aplica) ---

// GET /api/mesas - Obtiene todas las mesas (opcionalmente filtradas por ubicacion)
router.get('/', mesasController.obtenerMesas);

// POST /api/mesas - Crea una nueva mesa
router.post('/', mesasController.crearMesa);

// GET /api/mesas/zona/:zonaId - Obtiene mesas de una zona/ubicación específica
router.get('/zona/:zonaId', mesasController.obtenerMesasPorZona);

// PUT /api/mesas/:mesaId - Actualiza una mesa existente (mover posición, cambiar estado)
router.put('/:mesaId', mesasController.actualizarMesa);

// DELETE /api/mesas/:mesaId - Elimina una mesa
router.delete('/:mesaId', mesasController.eliminarMesa);

export default router; 