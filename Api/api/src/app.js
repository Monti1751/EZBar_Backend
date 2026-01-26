import express from 'express';
import cors from 'cors'; // Permite peticiones desde otros dominios (Cross-Origin Resource Sharing)
import { errorHandler } from './middleware/errorHandler.js';
import mesasRoutes from './routes/mesasRoutes.js';
import pedidosRoutes from './routes/pedidosRoutes.js';
import productosRoutes from './routes/productosRoutes.js';
import zonasRoutes from './routes/zonasRoutes.js';
import categoriasRoutes from './routes/categoriasRoutes.js';
import authRoutes from './routes/authRoutes.js';
import { verificarBackend } from './utils/retryHelper.js';
import { CONFIG } from './config/constants.js';
import pool from './config/database.js';

// Crear la aplicación Express
const app = express();

// --- Configuración Global (Middleware) ---
app.use(cors()); // Habilitar CORS para permitir conexión desde el móvil
app.use(express.json()); // Permitir que el servidor entienda JSON en el cuerpo de las peticiones
app.use(express.urlencoded({ extended: true })); // Permitir datos codificados en URL

// --- Definición de Rutas API ---
// Aquí asociamos cada prefijo de URL a su archivo de rutas correspondiente
app.use('/api/mesas', mesasRoutes); // Rutas para gestión de mesas (ver, crear, actualizar)
app.use('/api/pedidos', pedidosRoutes); // Rutas para gestión de pedidos
app.use('/api/productos', productosRoutes); // Rutas para productos
app.use('/api/zonas', zonasRoutes); // Rutas para zonas del bar
app.use('/api/categorias', categoriasRoutes); // Rutas para categorías de productos
app.use('/api/auth', authRoutes); // Rutas para autenticación (login)


// --- Ruta Health Check (Comprobación de Estado) ---
// Usada por el sistema para verificar que el API y la Base de Datos funcionan
app.get('/api/health', async (req, res) => {
  const backendStatus = await verificarBackend();

  try {
    // Intentar una consulta simple a la base de datos para verificar conexión
    await pool.query('SELECT 1');
    res.json({
      status: 'OK',
      message: 'API Node.js funcionando correctamente',
      backend: CONFIG.BACKEND_URL,
      backendStatus: backendStatus ? 'ONLINE' : 'OFFLINE',
      database: 'ONLINE'
    });
  } catch (error) {
    // Si falla la BD, respondemos con detalle del error
    res.json({
      status: 'OK', // La API en sí funciona, aunque la BD falle
      message: 'API Node.js funcionando',
      backend: CONFIG.BACKEND_URL,
      backendStatus: backendStatus ? 'ONLINE' : 'OFFLINE',
      database: 'OFFLINE',
      databaseError: error.message
    });
  }
});

// Ruta de prueba básica
app.get("/", async (req, res) => {
  try {
    const [rows] = await pool.query("SELECT 'Hola desde MariaDB!' AS mensaje");
    res.json(rows[0]);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// Middleware de manejo de errores global (siempre debe ir al final)
app.use(errorHandler);

export default app;