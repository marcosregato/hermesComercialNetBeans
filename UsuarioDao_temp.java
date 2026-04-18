package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;
import com.br.hermescomercialnetbeans.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    
    private static final Logger logger = LogManager.getLogger(UsuarioDao.class);
    
    // SQL queries
    private static final String INSERT_USUARIO = 
        "INSERT INTO usuarios (nome, login, senha, email, cargo, nivel_acesso, data_cadastro, " +
        "ultimo_acesso, ativo, permissao_venda, permissao_caixa, permissao_relatorio, tipo_usuario, " +
        "matricula, departamento, salario, data_admissao, data_demissao, " +
        "cpf, data_nascimento, telefone, endereco, bairro, cidade, estado, cep, limite_credito, pontos_fidelidade, " +
        "cnpj, razao_social, nome_fantasia, inscricao_estadual, telefone_contato, email_contato, " +
        "endereco_fornecedor, condicoes_pagamento, prazo_entrega) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    public UsuarioDao() {
        // Construtor vazio - usa PostgreSQLConnection.getConnection() diretamente
    }
    
    /**
     * Salva um novo usuário no banco de dados
     */
    public void salvar(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(INSERT_USUARIO, Statement.RETURN_GENERATED_KEYS);
            
            // Campos básicos
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getCargo());
            stmt.setString(6, usuario.getNivelAcesso());
            stmt.setTimestamp(7, Timestamp.valueOf(usuario.getDataCadastro()));
            stmt.setTimestamp(8, usuario.getUltimoAcesso() != null ? Timestamp.valueOf(usuario.getUltimoAcesso()) : null);
            stmt.setBoolean(9, usuario.getAtivo());
            stmt.setString(10, usuario.getPermissaoVenda());
            stmt.setString(11, usuario.getPermissaoCaixa());
            stmt.setString(12, usuario.getPermissaoRelatorio());
            stmt.setString(13, usuario.getTipoUsuario());
            
            // Campos específicos de Funcionário
            stmt.setString(14, usuario.getMatricula());
            stmt.setString(15, usuario.getDepartamento());
            stmt.setDouble(16, usuario.getSalario() != null ? usuario.getSalario() : 0.0);
            stmt.setTimestamp(17, usuario.getDataAdmissao() != null ? Timestamp.valueOf(usuario.getDataAdmissao()) : null);
            stmt.setString(18, usuario.getDataDemissao());
            
            // Campos específicos de Cliente
            stmt.setString(19, usuario.getCpf());
            stmt.setString(20, usuario.getDataNascimento());
            stmt.setString(21, usuario.getTelefone());
            stmt.setString(22, usuario.getEndereco());
            stmt.setString(23, usuario.getBairro());
            stmt.setString(24, usuario.getCidade());
            stmt.setString(25, usuario.getEstado());
            stmt.setString(26, usuario.getCep());
            stmt.setString(27, usuario.getLimiteCredito());
            stmt.setInt(28, usuario.getPontosFidelidade() != null ? usuario.getPontosFidelidade() : 0);
            
            // Campos específicos de Fornecedor
            stmt.setString(29, usuario.getCnpj());
            stmt.setString(30, usuario.getRazaoSocial());
            stmt.setString(31, usuario.getNomeFantasia());
            stmt.setString(32, usuario.getInscricaoEstadual());
            stmt.setString(33, usuario.getTelefoneContato());
            stmt.setString(34, usuario.getEmailContato());
            stmt.setString(35, usuario.getEnderecoFornecedor());
            stmt.setString(36, usuario.getCondicoesPagamento());
            stmt.setInt(37, usuario.getPrazoEntrega() != null ? usuario.getPrazoEntrega() : 0);
            
            stmt.executeUpdate();
            
            // Recupera o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
            
            logger.info("Usuário salvo com sucesso: " + usuario.getNome());
            
        } catch (SQLException e) {
            logger.error("Erro ao salvar usuário: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }
    
    private void fecharRecursos(Connection conn, Statement stmt, ResultSet rs) {
        // Implementação básica para fechar recursos
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            logger.error("Erro ao fechar recursos: " + e.getMessage(), e);
        }
    }
}
