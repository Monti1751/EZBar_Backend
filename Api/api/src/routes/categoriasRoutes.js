import express from 'express';
import * as categoriasController from '../controllers/categoriasController.js';

const router = express.Router();

// GET /api/categorias - Obtiene todas las categorías de productos
router.get('/', categoriasController.obtenerCategorias);

// POST /api/categorias - Crea una nueva categoría
router.post('/', categoriasController.crearCategoria);

// DELETE /api/categorias/:id - Elimina una categoría
router.delete('/:id', categoriasController.eliminarCategoria);

export default router;
