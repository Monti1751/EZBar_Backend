import pool from './src/config/database.js';
import { CONFIG } from './src/config/constants.js';

async function checkCascade() {
    console.log('--- Verificando Reglas CASCADE ---');
    try {
        const [rows] = await pool.query(`
      SELECT CONSTRAINT_NAME, DELETE_RULE, TABLE_NAME 
      FROM information_schema.REFERENTIAL_CONSTRAINTS 
      WHERE CONSTRAINT_SCHEMA = ?`, [CONFIG.DB.NAME]);

        console.table(rows);

        const hasCascade = rows.some(r => r.DELETE_RULE === 'CASCADE');
        if (hasCascade) {
            console.log('✅ ÉXITO: Se encontraron reglas CASCADE.');
        } else {
            console.log('❌ FALLO: No se encontraron reglas CASCADE.');
        }
        process.exit(0);
    } catch (error) {
        console.error('ERROR:', error.message);
        process.exit(1);
    }
}

checkCascade();
