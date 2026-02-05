import winston from 'winston';
import 'winston-daily-rotate-file';
import path from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Asegurar que el directorio de logs centralizado exista
const logDir = path.resolve(__dirname, '..', '..', '..', 'logs');
if (!fs.existsSync(logDir)) {
    fs.mkdirSync(logDir, { recursive: true });
}

// Determine log level based on environment
const nodeEnv = process.env.NODE_ENV || 'development';
const logLevel = process.env.LOG_LEVEL || (nodeEnv === 'production' ? 'info' : 'debug');
const isProduction = nodeEnv === 'production';

// Custom format for console output
const consoleFormat = winston.format.combine(
    winston.format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
    winston.format.colorize(),
    winston.format.printf(({ timestamp, level, message, service, ...meta }) => {
        let msg = `${timestamp} [${service}] ${level}: ${message}`;

        // Add metadata if present
        if (Object.keys(meta).length > 0) {
            msg += ` ${JSON.stringify(meta)}`;
        }

        return msg;
    })
);

// Custom format for file output
const fileFormat = winston.format.combine(
    winston.format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
    winston.format.errors({ stack: true }),
    winston.format.splat(),
    winston.format.json()
);

// Create transports array
const transports = [];

// Daily rotate file for all logs
transports.push(
    new winston.transports.DailyRotateFile({
        filename: path.join(logDir, 'combined-%DATE%.log'),
        datePattern: 'YYYY-MM-DD',
        maxSize: '20m',
        maxFiles: '14d',
        format: fileFormat,
        level: logLevel
    })
);

// Error logs (always enabled)
transports.push(
    new winston.transports.DailyRotateFile({
        filename: path.join(logDir, 'error-%DATE%.log'),
        datePattern: 'YYYY-MM-DD',
        maxSize: '20m',
        maxFiles: '30d',
        format: fileFormat,
        level: 'error'
    })
);

// Debug logs (only in development)
if (!isProduction) {
    transports.push(
        new winston.transports.DailyRotateFile({
            filename: path.join(logDir, 'debug-%DATE%.log'),
            datePattern: 'YYYY-MM-DD',
            maxSize: '20m',
            maxFiles: '7d',
            format: fileFormat,
            level: 'debug'
        })
    );
}

// Console transport (always enabled, but with different formats)
transports.push(
    new winston.transports.Console({
        format: consoleFormat,
        level: logLevel
    })
);

// Create the logger
const logger = winston.createLogger({
    level: logLevel,
    defaultMeta: { service: 'ezbar-api' },
    transports,
    // Don't exit on handled exceptions
    exitOnError: false
});

// Add custom methods for better developer experience
logger.request = (req, meta = {}) => {
    logger.info(`${req.method} ${req.originalUrl}`, {
        ip: req.ip || req.connection.remoteAddress,
        userAgent: req.get('user-agent'),
        ...meta
    });
};

logger.response = (req, res, responseTime, meta = {}) => {
    const level = res.statusCode >= 400 ? 'warn' : 'info';
    logger.log(level, `${req.method} ${req.originalUrl} ${res.statusCode} - ${responseTime}ms`, {
        ip: req.ip || req.connection.remoteAddress,
        statusCode: res.statusCode,
        responseTime,
        ...meta
    });
};

logger.database = (query, duration, meta = {}) => {
    logger.debug(`DB Query: ${query.substring(0, 100)}... (${duration}ms)`, meta);
};

logger.cache = (action, key, meta = {}) => {
    logger.debug(`Cache ${action}: ${key}`, meta);
};

logger.memory = (usage, meta = {}) => {
    const heapUsed = (usage.heapUsed / 1024 / 1024).toFixed(2);
    const heapTotal = (usage.heapTotal / 1024 / 1024).toFixed(2);
    const percentage = ((usage.heapUsed / usage.heapTotal) * 100).toFixed(2);

    logger.debug(`Memory: ${heapUsed}MB / ${heapTotal}MB (${percentage}%)`, meta);
};

// Log startup information
logger.info('='.repeat(60));
logger.info(`Logger initialized - Environment: ${nodeEnv}`);
logger.info(`Log Level: ${logLevel}`);
logger.info(`Log Directory: ${logDir}`);
logger.info('='.repeat(60));

export default logger;
