package com.ezbar.util;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;

/**
 * Servicio de monitoreo de memoria.
 * Proporciona información sobre el uso de memoria y recursos del sistema.
 */
@Service
public class MemoryMonitoringService {

    private static final Logger logger = LoggerFactory.getLogger(MemoryMonitoringService.class);

    private static final long MB = 1024 * 1024;
    private static final long THRESHOLD_PERCENTAGE = 80;

    /**
     * Obtiene información de memoria del heap.
     * 
     * @return Map con información de memoria
     */
    public MemoryInfo getMemoryInfo() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed();
        long heapMax = memoryBean.getHeapMemoryUsage().getMax();
        double usagePercentage = (double) heapUsed / heapMax * 100;

        MemoryInfo info = new MemoryInfo(
                heapUsed / MB,
                heapMax / MB,
                (heapMax - heapUsed) / MB,
                String.format("%.2f", usagePercentage));

        if (usagePercentage > THRESHOLD_PERCENTAGE) {
            logger.warn(
                    "Alerta de memoria: {}% ({} MB / {} MB)",
                    info.getUsagePercentage(),
                    info.getHeapUsedMB(),
                    info.getHeapMaxMB());
        }

        return info;
    }

    /**
     * Solicita recolección de basura.
     */
    public void triggerGarbageCollection() {
        logger.debug("Ejecutando recolección de basura...");
        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.gc();

        long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        logger.info(
                "GC ejecutado: {} MB liberados",
                (before - after) / MB);
    }

    /**
     * Obtiene información general del sistema.
     * 
     * @return Información del sistema
     */
    public SystemInfo getSystemInfo() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        Runtime runtime = Runtime.getRuntime();

        return new SystemInfo(
                runtimeBean.getVmName(),
                runtimeBean.getVmVersion(),
                runtimeBean.getVmVendor(),
                runtime.availableProcessors(),
                runtime.maxMemory() / MB);
    }

    /**
     * Verifica si el uso de memoria está dentro de los límites segoros.
     * 
     * @return true si está dentro de los límites
     */
    public boolean isMemoryHealthy() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        double usagePercentage = (double) memoryBean.getHeapMemoryUsage().getUsed()
                / memoryBean.getHeapMemoryUsage().getMax() * 100;

        return usagePercentage < THRESHOLD_PERCENTAGE;
    }

    /**
     * Clase interna para información de memoria.
     */
    public static class MemoryInfo {
        private final long heapUsedMB;
        private final long heapMaxMB;
        private final long heapFreeMB;
        private final String usagePercentage;

        public MemoryInfo(long heapUsedMB, long heapMaxMB, long heapFreeMB, String usagePercentage) {
            this.heapUsedMB = heapUsedMB;
            this.heapMaxMB = heapMaxMB;
            this.heapFreeMB = heapFreeMB;
            this.usagePercentage = usagePercentage;
        }

        public long getHeapUsedMB() {
            return heapUsedMB;
        }

        public long getHeapMaxMB() {
            return heapMaxMB;
        }

        public long getHeapFreeMB() {
            return heapFreeMB;
        }

        public String getUsagePercentage() {
            return usagePercentage;
        }

        @Override
        public String toString() {
            return "MemoryInfo{" +
                    "heapUsedMB=" + heapUsedMB +
                    ", heapMaxMB=" + heapMaxMB +
                    ", heapFreeMB=" + heapFreeMB +
                    ", usagePercentage='" + usagePercentage + "%'" +
                    '}';
        }
    }

    /**
     * Clase interna para información del sistema.
     */
    public static class SystemInfo {
        private final String vmName;
        private final String vmVersion;
        private final String vmVendor;
        private final int availableProcessors;
        private final long maxMemoryMB;

        public SystemInfo(String vmName, String vmVersion, String vmVendor, int availableProcessors, long maxMemoryMB) {
            this.vmName = vmName;
            this.vmVersion = vmVersion;
            this.vmVendor = vmVendor;
            this.availableProcessors = availableProcessors;
            this.maxMemoryMB = maxMemoryMB;
        }

        public String getVmName() {
            return vmName;
        }

        public String getVmVersion() {
            return vmVersion;
        }

        public String getVmVendor() {
            return vmVendor;
        }

        public int getAvailableProcessors() {
            return availableProcessors;
        }

        public long getMaxMemoryMB() {
            return maxMemoryMB;
        }
    }
}
