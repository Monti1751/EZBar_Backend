import pool from './src/config/database.js';
import { CONFIG } from './src/config/constants.js';

console.log('--- Probando conexión a MariaDB ---');
console.log('Host:', CONFIG.DB.HOST);
console.log('DB:', CONFIG.DB.NAME);

try {
    const [rows] = await pool.query('SELECT 1 as test');
    console.log('Resultado:', rows[0].test === 1 ? 'EXITO' : 'FALLO');
    process.exit(0);
} catch (error) {
    console.error('ERROR de conexión:', error.message);
    process.exit(1);
}
