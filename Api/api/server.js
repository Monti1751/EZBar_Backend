// Load environment configuration first
import './src/config/env.js';

import https from 'https';
import http from 'http';
import app from './src/app.js';
import dgram from 'dgram';
import { networkInterfaces } from 'os';
import logger from './src/logger.js';
import { CONFIG } from './src/config/constants.js';
import { getSSLCredentials } from './src/config/ssl.js';
import { verificarBackend } from './src/utils/retryHelper.js';
import { memoryMonitor, gracefulShutdown } from './src/utils/index.js';

// Ports
const PORT = CONFIG.PORT;
const HTTPS_PORT = CONFIG.HTTPS_PORT;
const UDP_PORT = 3001;

// --- Configuración del Servicio de Descubrimiento UDP ---
const udpServer = dgram.createSocket('udp4');

udpServer.on('error', (err) => {
  logger.error(`UDP Server error:`, { error: err.stack });
  udpServer.close();
});

udpServer.on('message', (msg, rinfo) => {
  if (msg.toString().trim() === 'EZBAR_DISCOVER') {
    logger.info(`Solicitud de descubrimiento desde ${rinfo.address}:${rinfo.port}`);

    const nets = networkInterfaces();
    let localIp = '127.0.0.1';

    for (const name of Object.keys(nets)) {
      for (const net of nets[name]) {
        if (net.family === 'IPv4' && !net.internal) {
          if (net.address.startsWith('192') || net.address.startsWith('172') || net.address.startsWith('10')) {
            localIp = net.address;
          }
        }
      }
    }

    const message = JSON.stringify({
      ip: localIp,
      port: PORT,
      httpsPort: HTTPS_PORT,
      service: 'EZBar_API',
      environment: CONFIG.NODE_ENV
    });

    udpServer.send(message, rinfo.port, rinfo.address, (err) => {
      if (err) logger.error('Error enviando respuesta de descubrimiento:', err);
      else logger.info(`Respuesta de descubrimiento enviada a ${rinfo.address}: ${message}`);
    });
  }
});

udpServer.bind(UDP_PORT, () => {
  logger.info(`📡 UDP Discovery escuchando en el puerto ${UDP_PORT}`);
});

// --- Iniciar Servidores HTTP/HTTPS ---

let httpServer;
let httpsServer;

// Start HTTP server
httpServer = http.createServer(app);
httpServer.listen(PORT, '0.0.0.0', () => {
  logger.info('='.repeat(60));
  logger.info(`🚀 HTTP Server running on http://0.0.0.0:${PORT}`);
  logger.info(`🌍 Environment: ${CONFIG.NODE_ENV}`);
  logger.info(`📊 Database: ${CONFIG.DB.NAME}@${CONFIG.DB.HOST}`);
  logger.info(`🔗 Backend URL: ${CONFIG.BACKEND_URL}`);
  logger.info('='.repeat(60));
});

// Start HTTPS server if enabled
if (CONFIG.HTTPS_ENABLED) {
  try {
    const credentials = await getSSLCredentials();
    httpsServer = https.createServer(credentials, app);

    httpsServer.listen(HTTPS_PORT, '0.0.0.0', () => {
      logger.info('='.repeat(60));
      logger.info(`🔒 HTTPS Server running on https://0.0.0.0:${HTTPS_PORT}`);
      logger.info(`🌍 Environment: ${CONFIG.NODE_ENV}`);
      logger.info('='.repeat(60));
    });
  } catch (error) {
    logger.error('Failed to start HTTPS server:', error);
    logger.warn('Continuing with HTTP only...');
  }
}

// --- Initialize Services ---

// Start memory monitoring
memoryMonitor.start();

// Verify backend connection
verificarBackend();

// Periodic backend health check
setInterval(verificarBackend, CONFIG.HEARTBEAT_INTERVAL);

// --- Setup Graceful Shutdown ---

gracefulShutdown.registerServer(httpServer, 'HTTP Server');
if (httpsServer) {
  gracefulShutdown.registerServer(httpsServer, 'HTTPS Server');
}

gracefulShutdown.registerCleanupHandler(async () => {
  logger.info('Stopping memory monitor...');
  memoryMonitor.stop();
}, 'Memory Monitor');

gracefulShutdown.registerCleanupHandler(async () => {
  logger.info('Closing UDP server...');
  udpServer.close();
}, 'UDP Server');

gracefulShutdown.init();

// --- Final Startup Message ---

logger.info('='.repeat(60));
logger.info('✅ EZBar API started successfully');
logger.info(`📝 Log Level: ${CONFIG.LOG_LEVEL}`);
logger.info(`💾 Cache: ${CONFIG.CACHE.ENABLED ? 'ENABLED' : 'DISABLED'}`);
logger.info(`⏱️  Request Timeout: ${CONFIG.GLOBAL_TIMEOUT}ms`);
logger.info(`🔒 HTTPS: ${CONFIG.HTTPS_ENABLED ? 'ENABLED' : 'DISABLED'}`);
logger.info('='.repeat(60));