import { fetchWithRetry } from '../utils/retryHelper.js';
import { CONFIG } from '../config/constants.js';

export const obtenerPagos = async (req, res, next) => {
    try {
        const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/pagos`);
        res.json(response.data);
    } catch (error) {
        next(error);
    }
};

export const obtenerPagoPorId = async (req, res, next) => {
    try {
        const { id } = req.params;
        const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/pagos/${id}`);
        res.json(response.data);
    } catch (error) {
        next(error);
    }
};

export const crearPago = async (req, res, next) => {
    try {
        const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/pagos`, {
            method: 'POST',
            data: req.body
        });
        res.status(201).json(response.data);
    } catch (error) {
        next(error);
    }
};

export const actualizarPago = async (req, res, next) => {
    try {
        const { id } = req.params;
        const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/pagos/${id}`, {
            method: 'PUT',
            data: req.body
        });
        res.json(response.data);
    } catch (error) {
        next(error);
    }
};
