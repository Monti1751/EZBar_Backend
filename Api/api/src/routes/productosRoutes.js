import express from 'express';
import * as productosController from '../controllers/productosController.js';

const router = express.Router();

// GET /api/productos - Obtener todos los productos
router.get('/', productosController.obtenerProductos);

// POST /api/productos - Crear un nuevo producto (passthrough al backend java)
router.post('/', productosController.crearProducto);

// DELETE /api/productos/:id - Eliminar un producto
router.delete('/:id', productosController.eliminarProducto);

export default router; 