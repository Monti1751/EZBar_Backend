import pool from '../config/database.js';
import logger from '../logger.js';

// --- Obtener Productos ---
export const obtenerProductos = async (req, res, next) => {
  try {
    const [rows] = await pool.query(`
      SELECT 
        p.producto_id,
        p.nombre,
        p.descripcion,
        p.precio,
        p.categoria_id,
        p.imagen,
        p.url_imagen,
        p.ingredientes,
        p.extras,
        p.alergenos,
        c.nombre as categoria_nombre
      FROM productos p
      LEFT JOIN categorias c ON p.categoria_id = c.categoria_id
    `);
    logger.info(`🔍 Consulta productos: ${rows.length} filas obtenidas.`);
    if (rows.length === 0) {
      logger.warn('⚠️ La consulta de productos devolvió 0 resultados. Comprobar base de datos.');
    }

    const productos = rows.map(p => ({
      producto_id: p.producto_id,
      nombre: p.nombre,
      descripcion: p.descripcion || '',
      precio: typeof p.precio === 'string' ? parseFloat(p.precio) : p.precio,
      categoria_id: p.categoria_id,
      imagen_blob: p.imagen ? (Buffer.isBuffer(p.imagen) ? p.imagen.toString('base64') : p.imagen) : '',
      imagen_url: p.url_imagen || '',
      ingredientes: p.ingredientes || '',
      extras: p.extras || '',
      alergenos: p.alergenos || '',
      categoria: {
        categoria_id: p.categoria_id,
        nombre: p.categoria_nombre || ''
      }
    }));

    res.json(productos);
  } catch (error) {
    next(error);
  }
};

// --- Crear Producto ---
export const crearProducto = async (req, res, next) => {
  try {
    const { nombre, descripcion, precio, categoria_id, imagenBlob, ingredientes, extras, alergenos } = req.body;

    // Validar campos requeridos
    if (!nombre || precio === undefined) {
      return res.status(400).json({ error: 'Faltan campos requeridos: nombre y precio' });
    }

    // Convertir Base64 a Buffer para almacenamiento binario
    const imagenBuffer = imagenBlob ? Buffer.from(imagenBlob, 'base64') : null;

    const [result] = await pool.query(
      'INSERT INTO productos (nombre, descripcion, precio, categoria_id, imagen, ingredientes, extras, alergenos) VALUES (?, ?, ?, ?, ?, ?, ?, ?)',
      [nombre, descripcion || '', precio, categoria_id || null, imagenBuffer, ingredientes || '', extras || '', alergenos || '']
    );

    res.status(201).json({
      producto_id: result.insertId,
      nombre,
      descripcion: descripcion || '',
      precio: typeof precio === 'string' ? parseFloat(precio) : precio,
      categoria_id: categoria_id || null,
      imagen_blob: imagenBlob || '', // Devolver el base64 original que enviaron
      imagen_url: '',
      categoria: {
        categoria_id: categoria_id || null,
        nombre: ''
      }
    });
  } catch (error) {
    console.error('Error en crearProducto:', error.message);
    next(error);
  }
};

// --- Actualizar Producto ---
export const actualizarProducto = async (req, res, next) => {
  try {
    const { id } = req.params;
    const { nombre, descripcion, precio, categoria_id, imagenBlob, ingredientes, extras, alergenos } = req.body;

    // Validar que ID sea válido
    if (!id || isNaN(id)) {
      return res.status(400).json({ error: 'ID de producto inválido' });
    }

    // Validar campos requeridos
    if (!nombre || precio === undefined) {
      return res.status(400).json({ error: 'Faltan campos requeridos: nombre y precio' });
    }

    let query = 'UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, categoria_id = ?, ingredientes = ?, extras = ?, alergenos = ?';
    let params = [nombre, descripcion || '', precio, categoria_id || null, ingredientes || '', extras || '', alergenos || ''];

    if (imagenBlob !== undefined) {
      query += ', imagen = ?';
      const imagenBuffer = imagenBlob ? Buffer.from(imagenBlob, 'base64') : null;
      params.push(imagenBuffer);
    }

    query += ' WHERE producto_id = ?';
    params.push(parseInt(id));

    const [result] = await pool.query(query, params);

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: 'Producto no encontrado' });
    }

    // Devolver el producto actualizado con todos los campos
    const [rows] = await pool.query(
      `SELECT p.*, c.nombre as categoria_nombre 
       FROM productos p 
       LEFT JOIN categorias c ON p.categoria_id = c.categoria_id 
       WHERE p.producto_id = ? `,
      [parseInt(id)]
    );

    if (!rows || rows.length === 0) {
      return res.status(404).json({ error: 'Producto no encontrado después de actualizar' });
    }

    const producto = rows[0];
    res.json({
      producto_id: producto.producto_id,
      nombre: producto.nombre,
      descripcion: producto.descripcion || '',
      precio: typeof producto.precio === 'string' ? parseFloat(producto.precio) : producto.precio,
      categoria_id: producto.categoria_id,
      imagen_blob: producto.imagen ? (Buffer.isBuffer(producto.imagen) ? producto.imagen.toString('base64') : producto.imagen) : '',
      imagen_url: producto.url_imagen || '',
      ingredientes: producto.ingredientes || '',
      extras: producto.extras || '',
      alergenos: producto.alergenos || '',
      categoria: {
        categoria_id: producto.categoria_id,
        nombre: producto.categoria_nombre || ''
      }
    });
  } catch (error) {
    console.error('Error en actualizarProducto:', error.message);
    next(error);
  }
};

// --- Eliminar Producto ---
export const eliminarProducto = async (req, res, next) => {
  try {
    const { id } = req.params;

    if (!id || isNaN(id)) {
      return res.status(400).json({ error: 'ID de producto inválido' });
    }

    const [result] = await pool.query('DELETE FROM productos WHERE producto_id = ?', [parseInt(id)]);

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: 'Producto no encontrado' });
    }

    res.status(204).send();
  } catch (error) {
    console.error('Error en eliminarProducto:', error.message);
    next(error);
  }
};