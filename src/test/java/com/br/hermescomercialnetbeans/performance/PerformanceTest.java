package com.br.hermescomercialnetbeans.performance;

import com.br.hermescomercialnetbeans.utils.ValidarCampo;
import com.br.hermescomercialnetbeans.utils.ConvertDado;
import com.br.hermescomercialnetbeans.utils.EmissorCupomFiscal;
import com.br.hermescomercialnetbeans.model.Venda;
import com.br.hermescomercialnetbeans.model.ItemVenda;
import com.br.hermescomercialnetbeans.model.Pagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PerformanceTest {
    
    private ValidarCampo validarCampo;
    private ConvertDado convertDado;
    
    @BeforeEach
    void setUp() {
        validarCampo = new ValidarCampo();
        convertDado = new ConvertDado();
    }
    
    @Test
    @DisplayName("Deve validar CPF em tempo aceitável")
    void testPerformanceValidacaoCPF() {
        // Arrange
        String cpf = "123.456.789-09";
        int iteracoes = 10000;
        
        // Act
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iteracoes; i++) {
            validarCampo.isCPF(cpf);
        }
        
        long endTime = System.nanoTime();
        long duracao = endTime - startTime;
        double duracaoMs = duracao / 1_000_000.0;
        
        // Assert
        assertTrue(duracaoMs < 1000, 
            "Validação de CPF deve levar menos de 1 segundo para " + iteracoes + " iterações. Levou: " + duracaoMs + "ms");
        
        System.out.println("Validação de CPF: " + iteracoes + " iterações em " + duracaoMs + "ms");
    }
    
    @Test
    @DisplayName("Deve validar CNPJ em tempo aceitável")
    void testPerformanceValidacaoCNPJ() {
        // Arrange
        String cnpj = "12.345.678/0001-95";
        int iteracoes = 10000;
        
        // Act
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iteracoes; i++) {
            validarCampo.isCNPJ(cnpj);
        }
        
        long endTime = System.nanoTime();
        long duracao = endTime - startTime;
        double duracaoMs = duracao / 1_000_000.0;
        
        // Assert
        assertTrue(duracaoMs < 2000, 
            "Validação de CNPJ deve levar menos de 2 segundos para " + iteracoes + " iterações. Levou: " + duracaoMs + "ms");
        
        System.out.println("Validação de CNPJ: " + iteracoes + " iterações em " + duracaoMs + "ms");
    }
    
    @Test
    @DisplayName("Deve converter data em tempo aceitável")
    void testPerformanceConversaoData() {
        // Arrange
        String data = "01/12/2023";
        int iteracoes = 10000;
        
        // Act
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iteracoes; i++) {
            convertDado.convertData(data);
        }
        
        long endTime = System.nanoTime();
        long duracao = endTime - startTime;
        double duracaoMs = duracao / 1_000_000.0;
        
        // Assert
        assertTrue(duracaoMs < 1500, 
            "Conversão de data deve levar menos de 1.5 segundos para " + iteracoes + " iterações. Levou: " + duracaoMs + "ms");
        
        System.out.println("Conversão de data: " + iteracoes + " iterações em " + duracaoMs + "ms");
    }
    
    @Test
    @DisplayName("Deve gerar cupom fiscal em tempo aceitável")
    void testPerformanceGeracaoCupomFiscal() {
        // Arrange
        Venda venda = criarVendaTeste();
        List<ItemVenda> itens = criarItensTeste(10); // 10 itens
        List<Pagamento> pagamentos = criarPagamentosTeste(3); // 3 pagamentos
        int iteracoes = 1000;
        
        // Act
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iteracoes; i++) {
            EmissorCupomFiscal.gerarCupomFiscal(venda, itens, pagamentos);
        }
        
        long endTime = System.nanoTime();
        long duracao = endTime - startTime;
        double duracaoMs = duracao / 1_000_000.0;
        
        // Assert
        assertTrue(duracaoMs < 5000, 
            "Geração de cupom fiscal deve levar menos de 5 segundos para " + iteracoes + " iterações. Levou: " + duracaoMs + "ms");
        
        System.out.println("Geração de cupom fiscal: " + iteracoes + " iterações em " + duracaoMs + "ms");
    }
    
    @Test
    @DisplayName("Deve processar múltiplas validações em tempo aceitável")
    void testPerformanceMultiplasValidacoes() {
        // Arrange
        List<String> cpfs = Arrays.asList(
            "123.456.789-09", "111.444.777-35", "222.333.444-55",
            "333.222.111-66", "444.555.666-77"
        );
        List<String> cnpjs = Arrays.asList(
            "12.345.678/0001-95", "11.222.333/0001-81", "22.333.444/0001-72"
        );
        List<String> datas = Arrays.asList(
            "01/12/2023", "15/01/2024", "29/02/2024", "31/12/2023"
        );
        int iteracoes = 5000;
        
        // Act
        long startTime = System.nanoTime();
        
        for (int i = 0; i < iteracoes; i++) {
            // Validar CPFs
            for (String cpf : cpfs) {
                validarCampo.isCPF(cpf);
            }
            
            // Validar CNPJs
            for (String cnpj : cnpjs) {
                validarCampo.isCNPJ(cnpj);
            }
            
            // Validar datas
            for (String data : datas) {
                validarCampo.campoData(data);
            }
            
            // Converter datas
            for (String data : datas) {
                convertDado.convertData(data);
            }
        }
        
        long endTime = System.nanoTime();
        long duracao = endTime - startTime;
        double duracaoMs = duracao / 1_000_000.0;
        
        // Assert
        assertTrue(duracaoMs < 3000, 
            "Múltiplas validações devem levar menos de 3 segundos para " + iteracoes + " iterações. Levou: " + duracaoMs + "ms");
        
        System.out.println("Múltiplas validações: " + iteracoes + " iterações em " + duracaoMs + "ms");
    }
    
    @Test
    @DisplayName("Deve lidar com carga de memória aceitável")
    void testPerformanceMemoria() {
        // Arrange
        int quantidadeVendas = 1000;
        List<Venda> vendas = new ArrayList<>();
        
        // Act
        long memoriaInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        // Criar muitas vendas
        for (int i = 0; i < quantidadeVendas; i++) {
            Venda venda = criarVendaTeste();
            venda.setCodigo("VND-" + String.format("%04d", i));
            vendas.add(venda);
        }
        
        // Processar vendas
        for (Venda venda : vendas) {
            List<ItemVenda> itens = criarItensTeste(5);
            List<Pagamento> pagamentos = criarPagamentosTeste(2);
            EmissorCupomFiscal.gerarCupomFiscal(venda, itens, pagamentos);
        }
        
        long memoriaFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoriaUsada = memoriaFinal - memoriaInicial;
        double memoriaUsadaMB = memoriaUsada / (1024.0 * 1024.0);
        
        // Assert
        assertTrue(memoriaUsadaMB < 100, 
            "Uso de memória deve ser menor que 100MB para " + quantidadeVendas + " vendas. Usou: " + memoriaUsadaMB + "MB");
        
        System.out.println("Memória usada para " + quantidadeVendas + " vendas: " + memoriaUsadaMB + "MB");
    }
    
    @Test
    @DisplayName("Deve manter performance sob carga crescente")
    void testPerformanceCargaCrescente() {
        // Arrange
        int[] cargas = {100, 500, 1000, 2000};
        String cpf = "123.456.789-09";
        
        // Act & Assert
        for (int carga : cargas) {
            long startTime = System.nanoTime();
            
            for (int i = 0; i < carga; i++) {
                validarCampo.isCPF(cpf);
            }
            
            long endTime = System.nanoTime();
            double duracaoMs = (endTime - startTime) / 1_000_000.0;
            double tempoPorOperacao = duracaoMs / carga;
            
            // Assert - tempo por operação não deve degradar muito
            assertTrue(tempoPorOperacao < 0.5, 
                "Tempo por operação deve ser menor que 0.5ms. Para carga " + carga + ": " + tempoPorOperacao + "ms");
            
            System.out.println("Carga " + carga + ": " + duracaoMs + "ms total, " + tempoPorOperacao + "ms por operação");
        }
    }
    
    // Métodos auxiliares
    private Venda criarVendaTeste() {
        Venda venda = new Venda();
        venda.setCodigo("VND-001");
        venda.setClienteNome("Cliente Teste");
        venda.setUsuarioNome("Vendedor Teste");
        venda.setValorTotal(1000.00);
        venda.setValorDesconto(50.00);
        venda.setValorAcrescimo(0.00);
        venda.setValorFinal(950.00);
        venda.setFormaPagamento("DINHEIRO");
        venda.setObservacoes("Venda de performance test");
        return venda;
    }
    
    private List<ItemVenda> criarItensTeste(int quantidade) {
        List<ItemVenda> itens = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            ItemVenda item = new ItemVenda();
            item.setProdutoCodigo("PROD" + String.format("%03d", i + 1));
            item.setProdutoDescricao("Produto " + (i + 1));
            item.setQuantidade(i + 1);
            item.setValorUnitario(10.0 * (i + 1));
            item.setSubtotal(item.getQuantidade() * item.getValorUnitario());
            itens.add(item);
        }
        return itens;
    }
    
    private List<Pagamento> criarPagamentosTeste(int quantidade) {
        List<Pagamento> pagamentos = new ArrayList<>();
        double valorPorPagamento = 950.0 / quantidade;
        
        for (int i = 0; i < quantidade; i++) {
            Pagamento pagamento = new Pagamento();
            pagamento.setFormaPagamento(i == 0 ? Pagamento.FormaPagamento.DINHEIRO : Pagamento.FormaPagamento.CARTAO_CREDITO);
            pagamento.setValor(valorPorPagamento);
            pagamento.setStatus(Pagamento.StatusPagamento.PAGO);
            pagamentos.add(pagamento);
        }
        return pagamentos;
    }
}
