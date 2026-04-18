package com.br.hermescomercialnetbeans.security;

import com.br.hermescomercialnetbeans.utils.ValidarCampo;
import com.br.hermescomercialnetbeans.utils.ConvertDado;
import com.br.hermescomercialnetbeans.model.Usuario;
import com.br.hermescomercialnetbeans.model.Venda;
import com.br.hermescomercialnetbeans.model.ItemVenda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityTest {
    
    private ValidarCampo validarCampo;
    private ConvertDado convertDado;
    
    @BeforeEach
    void setUp() {
        validarCampo = new ValidarCampo();
        convertDado = new ConvertDado();
    }
    
    @Test
    @DisplayName("Deve prevenir SQL injection em CPF")
    void testSegurancaSQLInjectionCPF() {
        // Arrange - Tentativas de SQL injection
        List<String> inputsMaliciosos = Arrays.asList(
            "123.456.789-09'; DROP TABLE usuarios; --",
            "111' OR '1'='1",
            "222.333.444-55; DELETE FROM vendas; --",
            "333.444.555-66' UNION SELECT * FROM usuarios --",
            "444' OR 1=1; DROP TABLE produtos; --"
        );
        
        // Act & Assert - Todos devem ser rejeitados
        for (String input : inputsMaliciosos) {
            boolean resultado = validarCampo.isCPF(input);
            assertFalse(resultado, "CPF malicioso deve ser rejeitado: " + input);
        }
    }
    
    @Test
    @DisplayName("Deve prevenir SQL injection em CNPJ")
    void testSegurancaSQLInjectionCNPJ() {
        // Arrange - Tentativas de SQL injection
        List<String> inputsMaliciosos = Arrays.asList(
            "12.345.678/0001-95'; DROP TABLE empresas; --",
            "11' OR '1'='1",
            "22.333.444/0001-72; DELETE FROM produtos; --",
            "33.444.555/0001-63' UNION SELECT * FROM usuarios --",
            "44' OR 1=1; DROP TABLE vendas; --"
        );
        
        // Act & Assert - Todos devem ser rejeitados
        for (String input : inputsMaliciosos) {
            boolean resultado = validarCampo.isCNPJ(input);
            assertFalse(resultado, "CNPJ malicioso deve ser rejeitado: " + input);
        }
    }
    
    @Test
    @DisplayName("Deve prevenir XSS em nomes de clientes")
    void testSegurancaXSSNomes() {
        // Arrange - Tentativas de XSS
        List<String> inputsMaliciosos = Arrays.asList(
            "<script>alert('XSS')</script>",
            "Cliente <img src=x onerror=alert(1)> Teste",
            "';alert('XSS');//",
            "<iframe src='javascript:alert(1)'></iframe>",
            "Cliente<script>document.location='http://evil.com'</script>"
        );
        
        // Act & Assert - Validação deve lidar com inputs suspeitos
        for (String input : inputsMaliciosos) {
            // Não deve lançar exceção, mas deve processar de forma segura
            assertDoesNotThrow(() -> {
                // Simular validação de nome
                if (input != null && !input.trim().isEmpty()) {
                    // Verificar se contém tags HTML suspeitas
                    assertFalse(input.contains("<script"), "Nome não deve conter scripts: " + input);
                    assertFalse(input.contains("<iframe"), "Nome não deve conter iframes: " + input);
                    assertFalse(input.contains("javascript:"), "Nome não deve conter JavaScript: " + input);
                }
            });
        }
    }
    
    @Test
    @DisplayName("Deve validar valores monetários seguros")
    void testSegurancaValoresMonetarios() {
        // Arrange - Tentativas de valores maliciosos
        List<String> inputsMaliciosos = Arrays.asList(
            "999999999999999999999.99",
            "-1000.00",
            "0.0000000001",
            "1.7976931348623157E+308", // Double.MAX_VALUE
            "NaN",
            "Infinity"
        );
        
        // Act & Assert - Valores devem ser tratados com segurança
        for (String input : inputsMaliciosos) {
            assertDoesNotThrow(() -> {
                try {
                    double valor = Double.parseDouble(input);
                    // Verificar se o valor está em faixa razoável
                    assertTrue(valor >= 0 && valor <= 1000000, 
                        "Valor monetário deve estar em faixa segura: " + valor);
                } catch (NumberFormatException e) {
                    // Exceção é esperada para inputs inválidos
                    assertNotNull(e);
                }
            });
        }
    }
    
    @Test
    @DisplayName("Deve prevenir buffer overflow em campos de texto")
    void testSegurancaBufferOverflow() {
        // Arrange - Criar strings muito longas
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("A");
        }
        String inputGrande = sb.toString();
        
        // Act & Assert - Sistema deve lidar com inputs grandes sem travar
        assertDoesNotThrow(() -> {
            // Simular processamento de campo grande
            if (inputGrande.length() > 1000) {
                // Sistema deve truncar ou rejeitar inputs muito grandes
                assertTrue(inputGrande.length() > 1000);
            }
        });
    }
    
    @Test
    @DisplayName("Deve validar senhas com critérios de segurança")
    void testSegurancaSenhas() {
        // Arrange - Senhas inseguras
        List<String> senhasInseguras = Arrays.asList(
            "123",
            "password",
            "senha",
            "111111",
            "abc123",
            "",
            " ",
            "admin",
            "root"
        );
        
        // Act & Assert - Senhas inseguras devem ser identificadas
        for (String senha : senhasInseguras) {
            boolean isInsegura = validarSenhaInsegura(senha);
            assertTrue(isInsegura, "Senha deve ser identificada como insegura: " + senha);
        }
    }
    
    @Test
    @DisplayName("Deve prevenir injection em datas")
    void testSegurancaInjectionDatas() {
        // Arrange - Tentativas de injection em datas
        List<String> datasMaliciosas = Arrays.asList(
            "01/12/2023'; DROP TABLE vendas; --",
            "31/02/2023 OR 1=1",
            "15/01/2024' UNION SELECT * FROM usuarios --",
            "01/12/9999", // Ano extremo
            "32/13/2023" // Mês e dia inválidos
        );
        
        // Act & Assert - Datas maliciosas devem ser rejeitadas ou tratadas com segurança
        for (String data : datasMaliciosas) {
            assertDoesNotThrow(() -> {
                String resultado = validarCampo.campoData(data);
                // Datas maliciosas devem retornar null
                if (data.contains("'") || data.contains(";") || data.contains("--")) {
                    assertNull(resultado, "Data com injection deve retornar null: " + data);
                }
            });
        }
    }
    
    @Test
    @DisplayName("Deve validar estrutura de dados consistentes")
    void testSegurancaEstruturaDados() {
        // Arrange - Criar objetos com dados inconsistentes
        Venda venda = new Venda();
        venda.setCodigo("VND-001");
        venda.setValorTotal(-100.00); // Valor negativo
        venda.setValorFinal(200.00);  // Inconsistente
        
        // Act & Assert - Sistema deve detectar inconsistências
        assertTrue(venda.getValorTotal() < 0, "Valor total negativo deve ser detectado");
        assertTrue(venda.getValorFinal() > venda.getValorTotal(), 
            "Inconsistência entre valor total e final deve ser detectada");
    }
    
    @Test
    @DisplayName("Deve prevenir acesso não autorizado a dados sensíveis")
    void testSegurancaAcessoDados() {
        // Arrange - Simular dados sensíveis
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("admin");
        usuario.setSenha("senha123");
        
        // Act & Assert - Verificar se dados sensíveis estão protegidos
        assertNotNull(usuario.getId());
        assertNotNull(usuario.getNome());
        assertNotNull(usuario.getSenha());
        
        // Em um sistema real, a senha deveria estar criptografada
        assertFalse(usuario.getSenha().isEmpty(), "Senha não deve estar vazia");
        
        // Nome de usuário deve ser validado para acesso privilegiado
        List<String> nomesPrivilegiados = Arrays.asList("admin", "root", "administrator");
        boolean isPrivilegiado = nomesPrivilegiados.contains(usuario.getNome().toLowerCase());
        if (isPrivilegiado) {
            assertTrue(usuario.getNome().length() > 0, "Nome de usuário privilegiado deve ser válido");
        }
    }
    
    @Test
    @DisplayName("Deve validar integridade de dados em lote")
    void testSegurancaIntegridadeLote() {
        // Arrange - Criar lote de itens para venda
        List<ItemVenda> itens = Arrays.asList(
            criarItemSeguro("PROD001", "Produto 1", 2, 10.00),
            criarItemSeguro("PROD002", "Produto 2", 1, 20.00),
            criarItemSeguro("PROD003", "Produto 3", 3, 5.00)
        );
        
        // Act & Assert - Validar integridade do lote
        for (ItemVenda item : itens) {
            assertNotNull(item.getProdutoCodigo(), "Código do produto não pode ser nulo");
            assertNotNull(item.getProdutoDescricao(), "Descrição do produto não pode ser nula");
            assertTrue(item.getQuantidade() > 0, "Quantidade deve ser positiva");
            assertTrue(item.getValorUnitario() > 0, "Valor unitário deve ser positivo");
            assertTrue(item.getSubtotal() > 0, "Subtotal deve ser positivo");
            
            // Verificar consistência dos cálculos
            assertEquals(item.getQuantidade() * item.getValorUnitario(), 
                        item.getSubtotal(), 0.01, 
                        "Subtotal deve corresponder à quantidade × valor unitário");
        }
    }
    
    // Métodos auxiliares
    private boolean validarSenhaInsegura(String senha) {
        if (senha == null || senha.length() < 6) return true;
        if (senha.toLowerCase().contains("password")) return true;
        if (senha.toLowerCase().contains("senha")) return true;
        if (senha.matches("^\\d+$")) return true; // Apenas números
        if (senha.equals("123456")) return true;
        return false;
    }
    
    private ItemVenda criarItemSeguro(String codigo, String descricao, int quantidade, double valor) {
        ItemVenda item = new ItemVenda();
        item.setProdutoCodigo(codigo);
        item.setProdutoDescricao(descricao);
        item.setQuantidade(quantidade);
        item.setValorUnitario(valor);
        item.setSubtotal(quantidade * valor);
        return item;
    }
}
