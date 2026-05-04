import express from 'express';
import { obtenerPagos, obtenerPagoPorId, crearPago, actualizarPago } from '../controllers/pagosController.js';

const router = express.Router();

router.get('/', obtenerPagos);
router.get('/:id', obtenerPagoPorId);
router.post('/', crearPago);
router.put('/:id', actualizarPago);

export default router;
