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
      name: (m.nombre && m.nombre.trim() !== '') ? m.nombre : `Mesa ${m.numero_mesa}`,
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
      name: (m.nombre && m.nombre.trim() !== '') ? m.nombre : `Mesa ${m.numero_mesa}`,
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
    const { numero_mesa, numero, capacidad, ubicacion, estado, posX, posY, pos_x, pos_y, nombre } = req.body;

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
    if (nombre !== undefined) {
      updates.push('nombre = ?');
      params.push(nombre);
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
      name: (mesaActualizada.nombre && mesaActualizada.nombre.trim() !== '') ? mesaActualizada.nombre : `Mesa ${mesaActualizada.numero_mesa}`,
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
    const { numero_mesa, numero, capacidad, ubicacion, estado, posX, posY, pos_x, pos_y, nombre } = req.body;

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
      'INSERT INTO MESAS (numero_mesa, capacidad, ubicacion, estado, pos_x, pos_y, nombre) VALUES (?, ?, ?, ?, ?, ?, ?)',
      [finalNumero, capacidad || 4, ubicacion, estado || 'libre', finalPosX, finalPosY, nombre || null]
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
      nombre: nombre || null,
      name: (nombre && nombre.trim() !== '') ? nombre : `Mesa ${finalNumero}`
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

    try {
      const [result] = await pool.query('DELETE FROM MESAS WHERE mesa_id = ?', [mesaId]);
      if (result.affectedRows === 0) {
        return res.status(404).json({ message: 'Mesa no encontrada' });
      }
      return res.json({ message: 'Mesa eliminada correctamente' });
    } catch (dbError) {
      // Si el error es de clave foránea (ER_ROW_IS_REFERENCED o similar)
      if (dbError.code === 'ER_ROW_IS_REFERENCED' || dbError.errno === 1451) {
        console.log('⚠️ Detectado fallo de integridad referencial. Aplicando corrección CASCADE automática...');

        // Aplicar corrección agresiva
        await pool.query('SET FOREIGN_KEY_CHECKS = 0');

        // Intentar limpiar y recrear con CASCADE
        // Usamos nombres genéricos por si acaso
        try { await pool.query('ALTER TABLE pedidos DROP FOREIGN KEY pedidos_ibfk_1'); } catch (e) { }
        try { await pool.query('ALTER TABLE detalle_pedidos DROP FOREIGN KEY detalle_pedidos_ibfk_1'); } catch (e) { }
        try { await pool.query('ALTER TABLE pagos DROP FOREIGN KEY pagos_ibfk_1'); } catch (e) { }

        await pool.query('ALTER TABLE pedidos ADD CONSTRAINT pedidos_ibfk_1 FOREIGN KEY (mesa_id) REFERENCES mesas(mesa_id) ON DELETE CASCADE');
        await pool.query('ALTER TABLE detalle_pedidos ADD CONSTRAINT detalle_pedidos_ibfk_1 FOREIGN KEY (pedido_id) REFERENCES pedidos(pedido_id) ON DELETE CASCADE');
        await pool.query('ALTER TABLE pagos ADD CONSTRAINT pagos_ibfk_1 FOREIGN KEY (pedido_id) REFERENCES pedidos(pedido_id) ON DELETE CASCADE');

        await pool.query('SET FOREIGN_KEY_CHECKS = 1');

        console.log('✅ Corrección aplicada. Reintentando borrado...');

        // Reintentar el borrado
        const [retryResult] = await pool.query('DELETE FROM MESAS WHERE mesa_id = ?', [mesaId]);
        if (retryResult.affectedRows === 0) {
          return res.status(404).json({ message: 'Mesa no encontrada tras corregir' });
        }
        return res.json({ message: 'Mesa eliminada correctamente tras aplicar corrección de base de datos' });
      }
      throw dbError; // Propagar otros errores
    }
  } catch (error) {
    console.error('Error al eliminar mesa:', error);
    next(error);
  }
};