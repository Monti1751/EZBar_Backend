import express from 'express';
import cors from 'cors';
import compression from 'compression';
import { CONFIG } from './config/constants.js';
import pool from './config/database.js';
import logger from './logger.js';

// Import middleware
import {
  errorHandler,
  requestLogger,
  errorLogger,
  securityHeaders,
  rateLimiter,
  getCorsOptions,
  cacheMiddleware,
  cacheInvalidationMiddleware,
  timeoutMiddleware,
  memoryMiddleware
} from './middleware/index.js';

// Import routes
import mesasRoutes from './routes/mesasRoutes.js';
import pedidosRoutes from './routes/pedidosRoutes.js';
import productosRoutes from './routes/productosRoutes.js';
import zonasRoutes from './routes/zonasRoutes.js';
import categoriasRoutes from './routes/categoriasRoutes.js';
import authRoutes from './routes/authRoutes.js';

// Import utilities
import { verificarBackend, memoryMonitor } from './utils/index.js';
import { cacheService } from './services/index.js';

// Crear la aplicación Express
const app = express();

// --- Global Middleware (Order matters!) ---

// 1. Security headers (first for all requests)
app.use(securityHeaders());

// 2. Request timeout
app.use(timeoutMiddleware());

// 3. Request logging
app.use(requestLogger);

// 4. CORS
app.use(cors(getCorsOptions()));

// 5. Compression (gzip)
app.use(compression());

// 6. Body parsing
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));

// 7. Rate limiting (only in production or if explicitly enabled)
if (CONFIG.IS_PRODUCTION) {
  app.use(rateLimiter);
}

// --- API Routes ---

// Apply cache middleware to routes that benefit from caching
app.use('/api/mesas', cacheInvalidationMiddleware(), mesasRoutes);
app.use('/api/pedidos', cacheInvalidationMiddleware(), pedidosRoutes);
app.use('/api/productos', cacheInvalidationMiddleware(), productosRoutes);
app.use('/api/zonas', cacheInvalidationMiddleware(), zonasRoutes);
app.use('/api/categorias', cacheInvalidationMiddleware(), categoriasRoutes);
app.use('/api/auth', authRoutes); // No caching for auth

// --- Health & Monitoring Endpoints ---

/**
 * Health check endpoint
 * Returns API status, database status, backend status, and memory metrics
 */
app.get('/api/health', memoryMiddleware, async (req, res) => {
  const backendStatus = await verificarBackend();

  try {
    await pool.query('SELECT 1');
    res.json({
      status: 'OK',
      environment: CONFIG.NODE_ENV,
      message: 'API Node.js funcionando correctamente',
      timestamp: new Date().toISOString(),
      backend: {
        url: CONFIG.BACKEND_URL,
        status: backendStatus ? 'ONLINE' : 'OFFLINE'
      },
      database: {
        status: 'ONLINE',
        host: CONFIG.DB.HOST,
        name: CONFIG.DB.NAME
      },
      memory: req.memoryStats,
      cache: cacheService ? cacheService.getStats() : { enabled: false }
    });
  } catch (error) {
    res.json({
      status: 'DEGRADED',
      environment: CONFIG.NODE_ENV,
      message: 'API funcionando pero con problemas en base de datos',
      timestamp: new Date().toISOString(),
      backend: {
        url: CONFIG.BACKEND_URL,
        status: backendStatus ? 'ONLINE' : 'OFFLINE'
      },
      database: {
        status: 'OFFLINE',
        error: error.message
      },
      memory: req.memoryStats,
      cache: cacheService ? cacheService.getStats() : { enabled: false }
    });
  }
});

/**
 * Cache statistics endpoint
 */
app.get('/api/cache/stats', (req, res) => {
  if (!cacheService) {
    return res.json({ enabled: false });
  }

  res.json(cacheService.getStats());
});

/**
 * Cache management endpoint (flush cache)
 */
app.post('/api/cache/flush', (req, res) => {
  if (!cacheService) {
    return res.status(400).json({ error: 'Cache is not enabled' });
  }

  cacheService.flush();
  logger.info('Cache flushed via API endpoint');

  res.json({
    success: true,
    message: 'Cache flushed successfully'
  });
});

/**
 * Memory statistics endpoint
 */
app.get('/api/memory/stats', memoryMiddleware, (req, res) => {
  res.json(req.memoryStats);
});

/**
 * Force garbage collection endpoint (only if --expose-gc is enabled)
 */
app.post('/api/memory/gc', (req, res) => {
  if (!global.gc) {
    return res.status(400).json({
      error: 'Garbage collection not exposed',
      message: 'Start Node.js with --expose-gc flag to enable'
    });
  }

  memoryMonitor.forceGC();

  res.json({
    success: true,
    message: 'Garbage collection triggered'
  });
});

/**
 * Root endpoint - Basic test
 */
app.get("/", async (req, res) => {
  try {
    const [rows] = await pool.query("SELECT 'Hola desde MariaDB!' AS mensaje");
    res.json({
      ...rows[0],
      environment: CONFIG.NODE_ENV,
      version: '2.0.0'
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// --- Error Handling Middleware (Must be last!) ---
app.use(errorLogger);
app.use(errorHandler);

export default app;