import pool from './src/config/database.js';

async function verificarDatos() {
  const connection = await pool.getConnection();
  try {
    console.log('🔍 Verificando datos en la BD...\n');

    const [zonas] = await connection.query('SELECT * FROM ZONAS');
    console.log('📍 Zonas:', zonas.length);
    console.table(zonas);

    const [mesas] = await connection.query('SELECT * FROM MESAS LIMIT 5');
    console.log('\n🪑 Mesas (primeras 5):', mesas.length);
    console.table(mesas);

    const [categorias] = await connection.query('SELECT * FROM CATEGORIAS');
    console.log('\n📂 Categorías:', categorias.length);
    console.table(categorias);

    const [productos] = await connection.query('SELECT * FROM PRODUCTOS LIMIT 5');
    console.log('\n🍽️ Productos (primeros 5):', productos.length);
    console.table(productos);

  } catch (error) {
    console.error('❌ Error:', error.message);
  } finally {
    connection.release();
    process.exit(0);
  }
}

verificarDatos();
