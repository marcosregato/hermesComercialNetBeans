
package com.br.hermescomercialnetbeans.connectionDB;

import com.br.hermescomercialnetbeans.utils.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Singleton para gerenciamento de conexões com banco de dados
 * Implementa o padrão Singleton para garantir uma única instância no sistema
 * @author marcos
 */
public class ConnectionBD {

    // Instância única do Singleton
    private static ConnectionBD instance;
    
    // Configurações do banco de dados
    private static final String URL_POSTGRES = ConfigProperties.getPropertyStatic("URL_POSTGRES");
    private static final String USER_POSTGRES = ConfigProperties.getPropertyStatic("USER_POSTGRES");
    private static final String SENHA_POSTGRES = ConfigProperties.getPropertyStatic("PASSWORD_POSTGRES");

    private static final String URL_SQLITE = ConfigProperties.getPropertyStatic("PATH_SQLITE_DB");

    private static final String URL_MYSQL = ConfigProperties.getPropertyStatic("URL_MYSQL");
    private static final String USER_MYSQL = ConfigProperties.getPropertyStatic("USER_MYSQL");
    private static final String SENHA_MYSQL = ConfigProperties.getPropertyStatic("PASSWORD_MYSQL");

    private static final Logger logger = LogManager.getLogger(ConnectionBD.class);

    /**
     * Construtor privado para implementar o padrão Singleton
     */
    private ConnectionBD() {
        logger.info("Instância do ConnectionBD Singleton criada");
    }

    /**
     * Método estático para obter a instância única do Singleton
     * @return instância única de ConnectionBD
     */
    public static synchronized ConnectionBD getInstance() {
        if (instance == null) {
            synchronized (ConnectionBD.class) {
                if (instance == null) {
                    instance = new ConnectionBD();
                }
            }
        }
        return instance;
    }

    public Connection getConnection(String nomeBanco) {
        logger.info("Iniciando tentativa de conexão com o banco: " + nomeBanco);
        
        try {
            Connection con = null;
            
            // Verificar se as propriedades foram carregadas
            if (URL_POSTGRES == null || USER_POSTGRES == null || SENHA_POSTGRES == null) {
                logger.error("Propriedades do PostgreSQL não foram carregadas corretamente!");
                logger.error("URL_POSTGRES: " + URL_POSTGRES);
                logger.error("USER_POSTGRES: " + USER_POSTGRES);
                logger.error("SENHA_POSTGRES: " + (SENHA_POSTGRES != null ? "***" : "null"));
                return null;
            }
            
            if ("Postgres".equals(nomeBanco)) {
                logger.info("Conectando ao PostgreSQL com URL: " + URL_POSTGRES);
                logger.debug("Usuário PostgreSQL: " + USER_POSTGRES);
                con = DriverManager.getConnection(URL_POSTGRES, USER_POSTGRES, SENHA_POSTGRES);
                logger.info("Conectado com sucesso ao servidor PostgreSQL.");
                logger.debug("Conexão PostgreSQL estabelecida: " + (con != null ? "OK" : "FALHOU"));
            } else if ("SQLite".equals(nomeBanco)) {
                logger.info("Conectando ao SQLite com URL: " + URL_SQLITE);
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection(URL_SQLITE);
                logger.info("Conectado com sucesso ao servidor SQLite.");
                logger.debug("Conexão SQLite estabelecida: " + (con != null ? "OK" : "FALHOU"));
            } else if ("MySQL".equals(nomeBanco)) {
                logger.info("Conectando ao MySQL com URL: " + URL_MYSQL);
                logger.debug("Usuário MySQL: " + USER_MYSQL);
                con = DriverManager.getConnection(URL_MYSQL, USER_MYSQL, SENHA_MYSQL);
                logger.info("Conectado com sucesso ao servidor MySQL.");
                logger.debug("Conexão MySQL estabelecida: " + (con != null ? "OK" : "FALHOU"));
            } else {
                logger.error("Banco de dados não suportado: " + nomeBanco);
                logger.error("Bancos suportados: Postgres, SQLite, MySQL");
                return null;
            }
            
            if (con != null) {
                logger.info("Conexão estabelecida com sucesso para: " + nomeBanco);
                logger.debug("Validade da conexão: " + (!con.isClosed() ? "ATIVA" : "FECHADA"));
                logger.debug("Auto-commit: " + con.getAutoCommit());
            } else {
                logger.error("Falha ao estabelecer conexão para: " + nomeBanco);
            }
            
            return con;
        } catch (SQLException e) {
            logger.error("Erro SQL na conexão com o banco de dados: " + nomeBanco, e);
            logger.error("SQL State: " + e.getSQLState());
            logger.error("Error Code: " + e.getErrorCode());
            logger.error("Message: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error("Driver não encontrado para o banco: " + nomeBanco, e);
            logger.error("Verifique se o driver JDBC está no classpath");
        } catch (Exception e) {
            logger.error("Erro inesperado na conexão com o banco: " + nomeBanco, e);
        }
        
        logger.error("Retornando conexão nula para: " + nomeBanco);
        return null;
    }
}
