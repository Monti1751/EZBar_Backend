import pool from './src/config/database.js';
import bcrypt from 'bcryptjs';

async function fixPassword() {
    try {
        const username = 'admin'; // El usuario que viste en la BD
        const newPassword = 'password123'; // La contraseña que quieres que tenga

        console.log(`🔒 Generando hash para: ${newPassword}`);
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(newPassword, salt);

        console.log(`🔑 Hash generado: ${hashedPassword}`);

        const [result] = await pool.query(
            'UPDATE usuarios SET password_hash = ? WHERE nombre_usuario = ?',
            [hashedPassword, username]
        );

        if (result.affectedRows > 0) {
            console.log(`✅ Contraseña actualizada correctamente para el usuario: ${username}`);
        } else {
            console.log(`❌ No se encontró el usuario: ${username}`);

            // Intento de insertar si no existe (opcional, pero útil)
            // console.log("➕ Insertando usuario admin...");
            // await pool.query("INSERT INTO usuarios (nombre_usuario, password_hash, rol, activo) VALUES (?, ?, 'admin', 1)", [username, hashedPassword]);
        }

        process.exit(0);
    } catch (error) {
        console.error('❌ Error al actualizar contraseña:', error);
        process.exit(1);
    }
}

fixPassword();
