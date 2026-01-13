import pool from '../config/database.js';

// Obtener todas las zonas
export const obtenerZonas = async (req, res, next) => {
  try {
    // Obtener zonas de la tabla ZONAS
    const [rows] = await pool.query('SELECT * FROM ZONAS');

    // Mapear al formato esperado (id string o int, nombre)
    // El frontend espera { nombre: '...' } o { id: ..., nombre: ... }
    const zonas = rows.map(z => ({
      id: z.zona_id,
      nombre: z.nombre,
      name: z.nombre // Compatibilidad
    }));

    res.json(zonas);
  } catch (error) {
    next(error);
  }
};

// Crear una nueva zona
export const crearZona = async (req, res, next) => {
  try {
    const { nombre } = req.body;
    if (!nombre) return res.status(400).json({ message: 'El nombre de la zona es obligatorio' });

    // Insertar en tabla ZONAS
    const [result] = await pool.query('INSERT INTO ZONAS (nombre) VALUES (?)', [nombre]);

    res.status(201).json({
      id: result.insertId,
      nombre,
      name: nombre
    });
  } catch (error) {
    if (error.code === 'ER_DUP_ENTRY') {
      return res.status(400).json({ message: 'La zona ya existe' });
    }
    next(error);
  }
};

// Obtener zona por Ubicación (Nombre)
export const obtenerZonaPorUbicacion = async (req, res, next) => {
  try {
    const { ubicacion } = req.params;

    // Buscar en tabla ZONAS
    const [rows] = await pool.query('SELECT * FROM ZONAS WHERE nombre = ?', [ubicacion]);

    if (rows.length > 0) {
      res.json({
        id: rows[0].zona_id,
        nombre: rows[0].nombre,
        name: rows[0].nombre
      });
    } else {
      res.status(404).json({ error: 'Zona no encontrada' });
    }
  } catch (error) {
    next(error);
  }
};

// Actualizar zona
export const actualizarZona = async (req, res, next) => {
  try {
    const { zonaId } = req.params;
    const { nombre } = req.body;

    // Si zonaId es el nombre (string)
    const isIdNumeric = !isNaN(parseInt(zonaId));

    let query, params;
    if (isIdNumeric) {
      query = 'UPDATE ZONAS SET nombre = ? WHERE zona_id = ?';
      params = [nombre, zonaId];
    } else {
      query = 'UPDATE ZONAS SET nombre = ? WHERE nombre = ?';
      params = [nombre, zonaId];
    }

    const [result] = await pool.query(query, params);

    if (result.affectedRows === 0) {
      return res.status(404).json({ message: 'Zona no encontrada' });
    }

    res.json({ id: zonaId, nombre });
  } catch (error) {
    next(error);
  }
};

// Eliminar zona
export const eliminarZona = async (req, res, next) => {
  try {
    const { ubicacion } = req.params; // La ruta es DELETE /api/zonas/:ubicacion

    // Borrar de ZONAS (Cascade borrará mesas)
    const [result] = await pool.query('DELETE FROM ZONAS WHERE nombre = ?', [ubicacion]);

    if (result.affectedRows === 0) {
      // Intentar borrar por ID si fuese un número
      const zonaId = parseInt(ubicacion);
      if (!isNaN(zonaId)) {
        const [resId] = await pool.query('DELETE FROM ZONAS WHERE zona_id = ?', [zonaId]);
        if (resId.affectedRows > 0) return res.status(200).send();
      }
      return res.status(404).json({ message: 'Zona no encontrada' });
    }

    res.status(200).send();
  } catch (error) {
    next(error);
  }
};

// Guardar mesas de una zona (Sync)
export const guardarMesasDeZona = async (req, res, next) => {
  const connection = await pool.getConnection();
  try {
    await connection.beginTransaction();

    const { ubicacion } = req.params; // Nombre de la zona
    const { mesas } = req.body; // Array de mesas

    // 1. Borrar mesas actuales de esa zona
    // Nota: Si la zona no existe en ZONAS, esto no afecta pero el INSERT fallará por FK si 'ubicacion' no está en ZONAS.
    // Aseguramos que la zona exista.
    const [zonaRows] = await connection.query('SELECT nombre FROM ZONAS WHERE nombre = ?', [ubicacion]);
    if (zonaRows.length === 0) {
      // Si no existe la zona (raro si venimos del frontend), la creamos?
      // Mejor rebotamos error, o la creamos implícitamente.
      await connection.query('INSERT IGNORE INTO ZONAS (nombre) VALUES (?)', [ubicacion]);
    }

    await connection.query('DELETE FROM MESAS WHERE ubicacion = ?', [ubicacion]);

    // 2. Insertar nuevas mesas
    if (mesas && mesas.length > 0) {
      const values = mesas.map(m => [
        m.numero_mesa,
        m.capacidad,
        ubicacion,
        m.estado || 'libre',
        m.pos_x || 0,
        m.pos_y || 0
      ]);

      await connection.query(
        'INSERT INTO MESAS (numero_mesa, capacidad, ubicacion, estado, pos_x, pos_y) VALUES ?',
        [values]
      );
    }

    await connection.commit();
    res.json({ message: 'Mesas actualizadas' });
  } catch (error) {
    await connection.rollback();
    next(error);
  } finally {
    connection.release();
  }
};
