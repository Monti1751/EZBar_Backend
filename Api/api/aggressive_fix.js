import mysql from 'mysql2/promise';

async function fixDeletions() {
    console.log('--- Iniciando corrección AGRESIVA de borrado de mesas ---');
    let conn;
    try {
        conn = await mysql.createConnection({
            host: '127.0.0.1',
            user: 'root',
            password: 'm3f1',
            database: 'ezbardb' // Usamos el nombre que sale en el error del log
        });

        await conn.query('SET FOREIGN_KEY_CHECKS = 0');

        const dropConstraints = async (table, constraint) => {
            try {
                console.log(`Intentando borrar FK ${constraint} en tabla ${table}...`);
                await conn.query(`ALTER TABLE ${table} DROP FOREIGN KEY ${constraint}`);
                console.log(`✅ FK ${constraint} borrada.`);
            } catch (e) {
                console.log(`⚠️ No se pudo borrar ${constraint} (puede que no exista): ${e.message}`);
            }
        };

        // 1. Limpiar posibles nombres de constraints antiguas en PEDIDOS
        await dropConstraints('pedidos', 'pedidos_ibfk_1');
        await dropConstraints('pedidos', 'fk_pedidos_mesas'); // Nombre alternativo por si acaso

        // 2. Limpiar en DETALLE_PEDIDOS
        await dropConstraints('detalle_pedidos', 'detalle_pedidos_ibfk_1');

        // 3. Limpiar en PAGOS
        await dropConstraints('pagos', 'pagos_ibfk_1');

        console.log('--- Re-creando constraints con ON DELETE CASCADE ---');

        await conn.query(`
            ALTER TABLE pedidos 
            ADD CONSTRAINT pedidos_ibfk_1 
            FOREIGN KEY (mesa_id) REFERENCES mesas(mesa_id) 
            ON DELETE CASCADE
        `);
        console.log('✅ PEDIDOS -> MESAS (CASCADE) creado.');

        await conn.query(`
            ALTER TABLE detalle_pedidos 
            ADD CONSTRAINT detalle_pedidos_ibfk_1 
            FOREIGN KEY (pedido_id) REFERENCES pedidos(pedido_id) 
            ON DELETE CASCADE
        `);
        console.log('✅ DETALLE_PEDIDOS -> PEDIDOS (CASCADE) creado.');

        await conn.query(`
            ALTER TABLE pagos 
            ADD CONSTRAINT pagos_ibfk_1 
            FOREIGN KEY (pedido_id) REFERENCES pedidos(pedido_id) 
            ON DELETE CASCADE
        `);
        console.log('✅ PAGOS -> PEDIDOS (CASCADE) creado.');

        await conn.query('SET FOREIGN_KEY_CHECKS = 1');
        console.log('\n--- PROCESO FINALIZADO CON ÉXITO ---');

    } catch (err) {
        console.error('\n❌ ERROR FATAL:', err.message);
    } finally {
        if (conn) await conn.end();
        process.exit(0);
    }
}

fixDeletions();
