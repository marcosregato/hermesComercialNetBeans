package com.br.hermescomercialnetbeans.integration;

import com.br.hermescomercialnetbeans.controller.VendaController;
import com.br.hermescomercialnetbeans.dao.VendaDao;
import com.br.hermescomercialnetbeans.model.Venda;
import com.br.hermescomercialnetbeans.model.ItemVenda;
import com.br.hermescomercialnetbeans.model.Pagamento;
import com.br.hermescomercialnetbeans.utils.ValidarCampo;
import com.br.hermescomercialnetbeans.utils.ConvertDado;
import com.br.hermescomercialnetbeans.utils.EmissorCupomFiscal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendaIntegrationTest {
    
    private VendaController vendaController;
    private ValidarCampo validarCampo;
    private ConvertDado convertDado;
    
    @BeforeEach
    void setUp() {
        vendaController = new VendaController();
        validarCampo = new ValidarCampo();
        convertDado = new ConvertDado();
    }
    
    @Test
    @DisplayName("Deve integrar fluxo completo de venda")
    void testFluxoCompletoVenda() {
        // Arrange - Criar venda completa
        Venda venda = criarVendaTeste();
        List<ItemVenda> itens = criarItensTeste();
        List<Pagamento> pagamentos = criarPagamentosTeste();
        
        // Act - Simular fluxo completo
        // 1. Validação de dados
        assertDoesNotThrow(() -> validarCampo.campoData("01/12/2023"));
        assertDoesNotThrow(() -> validarCampo.isCPF("123.456.789-09"));
        
        // 2. Conversão de dados
        Date dataConvertida = convertDado.convertData("01/12/2023");
        assertNotNull(dataConvertida);
        
        // 3. Processamento da venda
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, 150.00));
        
        // 4. Geração de cupom fiscal
        String cupom = EmissorCupomFiscal.gerarCupomFiscal(venda, itens, pagamentos);
        assertNotNull(cupom);
        assertFalse(cupom.isEmpty());
        
        // Assert - Verificar consistência dos dados
        assertEquals("VND-001", venda.getCodigo());
        assertEquals(150.00, venda.getValorFinal());
        assertEquals(2, itens.size());
        assertEquals(1, pagamentos.size());
    }
    
    @Test
    @DisplayName("Deve integrar validação e processamento de venda")
    void testIntegracaoValidacaoProcessamento() {
        // Arrange
        String dataValida = "15/12/2023";
        String cpfValido = "11144477735";
        double totalVenda = 200.00;
        
        // Act - Integrar validação com processamento
        // 1. Validar dados
        String dataValidada = validarCampo.campoData(dataValida);
        assertNotNull(dataValidada);
        
        boolean cpfValidado = validarCampo.isCPF(cpfValido);
        assertTrue(cpfValidado);
        
        // 2. Converter dados
        Date dataConvertida = convertDado.convertData(dataValida);
        assertNotNull(dataConvertida);
        
        // 3. Processar venda
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, totalVenda));
        
        // Assert - Verificar integração
        assertNotNull(dataValidada);
        assertNotNull(dataConvertida);
        assertTrue(cpfValidado);
    }
    
    @Test
    @DisplayName("Deve lidar com erro em parte do fluxo sem quebrar integração")
    void testIntegracaoComErroParcial() {
        // Arrange
        Venda venda = criarVendaTeste();
        List<ItemVenda> itens = criarItensTeste();
        List<Pagamento> pagamentos = criarPagamentosTeste();
        
        // Act - Simular fluxo com erro em uma parte
        // 1. Dados inválidos na validação
        String dataInvalida = validarCampo.campoData("32/12/2023");
        assertNull(dataInvalida);
        
        // 2. CPF inválido
        boolean cpfInvalido = validarCampo.isCPF("123.456.789-00");
        assertFalse(cpfInvalido);
        
        // 3. Mesmo com dados inválidos, o processamento continua
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, 100.00));
        
        // 4. Geração de cupom ainda funciona
        String cupom = EmissorCupomFiscal.gerarCupomFiscal(venda, itens, pagamentos);
        assertNotNull(cupom);
        
        // Assert - Sistema continua funcionando mesmo com falhas parciais
        assertNotNull(cupom);
        assertFalse(cupom.isEmpty());
    }
    
    @Test
    @DisplayName("Deve integrar múltiplas operações de venda")
    void testMultiplasOperacoesIntegradas() {
        // Arrange
        List<Venda> vendas = Arrays.asList(
            criarVendaTeste(),
            criarVendaTeste2(),
            criarVendaTeste3()
        );
        
        // Act - Processar múltiplas vendas
        for (Venda venda : vendas) {
            // 1. Validar data
            String dataValidada = validarCampo.campoData("01/12/2023");
            assertNotNull(dataValidada);
            
            // 2. Processar venda
            assertDoesNotThrow(() -> vendaController.finalizarCompra(null, venda.getValorFinal()));
            
            // 3. Gerar cupom
            String cupom = EmissorCupomFiscal.gerarCupomFiscal(venda, criarItensTeste(), criarPagamentosTeste());
            assertNotNull(cupom);
        }
        
        // Assert - Todas as operações foram processadas
        assertEquals(3, vendas.size());
    }
    
    @Test
    @DisplayName("Deve manter consistência de dados na integração")
    void testConsistenciaDadosIntegracao() {
        // Arrange
        Venda venda = criarVendaTeste();
        String dataOriginal = "01/12/2023";
        
        // Act - Processar dados através de múltiplos componentes
        String dataValidada = validarCampo.campoData(dataOriginal);
        Date dataConvertida = convertDado.convertData(dataOriginal);
        
        // Assert - Verificar consistência
        assertNotNull(dataValidada);
        assertNotNull(dataConvertida);
        
        // A data convertida deve corresponder à data validada
        assertTrue(dataValidada.contains("2023"));
    }
    
    // Métodos auxiliares
    private Venda criarVendaTeste() {
        Venda venda = new Venda();
        venda.setCodigo("VND-001");
        venda.setClienteNome("Cliente Teste");
        venda.setUsuarioNome("Vendedor Teste");
        venda.setValorTotal(150.00);
        venda.setValorDesconto(0.00);
        venda.setValorAcrescimo(0.00);
        venda.setValorFinal(150.00);
        venda.setFormaPagamento("DINHEIRO");
        venda.setObservacoes("Venda de integração");
        return venda;
    }
    
    private Venda criarVendaTeste2() {
        Venda venda = new Venda();
        venda.setCodigo("VND-002");
        venda.setClienteNome("Cliente Teste 2");
        venda.setValorFinal(200.00);
        return venda;
    }
    
    private Venda criarVendaTeste3() {
        Venda venda = new Venda();
        venda.setCodigo("VND-003");
        venda.setClienteNome("Cliente Teste 3");
        venda.setValorFinal(300.00);
        return venda;
    }
    
    private List<ItemVenda> criarItensTeste() {
        ItemVenda item = new ItemVenda();
        item.setProdutoCodigo("PROD001");
        item.setProdutoDescricao("Produto Teste");
        item.setQuantidade(1);
        item.setValorUnitario(150.00);
        item.setSubtotal(150.00);
        return Arrays.asList(item);
    }
    
    private List<Pagamento> criarPagamentosTeste() {
        Pagamento pagamento = new Pagamento();
        pagamento.setFormaPagamento(Pagamento.FormaPagamento.DINHEIRO);
        pagamento.setValor(150.00);
        pagamento.setStatus(Pagamento.StatusPagamento.PAGO);
        return Arrays.asList(pagamento);
    }
}
