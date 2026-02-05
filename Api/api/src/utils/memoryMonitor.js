import logger from '../logger.js';
import { CONFIG } from '../config/constants.js';

/**
 * Memory Monitor
 * Tracks memory usage and warns about potential memory leaks
 */
class MemoryMonitor {
    constructor() {
        this.interval = null;
        this.lastHeapUsed = 0;
        this.warningCount = 0;
        this.criticalCount = 0;
    }

    /**
     * Start monitoring memory usage
     */
    start() {
        if (this.interval) {
            logger.warn('Memory monitor is already running');
            return;
        }

        logger.info(`Starting memory monitor (interval: ${CONFIG.MEMORY.CHECK_INTERVAL}ms)`);

        this.interval = setInterval(() => {
            this.checkMemory();
        }, CONFIG.MEMORY.CHECK_INTERVAL);

        // Initial check
        this.checkMemory();
    }

    /**
     * Stop monitoring
     */
    stop() {
        if (this.interval) {
            clearInterval(this.interval);
            this.interval = null;
            logger.info('Memory monitor stopped');
        }
    }

    /**
     * Check current memory usage
     */
    checkMemory() {
        const usage = process.memoryUsage();
        const heapUsedMB = (usage.heapUsed / 1024 / 1024).toFixed(2);
        const heapTotalMB = (usage.heapTotal / 1024 / 1024).toFixed(2);
        const rssMB = (usage.rss / 1024 / 1024).toFixed(2);
        const externalMB = (usage.external / 1024 / 1024).toFixed(2);
        const heapPercentage = usage.heapUsed / usage.heapTotal;

        // Log memory usage (debug level)
        logger.memory(usage);

        // Check for warnings
        if (heapPercentage >= CONFIG.MEMORY.HEAP_CRITICAL_THRESHOLD) {
            this.criticalCount++;
            logger.error(`🔴 CRITICAL: Memory usage at ${(heapPercentage * 100).toFixed(2)}%`, {
                heapUsed: `${heapUsedMB}MB`,
                heapTotal: `${heapTotalMB}MB`,
                rss: `${rssMB}MB`,
                external: `${externalMB}MB`,
                criticalCount: this.criticalCount
            });

            // If critical threshold is reached multiple times, suggest action
            if (this.criticalCount >= 3) {
                logger.error('⚠️  Consider restarting the application or investigating memory leaks');
            }
        } else if (heapPercentage >= CONFIG.MEMORY.HEAP_WARNING_THRESHOLD) {
            this.warningCount++;
            logger.warn(`🟡 WARNING: Memory usage at ${(heapPercentage * 100).toFixed(2)}%`, {
                heapUsed: `${heapUsedMB}MB`,
                heapTotal: `${heapTotalMB}MB`,
                rss: `${rssMB}MB`,
                external: `${externalMB}MB`,
                warningCount: this.warningCount
            });
        } else {
            // Reset counters if memory is back to normal
            this.warningCount = 0;
            this.criticalCount = 0;
        }

        // Detect potential memory leak (continuous growth)
        if (usage.heapUsed > this.lastHeapUsed * 1.5 && this.lastHeapUsed > 0) {
            logger.warn('⚠️  Potential memory leak detected: heap usage increased by 50%', {
                previous: `${(this.lastHeapUsed / 1024 / 1024).toFixed(2)}MB`,
                current: `${heapUsedMB}MB`
            });
        }

        this.lastHeapUsed = usage.heapUsed;
    }

    /**
     * Get current memory statistics
     */
    getStats() {
        const usage = process.memoryUsage();
        return {
            heapUsed: (usage.heapUsed / 1024 / 1024).toFixed(2) + ' MB',
            heapTotal: (usage.heapTotal / 1024 / 1024).toFixed(2) + ' MB',
            rss: (usage.rss / 1024 / 1024).toFixed(2) + ' MB',
            external: (usage.external / 1024 / 1024).toFixed(2) + ' MB',
            heapPercentage: ((usage.heapUsed / usage.heapTotal) * 100).toFixed(2) + '%',
            warningCount: this.warningCount,
            criticalCount: this.criticalCount
        };
    }

    /**
     * Force garbage collection (if --expose-gc flag is set)
     */
    forceGC() {
        if (global.gc) {
            logger.info('Forcing garbage collection...');
            global.gc();
            logger.info('Garbage collection completed');
            this.checkMemory();
        } else {
            logger.warn('Garbage collection not exposed. Start Node.js with --expose-gc flag to enable.');
        }
    }
}

// Export singleton instance
export const memoryMonitor = new MemoryMonitor();

/**
 * Express middleware to add memory stats to health check
 */
export function memoryMiddleware(req, res, next) {
    req.memoryStats = memoryMonitor.getStats();
    next();
}
