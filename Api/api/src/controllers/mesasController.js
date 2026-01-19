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

    console.log('GET /mesas returning:', mesas.length, 'items');
    if (mesas.length > 0) {
      console.log('Sample ID:', mesas[0].id, 'Type:', typeof mesas[0].id);
    }

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
    console.log(`PUT /mesas/${mesaId} received. Body:`, req.body);
    const { numero_mesa, numero, capacidad, ubicacion, estado, posX, posY, pos_x, pos_y } = req.body;

    // Fallbacks
    const finalNumero = numero_mesa || numero;
    const finalPosX = pos_x || posX;
    const finalPosY = pos_y || posY;

    // Construir query dinámica para actualizar solo lo que llega
    let updates = [];
    let params = [];

    if (finalNumero !== undefined) {
      updates.push('numero_mesa = ?');
      params.push(finalNumero);
    }
    if (capacidad !== undefined) {
      updates.push('capacidad = ?');
      params.push(capacidad);
    }
    if (ubicacion !== undefined) {
      updates.push('ubicacion = ?');
      params.push(ubicacion);
    }
    if (estado !== undefined) {
      updates.push('estado = ?');
      params.push(estado);
    }
    if (finalPosX !== undefined) {
      updates.push('pos_x = ?');
      params.push(finalPosX);
    }
    if (finalPosY !== undefined) {
      updates.push('pos_y = ?');
      params.push(finalPosY);
    }

    if (updates.length === 0) {
      return res.status(400).json({ message: 'No se enviaron datos para actualizar' });
    }

    params.push(mesaId);

    const query = `UPDATE MESAS SET ${updates.join(', ')} WHERE mesa_id = ?`;

    const [result] = await pool.query(query, params);

    if (result.affectedRows === 0) {
      return res.status(404).json({ message: 'Mesa no encontrada' });
    }

    // Devolver la mesa actualizada
    const [rows] = await pool.query('SELECT * FROM MESAS WHERE mesa_id = ?', [mesaId]);
    const mesaActualizada = rows[0];

    res.json({
      ...mesaActualizada,
      name: `Mesa ${mesaActualizada.numero_mesa}`,
      id: mesaActualizada.mesa_id
    });

  } catch (error) {
    console.error('Error al actualizar mesa:', error);
    next(error);
  }
};

export const crearMesa = async (req, res, next) => {
  try {
    // Frontend envía: numero_mesa, capacidad, ubicacion, estado, etc.
    const { numero_mesa, numero, capacidad, ubicacion, estado, posX, posY, pos_x, pos_y } = req.body;

    // Validación básica
    if (!ubicacion) {
      return res.status(400).json({ error: 'La ubicación es obligatoria' });
    }

    // Fallbacks para compatibilidad
    // Si no viene numero, intentamos buscar el maximo actual para esa zona + 1, o error?
    // Por ahora asumimos que el frontend envia numero o numero_mesa
    const finalNumero = numero_mesa || numero;

    if (finalNumero === undefined) {
      return res.status(400).json({ error: 'El número de mesa es obligatorio' });
    }

    const finalPosX = pos_x || posX || 0;
    const finalPosY = pos_y || posY || 0;

    const [result] = await pool.query(
      'INSERT INTO MESAS (numero_mesa, capacidad, ubicacion, estado, pos_x, pos_y) VALUES (?, ?, ?, ?, ?, ?)',
      [finalNumero, capacidad || 4, ubicacion, estado || 'libre', finalPosX, finalPosY]
    );

    res.status(201).json({
      id: result.insertId,
      numero_mesa: finalNumero,
      capacidad: capacidad || 4,
      ubicacion,
      estado: estado || 'libre',
      pos_x: finalPosX,
      pos_y: finalPosY,
      name: `Mesa ${finalNumero}`
    });
  } catch (error) {
    console.error('Error al crear mesa:', error);
    // Manejo especifico de errores de BD si es necesario
    if (error.code === 'ER_DUP_ENTRY') {
      return res.status(409).json({ error: 'Ya existe una mesa con ese número en esa ubicación' });
    }
    res.status(500).json({ error: 'Error interno al crear mesa: ' + error.message });
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