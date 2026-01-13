import { fetchWithRetry } from '../utils/retryHelper.js';
import { CONFIG } from '../config/constants.js';
import pool from '../config/database.js';

export const obtenerMesas = async (req, res, next) => {
  try {
    const { ubicacion } = req.query;

    let query = 'SELECT * FROM MESAS';
    let params = [];

    if (ubicacion) {
      query += ' WHERE ubicacion = ?';
      params.push(ubicacion);
    }

    query += ' ORDER BY numero_mesa ASC';

    const [rows] = await pool.query(query, params);

    // Mapear respuesta para compatibilidad con Frontend
    const mesas = rows.map(m => ({
      ...m,
      name: `Mesa ${m.numero_mesa}`, // Asegurar campo name
      id: m.mesa_id
    }));

    res.json(mesas);
  } catch (error) {
    next(error);
  }
};

export const obtenerMesasPorZona = async (req, res, next) => {
  try {
    const { zonaId } = req.params; // zonaId en este contexto es el nombre de la zona (ubicacion)

    const [rows] = await pool.query('SELECT * FROM MESAS WHERE ubicacion = ? ORDER BY numero_mesa ASC', [zonaId]);

    const mesas = rows.map(m => ({
      ...m,
      name: `Mesa ${m.numero_mesa}`,
      id: m.mesa_id
    }));

    res.json(mesas);
  } catch (error) {
    next(error);
  }
};

export const actualizarMesa = async (req, res, next) => {
  try {
    const { mesaId } = req.params;
    const response = await fetchWithRetry(
      `${CONFIG.BACKEND_URL}/mesas/${mesaId}`,
      {
        method: 'PUT',
        data: req.body
      }
    );
    res.json(response.data);
  } catch (error) {
    next(error);
  }
};

export const crearMesa = async (req, res, next) => {
  try {
    // Frontend envía: numero_mesa, capacidad, ubicacion, estado, etc.
    const { numero_mesa, numero, capacidad, ubicacion, estado, posX, posY, pos_x, pos_y } = req.body;

    // Fallbacks para compatibilidad
    const finalNumero = numero_mesa || numero;
    const finalPosX = pos_x || posX || 0;
    const finalPosY = pos_y || posY || 0;

    const [result] = await pool.query(
      'INSERT INTO MESAS (numero_mesa, capacidad, ubicacion, estado, pos_x, pos_y) VALUES (?, ?, ?, ?, ?, ?)',
      [finalNumero, capacidad, ubicacion, estado || 'libre', finalPosX, finalPosY]
    );

    res.status(201).json({
      id: result.insertId,
      numero_mesa: finalNumero,
      capacidad,
      ubicacion,
      estado: estado || 'libre',
      pos_x: finalPosX,
      pos_y: finalPosY
    });
  } catch (error) {
    console.error('Error al crear mesa:', error);
    res.status(500).json({ error: 'Error al crear mesa' });
  }
};

export const eliminarMesa = async (req, res, next) => {
  try {
    const { mesaId } = req.params;
    const [result] = await pool.query('DELETE FROM MESAS WHERE mesa_id = ?', [mesaId]);

    if (result.affectedRows === 0) {
      return res.status(404).json({ message: 'Mesa no encontrada' });
    }

    res.json({ message: 'Mesa eliminada correctamete' });
  } catch (error) {
    next(error);
  }
}; 