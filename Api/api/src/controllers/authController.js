import pool from '../config/database.js';
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import { CONFIG } from '../config/constants.js';

// Usar una clave secreta segura, idealmente de variables de entorno
const JWT_SECRET = process.env.JWT_SECRET || 'tu_clave_secreta_super_segura';

export const login = async (req, res) => {
    const { username, password } = req.body;

    if (!username || !password) {
        return res.status(400).json({
            status: 'ERROR',
            message: 'Usuario y contraseña son requeridos'
        });
    }

    try {
        // Buscar usuario en la base de datos
        const [rows] = await pool.query(
            'SELECT usuario_id, empleado_id, nombre_usuario, password_hash, rol, activo FROM usuarios WHERE nombre_usuario = ?',
            [username]
        );

        if (rows.length === 0) {
            return res.status(401).json({
                status: 'ERROR',
                message: 'Credenciales inválidas'
            });
        }

        const usuario = rows[0];

        // Verificar si el usuario está activo
        if (!usuario.activo) {
            return res.status(403).json({
                status: 'ERROR',
                message: 'El usuario está desactivado'
            });
        }

        // Comparar contraseñas
        const validPassword = await bcrypt.compare(password, usuario.password_hash);

        if (!validPassword) {
            return res.status(401).json({
                status: 'ERROR',
                message: 'Credenciales inválidas'
            });
        }

        // Generar Token JWT
        const token = jwt.sign(
            {
                id: usuario.usuario_id,
                username: usuario.nombre_usuario,
                rol: usuario.rol
            },
            JWT_SECRET,
            { expiresIn: '8h' } // El token expira en 8 horas
        );

        // Obtener información del empleado si es necesario (opcional)
        // const [empleadoRows] = await pool.query('SELECT nombre FROM empleados WHERE empleado_id = ?', [usuario.empleado_id]);
        // const nombreEmpleado = empleadoRows.length > 0 ? empleadoRows[0].nombre : 'Desconocido';

        res.json({
            status: 'OK',
            message: 'Login exitoso',
            data: {
                token,
                usuario: {
                    id: usuario.usuario_id,
                    username: usuario.nombre_usuario,
                    rol: usuario.rol,
                    empleadoId: usuario.empleado_id
                }
            }
        });

    } catch (error) {
        console.error('Error en login:', error);
        res.status(500).json({
            status: 'ERROR',
            message: 'Error interno del servidor',
            error: error.message
        });
    }
};
