import express from 'express';
import { login, verifyToken } from '../controllers/authController.js';

const router = express.Router();

// Definir la ruta POST para /login
router.post('/login', login);

// Nueva ruta para verificar el token y obtener el rol
router.get('/verify', verifyToken);

export default router;
