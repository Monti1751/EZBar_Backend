import mysql from 'mysql2/promise';
import { CONFIG } from './src/config/constants.js';

async function fixSchema() {
    const pool = mysql.createPool({
        host: CONFIG.DB.HOST,
        port: CONFIG.DB.PORT,
        user: CONFIG.DB.USER,
        password: CONFIG.DB.PASSWORD,
        database: CONFIG.DB.NAME
    });

    try {
        console.log('Adding missing columns to productos...');

        // Add columns if they don't exist
        const columnsToAdd = [
            'ingredientes TEXT',
            'extras TEXT',
            'alergenos TEXT'
        ];

        for (const col of columnsToAdd) {
            try {
                await pool.query(`ALTER TABLE productos ADD COLUMN ${col}`);
                console.log(`Added column: ${col}`);
            } catch (e) {
                if (e.code === 'ER_DUP_COLUMN_NAME') {
                    console.log(`Column already exists: ${col}`);
                } else {
                    console.error(`Error adding ${col}:`, e.message);
                }
            }
        }

        console.log('Schema update complete.');
    } catch (error) {
        console.error('Error:', error);
    } finally {
        await pool.end();
    }
}

fixSchema();
