import express from 'express';
import * as productosController from '../controllers/productosController.js';

const router = express.Router();

router.get('/', productosController.obtenerProductos);
router.post('/', productosController.crearProducto);
router.delete('/:id', productosController.eliminarProducto);

export default router; 