import { cacheService } from '../services/cacheService.js';
import logger from '../logger.js';
import { CONFIG } from '../config/constants.js';

/**
 * Cache middleware for GET requests
 * Caches responses for frequently accessed endpoints
 */
export function cacheMiddleware(options = {}) {
    const {
        ttl = CONFIG.CACHE.TTL,
        keyGenerator = (req) => `${req.method}:${req.originalUrl}`
    } = options;

    return (req, res, next) => {
        // Only cache GET requests
        if (req.method !== 'GET' || !CONFIG.CACHE.ENABLED || !cacheService) {
            return next();
        }

        const key = keyGenerator(req);
        const cached = cacheService.get(key);

        if (cached) {
            logger.debug(`Cache hit for: ${key}`);
            return res.json(cached);
        }

        // Store original res.json
        const originalJson = res.json.bind(res);

        // Override res.json to cache the response
        res.json = function (data) {
            // Only cache successful responses
            if (res.statusCode >= 200 && res.statusCode < 300) {
                cacheService.set(key, data, ttl);
                logger.debug(`Cached response for: ${key}`);
            }

            return originalJson(data);
        };

        next();
    };
}

/**
 * Cache invalidation middleware
 * Invalidates cache on POST, PUT, PATCH, DELETE requests
 */
export function cacheInvalidationMiddleware(pattern) {
    return (req, res, next) => {
        if (!CONFIG.CACHE.ENABLED || !cacheService) {
            return next();
        }

        // Only invalidate on mutation methods
        if (['POST', 'PUT', 'PATCH', 'DELETE'].includes(req.method)) {
            // Store original res.json
            const originalJson = res.json.bind(res);

            // Override res.json to invalidate cache after successful response
            res.json = function (data) {
                // Only invalidate on successful responses
                if (res.statusCode >= 200 && res.statusCode < 300) {
                    const invalidationPattern = pattern || `GET:${req.baseUrl}.*`;
                    const count = cacheService.deletePattern(invalidationPattern);

                    if (count > 0) {
                        logger.info(`Invalidated ${count} cache entries matching: ${invalidationPattern}`);
                    }
                }

                return originalJson(data);
            };
        }

        next();
    };
}

/**
 * Create cache key from request
 */
export function createCacheKey(req, includeQuery = true, includeParams = true) {
    let key = `${req.method}:${req.baseUrl}${req.path}`;

    if (includeQuery && Object.keys(req.query).length > 0) {
        const sortedQuery = Object.keys(req.query)
            .sort()
            .map(k => `${k}=${req.query[k]}`)
            .join('&');
        key += `?${sortedQuery}`;
    }

    if (includeParams && Object.keys(req.params).length > 0) {
        const sortedParams = Object.keys(req.params)
            .sort()
            .map(k => `${k}=${req.params[k]}`)
            .join('&');
        key += `#${sortedParams}`;
    }

    return key;
}
