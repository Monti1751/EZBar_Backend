export const errorHandler = (err, req, res, next) => {
  console.error('💥 Error:', err.message);
  
  // Error de conexión al backend
  if (err.code === 'ECONNREFUSED') {
    return res.status(503).json({
      error: 'Servicio no disponible',
      message: 'El backend no está disponible en este momento',
      details: err.message
    });
  }
  
  // Error de timeout
  if (err.code === 'ETIMEDOUT') {
    return res.status(504).json({
      error: 'Timeout',
      message: 'El servidor tardó demasiado en responder',
      details: err.message
    });
  }
  
  // Error genérico
  res.status(err.status || 500).json({
    error: err.message || 'Error interno del servidor',
    details: process.env.NODE_ENV === 'development' ? err.stack : undefined
  });
};