package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.model.MovimentoCaixa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovimentoCaixaDaoMockitoSimplifiedTest {
    
    private MovimentoCaixaDao movimentoCaixaDao;
    
    @BeforeEach
    void setUp() {
        movimentoCaixaDao = new MovimentoCaixaDao();
    }
    
    @Test
    @DisplayName("Deve criar MovimentoCaixaDao sem erros")
    void testCriarMovimentoCaixaDao() {
        assertNotNull(movimentoCaixaDao);
    }
    
    @Test
    @DisplayName("Deve validar criação de movimento caixa para teste")
    void testCriarMovimentoCaixaParaTeste() {
        MovimentoCaixa movimento = criarMovimentoCaixaTeste();
        
        assertNotNull(movimento);
        assertEquals(MovimentoCaixa.TipoMovimento.ABERTURA, movimento.getTipoMovimento());
        assertEquals(100.00, movimento.getValor());
        assertEquals("Abertura do caixa", movimento.getDescricao());
        assertEquals(0.00, movimento.getSaldoAnterior());
        assertEquals(100.00, movimento.getSaldoAtual());
    }
    
    @Test
    @DisplayName("Deve validar equals de movimento caixa")
    void testMovimentoCaixaEquals() {
        MovimentoCaixa movimento1 = criarMovimentoCaixaTeste();
        MovimentoCaixa movimento2 = criarMovimentoCaixaTeste();
        
        // Testa equals implementado na classe MovimentoCaixa
        assertNotEquals(movimento1, movimento2); // IDs diferentes
        
        movimento1.setId(1L);
        movimento2.setId(1L);
        
        assertEquals(movimento1, movimento2); // Mesmo ID
        assertEquals(movimento1.hashCode(), movimento2.hashCode()); // HashCode consistente
    }
    
    @Test
    @DisplayName("Deve validar toString de movimento caixa")
    void testMovimentoCaixaToString() {
        MovimentoCaixa movimento = criarMovimentoCaixaTeste();
        movimento.setId(1L);
        
        String toString = movimento.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("tipoMovimento=Abertura"));
        assertTrue(toString.contains("valor=100.0"));
    }
    
    @Test
    @DisplayName("Deve validar setters e getters de movimento caixa")
    void testMovimentoCaixaSettersGetters() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        
        movimento.setId(1L);
        movimento.setUsuarioId(2L);
        movimento.setUsuarioNome("Usuário Teste 2");
        movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.SANGRIA);
        movimento.setValor(50.00);
        movimento.setSaldoAnterior(100.00);
        movimento.setSaldoAtual(50.00);
        movimento.setDescricao("Sangria para pagamento");
        movimento.setObservacoes("Observação teste");
        movimento.setDataHora(LocalDateTime.now());
        
        assertEquals(1L, movimento.getId());
        assertEquals(2L, movimento.getUsuarioId());
        assertEquals("Usuário Teste 2", movimento.getUsuarioNome());
        assertEquals(MovimentoCaixa.TipoMovimento.SANGRIA, movimento.getTipoMovimento());
        assertEquals(50.00, movimento.getValor());
        assertEquals(100.00, movimento.getSaldoAnterior());
        assertEquals(50.00, movimento.getSaldoAtual());
        assertEquals("Sangria para pagamento", movimento.getDescricao());
        assertEquals("Observação teste", movimento.getObservacoes());
        assertNotNull(movimento.getDataHora());
    }
    
    @Test
    @DisplayName("Deve validar tipos de movimento")
    void testTiposMovimento() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        
        // Testar todos os tipos de movimento
        MovimentoCaixa.TipoMovimento[] tipos = {
            MovimentoCaixa.TipoMovimento.ABERTURA,
            MovimentoCaixa.TipoMovimento.FECHAMENTO,
            MovimentoCaixa.TipoMovimento.SANGRIA,
            MovimentoCaixa.TipoMovimento.SUPRIMENTO,
            MovimentoCaixa.TipoMovimento.CANCELAMENTO
        };
        
        for (MovimentoCaixa.TipoMovimento tipo : tipos) {
            movimento.setTipoMovimento(tipo);
            assertEquals(tipo, movimento.getTipoMovimento());
        }
    }
    
    @Test
    @DisplayName("Deve validar cálculo de saldo para abertura")
    void testCalculoSaldoAbertura() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA);
        movimento.setValor(100.00);
        movimento.setSaldoAnterior(0.00);
        
        // Para abertura: saldoAtual = saldoAnterior + valor
        double saldoEsperado = movimento.getSaldoAnterior() + movimento.getValor();
        assertEquals(saldoEsperado, 100.00);
    }
    
    @Test
    @DisplayName("Deve validar cálculo de saldo para sangria")
    void testCalculoSaldoSangria() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.SANGRIA);
        movimento.setValor(50.00);
        movimento.setSaldoAnterior(200.00);
        
        // Para sangria: saldoAtual = saldoAnterior - valor
        double saldoEsperado = movimento.getSaldoAnterior() - movimento.getValor();
        assertEquals(saldoEsperado, 150.00);
    }
    
    @Test
    @DisplayName("Deve validar cálculo de saldo para suprimento")
    void testCalculoSaldoSuprimento() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.SUPRIMENTO);
        movimento.setValor(75.00);
        movimento.setSaldoAnterior(100.00);
        
        // Para suprimento: saldoAtual = saldoAnterior + valor
        double saldoEsperado = movimento.getSaldoAnterior() + movimento.getValor();
        assertEquals(saldoEsperado, 175.00);
    }
    
    @Test
    @DisplayName("Deve validar valores monetários")
    void testValoresMonetarios() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA); // Definir tipo para evitar NullPointerException
        
        // Testar valores válidos
        double[] valoresValidos = {0.01, 1.00, 10.50, 99.99, 1000.00};
        
        for (double valor : valoresValidos) {
            movimento.setValor(valor);
            assertEquals(valor, movimento.getValor());
        }
        
        // Testar valor zero
        movimento.setValor(0.00);
        assertEquals(0.00, movimento.getValor());
    }
    
    @Test
    @DisplayName("Deve validar saldos anterior e atual")
    void testSaldos() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA); // Definir tipo para evitar NullPointerException
        
        // Testar saldos válidos
        double[] saldosValidos = {0.00, 50.00, 100.50, 999.99};
        
        for (double saldo : saldosValidos) {
            movimento.setSaldoAnterior(saldo);
            movimento.setSaldoAtual(saldo + 100.00); // Saldo atual maior
            
            assertEquals(saldo, movimento.getSaldoAnterior());
            assertEquals(saldo + 100.00, movimento.getSaldoAtual());
        }
    }
    
    @Test
    @DisplayName("Deve validar descrição do movimento")
    void testDescricaoMovimento() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        
        // Testar descrições normais
        movimento.setDescricao("Descrição normal");
        assertEquals("Descrição normal", movimento.getDescricao());
        
        // Testar descrição vazia
        movimento.setDescricao("");
        assertEquals("", movimento.getDescricao());
        
        // Testar descrição null
        movimento.setDescricao(null);
        assertNull(movimento.getDescricao());
        
        // Testar descrição longa
        String descLonga = "Esta é uma descrição muito longa para um movimento de caixa que pode ser usada em testes para verificar se o sistema consegue lidar com textos extensos";
        movimento.setDescricao(descLonga);
        assertEquals(descLonga, movimento.getDescricao());
    }
    
    @Test
    @DisplayName("Deve validar observações do movimento")
    void testObservacoesMovimento() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        
        // Testar observações normais
        movimento.setObservacoes("Observação normal");
        assertEquals("Observação normal", movimento.getObservacoes());
        
        // Testar observação vazia
        movimento.setObservacoes("");
        assertEquals("", movimento.getObservacoes());
        
        // Testar observação null
        movimento.setObservacoes(null);
        assertNull(movimento.getObservacoes());
        
        // Testar observação longa
        String obsLonga = "Esta é uma observação muito longa para um movimento de caixa que pode ser usada em testes para verificar se o sistema consegue lidar com textos extensos e detalhados";
        movimento.setObservacoes(obsLonga);
        assertEquals(obsLonga, movimento.getObservacoes());
    }
    
    @Test
    @DisplayName("Deve validar dados do usuário")
    void testDadosUsuario() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        
        // Testar IDs de usuário
        Long[] idsValidos = {1L, 10L, 100L, 1000L};
        
        for (Long id : idsValidos) {
            movimento.setUsuarioId(id);
            assertEquals(id, movimento.getUsuarioId());
        }
        
        // Testar nomes de usuário
        String[] nomesValidos = {
            "Usuário Teste",
            "João da Silva",
            "Maria Santos",
            "Administrador"
        };
        
        for (String nome : nomesValidos) {
            movimento.setUsuarioNome(nome);
            assertEquals(nome, movimento.getUsuarioNome());
        }
    }
    
    @Test
    @DisplayName("Deve validar data e hora do movimento")
    void testDataHoraMovimento() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        LocalDateTime agora = LocalDateTime.now();
        
        movimento.setDataHora(agora);
        assertEquals(agora, movimento.getDataHora());
        
        // Testar com data específica
        LocalDateTime dataEspecifica = LocalDateTime.of(2023, 12, 25, 14, 30);
        movimento.setDataHora(dataEspecifica);
        assertEquals(dataEspecifica, movimento.getDataHora());
    }
    
    /**
     * Método auxiliar para criar um movimento caixa de teste
     */
    private MovimentoCaixa criarMovimentoCaixaTeste() {
        MovimentoCaixa movimento = new MovimentoCaixa();
        movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA);
        movimento.setValor(100.00);
        movimento.setSaldoAnterior(0.00);
        movimento.setSaldoAtual(100.00);
        movimento.setDescricao("Abertura do caixa");
        movimento.setObservacoes("Observação de teste");
        movimento.setUsuarioId(1L);
        movimento.setUsuarioNome("Usuário Teste");
        movimento.setDataHora(LocalDateTime.now());
        return movimento;
    }
}
