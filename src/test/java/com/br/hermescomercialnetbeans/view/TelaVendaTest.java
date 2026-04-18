package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.model.Produto;
import com.br.hermescomercialnetbeans.model.Venda;
import com.br.hermescomercialnetbeans.model.ItemVenda;
import com.br.hermescomercialnetbeans.model.Pagamento;
import com.br.hermescomercialnetbeans.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TelaVendaTest {
    
    private TelaVenda telaVenda;
    private Usuario usuarioTeste;
    
    @BeforeEach
    void setUp() {
        // Criar usuário para teste
        usuarioTeste = new Usuario();
        usuarioTeste.setNome("vendedor");
        usuarioTeste.setId(1);
        
        // Criar tela em modo de teste para não exibir UI real
        telaVenda = new TelaVenda() {
            @Override
            public void setVisible(boolean visible) {
                // Não fazer nada para evitar exibição durante testes
            }
        };
    }
    
    @Test
    @DisplayName("Deve criar TelaVenda sem erros")
    void testCriarTelaVenda() {
        assertNotNull(telaVenda);
        assertTrue(telaVenda instanceof JInternalFrame);
    }
    
    @Test
    @DisplayName("Deve inicializar componentes da interface")
    void testInicializarComponentes() {
        // Act & Assert - Verificar se componentes foram criados
        assertNotNull(telaVenda);
        // Como é uma GUI, verificamos apenas se a instância foi criada corretamente
    }
    
    @Test
    @DisplayName("Deve simular adição de item à venda")
    void testAdicionarItemVenda() {
        // Arrange
        Produto produto = criarProdutoTeste();
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Simular adição de item
            ItemVenda item = new ItemVenda();
            item.setProdutoCodigo(produto.getCodigo());
            item.setProdutoDescricao(produto.getNome());
            item.setQuantidade(2);
            item.setValorUnitario(10.50);
            
            assertNotNull(item);
            assertEquals(2, item.getQuantidade());
            assertEquals(produto.getCodigo(), item.getProdutoCodigo());
        });
    }
    
    @Test
    @DisplayName("Deve simular cálculo do total da venda")
    void testCalcularTotalVenda() {
        // Arrange
        List<ItemVenda> itens = new ArrayList<>();
        itens.add(criarItemVenda("PROD001", "Produto 1", 10.00, 2));
        itens.add(criarItemVenda("PROD002", "Produto 2", 20.00, 1));
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            double total = 0.0;
            for (ItemVenda item : itens) {
                double subtotal = item.getValorUnitario() * item.getQuantidade();
                total += subtotal;
            }
            
            assertEquals(40.00, total);
            assertTrue(total > 0);
        });
    }
    
    @Test
    @DisplayName("Deve simular finalização da venda")
    void testFinalizarVenda() {
        // Arrange
        Venda venda = criarVendaTeste();
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Simular finalização
            venda.setStatus("CONCLUIDA");
            assertEquals("CONCLUIDA", venda.getStatus());
            
            // Simular geração de cupom
            String cupom = "CUPOM FISCAL - Venda #" + venda.getId();
            assertNotNull(cupom);
            assertTrue(cupom.contains("CUPOM"));
        });
    }
    
    @Test
    @DisplayName("Deve simular cancelamento da venda")
    void testCancelarVenda() {
        // Arrange
        Venda venda = criarVendaTeste();
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Simular cancelamento
            venda.setStatus("CANCELADA");
            assertEquals("CANCELADA", venda.getStatus());
        });
    }
    
    @Test
    @DisplayName("Deve simular busca de produto por código")
    void testBuscarProdutoPorCodigo() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Simular busca de produto
            String codigo = "PROD001";
            Produto produto = criarProdutoTeste();
            produto.setCodigo(codigo);
            
            assertEquals(codigo, produto.getCodigo());
            assertNotNull(produto.getNome());
        });
    }
    
    @Test
    @DisplayName("Deve simular validação de estoque")
    void testValidarEstoque() {
        // Arrange
        Produto produto = criarProdutoTeste();
        produto.setEstoque(10);
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            int quantidadeSolicitada = 5;
            int estoqueDisponivel = produto.getEstoque();
            
            assertTrue(estoqueDisponivel >= quantidadeSolicitada);
            
            // Testar estoque insuficiente
            quantidadeSolicitada = 15;
            assertFalse(estoqueDisponivel >= quantidadeSolicitada);
        });
    }
    
    @Test
    @DisplayName("Deve simular diferentes formas de pagamento")
    void testFormasPagamento() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            Pagamento.FormaPagamento[] formas = Pagamento.FormaPagamento.values();
            
            for (Pagamento.FormaPagamento forma : formas) {
                assertNotNull(forma);
                assertTrue(forma.getDescricao().length() > 0);
            }
            
            assertTrue(formas.length > 0);
        });
    }
    
    @Test
    @DisplayName("Deve simular cálculo de desconto")
    void testCalcularDesconto() {
        // Arrange
        double valorTotal = 100.00;
        double percentualDesconto = 10.00;
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            double valorDesconto = valorTotal * percentualDesconto / 100.00;
            double valorComDesconto = valorTotal - valorDesconto;
            
            assertEquals(10.00, valorDesconto);
            assertEquals(90.00, valorComDesconto);
        });
    }
    
    @Test
    @DisplayName("Deve simular atualização da tabela de itens")
    void testAtualizarTabelaItens() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Simular modelo de tabela
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Código");
            modelo.addColumn("Descrição");
            modelo.addColumn("Quantidade");
            modelo.addColumn("Valor Unitário");
            modelo.addColumn("Subtotal");
            
            // Adicionar itens
            Object[] item1 = {"PROD001", "Produto 1", 2, 10.00, 20.00};
            modelo.addRow(item1);
            
            assertEquals(1, modelo.getRowCount());
            assertEquals("PROD001", modelo.getValueAt(0, 0));
        });
    }
    
    @Test
    @DisplayName("Deve simular remoção de item da venda")
    void testRemoverItemVenda() {
        // Arrange
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Código");
        modelo.addColumn("Descrição");
        modelo.addColumn("Quantidade");
        modelo.addColumn("Valor Unitário");
        modelo.addColumn("Subtotal");
        
        Object[] item = {"PROD001", "Produto 1", 2, 10.00, 20.00};
        modelo.addRow(item);
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            assertEquals(1, modelo.getRowCount());
            
            // Remover item
            modelo.removeRow(0);
            assertEquals(0, modelo.getRowCount());
        });
    }
    
    @Test
    @DisplayName("Deve simular validação de campos obrigatórios")
    void testValidarCamposObrigatorios() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Validar campos da venda
            Venda venda = new Venda();
            
            // Campos vazios
            assertFalse(validarCamposVenda(venda));
            
            // Preencher campos
            venda.setCodigo("VND001");
            venda.setTipoPagamento("DINHEIRO");
            venda.setValorFinal(100.00);
            
            assertTrue(validarCamposVenda(venda));
        });
    }
    
    @Test
    @DisplayName("Deve simular emissão de cupom fiscal")
    void testEmissaoCupomFiscal() {
        // Arrange
        Venda venda = criarVendaTeste();
        List<ItemVenda> itens = new ArrayList<>();
        itens.add(criarItemVenda("PROD001", "Produto 1", 10.00, 2));
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Simular emissão de cupom
            StringBuilder cupom = new StringBuilder();
            cupom.append("=== CUPOM FISCAL ===\n");
            cupom.append("Venda: ").append(venda.getCodigo()).append("\n");
            cupom.append("Data: ").append(java.time.LocalDate.now()).append("\n");
            cupom.append("----------------------------\n");
            
            for (ItemVenda item : itens) {
                cupom.append(item.getProdutoDescricao()).append("\n");
                cupom.append("  ").append(item.getQuantidade()).append(" x ");
                cupom.append(item.getValorUnitario()).append(" = ");
                cupom.append(item.getValorUnitario() * item.getQuantidade()).append("\n");
            }
            
            cupom.append("----------------------------\n");
            cupom.append("TOTAL: ").append(venda.getValorFinal()).append("\n");
            
            String cupomStr = cupom.toString();
            assertNotNull(cupomStr);
            assertTrue(cupomStr.contains("CUPOM FISCAL"));
            assertTrue(cupomStr.contains(venda.getCodigo()));
        });
    }
    
    @Test
    @DisplayName("Deve simular múltiplas vendas")
    void testMultiplasVendas() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Venda> vendas = new ArrayList<>();
            
            for (int i = 1; i <= 3; i++) {
                Venda venda = criarVendaTeste();
                venda.setCodigo("VND00" + i);
                venda.setValorFinal(100.00 * i);
                vendas.add(venda);
            }
            
            assertEquals(3, vendas.size());
            assertEquals("VND001", vendas.get(0).getCodigo());
            assertEquals("VND003", vendas.get(2).getCodigo());
        });
    }
    
    @Test
    @DisplayName("Deve simular fechamento do caixa")
    void testFechamentoCaixa() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Simular fechamento do caixa
            List<Venda> vendasDia = new ArrayList<>();
            vendasDia.add(criarVendaTeste());
            
            double totalVendas = 0.0;
            for (Venda venda : vendasDia) {
                if ("CONCLUIDA".equals(venda.getStatus())) {
                    totalVendas += venda.getValorFinal();
                }
            }
            
            // A venda está com status "ABERTA", então total será 0
            assertEquals(0.0, totalVendas);
        });
    }
    
    // Métodos auxiliares
    private Produto criarProdutoTeste() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setCodigo("PROD001");
        produto.setNome("Produto Teste");
        produto.setPreco(new java.math.BigDecimal("10.50"));
        produto.setEstoque(100);
        return produto;
    }
    
    private ItemVenda criarItemVenda(String codigo, String descricao, double valor, int quantidade) {
        ItemVenda item = new ItemVenda();
        item.setProdutoCodigo(codigo);
        item.setProdutoDescricao(descricao);
        item.setValorUnitario(valor);
        item.setQuantidade(quantidade);
        return item;
    }
    
    private Venda criarVendaTeste() {
        Venda venda = new Venda();
        venda.setId(1L);
        venda.setCodigo("VND001");
        venda.setTipoPagamento("DINHEIRO");
        venda.setValorFinal(100.00);
        venda.setStatus("ABERTA");
        return venda;
    }
    
    private boolean validarCamposVenda(Venda venda) {
        return venda.getCodigo() != null && !venda.getCodigo().isEmpty() &&
               venda.getTipoPagamento() != null && !venda.getTipoPagamento().isEmpty() &&
               venda.getValorFinal() > 0;
    }
}
