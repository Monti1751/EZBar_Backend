import pool from './src/config/database.js';

async function checkData() {
    try {
        console.log('--- EMPLEADOS ---');
        const [empleados] = await pool.query('SELECT * FROM EMPLEADOS');
        console.table(empleados);

        console.log('\n--- USUARIOS ---');
        const [usuarios] = await pool.query('SELECT * FROM USUARIOS');
        console.table(usuarios);

        process.exit(0);
    } catch (error) {
        console.error('Error:', error);
        process.exit(1);
    }
}

checkData();
