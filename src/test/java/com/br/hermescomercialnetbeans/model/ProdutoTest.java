package com.br.hermescomercialnetbeans.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

/**
 * Testes unitários para a classe Produto
 * @author marcos
 */
class ProdutoTest {

    private Produto produto;
    
    @BeforeEach
    void setUp() {
        produto = new Produto();
    }
    
    @Test
    @DisplayName("Deve criar produto com valores padrão")
    void testProdutoValoresPadrao() {
        assertNotNull(produto);
        assertNull(produto.getId());
        assertNull(produto.getNome());
        assertNull(produto.getCodigo());
        assertNull(produto.getDescricao());
        assertNull(produto.getPreco());
        assertNull(produto.getPrecoCompra());
        assertNull(produto.getCategoria());
        assertNull(produto.getSubCategoria());
        assertNull(produto.getMarca());
        assertNull(produto.getDataCompra());
        assertEquals(Integer.valueOf(0), produto.getEstoque());
        assertEquals(Integer.valueOf(0), produto.getEstoqueMinimo());
        assertTrue(produto.getAtivo());
    }
    
    @Test
    @DisplayName("Deve definir e obter valores básicos do produto")
    void testSetGetValoresBasicos() {
        produto.setId(1);
        produto.setNome("Notebook Dell");
        produto.setCodigo("NOTE001");
        produto.setPreco(new BigDecimal("3500.00"));
        produto.setPrecoCompra(new BigDecimal("2800.00"));
        produto.setCategoria("Informática");
        produto.setSubCategoria("Notebooks");
        produto.setMarca("Dell");
        produto.setDataCompra("2023-12-01");
        produto.setEstoque(10);
        produto.setEstoqueMinimo(5);
        produto.setAtivo(true);
        
        assertEquals(Integer.valueOf(1), produto.getId());
        assertEquals("Notebook Dell", produto.getNome());
        assertEquals("NOTE001", produto.getCodigo());
        assertEquals(new BigDecimal("3500.00"), produto.getPreco());
        assertEquals(new BigDecimal("2800.00"), produto.getPrecoCompra());
        assertEquals("Informática", produto.getCategoria());
        assertEquals("Notebooks", produto.getSubCategoria());
        assertEquals("Dell", produto.getMarca());
        assertEquals("2023-12-01", produto.getDataCompra());
        assertEquals(Integer.valueOf(10), produto.getEstoque());
        assertEquals(Integer.valueOf(5), produto.getEstoqueMinimo());
        assertTrue(produto.getAtivo());
    }
    
    @Test
    @DisplayName("Deve validar equals com mesmo ID")
    void testEqualsMesmoId() {
        Produto produto1 = new Produto();
        produto1.setId(1);
        
        Produto produto2 = new Produto();
        produto2.setId(1);
        
        assertEquals(produto1, produto2);
        assertEquals(produto1.hashCode(), produto2.hashCode());
    }
    
    @Test
    @DisplayName("Deve validar not equals com IDs diferentes")
    void testNotEqualsIdsDiferentes() {
        Produto produto1 = new Produto();
        produto1.setId(1);
        
        Produto produto2 = new Produto();
        produto2.setId(2);
        
        assertNotEquals(produto1, produto2);
    }
    
    @Test
    @DisplayName("Deve validar toString")
    void testToString() {
        produto.setId(1);
        produto.setNome("Notebook Dell");
        produto.setCodigo("NOTE001");
        
        String toString = produto.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nome='Notebook Dell'"));
        assertTrue(toString.contains("codigo='NOTE001'"));
    }
}
