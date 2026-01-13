import express from 'express';
import cors from 'cors';
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

const app = express();

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Rutas
app.use('/api/mesas', mesasRoutes);
app.use('/api/pedidos', pedidosRoutes);
app.use('/api/productos', productosRoutes);
app.use('/api/zonas', zonasRoutes);
app.use('/api/categorias', categoriasRoutes);
app.use('/api/auth', authRoutes);


// Health check
app.get('/api/health', async (req, res) => {
  const backendStatus = await verificarBackend();

  try {
    await pool.query('SELECT 1');
    res.json({
      status: 'OK',
      message: 'API Node.js funcionando correctamente',
      backend: CONFIG.BACKEND_URL,
      backendStatus: backendStatus ? 'ONLINE' : 'OFFLINE',
      database: 'ONLINE'
    });
  } catch (error) {
    res.json({
      status: 'OK',
      message: 'API Node.js funcionando',
      backend: CONFIG.BACKEND_URL,
      backendStatus: backendStatus ? 'ONLINE' : 'OFFLINE',
      database: 'OFFLINE',
      databaseError: error.message
    });
  }
});

// Ruta de prueba (tu endpoint original)
app.get("/", async (req, res) => {
  try {
    const [rows] = await pool.query("SELECT 'Hola desde MariaDB!' AS mensaje");
    res.json(rows[0]);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// Manejo de errores (debe ir al final)
app.use(errorHandler);

export default app;