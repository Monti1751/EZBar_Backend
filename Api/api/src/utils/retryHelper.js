import axios from 'axios';
import { CONFIG } from '../config/constants.js';

export const fetchWithRetry = async (url, options = {}, attempts = CONFIG.RETRY_ATTEMPTS) => {
  try {
    const response = await axios({
      url,
      timeout: CONFIG.REQUEST_TIMEOUT,
      ...options
    });
    return response;
  } catch (error) {
    if (attempts > 1 && (error.code === 'ECONNREFUSED' || error.code === 'ETIMEDOUT')) {
      console.log(`⚠️ Reintentando... (${CONFIG.RETRY_ATTEMPTS - attempts + 1}/${CONFIG.RETRY_ATTEMPTS})`);
      await new Promise(resolve => setTimeout(resolve, CONFIG.RETRY_DELAY));
      return fetchWithRetry(url, options, attempts - 1);
    }
    throw error;
  }
};

export const verificarBackend = async () => {
  try {
    const https = await import('https');
    await axios.get(`${CONFIG.BACKEND_URL}/actuator/health`, {
      timeout: 3000,
      httpsAgent: new https.Agent({ rejectUnauthorized: false })
    });
    console.log('✅ Backend Java disponible');
    return true;
  } catch (error) {
    console.error('❌ Backend Java no disponible:', error.message);
    return false;
  }
};