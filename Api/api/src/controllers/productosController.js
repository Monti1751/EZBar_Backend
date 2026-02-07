import { fetchWithRetry } from '../utils/retryHelper.js';
import { CONFIG } from '../config/constants.js';
import logger from '../logger.js';

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

// --- Actualizar Producto ---
export const actualizarProducto = async (req, res, next) => {
  try {
    const { id } = req.params;
    const bodySize = JSON.stringify(req.body).length;
    logger.info(`Incoming PUT /api/productos/${id} - Payload size: ${bodySize} chars`);

    if (req.body.imagenBlob) {
      logger.debug(`Payload contains imagenBlob of length: ${req.body.imagenBlob.length}`);
    }

    const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/productos/${id}`, {
      method: 'PUT',
      data: req.body
    });

    logger.debug(`Java backend response for PUT /productos/${id}:`, { data: response.data });
    res.json(response.data);
  } catch (error) {
    logger.error(`Error processing PUT /api/productos/${id}`, { error: error.message });
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