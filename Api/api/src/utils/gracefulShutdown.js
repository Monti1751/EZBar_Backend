import logger from '../logger.js';
import pool from '../config/database.js';

/**
 * Graceful Shutdown Handler
 * Ensures clean shutdown of all resources
 */
class GracefulShutdown {
    constructor() {
        this.isShuttingDown = false;
        this.servers = [];
        this.cleanupHandlers = [];
    }

    /**
     * Register HTTP/HTTPS servers for graceful shutdown
     */
    registerServer(server, name = 'Server') {
        this.servers.push({ server, name });
    }

    /**
     * Register custom cleanup handler
     */
    registerCleanupHandler(handler, name = 'Cleanup') {
        this.cleanupHandlers.push({ handler, name });
    }

    /**
     * Initialize graceful shutdown handlers
     */
    init() {
        // Handle different shutdown signals
        process.on('SIGTERM', () => this.shutdown('SIGTERM'));
        process.on('SIGINT', () => this.shutdown('SIGINT'));

        // Handle uncaught exceptions
        process.on('uncaughtException', (error) => {
            logger.error('Uncaught Exception:', error);
            this.shutdown('uncaughtException');
        });

        // Handle unhandled promise rejections
        process.on('unhandledRejection', (reason, promise) => {
            logger.error('Unhandled Rejection at:', promise, 'reason:', reason);
            this.shutdown('unhandledRejection');
        });

        logger.info('✅ Graceful shutdown handlers initialized');
    }

    /**
     * Perform graceful shutdown
     */
    async shutdown(signal) {
        if (this.isShuttingDown) {
            logger.warn('Shutdown already in progress...');
            return;
        }

        this.isShuttingDown = true;
        logger.info('='.repeat(60));
        logger.info(`🛑 Graceful shutdown initiated (signal: ${signal})`);
        logger.info('='.repeat(60));

        try {
            // 1. Stop accepting new connections
            logger.info('Closing HTTP/HTTPS servers...');
            for (const { server, name } of this.servers) {
                await new Promise((resolve) => {
                    server.close(() => {
                        logger.info(`✅ ${name} closed`);
                        resolve();
                    });
                });
            }

            // 2. Run custom cleanup handlers
            logger.info('Running cleanup handlers...');
            for (const { handler, name } of this.cleanupHandlers) {
                try {
                    await handler();
                    logger.info(`✅ ${name} completed`);
                } catch (error) {
                    logger.error(`❌ ${name} failed:`, error);
                }
            }

            // 3. Close database connections
            logger.info('Closing database connections...');
            await pool.end();
            logger.info('✅ Database connections closed');

            // 4. Final log
            logger.info('='.repeat(60));
            logger.info('✅ Graceful shutdown completed successfully');
            logger.info('='.repeat(60));

            // Exit process
            process.exit(0);
        } catch (error) {
            logger.error('❌ Error during graceful shutdown:', error);
            process.exit(1);
        }
    }
}

// Export singleton instance
export const gracefulShutdown = new GracefulShutdown();
