import mysql from 'mysql2/promise';
import { CONFIG } from './src/config/constants.js';

async function checkSchema() {
    const pool = mysql.createPool({
        host: CONFIG.DB.HOST,
        port: CONFIG.DB.PORT,
        user: CONFIG.DB.USER,
        password: CONFIG.DB.PASSWORD,
        database: CONFIG.DB.NAME
    });

    try {
        const [rows] = await pool.query('DESCRIBE productos');
        console.log('Columns in "productos":');
        rows.forEach(row => {
            console.log(`- ${row.Field}: ${row.Type}`);
        });
    } catch (error) {
        console.error('Error:', error);
    } finally {
        await pool.end();
    }
}

checkSchema();
