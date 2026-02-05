import { CONFIG } from '../config/constants.js';
import logger from '../logger.js';

/**
 * Global timeout middleware
 * Sets a timeout for all requests
 */
export function timeoutMiddleware(timeout = CONFIG.GLOBAL_TIMEOUT) {
    return (req, res, next) => {
        // Set timeout
        const timeoutId = setTimeout(() => {
            if (!res.headersSent) {
                logger.warn(`Request timeout: ${req.method} ${req.originalUrl} (${timeout}ms)`, {
                    ip: req.ip || req.connection.remoteAddress,
                    timeout
                });

                res.status(408).json({
                    error: 'Request Timeout',
                    message: `Request took longer than ${timeout}ms to complete`,
                    timeout
                });
            }
        }, timeout);

        // Clear timeout when response is sent
        res.on('finish', () => {
            clearTimeout(timeoutId);
        });

        res.on('close', () => {
            clearTimeout(timeoutId);
        });

        next();
    };
}

/**
 * Create a timeout middleware with custom timeout
 */
export function createTimeoutMiddleware(timeout) {
    return timeoutMiddleware(timeout);
}

/**
 * Route-specific timeout decorator
 * Usage: app.get('/slow-route', withTimeout(60000), handler)
 */
export function withTimeout(timeout) {
    return timeoutMiddleware(timeout);
}
