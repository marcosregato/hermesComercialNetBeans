package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.model.Usuario;
import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gerenciar operações de banco de dados da entidade Usuario
 * Suporta os três tipos de usuário: Funcionário, Cliente e Fornecedor
 * 
 * @author marcos
 */
public class UsuarioDao {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UsuarioDao.class);
    
    // SQL queries
    private static final String INSERT_USUARIO = 
        "INSERT INTO usuarios (nome, login, senha, email, cargo, nivel_acesso, data_cadastro, " +
        "ultimo_acesso, ativo, permissao_venda, permissao_caixa, permissao_relatorio, tipo_usuario, " +
        "matricula, departamento, salario, data_admissao, data_demissao, " +
        "cpf, data_nascimento, telefone, endereco, bairro, cidade, estado, cep, limite_credito, pontos_fidelidade, " +
        "cnpj, razao_social, nome_fantasia, inscricao_estadual, telefone_contato, email_contato, " +
        "endereco_fornecedor, condicoes_pagamento, prazo_entrega) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String UPDATE_USUARIO = 
        "UPDATE usuarios SET nome = ?, login = ?, senha = ?, email = ?, cargo = ?, nivel_acesso = ?, " +
        "ultimo_acesso = ?, ativo = ?, permissao_venda = ?, permissao_caixa = ?, permissao_relatorio = ?, tipo_usuario = ?, " +
        "matricula = ?, departamento = ?, salario = ?, data_admissao = ?, data_demissao = ?, " +
        "cpf = ?, data_nascimento = ?, telefone = ?, endereco = ?, bairro = ?, cidade = ?, estado = ?, cep = ?, " +
        "limite_credito = ?, pontos_fidelidade = ?, cnpj = ?, razao_social = ?, nome_fantasia = ?, " +
        "inscricao_estadual = ?, telefone_contato = ?, email_contato = ?, endereco_fornecedor = ?, " +
        "condicoes_pagamento = ?, prazo_entrega = ? WHERE id = ?";
    
    private static final String DELETE_USUARIO = "DELETE FROM usuarios WHERE id = ?";
    private static final String SELECT_ALL_USUARIOS = "SELECT * FROM usuarios ORDER BY nome";
    private static final String SELECT_USUARIO_BY_ID = "SELECT * FROM usuarios WHERE id = ?";
    private static final String SELECT_USUARIO_BY_LOGIN = "SELECT * FROM usuarios WHERE login = ?";
    private static final String SELECT_USUARIO_BY_LOGIN_SENHA = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
    private static final String SELECT_USUARIOS_BY_TIPO = "SELECT * FROM usuarios WHERE tipo_usuario = ? ORDER BY nome";
    private static final String SELECT_USUARIOS_ATIVOS = "SELECT * FROM usuarios WHERE ativo = true ORDER BY nome";
    private static final String SELECT_FUNCIONARIOS_ATIVOS = "SELECT * FROM usuarios WHERE tipo_usuario = 'FUNCIONARIO' AND ativo = true AND data_demissao IS NULL ORDER BY nome";
    private static final String SELECT_CLIENTES_ATIVOS = "SELECT * FROM usuarios WHERE tipo_usuario = 'CLIENTE' AND ativo = true ORDER BY nome";
    private static final String SELECT_FORNECEDORES_ATIVOS = "SELECT * FROM usuarios WHERE tipo_usuario = 'FORNECEDOR' AND ativo = true ORDER BY nome";
    private static final String SELECT_USUARIO_BY_CPF = "SELECT * FROM usuarios WHERE cpf = ? AND tipo_usuario = 'CLIENTE'";
    private static final String SELECT_USUARIO_BY_CNPJ = "SELECT * FROM usuarios WHERE cnpj = ? AND tipo_usuario = 'FORNECEDOR'";
    
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
    
    /**
     * Atualiza um usuário existente no banco de dados
     */
    public void atualizar(Usuario usuario) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(UPDATE_USUARIO);
            
            int paramIndex = 1;
            
            // Campos básicos
            stmt.setString(paramIndex++, usuario.getNome());
            stmt.setString(paramIndex++, usuario.getLogin());
            stmt.setString(paramIndex++, usuario.getSenha());
            stmt.setString(paramIndex++, usuario.getEmail());
            stmt.setString(paramIndex++, usuario.getCargo());
            stmt.setString(paramIndex++, usuario.getNivelAcesso());
            stmt.setTimestamp(paramIndex++, usuario.getUltimoAcesso() != null ? Timestamp.valueOf(usuario.getUltimoAcesso()) : null);
            stmt.setBoolean(paramIndex++, usuario.getAtivo());
            stmt.setString(paramIndex++, usuario.getPermissaoVenda());
            stmt.setString(paramIndex++, usuario.getPermissaoCaixa());
            stmt.setString(paramIndex++, usuario.getPermissaoRelatorio());
            stmt.setString(paramIndex++, usuario.getTipoUsuario());
            
            // Campos específicos de Funcionário
            stmt.setString(paramIndex++, usuario.getMatricula());
            stmt.setString(paramIndex++, usuario.getDepartamento());
            stmt.setDouble(paramIndex++, usuario.getSalario() != null ? usuario.getSalario() : 0.0);
            stmt.setTimestamp(paramIndex++, usuario.getDataAdmissao() != null ? Timestamp.valueOf(usuario.getDataAdmissao()) : null);
            stmt.setString(paramIndex++, usuario.getDataDemissao());
            
            // Campos específicos de Cliente
            stmt.setString(paramIndex++, usuario.getCpf());
            stmt.setString(paramIndex++, usuario.getDataNascimento());
            stmt.setString(paramIndex++, usuario.getTelefone());
            stmt.setString(paramIndex++, usuario.getEndereco());
            stmt.setString(paramIndex++, usuario.getBairro());
            stmt.setString(paramIndex++, usuario.getCidade());
            stmt.setString(paramIndex++, usuario.getEstado());
            stmt.setString(paramIndex++, usuario.getCep());
            stmt.setString(paramIndex++, usuario.getLimiteCredito());
            stmt.setInt(paramIndex++, usuario.getPontosFidelidade() != null ? usuario.getPontosFidelidade() : 0);
            
            // Campos específicos de Fornecedor
            stmt.setString(paramIndex++, usuario.getCnpj());
            stmt.setString(paramIndex++, usuario.getRazaoSocial());
            stmt.setString(paramIndex++, usuario.getNomeFantasia());
            stmt.setString(paramIndex++, usuario.getInscricaoEstadual());
            stmt.setString(paramIndex++, usuario.getTelefoneContato());
            stmt.setString(paramIndex++, usuario.getEmailContato());
            stmt.setString(paramIndex++, usuario.getEnderecoFornecedor());
            stmt.setString(paramIndex++, usuario.getCondicoesPagamento());
            stmt.setInt(paramIndex++, usuario.getPrazoEntrega() != null ? usuario.getPrazoEntrega() : 0);
            
            // WHERE clause
            stmt.setInt(paramIndex, usuario.getId());
            
            stmt.executeUpdate();
            logger.info("Usuário atualizado com sucesso: " + usuario.getNome());
            
        } catch (SQLException e) {
            logger.error("Erro ao atualizar usuário: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }
    
    /**
     * Remove um usuário do banco de dados
     */
    public void remover(Integer id) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(DELETE_USUARIO);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
            logger.info("Usuário removido com sucesso, ID: " + id);
            
        } catch (SQLException e) {
            logger.error("Erro ao remover usuário: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }
    
    /**
     * Busca todos os usuários
     */
    public List<Usuario> listar() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_ALL_USUARIOS);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao listar usuários: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuarios;
    }
    
    /**
     * Busca usuário por ID
     */
    public Usuario buscarPorId(Integer id) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_USUARIO_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = mapResultSetToUsuario(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar usuário por ID: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuario;
    }
    
    /**
     * Busca usuário por login
     */
    public Usuario buscarPorLogin(String login) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_USUARIO_BY_LOGIN);
            stmt.setString(1, login);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = mapResultSetToUsuario(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar usuário por login: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuario;
    }
    
    /**
     * Autentica usuário por login e senha
     */
    public Usuario autenticar(String login, String senha) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_USUARIO_BY_LOGIN_SENHA);
            stmt.setString(1, login);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = mapResultSetToUsuario(rs);
                // Atualiza último acesso
                usuario.registrarAcesso();
                atualizarUltimoAcesso(usuario.getId());
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao autenticar usuário: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuario;
    }
    
    /**
     * Busca usuários por tipo
     */
    public List<Usuario> buscarPorTipo(String tipoUsuario) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_USUARIOS_BY_TIPO);
            stmt.setString(1, tipoUsuario);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar usuários por tipo: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuarios;
    }
    
    /**
     * Busca apenas usuários ativos
     */
    public List<Usuario> buscarAtivos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_USUARIOS_ATIVOS);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar usuários ativos: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuarios;
    }
    
    /**
     * Busca funcionários ativos
     */
    public List<Usuario> buscarFuncionariosAtivos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_FUNCIONARIOS_ATIVOS);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar funcionários ativos: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuarios;
    }
    
    /**
     * Busca clientes ativos
     */
    public List<Usuario> buscarClientesAtivos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_CLIENTES_ATIVOS);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar clientes ativos: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuarios;
    }
    
    /**
     * Busca fornecedores ativos
     */
    public List<Usuario> buscarFornecedoresAtivos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_FORNECEDORES_ATIVOS);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar fornecedores ativos: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuarios;
    }
    
    /**
     * Busca cliente por CPF
     */
    public Usuario buscarClientePorCpf(String cpf) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_USUARIO_BY_CPF);
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = mapResultSetToUsuario(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar cliente por CPF: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuario;
    }
    
    /**
     * Busca fornecedor por CNPJ
     */
    public Usuario buscarFornecedorPorCnpj(String cnpj) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement(SELECT_USUARIO_BY_CNPJ);
            stmt.setString(1, cnpj);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuario = mapResultSetToUsuario(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar fornecedor por CNPJ: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, rs);
        }
        
        return usuario;
    }
    
    /**
     * Atualiza apenas o último acesso do usuário
     */
    private void atualizarUltimoAcesso(Integer id) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = PostgreSQLConnection.getConnection();
            stmt = conn.prepareStatement("UPDATE usuarios SET ultimo_acesso = ? WHERE id = ?");
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            logger.error("Erro ao atualizar último acesso: " + e.getMessage(), e);
            throw e;
        } finally {
            fecharRecursos(conn, stmt, null);
        }
    }
    
    /**
     * Converte ResultSet para objeto Usuario
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        
        // Campos básicos
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setLogin(rs.getString("login"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setEmail(rs.getString("email"));
        usuario.setCargo(rs.getString("cargo"));
        usuario.setNivelAcesso(rs.getString("nivel_acesso"));
        
        Timestamp dataCadastro = rs.getTimestamp("data_cadastro");
        if (dataCadastro != null) {
            usuario.setDataCadastro(dataCadastro.toLocalDateTime());
        }
        
        Timestamp ultimoAcesso = rs.getTimestamp("ultimo_acesso");
        if (ultimoAcesso != null) {
            usuario.setUltimoAcesso(ultimoAcesso.toLocalDateTime());
        }
        
        usuario.setAtivo(rs.getBoolean("ativo"));
        usuario.setPermissaoVenda(rs.getString("permissao_venda"));
        usuario.setPermissaoCaixa(rs.getString("permissao_caixa"));
        usuario.setPermissaoRelatorio(rs.getString("permissao_relatorio"));
        usuario.setTipoUsuario(rs.getString("tipo_usuario"));
        
        // Campos específicos de Funcionário
        usuario.setMatricula(rs.getString("matricula"));
        usuario.setDepartamento(rs.getString("departamento"));
        usuario.setSalario(rs.getDouble("salario"));
        if (rs.getDouble("salario") == 0.0) {
            usuario.setSalario(null);
        }
        
        Timestamp dataAdmissao = rs.getTimestamp("data_admissao");
        if (dataAdmissao != null) {
            usuario.setDataAdmissao(dataAdmissao.toLocalDateTime());
        }
        
        usuario.setDataDemissao(rs.getString("data_demissao"));
        
        // Campos específicos de Cliente
        usuario.setCpf(rs.getString("cpf"));
        usuario.setDataNascimento(rs.getString("data_nascimento"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setEndereco(rs.getString("endereco"));
        usuario.setBairro(rs.getString("bairro"));
        usuario.setCidade(rs.getString("cidade"));
        usuario.setEstado(rs.getString("estado"));
        usuario.setCep(rs.getString("cep"));
        usuario.setLimiteCredito(rs.getString("limite_credito"));
        usuario.setPontosFidelidade(rs.getInt("pontos_fidelidade"));
        
        // Campos específicos de Fornecedor
        usuario.setCnpj(rs.getString("cnpj"));
        usuario.setRazaoSocial(rs.getString("razao_social"));
        usuario.setNomeFantasia(rs.getString("nome_fantasia"));
        usuario.setInscricaoEstadual(rs.getString("inscricao_estadual"));
        usuario.setTelefoneContato(rs.getString("telefone_contato"));
        usuario.setEmailContato(rs.getString("email_contato"));
        usuario.setEnderecoFornecedor(rs.getString("endereco_fornecedor"));
        usuario.setCondicoesPagamento(rs.getString("condicoes_pagamento"));
        usuario.setPrazoEntrega(rs.getInt("prazo_entrega"));
        
        return usuario;
    }
    
    /**
     * Fecha os recursos do banco de dados
     */
    private void fecharRecursos(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            logger.error("Erro ao fechar recursos do banco: " + e.getMessage(), e);
        }
    }
}
