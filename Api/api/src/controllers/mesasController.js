import { fetchWithRetry } from '../utils/retryHelper.js';
import { CONFIG } from '../config/constants.js';
import pool from '../config/database.js';

// --- Obtener todas las mesas ---
export const obtenerMesas = async (req, res, next) => {
  try {
    // Se puede filtrar por 'ubicacion' en la query string (ej: ?ubicacion=Terraza)
    const { ubicacion } = req.query;

    let query = 'SELECT * FROM MESAS';
    let params = [];

    if (ubicacion) {
      query += ' WHERE ubicacion = ?';
      params.push(ubicacion);
    }

    query += ' ORDER BY numero_mesa ASC';

    // Ejecutar consulta a la base de datos
    const [rows] = await pool.query(query, params);

    // Mapear respuesta para compatibilidad con el Frontend (Flutter)
    // El frontend espera 'name' y 'id'
    const mesas = rows.map(m => ({
      ...m, // Copiar todos los campos originales
      name: `Mesa ${m.numero_mesa}`, // Campo 'name' para mostrar en lista
      id: m.mesa_id // Campo 'id' estandarizado
    }));

    // Log para depuración
    console.log('GET /mesas devolviendo:', mesas.length, 'mesas');

    res.json(mesas);
  } catch (error) {
    next(error); // Pasar error al middleware global
  }
};

// --- Obtener mesas filtradas por Zona ---
export const obtenerMesasPorZona = async (req, res, next) => {
  try {
    const { zonaId } = req.params; // La zona viene como parametro en la URL

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

// --- Actualizar una mesa existente ---
export const actualizarMesa = async (req, res, next) => {
  try {
    const { mesaId } = req.params; // ID de la mesa a editar
    // Datos que vienen del frontend
    const { numero_mesa, numero, capacidad, ubicacion, estado, posX, posY, pos_x, pos_y } = req.body;

    // Manejo de nombres de campos alternativos (compatibilidad)
    const finalNumero = numero_mesa || numero;
    const finalPosX = pos_x || posX;
    const finalPosY = pos_y || posY;

    // Construir la consulta SQL dinámica para actualizar SOLO lo que se envíe
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

    params.push(mesaId); // ID para el WHERE

    const query = `UPDATE MESAS SET ${updates.join(', ')} WHERE mesa_id = ?`;

    const [result] = await pool.query(query, params);

    if (result.affectedRows === 0) {
      return res.status(404).json({ message: 'Mesa no encontrada' });
    }

    // Devolver la mesa actualizada completa
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

// --- Crear una nueva mesa ---
export const crearMesa = async (req, res, next) => {
  try {
    // Datos recibidos
    const { numero_mesa, numero, capacidad, ubicacion, estado, posX, posY, pos_x, pos_y } = req.body;

    // Validación básica: Ubicación obligatoria
    if (!ubicacion) {
      return res.status(400).json({ error: 'La ubicación es obligatoria' });
    }

    // Normalizar datos
    const finalNumero = numero_mesa || numero;
    if (finalNumero === undefined) {
      return res.status(400).json({ error: 'El número de mesa es obligatorio' });
    }

    const finalPosX = pos_x || posX || 0;
    const finalPosY = pos_y || posY || 0;

    // Insertar en Base de Datos
    const [result] = await pool.query(
      'INSERT INTO MESAS (numero_mesa, capacidad, ubicacion, estado, pos_x, pos_y) VALUES (?, ?, ?, ?, ?, ?)',
      [finalNumero, capacidad || 4, ubicacion, estado || 'libre', finalPosX, finalPosY]
    );

    // Responder con la mesa creada
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
    // Manejo de error si ya existe el numero de mesa (si hay restriccion UNIQUE)
    if (error.code === 'ER_DUP_ENTRY') {
      return res.status(409).json({ error: 'Ya existe una mesa con ese número en esa ubicación' });
    }
    res.status(500).json({ error: 'Error interno al crear mesa: ' + error.message });
  }
};

// --- Eliminar mesa ---
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