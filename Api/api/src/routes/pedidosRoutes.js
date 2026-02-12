import express from 'express';
import * as pedidosController from '../controllers/pedidosController.js';

const router = express.Router();

// POST /api/pedidos - Crea un nuevo pedido (Generalmente se usa para sincronizar o crear desde backend java)
router.post('/', pedidosController.crearPedido);
// GET /api/pedidos - Obtiene todos los pedidos
router.get('/', pedidosController.obtenerPedidos);

// GET /api/pedidos/:id/detalles - Obtiene los productos (detalles) de un pedido específico
router.get('/:id/detalles', pedidosController.obtenerDetallesPedido);

// GET /api/pedidos/mesa/:mesaId/activo - Obtiene el pedido que está actualmente abierto en una mesa
router.get('/mesa/:mesaId/activo', pedidosController.obtenerPedidoActivoPorMesa);

// POST /api/pedidos/mesa/:mesaId/agregar-producto - Agrega un producto a la mesa (al pedido activo o crea uno nuevo)
router.post('/mesa/:mesaId/agregar-producto', pedidosController.agregarProductoAMesa);

// DELETE /api/pedidos/detalles/:detalleId - Elimina un producto (línea de detalle) de un pedido
router.delete('/detalles/:detalleId', pedidosController.eliminarDetallePedido);

// PUT /api/pedidos/:id/finalizar - Marca un pedido como pagado
router.put('/:id/finalizar', pedidosController.finalizarPedido);

export default router;