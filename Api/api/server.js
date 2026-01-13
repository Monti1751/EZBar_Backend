import 'dotenv/config';
import app from './src/app.js';
import { verificarBackend } from './src/utils/retryHelper.js';
import { CONFIG } from './src/config/constants.js';

const PORT = process.env.PORT || 3000;

app.listen(PORT, '0.0.0.0', () => {
  console.log(`🚀 API Node.js corriendo en http://0.0.0.0:${PORT}`);
  console.log(`🔗 Backend configurado: ${CONFIG.BACKEND_URL}`);
  console.log(`📊 Base de datos: ${process.env.DB_NAME}`);
  
  // Verificación inicial del backend
  verificarBackend();
  
  // Verificación periódica cada 30 segundos
  setInterval(verificarBackend, CONFIG.HEARTBEAT_INTERVAL);
});