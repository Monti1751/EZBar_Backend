import NodeCache from 'node-cache';
import logger from '../logger.js';
import { CONFIG } from '../config/constants.js';

/**
 * Cache Service
 * In-memory cache with TTL support and statistics
 */
class CacheService {
    constructor() {
        this.cache = new NodeCache({
            stdTTL: CONFIG.CACHE.TTL,
            checkperiod: CONFIG.CACHE.CHECK_PERIOD,
            useClones: false // Better performance, but be careful with object mutations
        });

        this.stats = {
            hits: 0,
            misses: 0,
            sets: 0,
            deletes: 0
        };

        // Log cache events
        this.cache.on('set', (key, value) => {
            logger.cache('SET', key);
        });

        this.cache.on('del', (key, value) => {
            logger.cache('DELETE', key);
        });

        this.cache.on('expired', (key, value) => {
            logger.cache('EXPIRED', key);
        });

        logger.info(`✅ Cache service initialized (TTL: ${CONFIG.CACHE.TTL}s, Check period: ${CONFIG.CACHE.CHECK_PERIOD}s)`);
    }

    /**
     * Get value from cache
     */
    get(key) {
        const value = this.cache.get(key);

        if (value !== undefined) {
            this.stats.hits++;
            logger.cache('HIT', key);
            return value;
        }

        this.stats.misses++;
        logger.cache('MISS', key);
        return null;
    }

    /**
     * Set value in cache
     */
    set(key, value, ttl = null) {
        const success = ttl
            ? this.cache.set(key, value, ttl)
            : this.cache.set(key, value);

        if (success) {
            this.stats.sets++;
        }

        return success;
    }

    /**
     * Delete value from cache
     */
    delete(key) {
        const count = this.cache.del(key);
        if (count > 0) {
            this.stats.deletes++;
        }
        return count;
    }

    /**
     * Delete multiple keys matching a pattern
     */
    deletePattern(pattern) {
        const keys = this.cache.keys();
        const regex = new RegExp(pattern);
        const matchingKeys = keys.filter(key => regex.test(key));

        if (matchingKeys.length > 0) {
            const count = this.cache.del(matchingKeys);
            this.stats.deletes += count;
            logger.info(`Deleted ${count} cache entries matching pattern: ${pattern}`);
            return count;
        }

        return 0;
    }

    /**
     * Check if key exists in cache
     */
    has(key) {
        return this.cache.has(key);
    }

    /**
     * Clear all cache
     */
    flush() {
        this.cache.flushAll();
        logger.info('Cache flushed');
    }

    /**
     * Get cache statistics
     */
    getStats() {
        const cacheStats = this.cache.getStats();
        const hitRate = this.stats.hits + this.stats.misses > 0
            ? ((this.stats.hits / (this.stats.hits + this.stats.misses)) * 100).toFixed(2)
            : 0;

        return {
            enabled: CONFIG.CACHE.ENABLED,
            keys: cacheStats.keys,
            hits: this.stats.hits,
            misses: this.stats.misses,
            hitRate: `${hitRate}%`,
            sets: this.stats.sets,
            deletes: this.stats.deletes,
            ksize: cacheStats.ksize,
            vsize: cacheStats.vsize
        };
    }

    /**
     * Get all cache keys
     */
    getKeys() {
        return this.cache.keys();
    }

    /**
     * Get TTL for a key
     */
    getTTL(key) {
        return this.cache.getTtl(key);
    }

    /**
     * Update TTL for a key
     */
    setTTL(key, ttl) {
        return this.cache.ttl(key, ttl);
    }
}

// Export singleton instance
export const cacheService = CONFIG.CACHE.ENABLED ? new CacheService() : null;

/**
 * Helper function to get or set cache
 */
export async function getOrSet(key, fetchFunction, ttl = null) {
    if (!cacheService) {
        return await fetchFunction();
    }

    // Try to get from cache
    const cached = cacheService.get(key);
    if (cached !== null) {
        return cached;
    }

    // Fetch and cache
    const value = await fetchFunction();
    cacheService.set(key, value, ttl);
    return value;
}
