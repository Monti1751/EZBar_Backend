import pool from '../config/database.js';

// --- Obtener Categorías ---
export const obtenerCategorias = async (req, res, next) => {
  try {
    const [rows] = await pool.query('SELECT * FROM categorias');

    const categorias = rows.map(c => ({
      categoria_id: c.categoria_id,
      nombre: c.nombre,
      descripcion: c.descripcion
    }));

    res.json(categorias);
  } catch (error) {
    next(error);
  }
};

// --- Crear Categoría ---
export const crearCategoria = async (req, res, next) => {
  try {
    const { nombre } = req.body;
    if (!nombre) {
      return res.status(400).json({ error: 'Falta el nombre de la categoría' });
    }

    const [result] = await pool.query(
      'INSERT INTO categorias (nombre) VALUES (?)',
      [nombre]
    );

    res.status(201).json({
      categoria_id: result.insertId,
      nombre,
      descripcion: ''
    });
  } catch (error) {
    next(error);
  }
};

// --- Eliminar Categoría ---
export const eliminarCategoria = async (req, res, next) => {
  try {
    const { id } = req.params;

    if (!id || isNaN(id)) {
      return res.status(400).json({ error: 'ID de categoría inválido' });
    }

    // Con ON DELETE CASCADE en la BD, no necesitamos comprobar manualmente los productos.
    // El borrado de una categoría borrará automáticamente sus platos asociados.

    const [result] = await pool.query('DELETE FROM categorias WHERE categoria_id = ?', [parseInt(id)]);

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: 'Categoría no encontrada' });
    }

    res.status(204).send();
  } catch (error) {
    next(error);
  }
};
