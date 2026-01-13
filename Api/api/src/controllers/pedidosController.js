import { fetchWithRetry } from '../utils/retryHelper.js';
import { CONFIG } from '../config/constants.js';
import pool from '../config/database.js';

export const crearPedido = async (req, res, next) => {
  try {
    const response = await fetchWithRetry(
      `${CONFIG.BACKEND_URL}/pedidos`,
      {
        method: 'POST',
        data: req.body
      }
    );
    res.status(201).json(response.data);
  } catch (error) {
    next(error);
  }
};

export const obtenerPedidos = async (req, res, next) => {
  try {
    const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/pedidos`);
    res.json(response.data);
  } catch (error) {
    next(error);
  }
};

export const obtenerDetallesPedido = async (req, res, next) => {
  let connection;
  try {
    const { id } = req.params;
    connection = await pool.getConnection();
    const [detalles] = await connection.query(
      `SELECT dp.detalle_id, dp.pedido_id, dp.producto_id, 
              dp.cantidad, dp.precio_unitario, dp.total_linea,
              p.nombre, p.precio
       FROM DETALLE_PEDIDOS dp
       JOIN PRODUCTOS p ON dp.producto_id = p.producto_id
       WHERE dp.pedido_id = ?`,
      [id]
    );

    // Formatear respuesta para incluir el producto como objeto
    const detallesFormateados = detalles.map(d => ({
      detalle_id: d.detalle_id,
      pedido_id: d.pedido_id,
      producto_id: d.producto_id,
      cantidad: d.cantidad,
      precio_unitario: d.precio_unitario,
      total_linea: d.total_linea,
      producto: {
        producto_id: d.producto_id,
        nombre: d.nombre,
        precio: d.precio
      }
    }));

    res.json(detallesFormateados);
  } catch (error) {
    next(error);
  } finally {
    if (connection) connection.release();
  }
};

export const obtenerPedidoActivoPorMesa = async (req, res, next) => {
  let connection;
  try {
    const { mesaId } = req.params;
    connection = await pool.getConnection();
    const [pedidos] = await connection.query(
      `SELECT * FROM PEDIDOS WHERE mesa_id = ? AND estado NOT IN ('pagado','cancelado') ORDER BY pedido_id DESC LIMIT 1`,
      [mesaId]
    );
    if (pedidos.length === 0) {
      return res.json(null);
    }
    res.json(pedidos[0]);
  } catch (error) {
    next(error);
  } finally {
    if (connection) connection.release();
  }
};

export const agregarProductoAMesa = async (req, res, next) => {
  let connection;
  try {
    const { mesaId } = req.params;
    const { productoId } = req.body;

    if (!mesaId || !productoId) {
      return res.status(400).json({ error: 'mesaId y productoId son requeridos' });
    }

    console.log(`📥 Agregando producto ${productoId} a mesa ${mesaId}`);

    connection = await pool.getConnection();

    // 1. Verificar que la mesa existe
    const [mesas] = await connection.query(
      'SELECT mesa_id FROM MESAS WHERE mesa_id = ?',
      [mesaId]
    );

    if (mesas.length === 0) {
      return res.status(404).json({ error: `Mesa ${mesaId} no encontrada` });
    }

    // 2. Verificar que el producto existe
    const [productos] = await connection.query(
      'SELECT producto_id, precio FROM PRODUCTOS WHERE producto_id = ? AND activo = TRUE',
      [productoId]
    );

    if (productos.length === 0) {
      return res.status(404).json({ error: `Producto ${productoId} no encontrado` });
    }

    const precioProducto = parseFloat(productos[0].precio);

    // 3. Obtener un empleado válido
    const [empleados] = await connection.query(
      'SELECT empleado_id FROM EMPLEADOS WHERE activo = TRUE LIMIT 1'
    );

    let empleadoId;
    if (empleados.length > 0) {
      empleadoId = empleados[0].empleado_id;
    } else {
      return res.status(500).json({
        error: 'No hay empleados disponibles en el sistema.'
      });
    }

    console.log(`✅ Usando empleado: ${empleadoId}`);

    // 4. Buscar pedido activo para esta mesa
    const [pedidosExistentes] = await connection.query(
      `SELECT pedido_id, total_pedido FROM PEDIDOS 
       WHERE mesa_id = ? 
       AND estado NOT IN ('pagado', 'cancelado')
       ORDER BY pedido_id DESC LIMIT 1`,
      [mesaId]
    );

    let pedidoId;

    if (pedidosExistentes.length > 0) {
      pedidoId = pedidosExistentes[0].pedido_id;
      console.log(`✅ Usando pedido existente: ${pedidoId}`);
    } else {
      const [resultado] = await connection.query(
        `INSERT INTO PEDIDOS (mesa_id, empleado_id, estado, numero_comensales, total_pedido)
         VALUES (?, ?, 'pendiente', 1, 0)`,
        [mesaId, empleadoId]
      );
      pedidoId = resultado.insertId;
      console.log(`✨ Pedido creado: ${pedidoId}`);
    }

    // 5. Verificar si el producto ya existe
    const [detalleExistente] = await connection.query(
      `SELECT detalle_id, cantidad, total_linea FROM DETALLE_PEDIDOS 
       WHERE pedido_id = ? AND producto_id = ?`,
      [pedidoId, productoId]
    );

    if (detalleExistente.length > 0) {
      const detalleId = detalleExistente[0].detalle_id;
      const cantidadActual = parseFloat(detalleExistente[0].cantidad) || 1;
      const nuevaCantidad = cantidadActual + 1;
      const nuevoTotal = nuevaCantidad * precioProducto;

      await connection.query(
        `UPDATE DETALLE_PEDIDOS 
         SET cantidad = ?, total_linea = ?
         WHERE detalle_id = ?`,
        [nuevaCantidad, nuevoTotal, detalleId]
      );
      console.log(`📈 Cantidad actualizada para producto ${productoId}`);
    } else {
      const totalLinea = 1 * precioProducto;
      await connection.query(
        `INSERT INTO DETALLE_PEDIDOS (pedido_id, producto_id, cantidad, precio_unitario, total_linea)
         VALUES (?, ?, 1, ?, ?)`,
        [pedidoId, productoId, precioProducto, totalLinea]
      );
      console.log(`➕ Detalle creado para producto ${productoId}`);
    }

    // 6. Actualizar total del pedido
    const [totales] = await connection.query(
      `SELECT SUM(total_linea) as total FROM DETALLE_PEDIDOS WHERE pedido_id = ?`,
      [pedidoId]
    );

    const totalPedido = parseFloat(totales[0].total) || 0;

    await connection.query(
      `UPDATE PEDIDOS SET total_pedido = ? WHERE pedido_id = ?`,
      [totalPedido, pedidoId]
    );

    console.log(`✅ Producto agregado exitosamente. Total pedido: $${totalPedido}`);

    res.status(201).json({
      success: true,
      pedido_id: pedidoId,
      total_pedido: totalPedido,
      mensaje: 'Producto agregado correctamente'
    });

  } catch (error) {
    console.error('❌ Error en agregarProductoAMesa:', error);
    next(error);
  } finally {
    if (connection) {
      connection.release();
    }
  }
};

export const eliminarDetallePedido = async (req, res, next) => {
  let connection;
  try {
    const { detalleId } = req.params;

    if (!detalleId) {
      return res.status(400).json({ error: 'detalleId es requerido' });
    }

    console.log(`🗑️ Eliminando detalle ${detalleId}`);

    connection = await pool.getConnection();

    // 1. Obtener el pedido_id antes de eliminar
    const [detalles] = await connection.query(
      'SELECT pedido_id FROM DETALLE_PEDIDOS WHERE detalle_id = ?',
      [detalleId]
    );

    if (detalles.length === 0) {
      return res.status(404).json({ error: `Detalle ${detalleId} no encontrado` });
    }

    const pedidoId = detalles[0].pedido_id;

    // 2. Eliminar el detalle
    await connection.query(
      'DELETE FROM DETALLE_PEDIDOS WHERE detalle_id = ?',
      [detalleId]
    );

    console.log(`✅ Detalle eliminado`);

    // 3. Recalcular el total del pedido
    const [totales] = await connection.query(
      `SELECT SUM(total_linea) as total FROM DETALLE_PEDIDOS WHERE pedido_id = ?`,
      [pedidoId]
    );

    const totalPedido = parseFloat(totales[0].total) || 0;

    await connection.query(
      `UPDATE PEDIDOS SET total_pedido = ? WHERE pedido_id = ?`,
      [totalPedido, pedidoId]
    );

    console.log(`✅ Total del pedido actualizado: $${totalPedido}`);

    res.status(200).json({
      success: true,
      mensaje: 'Detalle eliminado correctamente',
      total_pedido: totalPedido
    });

  } catch (error) {
    console.error('❌ Error en eliminarDetallePedido:', error);
    next(error);
  } finally {
    if (connection) {
      connection.release();
    }
  }
};
