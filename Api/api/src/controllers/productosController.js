import { fetchWithRetry } from '../utils/retryHelper.js';
import { CONFIG } from '../config/constants.js';

// --- Obtener Productos ---
// Este controlador actúa como un "proxy", redirigiendo la petición al backend principal (Java)
export const obtenerProductos = async (req, res, next) => {
  try {
    const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/productos`);
    res.json(response.data);
  } catch (error) {
    next(error);
  }
};

// --- Crear Producto ---
export const crearProducto = async (req, res, next) => {
  try {
    const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/productos`, {
      method: 'POST',
      data: req.body
    });
    res.status(201).json(response.data);
  } catch (error) {
    next(error);
  }
};

// --- Eliminar Producto ---
export const eliminarProducto = async (req, res, next) => {
  try {
    const { id } = req.params;
    await fetchWithRetry(`${CONFIG.BACKEND_URL}/productos/${id}`, {
      method: 'DELETE'
    });
    res.status(204).send();
  } catch (error) {
    next(error);
  }
};