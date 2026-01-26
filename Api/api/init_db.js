import pool from './src/config/database.js';

async function initDb() {
  const connection = await pool.getConnection();
  try {
    console.log('🔧 Inicializando base de datos...');

    // Crear puesto si no existe
    await connection.query(
      `INSERT IGNORE INTO PUESTOS (puesto_id, nombre_puesto) 
       VALUES (1, 'Mesero')`
    );
    console.log('✅ Puesto creado/verificado');

    // Crear empleado sistema si no existe
    await connection.query(
      `INSERT IGNORE INTO EMPLEADOS (empleado_id, nombre_empleado, apellido_empleado, nombre_usuario, dni, puesto_id, password_hash, activo) 
       VALUES (1, 'Sistema', 'Sistema', 'sistema', '000-000-000', 1, 'sistema', TRUE)`
    );
    console.log('✅ Empleado sistema creado/verificado');

    // Verificar datos
    const [empleados] = await connection.query('SELECT * FROM EMPLEADOS');
    console.log('📋 Empleados en la BD:');
    console.table(empleados);

  } catch (error) {
    console.error('❌ Error inicializando BD:', error.message);
  } finally {
    connection.release();
    process.exit(0);
  }
}

initDb();
