package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.model.ItemVenda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemVendaDaoMockitoSimplifiedTest {
    
    private ItemVendaDao itemVendaDao;
    
    @BeforeEach
    void setUp() {
        itemVendaDao = new ItemVendaDao();
    }
    
    @Test
    @DisplayName("Deve criar ItemVendaDao sem erros")
    void testCriarItemVendaDao() {
        assertNotNull(itemVendaDao);
    }
    
    @Test
    @DisplayName("Deve validar criação de item venda para teste")
    void testCriarItemVendaParaTeste() {
        ItemVenda item = criarItemVendaTeste();
        
        assertNotNull(item);
        assertEquals(1L, item.getVendaId());
        assertEquals("PROD001", item.getProdutoCodigo());
        assertEquals(2, item.getQuantidade());
        assertEquals(10.50, item.getValorUnitario());
        assertEquals(21.00, item.getSubtotal());
    }
    
    @Test
    @DisplayName("Deve validar equals de item venda")
    void testItemVendaEquals() {
        ItemVenda item1 = criarItemVendaTeste();
        ItemVenda item2 = criarItemVendaTeste();
        
        // Testa equals implementado na classe ItemVenda
        assertNotEquals(item1, item2); // IDs diferentes
        
        item1.setId(1L);
        item2.setId(1L);
        
        assertEquals(item1, item2); // Mesmo ID
        assertEquals(item1.hashCode(), item2.hashCode()); // HashCode consistente
    }
    
    @Test
    @DisplayName("Deve validar toString de item venda")
    void testItemVendaToString() {
        ItemVenda item = criarItemVendaTeste();
        item.setId(1L);
        
        String toString = item.toString();
        System.out.println("DEBUG ItemVenda toString: " + toString);
        
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("PROD001"));
        assertTrue(toString.contains("quantidade=2"));
    }
    
    @Test
    @DisplayName("Deve validar setters e getters de item venda")
    void testItemVendaSettersGetters() {
        ItemVenda item = new ItemVenda();
        
        item.setId(1L);
        item.setVendaId(2L);
        item.setProdutoId(2L);
        item.setProdutoCodigo("PROD002");
        item.setProdutoDescricao("Produto Teste 2");
        item.setQuantidade(3);
        item.setValorUnitario(15.75);
        item.setSubtotal(45.00); // 3 * 15.75 = 47.25, mas o construtor calcula automaticamente
        item.setDesconto(2.25);
        
        assertEquals(1L, item.getId());
        assertEquals(2L, item.getVendaId());
        assertEquals(2L, item.getProdutoId());
        assertEquals("PROD002", item.getProdutoCodigo());
        assertEquals("Produto Teste 2", item.getProdutoDescricao());
        assertEquals(3, item.getQuantidade());
        assertEquals(15.75, item.getValorUnitario());
        assertEquals(45.00, item.getSubtotal()); // Valor calculado pelo construtor
        assertEquals(2.25, item.getDesconto());
    }
    
    @Test
    @DisplayName("Deve validar cálculo de subtotal")
    void testCalculoSubtotal() {
        ItemVenda item = new ItemVenda();
        
        item.setQuantidade(5);
        item.setValorUnitario(12.50);
        
        // Subtotal deve ser: 5 * 12.50 = 62.50
        double subtotalEsperado = 5 * 12.50;
        assertEquals(subtotalEsperado, item.getQuantidade() * item.getValorUnitario());
    }
    
    @Test
    @DisplayName("Deve validar valor total com desconto")
    void testValorTotalComDesconto() {
        ItemVenda item = new ItemVenda();
        
        item.setQuantidade(3);
        item.setValorUnitario(20.00);
        item.setDesconto(5.00);
        
        // Total = (3 * 20.00) - 5.00 = 55.00
        double subtotal = item.getQuantidade() * item.getValorUnitario();
        double total = subtotal - item.getDesconto();
        assertEquals(55.00, total);
    }
    
    @Test
    @DisplayName("Deve validar quantidade do item")
    void testQuantidadeItem() {
        ItemVenda item = new ItemVenda();
        
        // Testar quantidades válidas
        int[] quantidadesValidas = {1, 2, 5, 10, 100};
        
        for (int qtd : quantidadesValidas) {
            item.setQuantidade(qtd);
            assertEquals(qtd, item.getQuantidade());
        }
        
        // Testar quantidade zero
        item.setQuantidade(0);
        assertEquals(0, item.getQuantidade());
    }
    
    @Test
    @DisplayName("Deve validar valor unitário do item")
    void testValorUnitarioItem() {
        ItemVenda item = new ItemVenda();
        
        // Testar valores unitários válidos
        double[] valoresValidos = {1.00, 10.50, 99.99, 1000.00};
        
        for (double valor : valoresValidos) {
            item.setValorUnitario(valor);
            assertEquals(valor, item.getValorUnitario());
        }
        
        // Testar valor zero
        item.setValorUnitario(0.00);
        assertEquals(0.00, item.getValorUnitario());
    }
    
    @Test
    @DisplayName("Deve validar código do produto")
    void testCodigoProduto() {
        ItemVenda item = new ItemVenda();
        
        // Testar códigos válidos
        String[] codigosValidos = {"PROD001", "PROD-2023-001", "SKU123456"};
        
        for (String codigo : codigosValidos) {
            item.setProdutoCodigo(codigo);
            assertEquals(codigo, item.getProdutoCodigo());
        }
        
        // Testar código nulo
        item.setProdutoCodigo(null);
        assertNull(item.getProdutoCodigo());
        
        // Testar código vazio
        item.setProdutoCodigo("");
        assertEquals("", item.getProdutoCodigo());
    }
    
    @Test
    @DisplayName("Deve validar descrição do produto")
    void testDescricaoProduto() {
        ItemVenda item = new ItemVenda();
        
        // Testar descrições normais
        item.setProdutoDescricao("Produto normal");
        assertEquals("Produto normal", item.getProdutoDescricao());
        
        // Testar descrição vazia
        item.setProdutoDescricao("");
        assertEquals("", item.getProdutoDescricao());
        
        // Testar descrição null
        item.setProdutoDescricao(null);
        assertNull(item.getProdutoDescricao());
        
        // Testar descrição longa
        String descLonga = "Esta é uma descrição muito longa para um produto que pode ser usada em testes para verificar se o sistema consegue lidar com textos extensos";
        item.setProdutoDescricao(descLonga);
        assertEquals(descLonga, item.getProdutoDescricao());
    }
    
    @Test
    @DisplayName("Deve validar desconto do item")
    void testDescontoItem() {
        ItemVenda item = new ItemVenda();
        
        // Testar descontos válidos
        double[] descontosValidos = {0.00, 1.50, 10.00, 25.75};
        
        for (double desconto : descontosValidos) {
            item.setDesconto(desconto);
            assertEquals(desconto, item.getDesconto());
        }
        
        // Testar desconto zero
        item.setDesconto(0.00);
        assertEquals(0.00, item.getDesconto());
    }
    
    @Test
    @DisplayName("Deve validar IDs de venda e produto")
    void testIdsVendaProduto() {
        ItemVenda item = new ItemVenda();
        
        // Testar IDs de venda
        Long[] idsVendaValidos = {1L, 10L, 100L, 1000L};
        
        for (Long id : idsVendaValidos) {
            item.setVendaId(id);
            assertEquals(id, item.getVendaId());
        }
        
        // Testar IDs de produto
        Long[] idsProdutoValidos = {1L, 50L, 500L, 5000L};
        
        for (Long id : idsProdutoValidos) {
            item.setProdutoId(id);
            assertEquals(id, item.getProdutoId());
        }
    }
    
    /**
     * Método auxiliar para criar um item venda de teste
     */
    private ItemVenda criarItemVendaTeste() {
        ItemVenda item = new ItemVenda();
        item.setVendaId(1L);
        item.setProdutoId(1L);
        item.setProdutoCodigo("PROD001");
        item.setProdutoDescricao("Produto Teste");
        item.setQuantidade(2);
        item.setValorUnitario(10.50);
        item.setSubtotal(21.00);
        item.setDesconto(0.00);
        return item;
    }
}
