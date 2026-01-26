import pool from '../src/config/database.js';
import bcrypt from 'bcryptjs';

const crearUsuarioPrueba = async () => {
    const connection = await pool.getConnection();
    try {
        await connection.beginTransaction();

        console.log('--- Creando usuario de prueba ---');

        // 1. Asegurarse de que existe un empleado (requisito de la FK)
        const [empleados] = await connection.query('SELECT empleado_id FROM empleados LIMIT 1');
        let empleadoId;

        if (empleados.length === 0) {
            console.log('No hay empleados. Creando un empleado dummy...');
            const [res] = await connection.query(
                "INSERT INTO empleados (nombre, puesto, salario, fecha_contratación) VALUES ('Test Employee', 'Tester', 1000, NOW())"
            );
            empleadoId = res.insertId;
            console.log(`Empleado creado con ID: ${empleadoId}`);
        } else {
            empleadoId = empleados[0].empleado_id;
            console.log(`Usando empleado existente ID: ${empleadoId}`);
        }

        // 2. Crear usuario
        const username = 'admin';
        const passwordOriginal = 'password123';
        const salt = await bcrypt.genSalt(10);
        const hash = await bcrypt.hash(passwordOriginal, salt);

        // Verificar si ya existe
        const [usuarios] = await connection.query('SELECT usuario_id FROM usuarios WHERE nombre_usuario = ?', [username]);

        if (usuarios.length > 0) {
            console.log(`El usuario '${username}' ya existe. Actualizando contraseña...`);
            await connection.query(
                'UPDATE usuarios SET password_hash = ?, activo = 1 WHERE nombre_usuario = ?',
                [hash, username]
            );
        } else {
            console.log(`Creando usuario '${username}'...`);
            await connection.query(
                'INSERT INTO usuarios (empleado_id, nombre_usuario, password_hash, rol, activo) VALUES (?, ?, ?, ?, 1)',
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
        connection.release();
        process.exit();
    }
};

crearUsuarioPrueba();
