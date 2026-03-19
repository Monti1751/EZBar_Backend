const mysql = require('mysql2/promise');

async function testConn() {
    console.log('Testing connection to ezbardb...');
    try {
        const connection = await mysql.createConnection({
            host: '127.0.0.1',
            user: 'root',
            password: 'm3f1',
            database: 'ezbardb',
            connectTimeout: 5000
        });
        console.log('✅ Connection SUCCESSFUL');
        await connection.end();
    } catch (e) {
        console.error('❌ Connection FAILED:', e.message);
    }
    process.exit(0);
}

testConn();
