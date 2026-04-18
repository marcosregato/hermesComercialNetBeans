
package com.br.hermescomercialnetbeans.connectionDB;

import com.br.hermescomercialnetbeans.utils.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {

    private static final String URL_POSTGRES = ConfigProperties.getProperty("URL_POSTGRES");
    private static final String USER_POSTGRES = ConfigProperties.getProperty("USER_POSTGRES");
    private static final String SENHA_POSTGRES = ConfigProperties.getProperty("PASSWORD_POSTGRES");

    private static final String URL_SQLITE = ConfigProperties.getProperty("PATH_SQLITE_DB");

    private static final String URL_MYSQL = ConfigProperties.getProperty("URL_MYSQL");
    private static final String USER_MYSQL = ConfigProperties.getProperty("USER_MYSQL"); // Corrigido de USERL_MYSQL
    private static final String SENHA_MYSQL = ConfigProperties.getProperty("PASSWORD_MYSQL");

    private static final Logger logger = LogManager.getLogger(ConnectionBD.class);

    public Connection getConnection(String nomeBanco) {
        logger.info("Tentando conectar ao banco: " + nomeBanco);
        
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
                con = DriverManager.getConnection(URL_POSTGRES, USER_POSTGRES, SENHA_POSTGRES);
                logger.info("Conectado com sucesso ao servidor Postgres.");
            } else if ("SQLite".equals(nomeBanco)) {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection(URL_SQLITE);
                logger.info("Conectado com sucesso ao servidor SQLite.");
            } else if ("MySQL".equals(nomeBanco)) {
                con = DriverManager.getConnection(URL_MYSQL, USER_MYSQL, SENHA_MYSQL);
                logger.info("Conectado com sucesso ao servidor MySQL.");
            }
            
            if (con != null) {
                logger.info("Conexão estabelecida com sucesso para: " + nomeBanco);
            }
            
            return con;
        } catch (SQLException e) {
            logger.error("Erro SQL na conexao com o banco de dados: " + nomeBanco, e);
            logger.error("SQL State: " + e.getSQLState());
            logger.error("Error Code: " + e.getErrorCode());
        } catch (ClassNotFoundException e) {
            logger.error("Driver não encontrado para o banco: " + nomeBanco, e);
        } catch (Exception e) {
            logger.error("Erro inesperado na conexao com o banco: " + nomeBanco, e);
        }
        
        logger.error("Retornando conexão nula para: " + nomeBanco);
        return null;
    }
}
