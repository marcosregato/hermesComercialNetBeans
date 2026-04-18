package com.br.hermescomercialnetbeans.utils;

import com.br.hermescomercialnetbeans.model.Venda;
import com.br.hermescomercialnetbeans.model.ItemVenda;
import com.br.hermescomercialnetbeans.model.Pagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmissorCupomFiscalTest {
    
    private EmissorCupomFiscal emissorCupomFiscal;
    
    @BeforeEach
    void setUp() {
        emissorCupomFiscal = new EmissorCupomFiscal();
    }
    
    @Test
    @DisplayName("Deve criar EmissorCupomFiscal sem erros")
    void testCriarEmissorCupomFiscal() {
        assertNotNull(emissorCupomFiscal);
    }
    
    @Test
    @DisplayName("Deve gerar cupom fiscal para venda completa")
    void testGerarCupomFiscalCompleto() {
        // Arrange
        Venda venda = criarVendaCompleta();
        List<ItemVenda> itens = criarListaItens();
        List<Pagamento> pagamentos = criarListaPagamentos();
        
        // Act
        String cupom = EmissorCupomFiscal.gerarCupomFiscal(venda, itens, pagamentos);
        
        // Assert
        assertNotNull(cupom);
        assertFalse(cupom.isEmpty());
    }
    
    @Test
    @DisplayName("Deve gerar cupom fiscal com listas vazias")
    void testGerarCupomFiscalListasVazias() {
        // Arrange
        Venda venda = criarVendaCompleta();
        List<ItemVenda> itens = Arrays.asList();
        List<Pagamento> pagamentos = Arrays.asList();
        
        // Act
        String cupom = EmissorCupomFiscal.gerarCupomFiscal(venda, itens, pagamentos);
        
        // Assert
        assertNotNull(cupom);
        // Não deve lançar exceção mesmo com listas vazias
    }
    
    /**
     * Método auxiliar para criar uma venda completa
     */
    private Venda criarVendaCompleta() {
        Venda venda = new Venda();
        venda.setCodigo("VND-001");
        venda.setClienteNome("Cliente Teste");
        venda.setUsuarioNome("Vendedor Teste");
        venda.setValorTotal(200.00);
        venda.setValorDesconto(10.00);
        venda.setValorAcrescimo(0.00);
        venda.setValorFinal(190.00);
        venda.setFormaPagamento("DINHEIRO");
        venda.setObservacoes("Venda de teste completa");
        return venda;
    }
    
    /**
     * Método auxiliar para criar lista de itens
     */
    private List<ItemVenda> criarListaItens() {
        ItemVenda item1 = new ItemVenda();
        item1.setProdutoCodigo("PROD001");
        item1.setProdutoDescricao("Produto Teste 1");
        item1.setQuantidade(2);
        item1.setValorUnitario(50.00);
        item1.setSubtotal(100.00);
        
        ItemVenda item2 = new ItemVenda();
        item2.setProdutoCodigo("PROD002");
        item2.setProdutoDescricao("Produto Teste 2");
        item2.setQuantidade(1);
        item2.setValorUnitario(100.00);
        item2.setSubtotal(100.00);
        
        return Arrays.asList(item1, item2);
    }
    
    /**
     * Método auxiliar para criar lista de pagamentos
     */
    private List<Pagamento> criarListaPagamentos() {
        Pagamento pagamento = new Pagamento();
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.DINHEIRO);
        pagamento.setValor(190.00);
        pagamento.setStatus(Pagamento.StatusPagamento.PAGO);
        
        return Arrays.asList(pagamento);
    }
}
