import 'dotenv/config';
import app from './src/app.js';
import { verificarBackend } from './src/utils/retryHelper.js';
import { CONFIG } from './src/config/constants.js';
import dgram from 'dgram';
import { networkInterfaces } from 'os';

const PORT = process.env.PORT || 3000;
const UDP_PORT = 3001;

// --- UDP Discovery Service ---
const udpServer = dgram.createSocket('udp4');

udpServer.on('error', (err) => {
  console.log(`UDP Server error:\n${err.stack}`);
  udpServer.close();
});

udpServer.on('message', (msg, rinfo) => {
  if (msg.toString().trim() === 'EZBAR_DISCOVER') {
    console.log(`Discovery request from ${rinfo.address}:${rinfo.port}`);

    // Obtener IP local real
    const nets = networkInterfaces();
    let localIp = '127.0.0.1';

    for (const name of Object.keys(nets)) {
      for (const net of nets[name]) {
        // Saltamos direcciones internas (no 127.0.0.1) y no IPv4
        if (net.family === 'IPv4' && !net.internal) {
          // Preferimos IPs tipicas de LAN (192.168, 172, 10)
          if (net.address.startsWith('192') || net.address.startsWith('172') || net.address.startsWith('10')) {
            localIp = net.address;
          }
        }
      }
    }

    const message = JSON.stringify({
      ip: localIp,
      port: PORT,
      service: 'EZBar_API'
    });

    udpServer.send(message, rinfo.port, rinfo.address, (err) => {
      if (err) console.error('Error sending discovery reply:', err);
      else console.log(`Sent discovery reply to ${rinfo.address}: ${message}`);
    });
  }
});

udpServer.bind(UDP_PORT, () => {
  console.log(`📡 UDP Discovery listening on port ${UDP_PORT}`);
});
// -----------------------------

app.listen(PORT, '0.0.0.0', () => {
  console.log(`🚀 API Node.js corriendo en http://0.0.0.0:${PORT}`);
  console.log(`🔗 Backend configurado: ${CONFIG.BACKEND_URL}`);
  console.log(`📊 Base de datos: ${process.env.DB_NAME}`);

  // Verificación inicial del backend
  verificarBackend();

  // Verificación periódica cada 30 segundos
  setInterval(verificarBackend, CONFIG.HEARTBEAT_INTERVAL);
});