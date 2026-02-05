import helmet from 'helmet';
import rateLimit from 'express-rate-limit';
import { CONFIG } from '../config/constants.js';
import logger from '../logger.js';

/**
 * Security headers middleware using Helmet
 */
export function securityHeaders() {
    return helmet({
        contentSecurityPolicy: CONFIG.IS_PRODUCTION ? undefined : false,
        crossOriginEmbedderPolicy: CONFIG.IS_PRODUCTION ? undefined : false,
    });
}

/**
 * Rate limiting middleware
 * Prevents abuse by limiting requests per IP
 */
export const rateLimiter = rateLimit({
    windowMs: CONFIG.SECURITY.RATE_LIMIT_WINDOW_MS,
    max: CONFIG.SECURITY.RATE_LIMIT_MAX_REQUESTS,
    message: 'Too many requests from this IP, please try again later.',
    standardHeaders: true,
    legacyHeaders: false,
    handler: (req, res) => {
        logger.warn(`Rate limit exceeded for IP: ${req.ip}`);
        res.status(429).json({
            error: 'Too many requests',
            message: 'Please try again later.'
        });
    }
});

/**
 * HTTP to HTTPS redirect middleware
 * Only active in production when HTTPS is enabled
 */
export function httpsRedirect(req, res, next) {
    if (CONFIG.IS_PRODUCTION && CONFIG.HTTPS_ENABLED && !req.secure) {
        logger.debug(`Redirecting HTTP to HTTPS: ${req.originalUrl}`);
        return res.redirect(301, `https://${req.headers.host}${req.originalUrl}`);
    }
    next();
}

/**
 * CORS configuration
 */
export function getCorsOptions() {
    return {
        origin: CONFIG.SECURITY.CORS_ORIGIN,
        credentials: true,
        optionsSuccessStatus: 200
    };
}
