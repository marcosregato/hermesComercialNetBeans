package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;

class UsuarioDaoIntegrationTestFixed {
    
    private Connection connection;
    
    @BeforeEach
    void setUp() throws SQLException {
        connection = PostgreSQLConnection.getConnection();
        
        // Limpar tabela de testes
        try (var stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM usuarios WHERE login LIKE 'test_%'");
        }
    }
    
    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados de teste
        try (var stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM usuarios WHERE login LIKE 'test_%'");
        }
        
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    @Test
    @DisplayName("Deve testar conexão com banco de dados")
    void testConexaoBancoDados() throws SQLException {
        assertNotNull(connection);
        assertFalse(connection.isClosed());
        
        // Verificar se tabela usuarios existe
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'usuarios'")) {
            
            assertTrue(rs.next());
            assertTrue(rs.getInt(1) > 0, "Tabela usuarios deve existir");
        }
    }
    
    @Test
    @DisplayName("Deve criar usuário básico com SQL simplificado")
    void testCriarUsuarioBasico() throws SQLException {
        // Inserir usuário diretamente com SQL simplificado
        String sql = "INSERT INTO usuarios (nome, login, senha, email, tipo_usuario, ativo, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "Teste Usuario");
            stmt.setString(2, "test_user_123");
            stmt.setString(3, "senha123");
            stmt.setString(4, "teste@email.com");
            stmt.setString(5, "FUNCIONARIO");
            stmt.setBoolean(6, true);
            stmt.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            
            int rowsAffected = stmt.executeUpdate();
            assertEquals(1, rowsAffected, "Deve inserir 1 usuário");
        }
        
        // Verificar se usuário foi inserido
        try (var stmt = connection.prepareStatement("SELECT * FROM usuarios WHERE login = ?")) {
            stmt.setString(1, "test_user_123");
            try (var rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Usuário deve ser encontrado");
                assertEquals("Teste Usuario", rs.getString("nome"));
                assertEquals("test_user_123", rs.getString("login"));
                assertEquals("FUNCIONARIO", rs.getString("tipo_usuario"));
            }
        }
    }
    
    @Test
    @DisplayName("Deve listar usuários ativos")
    void testListarUsuariosAtivos() throws SQLException {
        // Inserir alguns usuários de teste
        String sql = "INSERT INTO usuarios (nome, login, senha, email, tipo_usuario, ativo, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (var stmt = connection.prepareStatement(sql)) {
            // Usuário ativo
            stmt.setString(1, "Usuario Ativo 1");
            stmt.setString(2, "test_ativo_1");
            stmt.setString(3, "senha123");
            stmt.setString(4, "ativo1@teste.com");
            stmt.setString(5, "FUNCIONARIO");
            stmt.setBoolean(6, true);
            stmt.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
            
            // Usuário inativo
            stmt.setString(1, "Usuario Inativo 1");
            stmt.setString(2, "test_inativo_1");
            stmt.setString(3, "senha123");
            stmt.setString(4, "inativo1@teste.com");
            stmt.setString(5, "FUNCIONARIO");
            stmt.setBoolean(6, false);
            stmt.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        }
        
        // Contar usuários ativos
        try (var stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios WHERE ativo = true AND login LIKE 'test_%'")) {
            
            assertTrue(rs.next());
            int count = rs.getInt(1);
            assertTrue(count >= 1, "Deve encontrar pelo menos 1 usuário ativo");
        }
    }
    
    @Test
    @DisplayName("Deve atualizar usuário")
    void testAtualizarUsuario() throws SQLException {
        // Inserir usuário
        String insertSql = "INSERT INTO usuarios (nome, login, senha, email, tipo_usuario, ativo, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (var stmt = connection.prepareStatement(insertSql)) {
            stmt.setString(1, "Usuario Original");
            stmt.setString(2, "test_update_1");
            stmt.setString(3, "senha123");
            stmt.setString(4, "original@teste.com");
            stmt.setString(5, "FUNCIONARIO");
            stmt.setBoolean(6, true);
            stmt.setTimestamp(7, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        }
        
        // Atualizar usuário
        String updateSql = "UPDATE usuarios SET nome = ?, email = ? WHERE login = ?";
        
        try (var stmt = connection.prepareStatement(updateSql)) {
            stmt.setString(1, "Usuario Atualizado");
            stmt.setString(2, "atualizado@teste.com");
            stmt.setString(3, "test_update_1");
            
            int rowsAffected = stmt.executeUpdate();
            assertEquals(1, rowsAffected, "Deve atualizar 1 usuário");
        }
        
        // Verificar atualização
        try (var stmt = connection.prepareStatement("SELECT nome, email FROM usuarios WHERE login = ?")) {
            stmt.setString(1, "test_update_1");
            try (var rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Usuário deve ser encontrado");
                assertEquals("Usuario Atualizado", rs.getString("nome"));
                assertEquals("atualizado@teste.com", rs.getString("email"));
            }
        }
    }
}
