import pool from '../src/config/database.js';
import bcrypt from 'bcryptjs';

const crearUsuarioPrueba = async () => {
    const connection = await pool.getConnection();
    try {
        await connection.beginTransaction();

        console.log('--- Creando usuario de prueba ---');

        // 0. Asegurarse de que existe el puesto de Admin
        let [puestos] = await connection.query("SELECT puesto_id FROM PUESTOS WHERE nombre_puesto = 'Admin'");
        let puestoId;

        if (puestos.length === 0) {
            console.log("Creando puesto 'Admin'...");
            const [resPuesto] = await connection.query("INSERT INTO PUESTOS (nombre_puesto) VALUES ('Admin')");
            puestoId = resPuesto.insertId;
        } else {
            puestoId = puestos[0].puesto_id;
        }

        // 1. Asegurarse de que existe un empleado (requisito de la FK)
        // Buscamos uno que sea Admin
        const [empleados] = await connection.query('SELECT empleado_id FROM EMPLEADOS WHERE puesto_id = ? LIMIT 1', [puestoId]);
        let empleadoId;

        if (empleados.length === 0) {
            console.log('No hay empleados admin. Creando un empleado dummy...');
            // Generar datos dummy
            const randomSuffix = Math.floor(Math.random() * 10000);
            const dniDummy = `0000${randomSuffix}X`;
            const usuarioDummy = `admin_emp_${randomSuffix}`;

            const [res] = await connection.query(
                `INSERT INTO EMPLEADOS 
                (nombre_empleado, apellido_empleado, nombre_usuario, dni, puesto_id, password_hash, activo) 
                VALUES (?, ?, ?, ?, ?, ?, 1)`,
                ['Test', 'Admin', usuarioDummy, dniDummy, puestoId, 'dummy_hash']
            );
            empleadoId = res.insertId;
            console.log(`Empleado creado con ID: ${empleadoId}`);
        } else {
            empleadoId = empleados[0].empleado_id;
            console.log(`Usando empleado existente ID: ${empleadoId}`);
        }

        // 2. Crear usuario asociado al empleado
        const username = 'admin';
        const passwordOriginal = 'admin123'; // Pass más estándar para test
        const salt = await bcrypt.genSalt(10);
        const hash = await bcrypt.hash(passwordOriginal, salt);

        // Verificar si ya existe
        const [usuarios] = await connection.query('SELECT usuario_id FROM USUARIOS WHERE nombre_usuario = ?', [username]);

        if (usuarios.length > 0) {
            console.log(`El usuario '${username}' ya existe. Actualizando contraseña...`);
            await connection.query(
                'UPDATE USUARIOS SET password_hash = ?, activo = 1 WHERE nombre_usuario = ?',
                [hash, username]
            );
        } else {
            console.log(`Creando usuario '${username}'...`);
            await connection.query(
                'INSERT INTO USUARIOS (empleado_id, nombre_usuario, password_hash, rol, activo) VALUES (?, ?, ?, ?, 1)',
                [empleadoId, username, hash, 'admin']
            );
        }

        await connection.commit();
        console.log('✅ Usuario configurado exitosamente.');
        console.log(`👤 Usuario: ${username}`);
        console.log(`🔑 Contraseña: ${passwordOriginal}`);

    } catch (error) {
        await connection.rollback();
        console.error('❌ Error al crear usuario:', error);
    } finally {
        if (connection) connection.release();
        process.exit();
    }
};

crearUsuarioPrueba();
