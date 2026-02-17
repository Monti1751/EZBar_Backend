const mysql = require('mysql2/promise');
require('dotenv').config();

async function fixSchema() {
    const pool = mysql.createPool({
        host: process.env.DB_HOST || 'localhost',
        user: process.env.DB_USER || 'root',
        password: process.env.DB_PASSWORD || '',
        database: process.env.DB_NAME || 'ezbar_db',
        waitForConnections: true,
        connectionLimit: 10,
        queueLimit: 0
    });

    try {
        console.log('Modifying imagen column to LONGBLOB...');
        await pool.query('ALTER TABLE productos MODIFY COLUMN imagen LONGBLOB');
        console.log('Success! imagen column is now LONGBLOB.');
    } catch (error) {
        console.error('Error changing schema:', error);
    } finally {
        await pool.end();
    }
}

fixSchema();
