import { getEnv } from './env.js';

export const CONFIG = {
  // Environment
  NODE_ENV: process.env.NODE_ENV || 'development',
  IS_PRODUCTION: process.env.NODE_ENV === 'production',
  IS_DEVELOPMENT: process.env.NODE_ENV === 'development',
  IS_DEBUG: process.env.DEBUG === 'true',

  // Server
  PORT: getEnv('PORT', 3000, 'number'),
  HTTPS_PORT: getEnv('HTTPS_PORT', 3443, 'number'),
  HTTPS_ENABLED: getEnv('HTTPS_ENABLED', 'true', 'boolean'),

  // Backend
  BACKEND_URL: getEnv('BACKEND_URL', 'http://localhost:8080/api'),
  HEARTBEAT_INTERVAL: 30000, // 30 seconds

  // Database
  DB: {
    HOST: getEnv('DB_HOST', 'localhost'),
    PORT: getEnv('DB_PORT', 3306, 'number'),
    NAME: getEnv('DB_NAME', 'EZBarDB'),
    USER: getEnv('DB_USER', 'root'),
    PASSWORD: getEnv('DB_PASSWORD', ''),
    CONNECTION_LIMIT: getEnv('DB_CONNECTION_LIMIT', 10, 'number'),
    QUEUE_LIMIT: getEnv('DB_QUEUE_LIMIT', 0, 'number'),
    CONNECT_TIMEOUT: getEnv('DB_CONNECT_TIMEOUT', 10000, 'number'),
    ACQUIRE_TIMEOUT: getEnv('DB_ACQUIRE_TIMEOUT', 10000, 'number')
  },

  // Request Configuration
  RETRY_ATTEMPTS: getEnv('RETRY_ATTEMPTS', 3, 'number'),
  RETRY_DELAY: getEnv('RETRY_DELAY', 1000, 'number'),
  REQUEST_TIMEOUT: getEnv('REQUEST_TIMEOUT', 5000, 'number'),
  GLOBAL_TIMEOUT: getEnv('GLOBAL_TIMEOUT', 30000, 'number'),

  // Cache Configuration
  CACHE: {
    ENABLED: getEnv('CACHE_ENABLED', 'true', 'boolean'),
    TTL: getEnv('CACHE_TTL', 300, 'number'), // 5 minutes default
    CHECK_PERIOD: getEnv('CACHE_CHECK_PERIOD', 60, 'number') // 1 minute
  },

  // Memory Configuration
  MEMORY: {
    CHECK_INTERVAL: getEnv('MEMORY_CHECK_INTERVAL', 60000, 'number'), // 1 minute
    HEAP_WARNING_THRESHOLD: getEnv('MEMORY_HEAP_WARNING_THRESHOLD', 0.85, 'float'),
    HEAP_CRITICAL_THRESHOLD: getEnv('MEMORY_HEAP_CRITICAL_THRESHOLD', 0.95, 'float')
  },

  // Security
  SECURITY: {
    RATE_LIMIT_WINDOW_MS: getEnv('RATE_LIMIT_WINDOW_MS', 60000, 'number'),
    RATE_LIMIT_MAX_REQUESTS: getEnv('RATE_LIMIT_MAX_REQUESTS', 100, 'number'),
    CORS_ORIGIN: getEnv('CORS_ORIGIN', '*')
  },

  // SSL Configuration
  SSL: {
    KEY_PATH: getEnv('SSL_KEY_PATH', './certs/dev-key.pem'),
    CERT_PATH: getEnv('SSL_CERT_PATH', './certs/dev-cert.pem'),
    AUTO_GENERATE: getEnv('SSL_AUTO_GENERATE', 'true', 'boolean')
  },

  // Logging
  LOG_LEVEL: getEnv('LOG_LEVEL', 'info')
};