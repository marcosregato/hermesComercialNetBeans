
package com.br.hermescomercialnetbeans.connectionDB;

import com.br.hermescomercialnetbeans.util.ConfigProperties;
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
        try {
            Connection con = null;
            if ("Postgres".equals(nomeBanco)) {
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
            return con;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Erro na conexao com o banco de dados: " + nomeBanco, e);
        }
        return null;
    }
}
