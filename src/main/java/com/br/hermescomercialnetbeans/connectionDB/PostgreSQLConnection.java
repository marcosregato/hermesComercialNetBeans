package com.br.hermescomercialnetbeans.connectionDB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Singleton especializada para conexão com banco de dados PostgreSQL
 * Implementa o padrão Singleton para garantir uma única instância no sistema
 * @author marcos
 */
public class PostgreSQLConnection {
    
    // Instância única do Singleton
    private static PostgreSQLConnection instance;
    
    private static final Logger logger = LogManager.getLogger(PostgreSQLConnection.class);
    
    // Configurações do PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/hermescomercialdb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Abacaxi@121";

    /**
     * Construtor privado para implementar o padrão Singleton
     */
    private PostgreSQLConnection() {
        logger.info("Instância do PostgreSQLConnection Singleton criada");
    }

    /**
     * Método estático para obter a instância única do Singleton
     * @return instância única de PostgreSQLConnection
     */
    public static synchronized PostgreSQLConnection getInstance() {
        if (instance == null) {
            synchronized (PostgreSQLConnection.class) {
                if (instance == null) {
                    instance = new PostgreSQLConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Obtém uma conexão com o banco PostgreSQL
     * @return Connection objeto de conexão ou null em caso de erro
     */
    public static Connection getConnection() {
        try {
            logger.info("Tentando conectar ao PostgreSQL...");
            logger.info("URL: " + URL);
            logger.info("Usuário: " + USER);
            
            // Carregar driver PostgreSQL
            Class.forName("org.postgresql.Driver");
            
            // Estabelecer conexão
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            if (conn != null && !conn.isClosed()) {
                logger.info("Conexão estabelecida com sucesso ao PostgreSQL!");
                return conn;
            } else {
                logger.error("Conexão retornou como nula ou fechada");
                return null;
            }
            
        } catch (ClassNotFoundException e) {
            logger.error("Driver PostgreSQL não encontrado! Verifique se o driver está no classpath.", e);
            logger.error("Adicione a dependência: org.postgresql:postgresql");
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao PostgreSQL:", e);
            logger.error("SQL State: " + e.getSQLState());
            logger.error("Error Code: " + e.getErrorCode());
            logger.error("Verifique se:");
            logger.error("1. PostgreSQL está instalado e rodando na porta 5432");
            logger.error("2. Banco 'hermescomercialdb' existe");
            logger.error("3. Usuário 'postgres' com senha '123456' existe");
            logger.error("4. Firewall não está bloqueando a conexão");
        } catch (Exception e) {
            logger.error("Erro inesperado ao conectar ao PostgreSQL:", e);
        }
        
        return null;
    }
    
    /**
     * Testa a conexão com o banco PostgreSQL
     * @return true se conexão for bem-sucedida, false caso contrário
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null) {
                logger.info("Teste de conexão: SUCESSO");
                return true;
            } else {
                logger.error("Teste de conexão: FALHA - conexão nula");
                return false;
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Erro ao fechar conexão de teste:", e);
                }
            }
        }
    }
    
    /**
     * Obtém informações da conexão para debug
     * @return String com informações detalhadas
     */
    public static String getConnectionInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== Informações de Conexão PostgreSQL ===\n");
        info.append("URL: ").append(URL).append("\n");
        info.append("Usuário: ").append(USER).append("\n");
        info.append("Senha: ").append(PASSWORD.replaceAll(".", "*")).append("\n");
        info.append("Driver: org.postgresql.Driver\n");
        info.append("=====================================");
        return info.toString();
    }
    
    /**
     * Verifica se o driver PostgreSQL está disponível
     * @return true se driver estiver disponível
     */
    public static boolean isDriverAvailable() {
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("Driver PostgreSQL encontrado e disponível");
            return true;
        } catch (ClassNotFoundException e) {
            logger.error("Driver PostgreSQL não encontrado no classpath", e);
            return false;
        }
    }
}
