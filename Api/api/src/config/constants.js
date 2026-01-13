export const CONFIG = {
  RETRY_ATTEMPTS: parseInt(process.env.RETRY_ATTEMPTS) || 3,
  RETRY_DELAY: parseInt(process.env.RETRY_DELAY) || 1000,
  REQUEST_TIMEOUT: parseInt(process.env.REQUEST_TIMEOUT) || 5000,
  BACKEND_URL: process.env.BACKEND_URL || 'http://localhost:8080/api',
  HEARTBEAT_INTERVAL: 30000 // 30 segundos
};