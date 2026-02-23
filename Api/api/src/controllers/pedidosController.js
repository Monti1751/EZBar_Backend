import { fetchWithRetry } from '../utils/retryHelper.js';
import { CONFIG } from '../config/constants.js';
import pool from '../config/database.js';

// --- Crear Pedido (Proxy al backend Java si aplica o local) ---
export const crearPedido = async (req, res, next) => {
  try {
    // Intenta enviar el pedido al backend principal (Java) si esta configurado
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

// --- Obtener todos los pedidos ---
export const obtenerPedidos = async (req, res, next) => {
  try {
    // Solicita la lista al backend principal
    const response = await fetchWithRetry(`${CONFIG.BACKEND_URL}/pedidos`);
    res.json(response.data);
  } catch (error) {
    next(error);
  }
};

// --- Obtener detalles (productos) de un pedido ---
export const obtenerDetallesPedido = async (req, res, next) => {
  let connection;
  try {
    const { id } = req.params;
    connection = await pool.getConnection();

    // Consulta SQL uniendo DETALLE_PEDIDOS con PRODUCTOS para tener nombres y precios
    console.log(`🔍 [DIAGNOSTICO] Buscando detalles para pedido ID: ${id}`);
    const [detalles] = await connection.query(
      `SELECT dp.detalle_id, dp.pedido_id, dp.producto_id,
              dp.cantidad, dp.precio_unitario, dp.total_linea,
              p.nombre, p.precio
       FROM DETALLE_PEDIDOS dp
       JOIN PRODUCTOS p ON dp.producto_id = p.producto_id
       WHERE dp.pedido_id = ?`,
      [id]
    );

    console.log(`✅ [DIAGNOSTICO] Query DETALLES devolvió ${detalles.length} filas`);
    detalles.forEach((d, idx) => {
      console.log(`📄 [DIAGNOSTICO] Fila ${idx + 1}: Producto ${d.nombre}, Cantidad ${d.cantidad}, Total Linea ${d.total_linea}`);
    });

    // Formatear respuesta para incluir el producto como objeto anidado (más fácil para el frontend)
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

    console.log(`📤 [DIAGNOSTICO] Enviando ${detallesFormateados.length} detalles formateados`);
    res.json(detallesFormateados);
  } catch (error) {
    next(error);
  } finally {
    if (connection) connection.release(); // Liberar conexión al pool
  }
};

// --- Obtener el pedido ACTIVO de una mesa ---
// Un pedido activo es aquel que no ha sido pagado ni cancelado. Cada mesa solo debería tener uno.
export const obtenerPedidoActivoPorMesa = async (req, res, next) => {
  let connection;
  try {
    const { mesaId } = req.params;
    console.log(`🔍 [DIAGNOSTICO] Buscando pedido activo para mesa: ${mesaId}`);
    connection = await pool.getConnection();

    // Buscar el último pedido de esta mesa que no esté finalizado
    const [pedidos] = await connection.query(
      `SELECT * FROM PEDIDOS WHERE mesa_id = ? AND estado NOT IN ('pagado','cancelado', 'listo') ORDER BY pedido_id DESC LIMIT 1`,
      [mesaId]
    );

    if (pedidos.length === 0) {
      console.log(`⚠️ [DIAGNOSTICO] No se encontró pedido activo para mesa: ${mesaId}`);
      return res.json(null); // No hay nadie comiendo en esa mesa
    }

    console.log(`✅ [DIAGNOSTICO] Pedido activo encontrado: ID ${pedidos[0].pedido_id}, Estado: ${pedidos[0].estado}`);
    res.json(pedidos[0]);
  } catch (error) {
    next(error);
  } finally {
    if (connection) connection.release();
  }
};

// --- Agregar Producto a una Mesa (Lógica Compleja) ---
// Esta función maneja: crear pedido si no existe, o añadir a uno existente.
export const agregarProductoAMesa = async (req, res, next) => {
  let connection;
  try {
    const { mesaId } = req.params;
    const { productoId } = req.body; // Solo necesitamos saber qué producto agregar

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

    // 2. Verificar que el producto existe y obtener su precio actual
    const [productos] = await connection.query(
      'SELECT producto_id, precio FROM PRODUCTOS WHERE producto_id = ? AND activo = TRUE',
      [productoId]
    );

    if (productos.length === 0) {
      return res.status(404).json({ error: `Producto ${productoId} no encontrado` });
    }

    const precioProducto = parseFloat(productos[0].precio);

    // 3. Obtener un empleado válido (usuario 'por defecto' para pedidos de app)
    // En el futuro, esto podría venir del token de sesión
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
       AND estado NOT IN ('pagado', 'cancelado', 'listo')
       ORDER BY pedido_id DESC LIMIT 1`,
      [mesaId]
    );

    let pedidoId;

    if (pedidosExistentes.length > 0) {
      // Ya hay un pedido abierto, usaremos ese
      pedidoId = pedidosExistentes[0].pedido_id;
      console.log(`✅ Usando pedido existente: ${pedidoId}`);
    } else {
      // No hay pedido abierto, CREAMOS uno nuevo
      const [resultado] = await connection.query(
        `INSERT INTO PEDIDOS (mesa_id, empleado_id, estado, numero_comensales, total_pedido)
         VALUES (?, ?, 'pendiente', 1, 0)`,
        [mesaId, empleadoId]
      );
      pedidoId = resultado.insertId;
      console.log(`✨ Pedido creado: ${pedidoId}`);
    }

    // 5. Verificar si este producto ya estaba en el pedido (para sumar cantidad)
    const [detalleExistente] = await connection.query(
      `SELECT detalle_id, cantidad, total_linea FROM DETALLE_PEDIDOS
       WHERE pedido_id = ? AND producto_id = ?`,
      [pedidoId, productoId]
    );

    if (detalleExistente.length > 0) {
      // El producto ya está, incrementamos cantidad
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
      // Es la primera vez que se pide este producto en este pedido
      const totalLinea = 1 * precioProducto;
      await connection.query(
        `INSERT INTO DETALLE_PEDIDOS (pedido_id, producto_id, cantidad, precio_unitario, total_linea)
         VALUES (?, ?, 1, ?, ?)`,
        [pedidoId, productoId, precioProducto, totalLinea]
      );
      console.log(`➕ Detalle creado para producto ${productoId}`);
    }

    // 6. Recalcular el TOTAL del pedido completo
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

// --- Eliminar producto (detalle) de pedido ---
export const eliminarDetallePedido = async (req, res, next) => {
  let connection;
  try {
    const { detalleId } = req.params;

    if (!detalleId) {
      return res.status(400).json({ error: 'detalleId es requerido' });
    }

    console.log(`🗑️ Eliminando detalle ${detalleId}`);

    connection = await pool.getConnection();

    // 1. Obtener el pedido_id antes de eliminar para poder recalcular el total luego
    const [detalles] = await connection.query(
      'SELECT pedido_id FROM DETALLE_PEDIDOS WHERE detalle_id = ?',
      [detalleId]
    );

    if (detalles.length === 0) {
      return res.status(404).json({ error: `Detalle ${detalleId} no encontrado` });
    }

    const pedidoId = detalles[0].pedido_id;

    // 2. Verificar cantidad actual para decidir si borrar o decrementar
    const [detalleActual] = await connection.query(
      'SELECT cantidad, precio_unitario FROM DETALLE_PEDIDOS WHERE detalle_id = ?',
      [detalleId]
    );

    if (detalleActual.length === 0) {
      console.log(`⚠️ No se encontró el detalle ${detalleId} al intentar decrementarlo`);
      return res.status(404).json({ error: 'Detalle no encontrado' });
    }

    const cantidadActual = Number(detalleActual[0].cantidad);
    const precioUnitario = Number(detalleActual[0].precio_unitario);

    console.log(`📊 Detalle ${detalleId}: Cantidad actual = ${cantidadActual}, Precio Unitario = ${precioUnitario}`);

    if (cantidadActual > 1) {
      // Si hay más de uno, decrementamos cantidad y actualizamos total_linea
      const nuevaCantidad = cantidadActual - 1;
      const nuevoTotalLinea = nuevaCantidad * precioUnitario;

      console.log(`📉 Decrementando: ${cantidadActual} -> ${nuevaCantidad}. Nuevo total linea: ${nuevoTotalLinea}`);

      await connection.query(
        'UPDATE DETALLE_PEDIDOS SET cantidad = ?, total_linea = ? WHERE detalle_id = ?',
        [nuevaCantidad, nuevoTotalLinea, detalleId]
      );
      console.log(`✅ Cantidad decrementada exitosamente en la DB`);
    } else {
      // Si solo queda uno, eliminamos el registro
      console.log(`🗑️ Cantidad es 1 o menor, procediendo a eliminar registro completo`);
      await connection.query(
        'DELETE FROM DETALLE_PEDIDOS WHERE detalle_id = ?',
        [detalleId]
      );
      console.log(`✅ Registro eliminado exitosamente de la DB`);
    }

    console.log(`🔄 Recalculando total del pedido ${pedidoId}...`);

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

// --- Finalizar Pedido (Marcar como listo y liberar mesa) ---
export const finalizarPedido = async (req, res, next) => {
  let connection;
  try {
    const { id } = req.params;
    connection = await pool.getConnection();

    console.log(`💰 Finalizando pedido ${id}`);

    // Usar transacción para asegurar que tanto el pedido como la mesa se actualicen
    await connection.beginTransaction();

    // 1. Obtener el mesa_id de este pedido
    const [pedidos] = await connection.query(
      "SELECT mesa_id FROM PEDIDOS WHERE pedido_id = ?",
      [id]
    );

    if (pedidos.length === 0) {
      await connection.rollback();
      return res.status(404).json({ error: 'Pedido no encontrado' });
    }

    const mesaId = pedidos[0].mesa_id;

    // 2. Marcar pedido como 'listo'
    await connection.query(
      "UPDATE PEDIDOS SET estado = 'listo' WHERE pedido_id = ?",
      [id]
    );

    // 3. Liberar la mesa
    await connection.query(
      "UPDATE MESAS SET estado = 'libre' WHERE mesa_id = ?",
      [mesaId]
    );

    await connection.commit();

    console.log(`✅ Pedido ${id} finalizado y mesa ${mesaId} liberada`);
    res.json({ success: true, mensaje: 'Pedido finalizado y mesa liberada correctamente' });
  } catch (error) {
    if (connection) await connection.rollback();
    next(error);
  } finally {
    if (connection) connection.release();
  }
};