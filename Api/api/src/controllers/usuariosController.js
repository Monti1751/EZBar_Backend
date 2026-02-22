import pool from '../config/database.js';
import bcrypt from 'bcryptjs';

// --- Obtener todos los usuarios ---
export const obtenerUsuarios = async (req, res, next) => {
    try {
        const [rows] = await pool.query(
            'SELECT usuario_id AS id, nombre_usuario AS username, rol, activo, fecha_creacion FROM USUARIOS ORDER BY nombre_usuario ASC'
        );
        res.json(rows);
    } catch (error) {
        next(error);
    }
};

// --- Obtener usuario por ID ---
export const obtenerUsuarioPorId = async (req, res, next) => {
    try {
        const { id } = req.params;
        const [rows] = await pool.query(
            'SELECT usuario_id AS id, nombre_usuario AS username, rol, activo, fecha_creacion FROM USUARIOS WHERE usuario_id = ?',
            [id]
        );

        if (rows.length === 0) {
            return res.status(404).json({ message: 'Usuario no encontrado' });
        }

        res.json(rows[0]);
    } catch (error) {
        next(error);
    }
};

// --- Crear usuario ---
export const crearUsuario = async (req, res, next) => {
    try {
        const { username, password, rol, activo, empleado_id } = req.body;

        if (!username || !password) {
            return res.status(400).json({ error: 'Nombre de usuario y contraseña son obligatorios' });
        }

        // Encriptar contraseña
        const salt = await bcrypt.genSalt(10);
        const passwordHash = await bcrypt.hash(password, salt);

        // Si no se especifica empleado_id, usamos 1 (Sistema) por defecto
        // para cumplir con la restricción NOT NULL de la DB.
        const finalEmpleadoId = empleado_id || 1;

        const [result] = await pool.query(
            'INSERT INTO USUARIOS (nombre_usuario, password_hash, rol, activo, empleado_id) VALUES (?, ?, ?, ?, ?)',
            [username, passwordHash, rol || 'usuario', activo !== undefined ? activo : true, finalEmpleadoId]
        );

        res.status(201).json({
            id: result.insertId,
            username,
            rol: rol || 'usuario',
            activo: activo !== undefined ? activo : true,
            message: 'Usuario creado correctamente'
        });
    } catch (error) {
        if (error.code === 'ER_DUP_ENTRY') {
            return res.status(409).json({ error: 'El nombre de usuario ya existe' });
        }
        next(error);
    }
};

// --- Actualizar usuario ---
export const actualizarUsuario = async (req, res, next) => {
    try {
        const { id } = req.params;
        const { username, password, rol, activo } = req.body;

        let updates = [];
        let params = [];

        if (username) {
            updates.push('nombre_usuario = ?');
            params.push(username);
        }
        if (password) {
            const salt = await bcrypt.genSalt(10);
            const passwordHash = await bcrypt.hash(password, salt);
            updates.push('password_hash = ?');
            params.push(passwordHash);
        }
        if (rol) {
            updates.push('rol = ?');
            params.push(rol);
        }
        if (activo !== undefined) {
            updates.push('activo = ?');
            params.push(activo);
        }

        if (updates.length === 0) {
            return res.status(400).json({ message: 'No se enviaron datos para actualizar' });
        }

        params.push(id);
        const query = `UPDATE USUARIOS SET ${updates.join(', ')} WHERE usuario_id = ?`;

        const [result] = await pool.query(query, params);

        if (result.affectedRows === 0) {
            return res.status(404).json({ message: 'Usuario no encontrado' });
        }

        res.json({ message: 'Usuario actualizado correctamente' });
    } catch (error) {
        if (error.code === 'ER_DUP_ENTRY') {
            return res.status(409).json({ error: 'El nombre de usuario ya existe' });
        }
        next(error);
    }
};

// --- Eliminar usuario ---
export const eliminarUsuario = async (req, res, next) => {
    try {
        const { id } = req.params;
        const [result] = await pool.query('DELETE FROM USUARIOS WHERE usuario_id = ?', [id]);

        if (result.affectedRows === 0) {
            return res.status(404).json({ message: 'Usuario no encontrado' });
        }

        res.json({ message: 'Usuario eliminado correctamente' });
    } catch (error) {
        next(error);
    }
};
