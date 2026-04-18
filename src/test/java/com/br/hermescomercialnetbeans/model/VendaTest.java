package com.br.hermescomercialnetbeans.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VendaTest {
    
    private Venda venda;
    
    @BeforeEach
    void setUp() {
        venda = new Venda();
    }
    
    @Test
    @DisplayName("Deve criar venda com valores padrão")
    void testVendaValoresPadrao() {
        assertNotNull(venda);
        assertNull(venda.getId());
        assertNull(venda.getUsuarioId());
        assertNotNull(venda.getDataHora());
        assertNull(venda.getTipoPagamento());
        assertEquals(0.0, venda.getValorTotal());
        assertEquals(0.0, venda.getValorDesconto());
        assertEquals(0.0, venda.getValorFinal());
        assertEquals("ABERTA", venda.getStatus());
        assertNull(venda.getObservacoes());
        assertFalse(venda.isCancelada());
    }
    
    @Test
    @DisplayName("Deve definir e obter valores básicos da venda")
    void testSetGetValoresBasicos() {
        venda.setId(1L);
        venda.setUsuarioId(10L);
        venda.setTipoPagamento("DINHEIRO");
        venda.setValorTotal(100.0);
        venda.setValorDesconto(10.0);
        venda.setValorFinal(90.0);
        venda.setStatus("CONCLUIDA");
        venda.setObservacoes("Venda teste");
        
        assertEquals(Long.valueOf(1), venda.getId());
        assertEquals(Long.valueOf(10), venda.getUsuarioId());
        assertEquals("DINHEIRO", venda.getTipoPagamento());
        assertEquals(100.0, venda.getValorTotal());
        assertEquals(10.0, venda.getValorDesconto());
        assertEquals(90.0, venda.getValorFinal());
        assertEquals("CONCLUIDA", venda.getStatus());
        assertEquals("Venda teste", venda.getObservacoes());
    }
    
    @Test
    @DisplayName("Deve definir e obter data e hora da venda")
    void testSetGetDataHora() {
        LocalDateTime dataHora = LocalDateTime.of(2023, 12, 1, 14, 30);
        venda.setDataHora(dataHora);
        
        assertEquals(dataHora, venda.getDataHora());
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo ID")
    void testEqualsMesmoId() {
        Venda venda1 = new Venda();
        venda1.setId(1L);
        
        Venda venda2 = new Venda();
        venda2.setId(1L);
        
        assertEquals(venda1, venda2);
        assertEquals(venda1.hashCode(), venda2.hashCode());
    }
    
    @Test
    @DisplayName("Deve validar not equals com IDs diferentes")
    void testNotEqualsIdsDiferentes() {
        Venda venda1 = new Venda();
        venda1.setId(1L);
        
        Venda venda2 = new Venda();
        venda2.setId(2L);
        
        assertNotEquals(venda1, venda2);
    }
    
    @Test
    @DisplayName("Deve validar equals com objeto nulo")
    void testEqualsObjetoNulo() {
        Venda venda1 = new Venda();
        venda1.setId(1L);
        
        assertNotEquals(venda1, null);
    }
    
    @Test
    @DisplayName("Deve validar equals com classe diferente")
    void testEqualsClasseDiferente() {
        Venda venda1 = new Venda();
        venda1.setId(1L);
        
        assertNotEquals(venda1, "string");
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo objeto")
    void testEqualsMesmoObjeto() {
        Venda venda1 = new Venda();
        venda1.setId(1L);
        
        assertEquals(venda1, venda1);
    }
    
    @Test
    @DisplayName("Deve validar toString")
    void testToString() {
        venda.setId(1L);
        venda.setTipoPagamento("DINHEIRO");
        venda.setValorFinal(90.0);
        
        String toString = venda.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("tipoPagamento='DINHEIRO'"));
        assertTrue(toString.contains("valorFinal=90.0"));
    }
    
    @Test
    @DisplayName("Deve validar status da venda")
    void testStatusVenda() {
        venda.setStatus("ABERTA");
        assertEquals("ABERTA", venda.getStatus());
        
        venda.setStatus("CONCLUIDA");
        assertEquals("CONCLUIDA", venda.getStatus());
        
        venda.setStatus("CANCELADA");
        assertEquals("CANCELADA", venda.getStatus());
    }
    
    @Test
    @DisplayName("Deve validar formas de pagamento")
    void testFormasPagamento() {
        venda.setTipoPagamento("DINHEIRO");
        assertEquals("DINHEIRO", venda.getTipoPagamento());
        
        venda.setTipoPagamento("CARTAO_CREDITO");
        assertEquals("CARTAO_CREDITO", venda.getTipoPagamento());
        
        venda.setTipoPagamento("CARTAO_DEBITO");
        assertEquals("CARTAO_DEBITO", venda.getTipoPagamento());
        
        venda.setTipoPagamento("PIX");
        assertEquals("PIX", venda.getTipoPagamento());
    }
}
