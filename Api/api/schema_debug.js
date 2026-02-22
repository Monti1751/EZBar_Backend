import pool from './src/config/database.js';

async function describeTables() {
    try {
        console.log('--- DESCRIBE USUARIOS ---');
        const [usuariosCols] = await pool.query('DESCRIBE USUARIOS');
        console.table(usuariosCols);

        console.log('\n--- DESCRIBE EMPLEADOS ---');
        const [empleadosCols] = await pool.query('DESCRIBE EMPLEADOS');
        console.table(empleadosCols);

        const [existingUsers] = await pool.query('SELECT * FROM USUARIOS LIMIT 5');
        console.log('\n--- SAMPLE DATA FROM USUARIOS ---');
        console.table(existingUsers);

        process.exit(0);
    } catch (error) {
        console.error('Error:', error);
        process.exit(1);
    }
}

describeTables();
