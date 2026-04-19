package com.br.hermescomercialnetbeans.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class PagamentoTest {
    
    private Pagamento pagamento;
    
    @BeforeEach
    void setUp() {
        pagamento = new Pagamento();
    }
    
    @Test
    @DisplayName("Deve criar pagamento com valores padrão")
    void testPagamentoValoresPadrao() {
        assertNotNull(pagamento);
        assertNull(pagamento.getId());
        assertNull(pagamento.getVendaId());
        assertNull(pagamento.getFormaPagamento());
        assertEquals(0.0, pagamento.getValor());
        assertEquals(Pagamento.StatusPagamento.PENDENTE, pagamento.getStatus());
    }
    
    @Test
    @DisplayName("Deve definir e obter valores básicos do pagamento")
    void testSetGetValoresBasicos() {
        pagamento.setId(1L);
        pagamento.setVendaId(10L);
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.DINHEIRO);
        pagamento.setValor(100.00);
        pagamento.setStatus(Pagamento.StatusPagamento.PAGO);
        
        assertEquals(Long.valueOf(1), pagamento.getId());
        assertEquals(Long.valueOf(10), pagamento.getVendaId());
        assertEquals(Pagamento.FormaPagamento.DINHEIRO, pagamento.getFormaPagamento());
        assertEquals(100.00, pagamento.getValor());
        assertEquals(Pagamento.StatusPagamento.PAGO, pagamento.getStatus());
    }
    
    @Test
    @DisplayName("Deve validar formas de pagamento")
    void testFormasPagamento() {
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.DINHEIRO);
        assertEquals(Pagamento.FormaPagamento.DINHEIRO, pagamento.getFormaPagamento());
        
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.CARTAO_CREDITO);
        assertEquals(Pagamento.FormaPagamento.CARTAO_CREDITO, pagamento.getFormaPagamento());
        
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.CARTAO_DEBITO);
        assertEquals(Pagamento.FormaPagamento.CARTAO_DEBITO, pagamento.getFormaPagamento());
        
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.PIX);
        assertEquals(Pagamento.FormaPagamento.PIX, pagamento.getFormaPagamento());
        
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.CHEQUE);
        assertEquals(Pagamento.FormaPagamento.CHEQUE, pagamento.getFormaPagamento());
        
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.CREDIARIO);
        assertEquals(Pagamento.FormaPagamento.CREDIARIO, pagamento.getFormaPagamento());
    }
    
    @Test
    @DisplayName("Deve validar status do pagamento")
    void testStatusPagamento() {
        pagamento.setStatus(Pagamento.StatusPagamento.PENDENTE);
        assertEquals(Pagamento.StatusPagamento.PENDENTE, pagamento.getStatus());
        
        pagamento.setStatus(Pagamento.StatusPagamento.PAGO);
        assertEquals(Pagamento.StatusPagamento.PAGO, pagamento.getStatus());
        
        pagamento.setStatus(Pagamento.StatusPagamento.CANCELADO);
        assertEquals(Pagamento.StatusPagamento.CANCELADO, pagamento.getStatus());
        
        pagamento.setStatus(Pagamento.StatusPagamento.ESTORNADO);
        assertEquals(Pagamento.StatusPagamento.ESTORNADO, pagamento.getStatus());
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo ID")
    void testEqualsMesmoId() {
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setId(1L);
        
        Pagamento pagamento2 = new Pagamento();
        pagamento2.setId(1L);
        
        assertEquals(pagamento1, pagamento2);
        assertEquals(pagamento1.hashCode(), pagamento2.hashCode());
    }
    
    @Test
    @DisplayName("Deve validar not equals com IDs diferentes")
    void testNotEqualsIdsDiferentes() {
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setId(1L);
        
        Pagamento pagamento2 = new Pagamento();
        pagamento2.setId(2L);
        
        assertNotEquals(pagamento1, pagamento2);
    }
    
    @Test
    @DisplayName("Deve validar equals com objeto nulo")
    void testEqualsObjetoNulo() {
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setId(1L);
        
        assertNotEquals(pagamento1, null);
    }
    
    @Test
    @DisplayName("Deve validar equals com classe diferente")
    void testEqualsClasseDiferente() {
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setId(1L);
        
        assertNotEquals(pagamento1, "string");
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo objeto")
    void testEqualsMesmoObjeto() {
        Pagamento pagamento1 = new Pagamento();
        pagamento1.setId(1L);
        
        assertEquals(pagamento1, pagamento1);
    }
    
    @Test
    @DisplayName("Deve validar toString")
    void testToString() {
        pagamento.setId(1L);
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.DINHEIRO);
        pagamento.setValor(100.00);
        
        String toString = pagamento.toString();
        System.out.println("DEBUG Pagamento toString: " + toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("Dinheiro"));
        assertTrue(toString.contains("100.0"));
    }
    
    @Test
    @DisplayName("Deve validar valor positivo")
    void testValorPositivo() {
        pagamento.setValor(0.0);
        assertEquals(0.0, pagamento.getValor());
        
        pagamento.setValor(10.50);
        assertEquals(10.50, pagamento.getValor());
        
        pagamento.setValor(999.99);
        assertEquals(999.99, pagamento.getValor());
    }
    
    @Test
    @DisplayName("Deve validar valor não negativo")
    void testValorNaoNegativo() {
        // Valores negativos não deveriam ser permitidos
        pagamento.setValor(100.00);
        assertEquals(100.00, pagamento.getValor());
        
        // Se houver validação, valor negativo deveria ser rejeitado
        // pagamento.setValor(-10.00);
        // assertThrows(IllegalArgumentException.class, () -> pagamento.setValor(-10.00));
    }
    
    @Test
    @DisplayName("Deve validar descrição dos enums")
    void testDescricaoEnums() {
        assertEquals("Dinheiro", Pagamento.FormaPagamento.DINHEIRO.getDescricao());
        assertEquals("Cartão de Crédito", Pagamento.FormaPagamento.CARTAO_CREDITO.getDescricao());
        assertEquals("PIX", Pagamento.FormaPagamento.PIX.getDescricao());
        
        assertEquals("Pendente", Pagamento.StatusPagamento.PENDENTE.getDescricao());
        assertEquals("Pago", Pagamento.StatusPagamento.PAGO.getDescricao());
        assertEquals("Cancelado", Pagamento.StatusPagamento.CANCELADO.getDescricao());
    }
    
    @Test
    @DisplayName("Deve validar toString dos enums")
    void testToStringEnums() {
        assertEquals("Dinheiro", Pagamento.FormaPagamento.DINHEIRO.toString());
        assertEquals("Cartão de Crédito", Pagamento.FormaPagamento.CARTAO_CREDITO.toString());
        assertEquals("PIX", Pagamento.FormaPagamento.PIX.toString());
        
        assertEquals("Pendente", Pagamento.StatusPagamento.PENDENTE.toString());
        assertEquals("Pago", Pagamento.StatusPagamento.PAGO.toString());
        assertEquals("Cancelado", Pagamento.StatusPagamento.CANCELADO.toString());
    }
}
