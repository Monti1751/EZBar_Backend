const mysql = require('mysql2/promise');
const fs = require('fs');

async function fixSchema() {
    let output = '';
    const log = (msg) => {
        const line = typeof msg === 'string' ? msg : JSON.stringify(msg, null, 2);
        console.log(line);
        output += line + '\n';
    };
    let conn;
    try {
        conn = await mysql.createConnection({
            host: '127.0.0.1',
            user: 'root',
            password: 'm3f1',
            database: 'ezbardb'
        });

        log('--- Iniciando corrección agresiva de esquema (CASCADE) ---');

        // Desactivar temporalmente
        await conn.query('SET FOREIGN_KEY_CHECKS = 0');

        // Verificación inicial de FKs existentes para encontrar los nombres reales
        const [fksBefore] = await conn.query(`
            SELECT TABLE_NAME, CONSTRAINT_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME
            FROM information_schema.KEY_COLUMN_USAGE 
            WHERE REFERENCED_TABLE_NAME IS NOT NULL AND TABLE_SCHEMA = 'ezbardb'
        `);
        log('FKs detectadas:');
        log(fksBefore);

        // Definimos qué queremos lograr
        const targets = [
            { table: 'detalle_pedidos', column: 'producto_id', refTable: 'productos', refCol: 'producto_id', name: 'detalle_pedidos_ibfk_2' },
            { table: 'productos', column: 'categoria_id', refTable: 'categorias', refCol: 'categoria_id', name: 'productos_ibfk_1' }
        ];

        for (const target of targets) {
            // Buscamos si ya existe una FK para esa columna en esa tabla
            const existing = fksBefore.filter(f => f.TABLE_NAME === target.table && f.COLUMN_NAME === target.column);

            for (const ex of existing) {
                log(`Borrando FK existente: ${ex.CONSTRAINT_NAME} en ${target.table}`);
                try {
                    await conn.query(`ALTER TABLE ${target.table} DROP FOREIGN KEY ${ex.CONSTRAINT_NAME}`);
                } catch (e) {
                    log(`Aviso: No se pudo borrar ${ex.CONSTRAINT_NAME}: ${e.message}`);
                }
            }

            log(`Creando FK ${target.name} con CASCADE en ${target.table}`);
            await conn.query(`
            ALTER TABLE ${target.table} 
            ADD CONSTRAINT ${target.name} 
            FOREIGN KEY (${target.column}) 
            REFERENCES ${target.refTable}(${target.refCol}) 
            ON DELETE CASCADE
        `);
            log('✅ OK');
        }

        await conn.query('SET FOREIGN_KEY_CHECKS = 1');
        log('--- Esquema actualizado correctamente ---');

        // Verificación final
        const [rows] = await conn.query(`
            SELECT CONSTRAINT_NAME, DELETE_RULE 
            FROM information_schema.REFERENTIAL_CONSTRAINTS 
            WHERE CONSTRAINT_SCHEMA = 'ezbardb'
        `);
        log(rows);

    } catch (error) {
        console.error('❌ Error fatal:', error);
    } finally {
        if (conn) await conn.end();
        fs.writeFileSync('fix_debug.log', output);
    }
}

fixSchema();
