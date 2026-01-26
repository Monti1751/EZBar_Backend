// Cargar variables de entorno desde el archivo .env
import 'dotenv/config';
import app from './src/app.js';
import { verificarBackend } from './src/utils/retryHelper.js';
import { CONFIG } from './src/config/constants.js';
import dgram from 'dgram';
import { networkInterfaces } from 'os';
import logger from './src/logger.js';

// Puerto para la API REST (usada por la app móvil)
const PORT = process.env.PORT || 3000;
// Puerto para el servicio de descubrimiento UDP (para encontrar el servidor en la red)
const UDP_PORT = 3001;

// --- Configuración del Servicio de Descubrimiento UDP ---
// Esto permite que el móvil encuentre la IP del servidor automáticamente
const udpServer = dgram.createSocket('udp4');

// Manejar errores del servidor UDP
udpServer.on('error', (err) => {
  logger.error(`UDP Server error:\n${err.stack}`);
  udpServer.close();
});

// Escuchar mensajes UDP (cuando la app busca el servidor)
udpServer.on('message', (msg, rinfo) => {
  // Si recibimos el mensaje de búsqueda específico 'EZBAR_DISCOVER'
  if (msg.toString().trim() === 'EZBAR_DISCOVER') {
    logger.info(`Solicitud de descubrimiento desde ${rinfo.address}:${rinfo.port}`);

    // Determinar la IP local (WiFi/LAN) del servidor para enviarla al móvil
    const nets = networkInterfaces();
    let localIp = '127.0.0.1';

    for (const name of Object.keys(nets)) {
      for (const net of nets[name]) {
        // Buscamos solo direcciones IPv4 que no sean internas (localhost)
        if (net.family === 'IPv4' && !net.internal) {
          // Priorizamos rangos de IP típicos de redes locales (192.168.x.x, etc.)
          if (net.address.startsWith('192') || net.address.startsWith('172') || net.address.startsWith('10')) {
            localIp = net.address;
          }
        }
      }
    }

    // Preparamos la respuesta con nuestra IP y puerto
    const message = JSON.stringify({
      ip: localIp,
      port: PORT,
      service: 'EZBar_API'
    });

    // Enviamos la respuesta de vuelta al dispositivo que preguntó
    udpServer.send(message, rinfo.port, rinfo.address, (err) => {
      if (err) logger.error('Error enviando respuesta de descubrimiento:', err);
      else logger.info(`Respuesta de descubrimiento enviada a ${rinfo.address}: ${message}`);
    });
  }
});

// Iniciar el servidor UDP
udpServer.bind(UDP_PORT, () => {
  logger.info(`📡 UDP Discovery escuchando en el puerto ${UDP_PORT}`);
});
// -----------------------------

// --- Iniciar el Servidor API REST ---
app.listen(PORT, '0.0.0.0', () => {
  logger.info(`🚀 API Node.js ejecutándose en http://0.0.0.0:${PORT}`);
  logger.info(`🔗 URL del Backend configurada: ${CONFIG.BACKEND_URL}`);
  logger.info(`📊 Nombre de Base de datos: ${process.env.DB_NAME}`);

  // Verificar conexión con el backend Java al inicio
  verificarBackend();

  // Verificar conexión periódicamente cada 30 segundos
  setInterval(verificarBackend, CONFIG.HEARTBEAT_INTERVAL);
});