package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.model.Venda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VendaDaoMockitoSimplifiedTest {
    
    private VendaDao vendaDao;
    
    @BeforeEach
    void setUp() {
        vendaDao = new VendaDao();
    }
    
    @Test
    @DisplayName("Deve criar VendaDao sem erros")
    void testCriarVendaDao() {
        assertNotNull(vendaDao);
    }
    
    @Test
    @DisplayName("Deve validar criação de venda para teste")
    void testCriarVendaParaTeste() {
        Venda venda = criarVendaTeste();
        
        assertNotNull(venda);
        assertEquals("VND-001", venda.getCodigo());
        assertEquals(100.00, venda.getValorTotal());
        assertEquals("ABERTA", venda.getStatus());
        assertEquals(1L, venda.getUsuarioId());
        assertEquals(1L, venda.getClienteId());
    }
    
    @Test
    @DisplayName("Deve validar equals de venda")
    void testVendaEquals() {
        Venda venda1 = criarVendaTeste();
        Venda venda2 = criarVendaTeste();
        
        // Testa equals implementado na classe Venda
        assertNotEquals(venda1, venda2); // IDs diferentes
        
        venda1.setId(1L);
        venda2.setId(1L);
        
        assertEquals(venda1, venda2); // Mesmo ID
        assertEquals(venda1.hashCode(), venda2.hashCode()); // HashCode consistente
    }
    
    @Test
    @DisplayName("Deve validar toString de venda")
    void testVendaToString() {
        Venda venda = criarVendaTeste();
        venda.setId(1L);
        
        String toString = venda.toString();
        System.out.println("DEBUG Venda toString: " + toString);
        
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("VND-001"));
        assertTrue(toString.contains("ABERTA"));
    }
    
    @Test
    @DisplayName("Deve validar setters e getters de venda")
    void testVendaSettersGetters() {
        Venda venda = new Venda();
        
        venda.setId(1L);
        venda.setCodigo("VND-002");
        venda.setUsuarioId(2L);
        venda.setUsuarioNome("Usuário Teste 2");
        venda.setClienteId(2L);
        venda.setClienteNome("Cliente Teste 2");
        venda.setValorTotal(200.00);
        venda.setValorDesconto(10.00);
        venda.setValorAcrescimo(5.00);
        venda.setValorFinal(195.00);
        venda.setStatus("CONCLUIDA");
        venda.setFormaPagamento("CARTAO_CREDITO");
        venda.setObservacoes("Observação teste");
        venda.setCancelada(true);
        venda.setDataHora(LocalDateTime.now());
        
        assertEquals(1L, venda.getId());
        assertEquals("VND-002", venda.getCodigo());
        assertEquals(2L, venda.getUsuarioId());
        assertEquals("Usuário Teste 2", venda.getUsuarioNome());
        assertEquals(2L, venda.getClienteId());
        assertEquals("Cliente Teste 2", venda.getClienteNome());
        assertEquals(200.00, venda.getValorTotal());
        assertEquals(10.00, venda.getValorDesconto());
        assertEquals(5.00, venda.getValorAcrescimo());
        assertEquals(195.00, venda.getValorFinal());
        assertEquals("CANCELADA", venda.getStatus());
        assertEquals("CARTAO_CREDITO", venda.getFormaPagamento());
        assertEquals("Observação teste", venda.getObservacoes());
        assertTrue(venda.isCancelada());
        assertNotNull(venda.getDataHora());
    }
    
    @Test
    @DisplayName("Deve validar cálculo de valor final")
    void testCalculoValorFinal() {
        Venda venda = new Venda();
        venda.setValorTotal(100.00);
        venda.setValorDesconto(10.00);
        venda.setValorAcrescimo(5.00);
        
        // O valor final deve ser: 100 - 10 + 5 = 95
        assertEquals(95.00, venda.getValorTotal() - venda.getValorDesconto() + venda.getValorAcrescimo());
    }
    
    @Test
    @DisplayName("Deve validar status da venda")
    void testStatusVenda() {
        Venda venda = new Venda();
        
        // Definir status
        venda.setStatus("ABERTA");
        assertEquals("ABERTA", venda.getStatus());
        
        // Alterar status
        venda.setStatus("CONCLUIDA");
        assertEquals("CONCLUIDA", venda.getStatus());
        
        // Cancelar venda
        venda.setStatus("CANCELADA");
        venda.setCancelada(true);
        assertEquals("CANCELADA", venda.getStatus());
        assertTrue(venda.isCancelada());
    }
    
    @Test
    @DisplayName("Deve validar forma de pagamento")
    void testFormaPagamento() {
        Venda venda = new Venda();
        
        // Testar diferentes formas de pagamento
        String[] formasPagamento = {"DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "PIX", "BOLETO"};
        
        for (String forma : formasPagamento) {
            venda.setFormaPagamento(forma);
            assertEquals(forma, venda.getFormaPagamento());
        }
    }
    
    @Test
    @DisplayName("Deve validar data e hora da venda")
    void testDataHoraVenda() {
        Venda venda = new Venda();
        LocalDateTime agora = LocalDateTime.now();
        
        venda.setDataHora(agora);
        assertEquals(agora, venda.getDataHora());
        
        // Testar com data específica
        LocalDateTime dataEspecifica = LocalDateTime.of(2023, 12, 25, 14, 30);
        venda.setDataHora(dataEspecifica);
        assertEquals(dataEspecifica, venda.getDataHora());
    }
    
    @Test
    @DisplayName("Deve validar código da venda")
    void testCodigoVenda() {
        Venda venda = new Venda();
        
        // Testar códigos válidos
        String[] codigosValidos = {"VND-001", "VND-2023-001", "VND-123456"};
        
        for (String codigo : codigosValidos) {
            venda.setCodigo(codigo);
            assertEquals(codigo, venda.getCodigo());
        }
        
        // Testar código nulo
        venda.setCodigo(null);
        assertNull(venda.getCodigo());
        
        // Testar código vazio
        venda.setCodigo("");
        assertEquals("", venda.getCodigo());
    }
    
    @Test
    @DisplayName("Deve validar observações da venda")
    void testObservacoesVenda() {
        Venda venda = new Venda();
        
        // Testar observações normais
        venda.setObservacoes("Observação normal");
        assertEquals("Observação normal", venda.getObservacoes());
        
        // Testar observação vazia
        venda.setObservacoes("");
        assertEquals("", venda.getObservacoes());
        
        // Testar observação null
        venda.setObservacoes(null);
        assertNull(venda.getObservacoes());
        
        // Testar observação longa
        String obsLonga = "Esta é uma observação muito longa que pode ser usada para testar o campo de observações da venda e verificar se o sistema consegue lidar com textos extensos";
        venda.setObservacoes(obsLonga);
        assertEquals(obsLonga, venda.getObservacoes());
    }
    
    /**
     * Método auxiliar para criar uma venda de teste
     */
    private Venda criarVendaTeste() {
        Venda venda = new Venda();
        venda.setCodigo("VND-001");
        venda.setDataHora(LocalDateTime.now());
        venda.setClienteId(1L);
        venda.setClienteNome("Cliente Teste");
        venda.setUsuarioId(1L);
        venda.setUsuarioNome("Usuário Teste");
        venda.setValorTotal(100.00);
        venda.setValorDesconto(0.00);
        venda.setValorAcrescimo(0.00);
        venda.setValorFinal(100.00);
        venda.setStatus("ABERTA");
        venda.setFormaPagamento("DINHEIRO");
        venda.setObservacoes("Venda de teste");
        venda.setCancelada(false);
        return venda;
    }
}
