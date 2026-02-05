import dotenv from 'dotenv';
import path from 'path';
import { fileURLToPath } from 'url';
import fs from 'fs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

/**
 * Load environment variables based on NODE_ENV
 * Automatically selects .env.development or .env.production
 * Falls back to .env if environment-specific file doesn't exist
 */
export function loadEnvironment() {
    const nodeEnv = process.env.NODE_ENV || 'development';
    const rootDir = path.resolve(__dirname, '..', '..');

    // Determine which .env file to load
    const envFile = `.env.${nodeEnv}`;
    const envPath = path.join(rootDir, envFile);
    const defaultEnvPath = path.join(rootDir, '.env');

    // Load environment-specific file if it exists, otherwise fall back to .env
    if (fs.existsSync(envPath)) {
        console.log(`📋 Loading environment from: ${envFile}`);
        dotenv.config({ path: envPath });
    } else if (fs.existsSync(defaultEnvPath)) {
        console.log(`📋 Loading environment from: .env (${envFile} not found)`);
        dotenv.config({ path: defaultEnvPath });
    } else {
        console.warn(`⚠️  No environment file found. Using system environment variables.`);
    }

    // Validate required environment variables
    validateEnvironment();

    return {
        nodeEnv,
        isProduction: nodeEnv === 'production',
        isDevelopment: nodeEnv === 'development',
        isDebug: process.env.DEBUG === 'true'
    };
}

/**
 * Validate that all required environment variables are set
 */
function validateEnvironment() {
    const required = [
        'NODE_ENV',
        'PORT',
        'DB_HOST',
        'DB_PORT',
        'DB_NAME',
        'DB_USER'
    ];

    const missing = required.filter(key => !process.env[key]);

    if (missing.length > 0) {
        console.error(`❌ Missing required environment variables: ${missing.join(', ')}`);
        console.error('Please check your .env file configuration.');
        process.exit(1);
    }

    console.log('✅ Environment variables validated successfully');
}

/**
 * Get environment variable with type conversion and default value
 */
export function getEnv(key, defaultValue = undefined, type = 'string') {
    const value = process.env[key];

    if (value === undefined) {
        if (defaultValue !== undefined) {
            return defaultValue;
        }
        throw new Error(`Environment variable ${key} is not defined and no default value provided`);
    }

    switch (type) {
        case 'number':
            return parseInt(value, 10);
        case 'boolean':
            return value === 'true';
        case 'float':
            return parseFloat(value);
        case 'json':
            try {
                return JSON.parse(value);
            } catch (e) {
                throw new Error(`Failed to parse ${key} as JSON: ${e.message}`);
            }
        default:
            return value;
    }
}

// Auto-load environment on import
loadEnvironment();
