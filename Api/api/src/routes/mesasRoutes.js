import express from 'express';
import * as mesasController from '../controllers/mesasController.js';

const router = express.Router();

router.get('/', mesasController.obtenerMesas);
router.post('/', mesasController.crearMesa);
router.get('/zona/:zonaId', mesasController.obtenerMesasPorZona);
router.put('/:mesaId', mesasController.actualizarMesa);
router.delete('/:mesaId', mesasController.eliminarMesa);

export default router; 