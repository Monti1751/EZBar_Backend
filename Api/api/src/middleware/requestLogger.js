import logger from '../logger.js';

/**
 * Request logging middleware
 * Logs all incoming requests and their responses with timing information
 */
export function requestLogger(req, res, next) {
    const startTime = Date.now();

    // Log incoming request
    if (process.env.DEBUG === 'true') {
        logger.request(req, {
            body: req.body,
            query: req.query,
            params: req.params
        });
    } else {
        logger.request(req);
    }

    // Capture the original end function
    const originalEnd = res.end;

    // Override the end function to log response
    res.end = function (...args) {
        const responseTime = Date.now() - startTime;

        // Log response
        logger.response(req, res, responseTime);

        // Call the original end function
        originalEnd.apply(res, args);
    };

    next();
}

/**
 * Error logging middleware
 * Logs all errors with full stack traces
 */
export function errorLogger(err, req, res, next) {
    logger.error(`Error processing ${req.method} ${req.originalUrl}`, {
        error: err.message,
        stack: err.stack,
        ip: req.ip || req.connection.remoteAddress,
        body: req.body,
        query: req.query,
        params: req.params
    });

    next(err);
}
