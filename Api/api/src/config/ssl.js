import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { execSync } from 'child_process';
import logger from '../logger.js';
import { CONFIG } from './constants.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

/**
 * SSL Certificate Manager
 * Handles SSL certificate generation and loading for HTTPS
 */
class SSLManager {
    constructor() {
        this.certDir = path.resolve(__dirname, '..', '..', 'certs');
        this.keyPath = path.resolve(__dirname, '..', '..', CONFIG.SSL.KEY_PATH);
        this.certPath = path.resolve(__dirname, '..', '..', CONFIG.SSL.CERT_PATH);
    }

    /**
     * Get SSL credentials (key and cert)
     * Auto-generates self-signed certificate if needed in development
     */
    getCredentials() {
        // Ensure certs directory exists
        if (!fs.existsSync(this.certDir)) {
            fs.mkdirSync(this.certDir, { recursive: true });
        }

        // Check if certificates exist
        const keyExists = fs.existsSync(this.keyPath);
        const certExists = fs.existsSync(this.certPath);

        if (!keyExists || !certExists) {
            if (CONFIG.SSL.AUTO_GENERATE && CONFIG.IS_DEVELOPMENT) {
                logger.info('SSL certificates not found. Generating self-signed certificate for development...');
                this.generateSelfSignedCertificate();
            } else {
                throw new Error(
                    `SSL certificates not found at:\n` +
                    `  Key: ${this.keyPath}\n` +
                    `  Cert: ${this.certPath}\n` +
                    `Please provide valid SSL certificates or enable AUTO_GENERATE in development.`
                );
            }
        }

        // Read and return credentials
        try {
            const key = fs.readFileSync(this.keyPath, 'utf8');
            const cert = fs.readFileSync(this.certPath, 'utf8');

            logger.info('✅ SSL certificates loaded successfully');
            logger.debug(`Key: ${this.keyPath}`);
            logger.debug(`Cert: ${this.certPath}`);

            return { key, cert };
        } catch (error) {
            logger.error('Failed to read SSL certificates:', error);
            throw error;
        }
    }

    /**
     * Generate self-signed certificate for development
     * Uses OpenSSL if available, otherwise creates a basic certificate
     */
    generateSelfSignedCertificate() {
        try {
            // Try to use OpenSSL (most common)
            const command = `openssl req -x509 -newkey rsa:2048 -nodes ` +
                `-keyout "${this.keyPath}" -out "${this.certPath}" -days 365 ` +
                `-subj "/C=ES/ST=State/L=City/O=EZBar/CN=localhost"`;

            execSync(command, { stdio: 'pipe' });
            logger.info('✅ Self-signed SSL certificate generated successfully using OpenSSL');
        } catch (error) {
            // If OpenSSL is not available, use Node.js crypto (fallback)
            logger.warn('OpenSSL not available. Using Node.js crypto for certificate generation...');
            this.generateCertificateWithNodeCrypto();
        }
    }

    /**
     * Generate certificate using Node.js crypto module (fallback)
     */
    async generateCertificateWithNodeCrypto() {
        const { generateKeyPairSync } = await import('crypto');
        const forge = await this.loadForge();

        if (!forge) {
            throw new Error(
                'Cannot generate SSL certificate. Please install OpenSSL or the "node-forge" package:\n' +
                'npm install node-forge'
            );
        }

        // Generate key pair
        const keys = forge.pki.rsa.generateKeyPair(2048);

        // Create certificate
        const cert = forge.pki.createCertificate();
        cert.publicKey = keys.publicKey;
        cert.serialNumber = '01';
        cert.validity.notBefore = new Date();
        cert.validity.notAfter = new Date();
        cert.validity.notAfter.setFullYear(cert.validity.notBefore.getFullYear() + 1);

        const attrs = [{
            name: 'commonName',
            value: 'localhost'
        }, {
            name: 'countryName',
            value: 'ES'
        }, {
            shortName: 'ST',
            value: 'State'
        }, {
            name: 'localityName',
            value: 'City'
        }, {
            name: 'organizationName',
            value: 'EZBar'
        }];

        cert.setSubject(attrs);
        cert.setIssuer(attrs);
        cert.sign(keys.privateKey);

        // Save to files
        const pem = {
            privateKey: forge.pki.privateKeyToPem(keys.privateKey),
            certificate: forge.pki.certificateToPem(cert)
        };

        fs.writeFileSync(this.keyPath, pem.privateKey);
        fs.writeFileSync(this.certPath, pem.certificate);

        logger.info('✅ Self-signed SSL certificate generated successfully using Node.js crypto');
    }

    /**
     * Try to load node-forge if available
     */
    async loadForge() {
        try {
            const forge = await import('node-forge');
            return forge.default || forge;
        } catch (error) {
            return null;
        }
    }
}

// Export singleton instance
export const sslManager = new SSLManager();

/**
 * Get SSL credentials for HTTPS server
 */
export function getSSLCredentials() {
    return sslManager.getCredentials();
}
