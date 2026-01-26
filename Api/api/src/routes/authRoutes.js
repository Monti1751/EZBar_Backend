import express from 'express';
import { login } from '../controllers/authController.js';

const router = express.Router();

// Definir la ruta POST para /login
// Cuando se llame a esta URL, se ejecutará la función 'login' del controlador
router.post('/login', login);

export default router;
