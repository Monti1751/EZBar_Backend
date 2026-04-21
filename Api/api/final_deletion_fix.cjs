const mysql = require('mysql2/promise');
const fs = require('fs');

async function fix() {
    let output = '';
    const log = (msg) => {
        console.log(msg);
        output += msg + '\n';
    };

    try {
        const connection = await mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: 'm3f1',
            database: 'ezbardb'
        });

        log('Connection established to ezbardb.');

        await connection.query('SET FOREIGN_KEY_CHECKS = 0');

        const tables = ['pedidos', 'detalle_pedidos', 'pagos'];
        for (const table of tables) {
            // Buscamos todas las FKs de la tabla para borrarlas todas
            const [fks] = await connection.query(`
                SELECT CONSTRAINT_NAME 
                FROM information_schema.KEY_COLUMN_USAGE 
                WHERE TABLE_NAME = ? AND TABLE_SCHEMA = 'ezbardb' AND REFERENCED_TABLE_NAME IS NOT NULL
            `, [table]);

            for (const fk of fks) {
                log(`Dropping FK ${fk.CONSTRAINT_NAME} from ${table}...`);
                try {
                    await connection.query(`ALTER TABLE ${table} DROP FOREIGN KEY ${fk.CONSTRAINT_NAME}`);
                } catch (e) {
                    log(`Error dropping ${fk.CONSTRAINT_NAME}: ${e.message}`);
                }
            }
        }

        log('Adding new CASCADE constraints...');

        await connection.query(`
            ALTER TABLE pedidos 
            ADD CONSTRAINT fk_pedidos_mesas_cascade 
            FOREIGN KEY (mesa_id) REFERENCES mesas(mesa_id) 
            ON DELETE CASCADE
        `);
        log('✅ pedidos -> mesas (CASCADE) added.');

        await connection.query(`
            ALTER TABLE detalle_pedidos 
            ADD CONSTRAINT fk_detalles_pedidos_cascade 
            FOREIGN KEY (pedido_id) REFERENCES pedidos(pedido_id) 
            ON DELETE CASCADE
        `);
        log('✅ detalle_pedidos -> pedidos (CASCADE) added.');

        await connection.query(`
            ALTER TABLE pagos 
            ADD CONSTRAINT fk_pagos_pedidos_cascade 
            FOREIGN KEY (pedido_id) REFERENCES pedidos(pedido_id) 
            ON DELETE CASCADE
        `);
        log('✅ pagos -> pedidos (CASCADE) added.');

        await connection.query('SET FOREIGN_KEY_CHECKS = 1');

        await connection.end();
        log('Database update completed successfully.');
    } catch (err) {
        log('CRITICAL ERROR: ' + err.message);
    } finally {
        fs.writeFileSync('deletion_fix_output.txt', output);
    }
}

fix();
