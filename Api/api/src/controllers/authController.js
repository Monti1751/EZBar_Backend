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
            'SELECT usuario_id AS id, nombre_usuario AS nombre, password_hash AS password, rol, activo FROM USUARIOS WHERE nombre_usuario = ?',
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

        // 2. Verificar si el usuario está activo
        if (!usuario.activo) {
            return res.status(403).json({
                status: 'ERROR',
                message: 'El usuario está desactivado'
            });
        }

        // 3. Comparar la contraseña
        // Intentar primero con bcrypt
        let validPassword = await bcrypt.compare(password, usuario.password);

        // Si falla bcrypt, probar si es texto plano (para usuarios migrados/test)
        if (!validPassword && password === usuario.password) {
            validPassword = true;
            console.warn(`⚠️ ADVERTENCIA: El usuario '${username}' está usando contraseña en texto plano. Se recomienda actualizarla.`);
        }

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
                id: usuario.id,
                username: usuario.nombre,
                rol: usuario.rol
            },
            JWT_SECRET,
            { expiresIn: '8h' } // El token caduca en 8 horas por seguridad
        );

        // 4. Enviar respuesta exitosa con el token y datos del usuario
        res.json({
            status: 'OK',
            message: 'Login exitoso',
            data: {
                token,
                usuario: {
                    id: usuario.id,
                    username: usuario.nombre,
                    rol: usuario.rol
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

// Función para verificar el token actual y devolver el rol
export const verifyToken = async (req, res) => {
    try {
        // El token viene en el header Authorization: Bearer <token>
        const authHeader = req.headers.authorization;
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(401).json({
                status: 'ERROR',
                message: 'No se proporcionó token de autorización'
            });
        }

        const token = authHeader.split(' ')[1];

        // Verificar el token
        jwt.verify(token, JWT_SECRET, (err, decoded) => {
            if (err) {
                return res.status(401).json({
                    status: 'ERROR',
                    message: 'Token inválido o expirado'
                });
            }

            // Si es válido, devolver los datos del usuario que están en el token
            res.json({
                status: 'OK',
                message: 'Token válido',
                data: {
                    usuario: {
                        id: decoded.id,
                        username: decoded.username,
                        rol: decoded.rol
                    }
                }
            });
        });

    } catch (error) {
        console.error('Error en verifyToken:', error);
        res.status(500).json({
            status: 'ERROR',
            message: 'Error interno del servidor'
        });
    }
};
