import pool from './src/config/database.js';

async function verifyFix() {
    try {
        console.log('Testing obtenerUsuarios query...');
        const [rows] = await pool.query(
            'SELECT usuario_id AS id, nombre_usuario AS username, rol, activo, fecha_creacion FROM USUARIOS ORDER BY nombre_usuario ASC'
        );
        console.log('Success! Found', rows.length, 'users.');
        console.table(rows);
        process.exit(0);
    } catch (error) {
        console.error('Error:', error.message);
        process.exit(1);
    }
}

verifyFix();
