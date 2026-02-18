const mysql = require('mysql2/promise');
const fs = require('fs');

async function diag() {
    let log = '';
    const print = (msg) => { log += msg + '\n'; };

    try {
        const connection = await mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: 'm3f1',
            database: 'EZBarDB'
        });

        const [productos] = await connection.query('SELECT producto_id, nombre, categoria_id FROM productos');
        print('--- PRODUCTOS (' + productos.length + ') ---');
        productos.forEach(p => print(JSON.stringify(p)));

        const [categorias] = await connection.query('SELECT categoria_id, nombre FROM categorias');
        print('--- CATEGORIAS (' + categorias.length + ') ---');
        categorias.forEach(c => print(JSON.stringify(c)));

        await connection.end();
    } catch (err) {
        print('DIAG ERROR: ' + err.message);
    }
    fs.writeFileSync('db_diag_output.txt', log);
}

diag();
