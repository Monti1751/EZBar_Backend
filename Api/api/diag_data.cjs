const mysql = require('mysql2/promise');

async function diag() {
    try {
        const connection = await mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: 'm3f1',
            database: 'EZBarDB'
        });

        console.log('--- PRODUCTOS EN DB ---');
        const [productos] = await connection.query('SELECT producto_id, nombre, categoria_id FROM productos');
        console.table(productos);

        console.log('--- CATEGORIAS EN DB ---');
        const [categorias] = await connection.query('SELECT categoria_id, nombre FROM categorias');
        console.table(categorias);

        await connection.end();
    } catch (err) {
        console.error('DIAG ERROR:', err.message);
    }
}

diag();
