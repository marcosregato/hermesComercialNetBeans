package com.br.hermescomercialnetbeans.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Testes unitários para a classe Usuario
 * @author marcos
 */
class UsuarioTest {

    private Usuario usuario;
    
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }
    
    @Test
    @DisplayName("Deve criar usuário com valores padrão")
    void testUsuarioValoresPadrao() {
        assertNotNull(usuario);
        assertNull(usuario.getId());
        assertNull(usuario.getNome());
        assertNull(usuario.getLogin());
        assertNull(usuario.getSenha());
        assertNull(usuario.getEmail());
        assertNull(usuario.getCargo());
        assertNull(usuario.getNivelAcesso());
        assertNull(usuario.getTipoUsuario());
        assertTrue(usuario.getAtivo());
        assertNull(usuario.getDataCadastro());
        assertNull(usuario.getUltimoAcesso());
    }
    
    @Test
    @DisplayName("Deve definir e obter valores básicos do usuário")
    void testSetGetValoresBasicos() {
        usuario.setId(1);
        usuario.setNome("João Silva");
        usuario.setLogin("joao.silva");
        usuario.setSenha("senha123");
        usuario.setEmail("joao@teste.com");
        usuario.setCargo("Vendedor");
        usuario.setNivelAcesso("OPERADOR");
        usuario.setTipoUsuario("FUNCIONARIO");
        usuario.setAtivo(true);
        
        assertEquals(Integer.valueOf(1), usuario.getId());
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao.silva", usuario.getLogin());
        assertEquals("senha123", usuario.getSenha());
        assertEquals("joao@teste.com", usuario.getEmail());
        assertEquals("Vendedor", usuario.getCargo());
        assertEquals("OPERADOR", usuario.getNivelAcesso());
        assertEquals("FUNCIONARIO", usuario.getTipoUsuario());
        assertTrue(usuario.getAtivo());
    }
    
    @Test
    @DisplayName("Deve definir e obter datas do usuário")
    void testSetGetDatas() {
        LocalDateTime agora = LocalDateTime.now();
        usuario.setDataCadastro(agora);
        usuario.setUltimoAcesso(agora.plusHours(1));
        
        assertEquals(agora, usuario.getDataCadastro());
        assertEquals(agora.plusHours(1), usuario.getUltimoAcesso());
    }
    
    @Test
    @DisplayName("Deve definir e obter permissões do usuário")
    void testSetGetPermissoes() {
        usuario.setPermissaoVenda("SIM");
        usuario.setPermissaoCaixa("SIM");
        usuario.setPermissaoRelatorio("NAO");
        
        assertEquals("SIM", usuario.getPermissaoVenda());
        assertEquals("SIM", usuario.getPermissaoCaixa());
        assertEquals("NAO", usuario.getPermissaoRelatorio());
    }
    
    @Test
    @DisplayName("Deve definir e obter campos de funcionário")
    void testSetGetCamposFuncionario() {
        usuario.setMatricula("FUNC001");
        usuario.setDepartamento("Vendas");
        usuario.setSalario(2500.0);
        usuario.setDataAdmissao(LocalDateTime.of(2023, 1, 15, 9, 0));
        usuario.setDataDemissao("2024-12-31");
        
        assertEquals("FUNC001", usuario.getMatricula());
        assertEquals("Vendas", usuario.getDepartamento());
        assertEquals(Double.valueOf(2500.0), usuario.getSalario());
        assertEquals(LocalDateTime.of(2023, 1, 15, 9, 0), usuario.getDataAdmissao());
        assertEquals("2024-12-31", usuario.getDataDemissao());
    }
    
    @Test
    @DisplayName("Deve definir e obter campos de cliente")
    void testSetGetCamposCliente() {
        usuario.setCpf("123.456.789-00");
        usuario.setDataNascimento("1990-05-15");
        usuario.setTelefone("(11) 98765-4321");
        usuario.setEndereco("Rua das Flores, 123");
        usuario.setBairro("Centro");
        usuario.setCidade("São Paulo");
        usuario.setEstado("SP");
        usuario.setCep("01234-567");
        usuario.setLimiteCredito("1000.00");
        usuario.setPontosFidelidade(150);
        
        assertEquals("123.456.789-00", usuario.getCpf());
        assertEquals("1990-05-15", usuario.getDataNascimento());
        assertEquals("(11) 98765-4321", usuario.getTelefone());
        assertEquals("Rua das Flores, 123", usuario.getEndereco());
        assertEquals("Centro", usuario.getBairro());
        assertEquals("São Paulo", usuario.getCidade());
        assertEquals("SP", usuario.getEstado());
        assertEquals("01234-567", usuario.getCep());
        assertEquals("1000.00", usuario.getLimiteCredito());
        assertEquals(Integer.valueOf(150), usuario.getPontosFidelidade());
    }
    
    @Test
    @DisplayName("Deve definir e obter campos de fornecedor")
    void testSetGetCamposFornecedor() {
        usuario.setCnpj("12.345.678/0001-90");
        usuario.setRazaoSocial("Fornecedor ABC Ltda");
        usuario.setNomeFantasia("ABC Fornecedores");
        usuario.setInscricaoEstadual("123456789");
        usuario.setTelefoneContato("(11) 3456-7890");
        usuario.setEmailContato("contato@fornecedor.com");
        usuario.setEnderecoFornecedor("Av. Industrial, 500");
        usuario.setCondicoesPagamento("30 dias");
        usuario.setPrazoEntrega(15);
        
        assertEquals("12.345.678/0001-90", usuario.getCnpj());
        assertEquals("Fornecedor ABC Ltda", usuario.getRazaoSocial());
        assertEquals("ABC Fornecedores", usuario.getNomeFantasia());
        assertEquals("123456789", usuario.getInscricaoEstadual());
        assertEquals("(11) 3456-7890", usuario.getTelefoneContato());
        assertEquals("contato@fornecedor.com", usuario.getEmailContato());
        assertEquals("Av. Industrial, 500", usuario.getEnderecoFornecedor());
        assertEquals("30 dias", usuario.getCondicoesPagamento());
        assertEquals(Integer.valueOf(15), usuario.getPrazoEntrega());
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo ID")
    void testEqualsMesmoId() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        
        Usuario usuario2 = new Usuario();
        usuario2.setId(1);
        
        assertEquals(usuario1, usuario2);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }
    
    @Test
    @DisplayName("Deve validar not equals com IDs diferentes")
    void testNotEqualsIdsDiferentes() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        
        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        
        assertNotEquals(usuario1, usuario2);
    }
    
    @Test
    @DisplayName("Deve validar toString")
    void testToString() {
        usuario.setId(1);
        usuario.setNome("João Silva");
        usuario.setLogin("joao.silva");
        
        String toString = usuario.toString();
        System.out.println("DEBUG: toString output = " + toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nome='João Silva'"));
        assertTrue(toString.contains("login='joao.silva'"));
    }
}
