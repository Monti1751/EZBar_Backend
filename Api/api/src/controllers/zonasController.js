import pool from '../config/database.js';

// --- Obtener todas las zonas ---
export const obtenerZonas = async (req, res, next) => {
  try {
    // Obtener zonas de la tabla ZONAS
    const [rows] = await pool.query('SELECT * FROM ZONAS');

    // Mapear al formato esperado (id string o int, nombre)
    // El frontend espera { nombre: '...' } o { id: ..., nombre: ... }
    const zonas = rows.map(z => ({
      id: z.zona_id,
      nombre: z.nombre,
      name: z.nombre // Compatibilidad con frontend que puede buscar 'name'
    }));

    res.json(zonas);
  } catch (error) {
    next(error);
  }
};

// --- Crear una nueva zona ---
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
    // Controlar error si el nombre de zona ya existe (UNIQUE)
    if (error.code === 'ER_DUP_ENTRY') {
      return res.status(400).json({ message: 'La zona ya existe' });
    }
    next(error);
  }
};

// --- Obtener zona por Ubicación (Nombre) ---
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

// --- Actualizar zona (Renombrar) ---
export const actualizarZona = async (req, res, next) => {
  try {
    const { zonaId } = req.params;
    const { nombre } = req.body;

    // Detectar si zonaId es el nombre (string) o el ID numérico
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

// --- Eliminar zona ---
export const eliminarZona = async (req, res, next) => {
  const connection = await pool.getConnection();
  try {
    const { ubicacion } = req.params; // Puede ser un ID o el nombre de la zona
    await connection.beginTransaction();

    let nombreZona = ubicacion;
    let zonaId = null;

    // 1. Determinar si es ID o NOMBRE y obtener ambos
    const isNumeric = !isNaN(parseInt(ubicacion));
    if (isNumeric) {
      const [rows] = await connection.query('SELECT zona_id, nombre FROM ZONAS WHERE zona_id = ?', [ubicacion]);
      if (rows.length > 0) {
        zonaId = rows[0].zona_id;
        nombreZona = rows[0].nombre;
      }
    } else {
      const [rows] = await connection.query('SELECT zona_id, nombre FROM ZONAS WHERE nombre = ?', [ubicacion]);
      if (rows.length > 0) {
        zonaId = rows[0].zona_id;
        nombreZona = rows[0].nombre;
      }
    }

    if (!zonaId) {
      await connection.rollback();
      return res.status(404).json({ message: 'Zona no encontrada' });
    }

    console.log(`🗑️ Eliminando zona: ${nombreZona} (ID: ${zonaId}) y sus mesas asociadas.`);

    // 2. Borrar todas las mesas asociadas a esa zona (por nombre en 'ubicacion')
    const [resultMesas] = await connection.query('DELETE FROM MESAS WHERE ubicacion = ?', [nombreZona]);
    console.log(`✅ Mesas eliminadas: ${resultMesas.affectedRows}`);

    // 3. Borrar la zona
    const [resultZona] = await connection.query('DELETE FROM ZONAS WHERE zona_id = ?', [zonaId]);

    await connection.commit();
    res.status(200).json({
      message: 'Zona y mesas eliminadas correctamente',
      mesasEliminadas: resultMesas.affectedRows
    });

  } catch (error) {
    if (connection) await connection.rollback();
    console.error('Error al eliminar zona:', error);
    next(error);
  } finally {
    if (connection) connection.release();
  }
};

// --- Guardar mesas de una zona (Sincronización completa) ---
// Borra todas las mesas de la zona y las vuelve a crear con los datos recibidos
export const guardarMesasDeZona = async (req, res, next) => {
  const connection = await pool.getConnection(); // Usamos transacciones para seguridad
  try {
    await connection.beginTransaction();

    const { ubicacion } = req.params; // Nombre de la zona
    const { mesas } = req.body; // Array de mesas

    // 1. Aseguramos que la zona exista antes de insertar mesas
    const [zonaRows] = await connection.query('SELECT nombre FROM ZONAS WHERE nombre = ?', [ubicacion]);
    if (zonaRows.length === 0) {
      // Si no existe la zona se crea automáticamente (Estrategia 'upsert' básica)
      await connection.query('INSERT IGNORE INTO ZONAS (nombre) VALUES (?)', [ubicacion]);
    }

    // 2. Borrar mesas actuales de esa zona
    await connection.query('DELETE FROM MESAS WHERE ubicacion = ?', [ubicacion]);

    // 3. Insertar nuevas mesas recibidas
    if (mesas && mesas.length > 0) {
      // Preparamos array de arrays para insert masivo
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

    await connection.commit(); // Confirmar cambios
    res.json({ message: 'Mesas actualizadas' });
  } catch (error) {
    await connection.rollback(); // Deshacer cambios si algo falla
    next(error);
  } finally {
    connection.release();
  }
};
