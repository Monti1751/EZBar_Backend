import express from 'express';
import * as categoriasController from '../controllers/categoriasController.js';

const router = express.Router();

router.get('/', categoriasController.obtenerCategorias);
router.post('/', categoriasController.crearCategoria);
router.delete('/:id', categoriasController.eliminarCategoria);

export default router;
