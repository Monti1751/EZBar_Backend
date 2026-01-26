async function updatePrice(productoId, nuevoPrecio) {
  const dbModule = await import('../src/config/database.js');
  const pool = dbModule.default;
  const conn = await pool.getConnection();
  try {
    console.log('Conectado a la BD, actualizando precio...');
    const [res] = await conn.query('UPDATE PRODUCTOS SET precio = ? WHERE producto_id = ?', [nuevoPrecio, productoId]);
    console.log('Filas afectadas:', res.affectedRows);
    const [rows] = await conn.query('SELECT producto_id, nombre, precio FROM PRODUCTOS WHERE producto_id = ?', [productoId]);
    console.log('Producto actualizado:', rows[0]);
  } catch (err) {
    console.error('Error actualizando precio:', err.message);
    process.exitCode = 1;
  } finally {
    conn.release();
  }
}

const productoId = 2;
const nuevoPrecio = 5.5;
updatePrice(productoId, nuevoPrecio).then(() => process.exit());
