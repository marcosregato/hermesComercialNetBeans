package com.br.hermescomercialnetbeans.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Classe Singleton para gerenciamento de cache do sistema
 * Implementa o padrão Singleton com thread-safety para cache em memória
 * @author marcos
 */
public class CacheManager {
    
    // Instância única do Singleton
    private static CacheManager instance;
    
    private static final Logger logger = LogManager.getLogger(CacheManager.class);
    
    // Cache em memória com thread-safety
    private final Map<String, Object> cache;
    private final Map<String, Long> cacheTimestamps;
    private final ReentrantReadWriteLock lock;
    
    // Tempo de vida padrão do cache em milissegundos (5 minutos)
    private static final long DEFAULT_TTL = 5 * 60 * 1000;

    /**
     * Construtor privado para implementar o padrão Singleton
     */
    private CacheManager() {
        this.cache = new HashMap<>();
        this.cacheTimestamps = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        logger.info("Instância do CacheManager Singleton criada");
    }

    /**
     * Método estático para obter a instância única do Singleton
     * @return instância única de CacheManager
     */
    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    /**
     * Adiciona um objeto ao cache
     * @param key chave do cache
     * @param value valor a ser armazenado
     */
    public void put(String key, Object value) {
        put(key, value, DEFAULT_TTL);
    }

    /**
     * Adiciona um objeto ao cache com tempo de vida específico
     * @param key chave do cache
     * @param value valor a ser armazenado
     * @param ttl tempo de vida em milissegundos
     */
    public void put(String key, Object value, long ttl) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
            cacheTimestamps.put(key, System.currentTimeMillis() + ttl);
            logger.debug("Cache adicionado: " + key + " (TTL: " + ttl + "ms)");
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Obtém um objeto do cache
     * @param key chave do cache
     * @return valor armazenado ou null se não existir ou expirou
     */
    public Object get(String key) {
        lock.readLock().lock();
        try {
            Long expiryTime = cacheTimestamps.get(key);
            if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
                // Cache expirado ou não existe
                if (expiryTime != null) {
                    // Remove cache expirado
                    lock.readLock().unlock();
                    lock.writeLock().lock();
                    try {
                        cache.remove(key);
                        cacheTimestamps.remove(key);
                        logger.debug("Cache expirado e removido: " + key);
                    } finally {
                        lock.writeLock().unlock();
                        lock.readLock().lock();
                    }
                }
                return null;
            }
            
            logger.debug("Cache recuperado: " + key);
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Remove um objeto do cache
     * @param key chave do cache
     */
    public void remove(String key) {
        lock.writeLock().lock();
        try {
            cache.remove(key);
            cacheTimestamps.remove(key);
            logger.debug("Cache removido: " + key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Limpa todo o cache
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            int size = cache.size();
            cache.clear();
            cacheTimestamps.clear();
            logger.info("Cache limpo. " + size + " itens removidos.");
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Obtém o tamanho atual do cache
     * @return número de itens no cache
     */
    public int size() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Verifica se uma chave existe no cache e não expirou
     * @param key chave do cache
     * @return true se existe e não expirou
     */
    public boolean containsKey(String key) {
        return get(key) != null;
    }

    /**
     * Remove itens expirados do cache
     */
    public void cleanupExpired() {
        lock.writeLock().lock();
        try {
            long currentTime = System.currentTimeMillis();
            int removedCount = 0;
            
            cacheTimestamps.entrySet().removeIf(entry -> {
                if (currentTime > entry.getValue()) {
                    cache.remove(entry.getKey());
                    return true;
                }
                return false;
            });
            
            if (removedCount > 0) {
                logger.info("Cleanup: " + removedCount + " itens expirados removidos do cache.");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
