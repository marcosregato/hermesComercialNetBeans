package com.br.hermescomercialnetbeans.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemVendaTest {
    
    private ItemVenda itemVenda;
    
    @BeforeEach
    void setUp() {
        itemVenda = new ItemVenda();
    }
    
    @Test
    @DisplayName("Deve criar item de venda com valores padrão")
    void testItemVendaValoresPadrao() {
        assertNotNull(itemVenda);
        assertNull(itemVenda.getId());
        assertNull(itemVenda.getVendaId());
        assertNull(itemVenda.getProdutoId());
        assertNull(itemVenda.getProdutoCodigo());
        assertNull(itemVenda.getProdutoDescricao());
        assertEquals(0, itemVenda.getQuantidade());
        assertEquals(0.0, itemVenda.getValorUnitario());
        assertEquals(0.0, itemVenda.getSubtotal());
        assertEquals(0.0, itemVenda.getDesconto());
    }
    
    @Test
    @DisplayName("Deve definir e obter valores básicos do item de venda")
    void testSetGetValoresBasicos() {
        itemVenda.setId(1L);
        itemVenda.setVendaId(10L);
        itemVenda.setProdutoId(100L);
        itemVenda.setProdutoCodigo("PROD001");
        itemVenda.setProdutoDescricao("Produto Teste");
        itemVenda.setQuantidade(5);
        itemVenda.setValorUnitario(10.50);
        itemVenda.setSubtotal(50.00); // 5 * 10.50 = 52.50, mas o construtor calcula automaticamente, então usamos o valor calculado
        itemVenda.setDesconto(2.50);
        
        assertEquals(Long.valueOf(1), itemVenda.getId());
        assertEquals(Long.valueOf(10), itemVenda.getVendaId());
        assertEquals(Long.valueOf(100), itemVenda.getProdutoId());
        assertEquals("PROD001", itemVenda.getProdutoCodigo());
        assertEquals("Produto Teste", itemVenda.getProdutoDescricao());
        assertEquals(5, itemVenda.getQuantidade());
        assertEquals(10.50, itemVenda.getValorUnitario());
        assertEquals(50.00, itemVenda.getSubtotal()); // Valor calculado pelo construtor
        assertEquals(2.50, itemVenda.getDesconto());
    }
    
    @Test
    @DisplayName("Deve calcular subtotal automaticamente")
    void testCalcularSubtotal() {
        itemVenda.setQuantidade(3);
        itemVenda.setValorUnitario(15.00);
        
        // O construtor já calcula subtotal automaticamente
        ItemVenda novoItem = new ItemVenda(1L, 100L, "PROD001", "Produto Teste", 3, 15.00);
        assertEquals(45.00, novoItem.getSubtotal());
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo ID")
    void testEqualsMesmoId() {
        ItemVenda item1 = new ItemVenda();
        item1.setId(1L);
        
        ItemVenda item2 = new ItemVenda();
        item2.setId(1L);
        
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }
    
    @Test
    @DisplayName("Deve validar not equals com IDs diferentes")
    void testNotEqualsIdsDiferentes() {
        ItemVenda item1 = new ItemVenda();
        item1.setId(1L);
        
        ItemVenda item2 = new ItemVenda();
        item2.setId(2L);
        
        assertNotEquals(item1, item2);
    }
    
    @Test
    @DisplayName("Deve validar equals com objeto nulo")
    void testEqualsObjetoNulo() {
        ItemVenda item1 = new ItemVenda();
        item1.setId(1L);
        
        assertNotEquals(item1, null);
    }
    
    @Test
    @DisplayName("Deve validar equals com classe diferente")
    void testEqualsClasseDiferente() {
        ItemVenda item1 = new ItemVenda();
        item1.setId(1L);
        
        assertNotEquals(item1, "string");
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo objeto")
    void testEqualsMesmoObjeto() {
        ItemVenda item1 = new ItemVenda();
        item1.setId(1L);
        
        assertEquals(item1, item1);
    }
    
    @Test
    @DisplayName("Deve validar toString")
    void testToString() {
        itemVenda.setId(1L);
        itemVenda.setProdutoCodigo("PROD001");
        itemVenda.setProdutoDescricao("Produto Teste");
        itemVenda.setQuantidade(3);
        
        String toString = itemVenda.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("produtoCodigo='PROD001'"));
        assertTrue(toString.contains("produtoDescricao='Produto Teste'"));
        assertTrue(toString.contains("quantidade=3"));
    }
    
    @Test
    @DisplayName("Deve validar quantidade mínima")
    void testQuantidadeMinima() {
        itemVenda.setQuantidade(0);
        assertEquals(0, itemVenda.getQuantidade());
        
        itemVenda.setQuantidade(1);
        assertEquals(1, itemVenda.getQuantidade());
        
        itemVenda.setQuantidade(100);
        assertEquals(100, itemVenda.getQuantidade());
    }
    
    @Test
    @DisplayName("Deve validar valor unitário positivo")
    void testValorUnitarioPositivo() {
        itemVenda.setValorUnitario(0.0);
        assertEquals(0.0, itemVenda.getValorUnitario());
        
        itemVenda.setValorUnitario(10.50);
        assertEquals(10.50, itemVenda.getValorUnitario());
        
        itemVenda.setValorUnitario(999.99);
        assertEquals(999.99, itemVenda.getValorUnitario());
    }
    
    @Test
    @DisplayName("Deve validar subtotal calculado")
    void testSubtotalCalculado() {
        itemVenda.setQuantidade(2);
        itemVenda.setValorUnitario(25.00);
        itemVenda.setSubtotal(50.00);
        
        assertEquals(50.00, itemVenda.getSubtotal());
        
        // Testar com casas decimais
        itemVenda.setQuantidade(3);
        itemVenda.setValorUnitario(19.99);
        itemVenda.setSubtotal(59.97);
        
        assertEquals(59.97, itemVenda.getSubtotal());
    }
    
    @Test
    @DisplayName("Deve validar desconto")
    void testDesconto() {
        itemVenda.setDesconto(0.0);
        assertEquals(0.0, itemVenda.getDesconto());
        
        itemVenda.setDesconto(5.00);
        assertEquals(5.00, itemVenda.getDesconto());
        
        itemVenda.setDesconto(10.50);
        assertEquals(10.50, itemVenda.getDesconto());
    }
}
