package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Testes de integração para UsuarioDao
 * @author marcos
 */
class UsuarioDaoIntegrationTest {

    private UsuarioDao usuarioDao;
    
    @BeforeEach
    void setUp() {
        usuarioDao = new UsuarioDao();
    }
    
    @Test
    @DisplayName("Deve criar e salvar usuário com sucesso")
    void testSalvarUsuario() throws SQLException {
        Usuario usuario = criarUsuarioTeste();
        
        // Salvar usuário
        usuarioDao.salvar(usuario);
        
        // Verificar se ID foi gerado
        assertNotNull(usuario.getId());
        assertTrue(usuario.getId() > 0);
        
        // Buscar usuário salvo
        Usuario usuarioSalvo = usuarioDao.buscarPorId(usuario.getId());
        assertNotNull(usuarioSalvo);
        assertEquals(usuario.getNome(), usuarioSalvo.getNome());
        assertEquals(usuario.getLogin(), usuarioSalvo.getLogin());
        assertEquals(usuario.getTipoUsuario(), usuarioSalvo.getTipoUsuario());
    }
    
    @Test
    @DisplayName("Deve atualizar usuário existente")
    void testAtualizarUsuario() throws SQLException {
        // Criar e salvar usuário
        Usuario usuario = criarUsuarioTeste();
        usuarioDao.salvar(usuario);
        
        // Modificar dados
        String nomeOriginal = usuario.getNome();
        usuario.setNome("João Silva Atualizado");
        usuario.setCargo("Gerente");
        
        // Atualizar
        usuarioDao.atualizar(usuario);
        
        // Verificar atualização
        Usuario usuarioAtualizado = usuarioDao.buscarPorId(usuario.getId());
        assertNotNull(usuarioAtualizado);
        assertNotEquals(nomeOriginal, usuarioAtualizado.getNome());
        assertEquals("João Silva Atualizado", usuarioAtualizado.getNome());
        assertEquals("Gerente", usuarioAtualizado.getCargo());
    }
    
    @Test
    @DisplayName("Deve listar todos os usuários")
    void testListarUsuarios() throws SQLException {
        // Criar alguns usuários
        Usuario usuario1 = criarUsuarioTeste("teste1." + System.currentTimeMillis());
        usuario1.setNome("Usuário Teste 1");
        
        Usuario usuario2 = criarUsuarioTeste("teste2." + System.currentTimeMillis());
        usuario2.setNome("Usuário Teste 2");
        
        usuarioDao.salvar(usuario1);
        usuarioDao.salvar(usuario2);
        
        // Listar usuários
        List<Usuario> usuarios = usuarioDao.listar();
        assertNotNull(usuarios);
        assertTrue(usuarios.size() >= 2);
        
        // Verificar se usuários criados estão na lista
        boolean encontrouUsuario1 = usuarios.stream()
            .anyMatch(u -> "Usuário Teste 1".equals(u.getNome()));
        boolean encontrouUsuario2 = usuarios.stream()
            .anyMatch(u -> "Usuário Teste 2".equals(u.getNome()));
        
        assertTrue(encontrouUsuario1);
        assertTrue(encontrouUsuario2);
    }
    
    @Test
    @DisplayName("Deve buscar usuário por login")
    void testBuscarPorLogin() throws SQLException {
        // Criar usuário com login único
        String loginUnico = "login.teste." + System.currentTimeMillis();
        Usuario usuario = criarUsuarioTeste(loginUnico);
        usuarioDao.salvar(usuario);
        
        // Buscar por login
        Usuario usuarioEncontrado = usuarioDao.buscarPorLogin(loginUnico);
        assertNotNull(usuarioEncontrado);
        assertEquals(usuario.getId(), usuarioEncontrado.getId());
        assertEquals(loginUnico, usuarioEncontrado.getLogin());
    }
    
    @Test
    @DisplayName("Deve desativar usuário")
    void testDesativarUsuario() throws SQLException {
        // Criar usuário
        Usuario usuario = criarUsuarioTeste();
        usuarioDao.salvar(usuario);
        Integer id = usuario.getId();
        
        // Verificar que usuário existe e está ativo
        Usuario usuarioAtivo = usuarioDao.buscarPorId(id);
        assertNotNull(usuarioAtivo);
        assertTrue(usuarioAtivo.getAtivo());
        
        // Desativar usuário
        usuario.setAtivo(false);
        usuarioDao.atualizar(usuario);
        
        // Verificar que usuário foi desativado
        Usuario usuarioDesativado = usuarioDao.buscarPorId(id);
        assertNotNull(usuarioDesativado);
        assertFalse(usuarioDesativado.getAtivo());
    }
    
    @Test
    @DisplayName("Deve buscar usuários por tipo")
    void testBuscarPorTipo() throws SQLException {
        // Criar usuários de diferentes tipos
        Usuario funcionario = criarUsuarioTeste();
        funcionario.setNome("Funcionario Teste");
        funcionario.setTipoUsuario("FUNCIONARIO");
        usuarioDao.salvar(funcionario);
        
        Usuario cliente = criarUsuarioTeste();
        cliente.setNome("Cliente Teste");
        cliente.setTipoUsuario("CLIENTE");
        usuarioDao.salvar(cliente);
        
        // Buscar por tipo
        List<Usuario> funcionarios = usuarioDao.buscarPorTipo("FUNCIONARIO");
        List<Usuario> clientes = usuarioDao.buscarPorTipo("CLIENTE");
        
        assertNotNull(funcionarios);
        assertNotNull(clientes);
        
        assertTrue(funcionarios.stream()
            .anyMatch(u -> "Funcionario Teste".equals(u.getNome())));
        assertTrue(clientes.stream()
            .anyMatch(u -> "Cliente Teste".equals(u.getNome())));
    }
    
    private Usuario criarUsuarioTeste() {
        return criarUsuarioTeste("joao.silva." + System.currentTimeMillis());
    }
    
    private Usuario criarUsuarioTeste(String login) {
        Usuario usuario = new Usuario();
        usuario.setNome("João Silva");
        usuario.setLogin(login);
        usuario.setSenha("senha123");
        usuario.setEmail("joao@teste.com");
        usuario.setCargo("Vendedor");
        usuario.setNivelAcesso("OPERADOR");
        usuario.setTipoUsuario("FUNCIONARIO");
        usuario.setAtivo(true);
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setPermissaoVenda("SIM");
        usuario.setPermissaoCaixa("SIM");
        usuario.setPermissaoRelatorio("NAO");
        
        // Campos de funcionário
        usuario.setMatricula("FUNC001");
        usuario.setDepartamento("Vendas");
        usuario.setSalario(2500.0);
        usuario.setDataAdmissao(LocalDateTime.now());
        
        return usuario;
    }
}
