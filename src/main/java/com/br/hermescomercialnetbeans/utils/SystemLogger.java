package com.br.hermescomercialnetbeans.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Classe Singleton para gerenciamento centralizado de logs do sistema
 * Implementa o padrão Singleton para logging centralizado com buffer
 * @author marcos
 */
public class SystemLogger {
    
    // Instância única do Singleton
    private static SystemLogger instance;
    
    private static final Logger logger = LogManager.getLogger(SystemLogger.class);
    
    // Buffer para logs recentes (últimos 1000 logs)
    private final ConcurrentLinkedQueue<LogEntry> logBuffer;
    private static final int MAX_BUFFER_SIZE = 1000;
    
    // Formato de data/hora para logs
    private static final DateTimeFormatter LOG_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Entrada de log para o buffer
     */
    private static class LogEntry {
        final LocalDateTime timestamp;
        final String level;
        final String message;
        final String threadName;
        
        LogEntry(String level, String message) {
            this.timestamp = LocalDateTime.now();
            this.level = level;
            this.message = message;
            this.threadName = Thread.currentThread().getName();
        }
        
        @Override
        public String toString() {
            return String.format("[%s] [%s] [%s] %s", 
                timestamp.format(LOG_FORMATTER), level, threadName, message);
        }
    }

    /**
     * Construtor privado para implementar o padrão Singleton
     */
    private SystemLogger() {
        this.logBuffer = new ConcurrentLinkedQueue<>();
        logger.info("Instância do SystemLogger Singleton criada");
    }

    /**
     * Método estático para obter a instância única do Singleton
     * @return instância única de SystemLogger
     */
    public static synchronized SystemLogger getInstance() {
        if (instance == null) {
            synchronized (SystemLogger.class) {
                if (instance == null) {
                    instance = new SystemLogger();
                }
            }
        }
        return instance;
    }

    /**
     * Registra log de nível INFO
     * @param message mensagem do log
     */
    public void info(String message) {
        logger.info(message);
        addToBuffer("INFO", message);
    }

    /**
     * Registra log de nível WARN
     * @param message mensagem do log
     */
    public void warn(String message) {
        logger.warn(message);
        addToBuffer("WARN", message);
    }

    /**
     * Registra log de nível ERROR
     * @param message mensagem do log
     */
    public void error(String message) {
        logger.error(message);
        addToBuffer("ERROR", message);
    }

    /**
     * Registra log de nível ERROR com exceção
     * @param message mensagem do log
     * @param throwable exceção
     */
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
        addToBuffer("ERROR", message + " - " + throwable.getMessage());
    }

    /**
     * Registra log de nível DEBUG
     * @param message mensagem do log
     */
    public void debug(String message) {
        logger.debug(message);
        addToBuffer("DEBUG", message);
    }

    /**
     * Registra log de nível TRACE
     * @param message mensagem do log
     */
    public void trace(String message) {
        logger.trace(message);
        addToBuffer("TRACE", message);
    }

    /**
     * Adiciona entrada ao buffer circular
     * @param level nível do log
     * @param message mensagem do log
     */
    private void addToBuffer(String level, String message) {
        LogEntry entry = new LogEntry(level, message);
        logBuffer.offer(entry);
        
        // Mantém o buffer no tamanho máximo
        while (logBuffer.size() > MAX_BUFFER_SIZE) {
            logBuffer.poll();
        }
    }

    /**
     * Obtém os logs recentes do buffer
     * @param limit número máximo de logs a retornar
     * @return array com os logs recentes
     */
    public String[] getRecentLogs(int limit) {
        LogEntry[] entries = logBuffer.toArray(new LogEntry[0]);
        int size = Math.min(entries.length, limit);
        
        String[] logs = new String[size];
        int startIndex = Math.max(0, entries.length - size);
        
        for (int i = 0; i < size; i++) {
            logs[i] = entries[startIndex + i].toString();
        }
        
        return logs;
    }

    /**
     * Obtém todos os logs do buffer
     * @return array com todos os logs do buffer
     */
    public String[] getAllLogs() {
        return getRecentLogs(MAX_BUFFER_SIZE);
    }

    /**
     * Limpa o buffer de logs
     */
    public void clearBuffer() {
        logBuffer.clear();
        logger.info("Buffer de logs limpo");
    }

    /**
     * Obtém o tamanho atual do buffer
     * @return número de logs no buffer
     */
    public int getBufferSize() {
        return logBuffer.size();
    }

    /**
     * Registra log de operação de sistema
     * @param operation nome da operação
     * @param details detalhes da operação
     */
    public void logOperation(String operation, String details) {
        String message = String.format("OPERATION: %s | %s", operation, details);
        info(message);
    }

    /**
     * Registra log de performance
     * @param operation nome da operação
     * @param durationMs duração em milissegundos
     */
    public void logPerformance(String operation, long durationMs) {
        String message = String.format("PERFORMANCE: %s | Duration: %dms", operation, durationMs);
        info(message);
    }

    /**
     * Registra log de auditoria
     * @param user usuário que realizou a ação
     * @param action ação realizada
     * @param resource recurso afetado
     */
    public void logAudit(String user, String action, String resource) {
        String message = String.format("AUDIT: User='%s' Action='%s' Resource='%s'", user, action, resource);
        info(message);
    }
}
