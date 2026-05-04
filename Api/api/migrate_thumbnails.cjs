const mysql = require('mysql2/promise');

async function fix() {
    try {
        const connection = await mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: 'm3f1',
            database: 'ezbardb'
        });

        console.log('Adding column imagen_miniatura...');
        try {
            await connection.query('ALTER TABLE productos ADD COLUMN imagen_miniatura LONGBLOB');
            console.log('✅ Column added');
        } catch (e) {
            console.log('⚠️ Column might already exist: ' + e.message);
        }
        await connection.end();
    } catch (err) {
        console.error('❌ Error:', err.message);
    }
}

fix();
