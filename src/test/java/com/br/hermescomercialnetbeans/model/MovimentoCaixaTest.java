package com.br.hermescomercialnetbeans.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MovimentoCaixaTest {
    
    private MovimentoCaixa movimentoCaixa;
    
    @BeforeEach
    void setUp() {
        movimentoCaixa = new MovimentoCaixa();
    }
    
    @Test
    @DisplayName("Deve criar movimento de caixa com valores padrão")
    void testMovimentoCaixaValoresPadrao() {
        assertNotNull(movimentoCaixa);
        assertNull(movimentoCaixa.getId());
        assertNull(movimentoCaixa.getUsuarioId());
        assertNotNull(movimentoCaixa.getDataHora());
        assertNull(movimentoCaixa.getTipoMovimento());
        assertEquals(0.0, movimentoCaixa.getValor());
        assertNull(movimentoCaixa.getDescricao());
        assertNull(movimentoCaixa.getObservacoes());
    }
    
    @Test
    @DisplayName("Deve definir e obter valores básicos do movimento de caixa")
    void testSetGetValoresBasicos() {
        movimentoCaixa.setId(1L);
        movimentoCaixa.setUsuarioId(10L);
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA);
        movimentoCaixa.setValor(100.00);
        movimentoCaixa.setDescricao("Abertura de caixa");
        movimentoCaixa.setObservacoes("Observação teste");
        
        assertEquals(Long.valueOf(1), movimentoCaixa.getId());
        assertEquals(Long.valueOf(10), movimentoCaixa.getUsuarioId());
        assertEquals(MovimentoCaixa.TipoMovimento.ABERTURA, movimentoCaixa.getTipoMovimento());
        assertEquals(100.00, movimentoCaixa.getValor());
        assertEquals("Abertura de caixa", movimentoCaixa.getDescricao());
        assertEquals("Observação teste", movimentoCaixa.getObservacoes());
    }
    
    @Test
    @DisplayName("Deve definir e obter data e hora do movimento")
    void testSetGetDataHora() {
        LocalDateTime dataHora = LocalDateTime.of(2023, 12, 1, 14, 30);
        movimentoCaixa.setDataHora(dataHora);
        
        assertEquals(dataHora, movimentoCaixa.getDataHora());
    }
    
    @Test
    @DisplayName("Deve validar tipos de movimento")
    void testTiposMovimento() {
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA);
        assertEquals(MovimentoCaixa.TipoMovimento.ABERTURA, movimentoCaixa.getTipoMovimento());
        
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.SANGRIA);
        assertEquals(MovimentoCaixa.TipoMovimento.SANGRIA, movimentoCaixa.getTipoMovimento());
        
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.SUPRIMENTO);
        assertEquals(MovimentoCaixa.TipoMovimento.SUPRIMENTO, movimentoCaixa.getTipoMovimento());
        
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.VENDA);
        assertEquals(MovimentoCaixa.TipoMovimento.VENDA, movimentoCaixa.getTipoMovimento());
        
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.CANCELAMENTO);
        assertEquals(MovimentoCaixa.TipoMovimento.CANCELAMENTO, movimentoCaixa.getTipoMovimento());
        
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.FECHAMENTO);
        assertEquals(MovimentoCaixa.TipoMovimento.FECHAMENTO, movimentoCaixa.getTipoMovimento());
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo ID")
    void testEqualsMesmoId() {
        MovimentoCaixa movimento1 = new MovimentoCaixa();
        movimento1.setId(1L);
        
        MovimentoCaixa movimento2 = new MovimentoCaixa();
        movimento2.setId(1L);
        
        assertEquals(movimento1, movimento2);
        assertEquals(movimento1.hashCode(), movimento2.hashCode());
    }
    
    @Test
    @DisplayName("Deve validar not equals com IDs diferentes")
    void testNotEqualsIdsDiferentes() {
        MovimentoCaixa movimento1 = new MovimentoCaixa();
        movimento1.setId(1L);
        
        MovimentoCaixa movimento2 = new MovimentoCaixa();
        movimento2.setId(2L);
        
        assertNotEquals(movimento1, movimento2);
    }
    
    @Test
    @DisplayName("Deve validar equals com objeto nulo")
    void testEqualsObjetoNulo() {
        MovimentoCaixa movimento1 = new MovimentoCaixa();
        movimento1.setId(1L);
        
        assertNotEquals(movimento1, null);
    }
    
    @Test
    @DisplayName("Deve validar equals com classe diferente")
    void testEqualsClasseDiferente() {
        MovimentoCaixa movimento1 = new MovimentoCaixa();
        movimento1.setId(1L);
        
        assertNotEquals(movimento1, "string");
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo objeto")
    void testEqualsMesmoObjeto() {
        MovimentoCaixa movimento1 = new MovimentoCaixa();
        movimento1.setId(1L);
        
        assertEquals(movimento1, movimento1);
    }
    
    @Test
    @DisplayName("Deve validar toString")
    void testToString() {
        movimentoCaixa.setId(1L);
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA);
        movimentoCaixa.setValor(100.00);
        
        String toString = movimentoCaixa.toString();
        System.out.println("DEBUG MovimentoCaixa toString: " + toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("Abertura"));
        assertTrue(toString.contains("100.0"));
    }
    
    @Test
    @DisplayName("Deve validar valor positivo")
    void testValorPositivo() {
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA); // Definir tipo para evitar NullPointerException
        movimentoCaixa.setValor(0.0);
        assertEquals(0.0, movimentoCaixa.getValor());
        
        movimentoCaixa.setValor(10.50);
        assertEquals(10.50, movimentoCaixa.getValor());
        
        movimentoCaixa.setValor(999.99);
        assertEquals(999.99, movimentoCaixa.getValor());
    }
    
    @Test
    @DisplayName("Deve validar descrição e observação")
    void testDescricaoObservacao() {
        movimentoCaixa.setDescricao("Abertura do caixa");
        assertEquals("Abertura do caixa", movimentoCaixa.getDescricao());
        
        movimentoCaixa.setDescricao("Sangria para pagamento");
        assertEquals("Sangria para pagamento", movimentoCaixa.getDescricao());
        
        movimentoCaixa.setObservacoes("Observação importante");
        assertEquals("Observação importante", movimentoCaixa.getObservacoes());
        
        movimentoCaixa.setObservacoes("Detalhes adicionais");
        assertEquals("Detalhes adicionais", movimentoCaixa.getObservacoes());
    }
    
    @Test
    @DisplayName("Deve validar saldos")
    void testSaldos() {
        movimentoCaixa.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA); // Definir tipo para evitar NullPointerException
        movimentoCaixa.setSaldoAnterior(100.00);
        assertEquals(100.00, movimentoCaixa.getSaldoAnterior());
        
        movimentoCaixa.setSaldoAtual(150.00);
        assertEquals(150.00, movimentoCaixa.getSaldoAtual());
        
        movimentoCaixa.setSaldoAnterior(200.00);
        movimentoCaixa.setSaldoAtual(250.00);
        assertEquals(200.00, movimentoCaixa.getSaldoAnterior());
        assertEquals(250.00, movimentoCaixa.getSaldoAtual());
    }
}
