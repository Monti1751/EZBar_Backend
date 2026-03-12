import pool from './src/config/database.js';

async function fixConstraints() {
    try {
        console.log('Starting DB fix...');

        // Disable FK checks to safely drop/add
        await pool.query('SET FOREIGN_KEY_CHECKS = 0');

        console.log('Dropping/Re-adding constraint on PEDIDOS...');
        // Drop existing if named pedidos_ibfk_1 or similar
        try {
            await pool.query('ALTER TABLE PEDIDOS DROP FOREIGN KEY pedidos_ibfk_1');
        } catch (e) {
            console.log('Note: pedidos_ibfk_1 not found or already dropped');
        }

        await pool.query('ALTER TABLE PEDIDOS ADD CONSTRAINT pedidos_ibfk_1 FOREIGN KEY (mesa_id) REFERENCES MESAS(mesa_id) ON DELETE CASCADE');

        console.log('Dropping/Re-adding constraint on DETALLE_PEDIDOS...');
        try {
            await pool.query('ALTER TABLE DETALLE_PEDIDOS DROP FOREIGN KEY detalle_pedidos_ibfk_1');
        } catch (e) {
            console.log('Note: detalle_pedidos_ibfk_1 not found');
        }
        await pool.query('ALTER TABLE DETALLE_PEDIDOS ADD CONSTRAINT detalle_pedidos_ibfk_1 FOREIGN KEY (pedido_id) REFERENCES PEDIDOS(pedido_id) ON DELETE CASCADE');

        console.log('Dropping/Re-adding constraint on PAGOS...');
        try {
            await pool.query('ALTER TABLE PAGOS DROP FOREIGN KEY pagos_ibfk_1');
        } catch (e) {
            console.log('Note: pagos_ibfk_1 not found');
        }
        await pool.query('ALTER TABLE PAGOS ADD CONSTRAINT pagos_ibfk_1 FOREIGN KEY (pedido_id) REFERENCES PEDIDOS(pedido_id) ON DELETE CASCADE');

        await pool.query('SET FOREIGN_KEY_CHECKS = 1');

        console.log('✅ Success! Constraints updated with ON DELETE CASCADE.');
        process.exit(0);
    } catch (error) {
        console.error('❌ Error fixing database:', error);
        process.exit(1);
    }
}

fixConstraints();
