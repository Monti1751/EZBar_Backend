import pool from '../config/database.js';
import bcrypt from 'bcryptjs'; // Librería para encriptar y comparar contraseñas
import jwt from 'jsonwebtoken'; // Librería para generar tokens de sesión (JSON Web Tokens)
import { CONFIG } from '../config/constants.js';

// Usar una clave secreta segura para firmar los tokens.
// Si no está en las variables de entorno, se usa una por defecto (SOLO para desarrollo).
const JWT_SECRET = process.env.JWT_SECRET || 'tu_clave_secreta_super_segura';

// Función para iniciar sesión (Login)
export const login = async (req, res) => {
    // Extraer usuario y contraseña del cuerpo de la petición (JSON)
    const { username, password } = req.body;

    // Validación básica: verificar que ambos campos existan
    if (!username || !password) {
        return res.status(400).json({
            status: 'ERROR',
            message: 'Usuario y contraseña son requeridos'
        });
    }

    try {
        // 1. Buscar usuario en la base de datos por su nombre de usuario
        const [rows] = await pool.query(
            'SELECT usuario_id, empleado_id, nombre_usuario, password_hash, rol, activo FROM usuarios WHERE nombre_usuario = ?',
            [username]
        );

        // Si no se encuentra ningún usuario
        if (rows.length === 0) {
            return res.status(401).json({
                status: 'ERROR',
                message: 'Credenciales inválidas'
            });
        }

        const usuario = rows[0];

        // 2. Verificar si el usuario está activo (puede haber sido deshabilitado por un admin)
        if (!usuario.activo) {
            return res.status(403).json({
                status: 'ERROR',
                message: 'El usuario está desactivado'
            });
        }

        // 3. Comparar la contraseña ingresada con la encriptada en la base de datos
        const validPassword = await bcrypt.compare(password, usuario.password_hash);

        if (!validPassword) {
            return res.status(401).json({
                status: 'ERROR',
                message: 'Credenciales inválidas'
            });
        }

        // 4. Generar Token JWT
        // Este token servirá para identificar al usuario en futuras peticiones sin enviar la contraseña
        const token = jwt.sign(
            {
                id: usuario.usuario_id,
                username: usuario.nombre_usuario,
                rol: usuario.rol
            },
            JWT_SECRET,
            { expiresIn: '8h' } // El token caduca en 8 horas por seguridad
        );

        // 5. Enviar respuesta exitosa con el token y datos del usuario
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
        // Manejo de errores internos del servidor (fallo de base de datos, etc.)
        console.error('Error en login:', error);
        res.status(500).json({
            status: 'ERROR',
            message: 'Error interno del servidor',
            error: error.message
        });
    }
};
