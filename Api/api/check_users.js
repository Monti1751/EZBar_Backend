import pool from './src/config/database.js';

async function checkUsers() {
    try {
        const [rows] = await pool.query('SELECT usuario_id, nombre_usuario, password_hash, rol, activo FROM usuarios');
        console.log(JSON.stringify(rows, null, 2));
        process.exit(0);
    } catch (error) {
        console.error('Error al consultar usuarios:', error);
        process.exit(1);
    }
}

checkUsers();
