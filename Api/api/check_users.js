import pool from './src/config/database.js';

async function checkUsers() {
    try {
        console.log('Checking users table...');
        const [rows] = await pool.query('SELECT * FROM usuarios');
        console.table(rows);
        process.exit(0);
    } catch (error) {
        console.error('Error:', error);
        process.exit(1);
    }
}

checkUsers();
