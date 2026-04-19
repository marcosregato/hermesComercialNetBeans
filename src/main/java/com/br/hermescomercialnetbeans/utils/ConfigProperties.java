
package com.br.hermescomercialnetbeans.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe Singleton para gerenciamento de configurações do sistema
 * Implementa o padrão Singleton para garantir uma única instância no sistema
 * @author marcos
 */
public class ConfigProperties {

    // Instância única do Singleton
    private static ConfigProperties instance;
    
    private static final Logger logger = LogManager.getLogger(ConfigProperties.class);
    private final Properties properties = new Properties();

    /**
     * Construtor privado para implementar o padrão Singleton
     */
    private ConfigProperties() {
        loadProperties();
        logger.info("Instância do ConfigProperties Singleton criada");
    }

    /**
     * Método estático para obter a instância única do Singleton
     * @return instância única de ConfigProperties
     */
    public static synchronized ConfigProperties getInstance() {
        if (instance == null) {
            synchronized (ConfigProperties.class) {
                if (instance == null) {
                    instance = new ConfigProperties();
                }
            }
        }
        return instance;
    }

    /**
     * Carrega as propriedades do arquivo de configuração
     */
    private void loadProperties() {
        String path = System.getProperty("user.dir") + "/config.properties";
        try (InputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
            logger.info("Arquivo de configurações carregado com sucesso: " + path);
        } catch (Exception e) {
            logger.error("Error loading config.properties file from path: " + path, e);
        }
    }

    /**
     * Obtém uma propriedade do arquivo de configuração
     * @param key chave da propriedade
     * @return valor da propriedade ou null se não encontrada
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return value.trim();
        } else {
            logger.warn("Property '" + key + "' not found in config.properties");
            return null;
        }
    }

    /**
     * Método estático para compatibilidade com código existente
     * @param key chave da propriedade
     * @return valor da propriedade ou null se não encontrada
     */
    public static String getPropertyStatic(String key) {
        return getInstance().getProperty(key);
    }
}
