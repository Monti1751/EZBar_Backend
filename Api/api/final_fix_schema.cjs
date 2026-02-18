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
            database: 'EZBarDB'
        });

        log('Connection established to EZBarDB.');
        const columns = ['ingredientes', 'extras', 'alergenos'];
        for (const col of columns) {
            try {
                await connection.query(`ALTER TABLE productos ADD COLUMN ${col} TEXT`);
                log(`Successfully added column: ${col}`);
            } catch (e) {
                log(`Note for column ${col}: ${e.message}`);
            }
        }
        await connection.end();
        log('Database update completed.');
    } catch (err) {
        log('CRITICAL ERROR: ' + err.message);
    } finally {
        fs.writeFileSync('schema_fix_output.txt', output);
    }
}

fix();
