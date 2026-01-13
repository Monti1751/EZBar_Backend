import express from 'express'; 
import * as pedidosController from '../controllers/pedidosController.js'; 
 
const router = express.Router(); 
 
router.post('/', pedidosController.crearPedido); 
router.get('/', pedidosController.obtenerPedidos); 
router.get('/:id/detalles', pedidosController.obtenerDetallesPedido);
router.get('/mesa/:mesaId/activo', pedidosController.obtenerPedidoActivoPorMesa);
router.post('/mesa/:mesaId/agregar-producto', pedidosController.agregarProductoAMesa);
router.delete('/detalles/:detalleId', pedidosController.eliminarDetallePedido);
 
export default router; 