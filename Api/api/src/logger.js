import winston from 'winston';
import 'winston-daily-rotate-file';
import path from 'path';
import fs from 'fs';

// Asegurar que el directorio de logs centralizado exista
const logDir = path.resolve('..', '..', '..', 'logs'); // Sube 3 niveles desde api/src/logger.js hasta tfg/logs
if (!fs.existsSync(logDir)) {
    fs.mkdirSync(logDir, { recursive: true });
}

const logger = winston.createLogger({
    level: 'info',
    format: winston.format.combine(
        winston.format.timestamp({
            format: 'YYYY-MM-DD HH:mm:ss'
        }),
        winston.format.errors({ stack: true }),
        winston.format.splat(),
        winston.format.json()
    ),
    defaultMeta: { service: 'ezbar-api' },
    transports: [
        //
        // - Escribir todos los logs con nivel `error` o inferior en `node_error.log`
        //
        new winston.transports.File({
            filename: path.join(logDir, 'node_error.log'),
            level: 'error',
            maxsize: 5242880, // 5MB
            maxFiles: 5,
        }),
        //
        // - Escribir todos los logs con nivel `info` o inferior en `node_app.log`
        //
        new winston.transports.File({
            filename: path.join(logDir, 'node_app.log'),
            maxsize: 5242880, // 5MB
            maxFiles: 5,
        }),
    ],
});

//
// Si no estamos en producción, logging en consola con formato simple
//
if (process.env.NODE_ENV !== 'production') {
    logger.add(new winston.transports.Console({
        format: winston.format.combine(
            winston.format.colorize(),
            winston.format.simple()
        ),
    }));
}

export default logger;
