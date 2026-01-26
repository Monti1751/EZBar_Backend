import { fetchWithRetry } from '../utils/retryHelper.js';
import { CONFIG } from '../config/constants.js';

// --- Obtener Categorías ---
// Proxy al backend Java
export const obtenerCategorias = async (req, res, next) => {
  try {
    const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/categorias`);
    res.json(response.data);
  } catch (error) {
    next(error);
  }
};

// --- Crear Categoría ---
export const crearCategoria = async (req, res, next) => {
  try {
    const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/categorias`, {
      method: 'POST',
      data: req.body
    });
    res.status(201).json(response.data);
  } catch (error) {
    next(error);
  }
};

// --- Eliminar Categoría ---
export const eliminarCategoria = async (req, res, next) => {
  try {
    const { id } = req.params;
    await fetchWithRetry(`${CONFIG.BACKEND_URL}/categorias/${id}`, {
      method: 'DELETE'
    });
    res.status(204).send();
  } catch (error) {
    next(error);
  }
};
