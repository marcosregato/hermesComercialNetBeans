package com.br.hermescomercialnetbeans.connectionDB;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Testes de integração para PostgreSQLConnection
 * @author marcos
 */
class PostgreSQLConnectionTest {

    private Connection connection;
    
    @BeforeEach
    void setUp() throws SQLException {
        connection = PostgreSQLConnection.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }
    
    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    @Test
    @DisplayName("Deve estabelecer conexão com PostgreSQL")
    void testGetConnection() throws SQLException {
        Connection conn = PostgreSQLConnection.getConnection();
        assertNotNull(conn);
        assertFalse(conn.isClosed());
        assertTrue(conn.isValid(5));
        
        conn.close();
        assertTrue(conn.isClosed());
    }
    
    @Test
    @DisplayName("Deve executar consulta simples")
    void testExecutarConsultaSimples() throws SQLException {
        String query = "SELECT 1 as resultado";
        
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(query)) {
            
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("resultado"));
        }
    }
    
    @Test
    @DisplayName("Deve verificar tabela usuarios existe")
    void testVerificarTabelaUsuarios() throws SQLException {
        String query = "SELECT COUNT(*) as count FROM information_schema.tables WHERE table_name = 'usuarios'";
        
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(query)) {
            
            assertTrue(rs.next());
            assertTrue(rs.getInt("count") > 0);
        }
    }
    
    @Test
    @DisplayName("Deve verificar tabela produto existe")
    void testVerificarTabelaProdutos() throws SQLException {
        String query = "SELECT COUNT(*) as count FROM information_schema.tables WHERE table_name = 'produto'";
        
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(query)) {
            
            assertTrue(rs.next());
            assertTrue(rs.getInt("count") > 0);
        }
    }
    
    @Test
    @DisplayName("Deve verificar tabela venda existe")
    void testVerificarTabelaVendas() throws SQLException {
        String query = "SELECT COUNT(*) as count FROM information_schema.tables WHERE table_name = 'venda'";
        
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(query)) {
            
            assertTrue(rs.next());
            assertTrue(rs.getInt("count") > 0);
        }
    }
    
    @Test
    @DisplayName("Deve verificar tabela movimento_caixa existe")
    void testVerificarTabelaMovimentoCaixa() throws SQLException {
        String query = "SELECT COUNT(*) as count FROM information_schema.tables WHERE table_name = 'movimento_caixa'";
        
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(query)) {
            
            assertTrue(rs.next());
            assertTrue(rs.getInt("count") > 0);
        }
    }
    
    @Test
    @DisplayName("Deve verificar colunas básicas da tabela usuarios")
    void testVerificarColunasTabelaUsuarios() throws SQLException {
        String query = "SELECT column_name FROM information_schema.columns WHERE table_name = 'usuarios' AND column_name IN ('id', 'nome', 'login', 'senha')";
        
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(query)) {
            
            int colunasEncontradas = 0;
            while (rs.next()) {
                colunasEncontradas++;
            }
            
            assertEquals(4, colunasEncontradas);
        }
    }
    
    @Test
    @DisplayName("Deve verificar colunas tabela produto")
    void testVerificarColunasTabelaProdutos() throws SQLException {
        String query = "SELECT column_name FROM information_schema.columns WHERE table_name = 'produto' AND column_name IN ('id', 'nome', 'preco', 'estoque')";
        
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery(query)) {
            
            int colunasEncontradas = 0;
            while (rs.next()) {
                colunasEncontradas++;
            }
            
            assertEquals(4, colunasEncontradas);
        }
    }
}
