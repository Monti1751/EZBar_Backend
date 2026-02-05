import mysql from "mysql2/promise";
import { CONFIG } from './constants.js';
import logger from '../logger.js';

const pool = mysql.createPool({
  host: CONFIG.DB.HOST,
  port: CONFIG.DB.PORT,
  user: CONFIG.DB.USER,
  password: CONFIG.DB.PASSWORD,
  database: CONFIG.DB.NAME,
  waitForConnections: true,
  connectionLimit: CONFIG.DB.CONNECTION_LIMIT,
  queueLimit: CONFIG.DB.QUEUE_LIMIT,
  connectTimeout: CONFIG.DB.CONNECT_TIMEOUT,
  acquireTimeout: CONFIG.DB.ACQUIRE_TIMEOUT,
  enableKeepAlive: true,
  keepAliveInitialDelay: 0
});

// Verificar conexión al iniciar
pool.getConnection()
  .then(conn => {
    logger.info("✅ Conexión exitosa a MariaDB", {
      host: CONFIG.DB.HOST,
      database: CONFIG.DB.NAME,
      connectionLimit: CONFIG.DB.CONNECTION_LIMIT
    });
    conn.release();
  })
  .catch(err => {
    logger.error("❌ Error conectando a MariaDB:", {
      error: err.message,
      host: CONFIG.DB.HOST,
      database: CONFIG.DB.NAME
    });
  });

// Add query wrapper for logging
const originalQuery = pool.query.bind(pool);
pool.query = async function (...args) {
  const startTime = Date.now();
  try {
    const result = await originalQuery(...args);
    const duration = Date.now() - startTime;

    // Log slow queries (> 1 second)
    if (duration > 1000) {
      logger.warn(`Slow query detected (${duration}ms)`, {
        query: typeof args[0] === 'string' ? args[0].substring(0, 100) : 'prepared statement'
      });
    } else if (CONFIG.IS_DEBUG) {
      logger.database(
        typeof args[0] === 'string' ? args[0] : 'prepared statement',
        duration
      );
    }

    return result;
  } catch (error) {
    logger.error('Database query error:', {
      error: error.message,
      query: typeof args[0] === 'string' ? args[0].substring(0, 100) : 'prepared statement'
    });
    throw error;
  }
};

export default pool;