package com.br.hermescomercialnetbeans.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;

/**
 * Testes funcionais para TelaUsuario
 * @author marcos
 */
class TelaUsuarioTest {

    private TelaUsuario telaUsuario;
    private JFrame frameTeste;
    
    @BeforeEach
    void setUp() throws Exception {
        // Inicializar componentes em EDT
        SwingUtilities.invokeAndWait(() -> {
            frameTeste = new JFrame("Teste TelaUsuario");
            telaUsuario = new TelaUsuario();
            
            frameTeste.setContentPane(telaUsuario);
            frameTeste.pack();
            frameTeste.setVisible(false); // Não mostrar durante testes
        });
    }
    
    @AfterEach
    void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (frameTeste != null) {
                frameTeste.dispose();
            }
        });
    }
    
    @Test
    @DisplayName("Deve criar tela de usuário com componentes básicos")
    void testCriarTelaUsuario() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            assertNotNull(telaUsuario);
            assertTrue(telaUsuario.isClosable());
            assertTrue(telaUsuario.isMaximizable());
            assertTrue(telaUsuario.isIconifiable());
            assertTrue(telaUsuario.isResizable());
            
            // Verificar título
            assertNotNull(telaUsuario.getTitle());
            assertTrue(telaUsuario.getTitle().contains("Usuários"));
        });
    }
    
    @Test
    @DisplayName("Deve encontrar componentes principais da tela")
    void testEncontrarComponentesPrincipais() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            // Verificar se painéis principais existem
            Component[] componentes = telaUsuario.getContentPane().getComponents();
            assertTrue(componentes.length > 0);
            
            // Procurar por painel de filtros
            boolean encontrouPainelFiltros = encontrarComponentePorNome(telaUsuario, "painelFiltros") != null;
            boolean encontrouScrollTabela = encontrarComponentePorClasse(telaUsuario, "javax.swing.JScrollPane") != null;
            boolean encontrouTabela = encontrarComponentePorClasse(telaUsuario, "javax.swing.JTable") != null;
            
            assertTrue(encontrouScrollTabela || encontrouTabela);
        });
    }
    
    @Test
    @DisplayName("Deve encontrar botões principais")
    void testEncontrarBotoesPrincipais() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            // Procurar por botões principais
            Component[] botoes = encontrarBotoes(telaUsuario);
            assertTrue(botoes.length >= 6); // Novo, Salvar, Editar, Excluir, Cancelar, Limpar
            
            // Verificar textos dos botões
            boolean encontrouNovo = false;
            boolean encontrouSalvar = false;
            boolean encontrouEditar = false;
            boolean encontrouExcluir = false;
            boolean encontrouCancelar = false;
            boolean encontrouLimpar = false;
            
            for (Component botao : botoes) {
                if (botao instanceof javax.swing.JButton) {
                    String texto = ((javax.swing.JButton) botao).getText();
                    if ("Novo".equals(texto)) encontrouNovo = true;
                    if ("Salvar".equals(texto)) encontrouSalvar = true;
                    if ("Editar".equals(texto)) encontrouEditar = true;
                    if ("Excluir".equals(texto)) encontrouExcluir = true;
                    if ("Cancelar".equals(texto)) encontrouCancelar = true;
                    if ("Limpar".equals(texto)) encontrouLimpar = true;
                }
            }
            
            assertTrue(encontrouNovo);
            assertTrue(encontrouSalvar);
            assertTrue(encontrouEditar);
            assertTrue(encontrouExcluir);
            assertTrue(encontrouCancelar);
            assertTrue(encontrouLimpar);
        });
    }
    
    @Test
    @DisplayName("Deve encontrar campos de formulário")
    void testEncontrarCamposFormulario() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            // Procurar por campos de texto
            Component[] camposTexto = encontrarCamposTexto(telaUsuario);
            assertTrue(camposTexto.length >= 5); // ID, Nome, Login, Senha, Email
            
            // Procurar por abas
            java.util.List<Component> listaAbas = new java.util.ArrayList<>();
            encontrarComponentesPorClasse(telaUsuario, "javax.swing.JTabbedPane", listaAbas);
            Component[] abas = listaAbas.toArray(new Component[0]);
            assertTrue(abas.length > 0);
            
            if (abas.length > 0) {
                javax.swing.JTabbedPane tabbedPane = (javax.swing.JTabbedPane) abas[0];
                assertTrue(tabbedPane.getTabCount() >= 3); // Básicos, Funcionário, Cliente, Fornecedor
            }
        });
    }
    
    @Test
    @DisplayName("Deve ter tamanho e posição corretos")
    void testTamanhoPosicao() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            assertTrue(telaUsuario.getWidth() > 0);
            assertTrue(telaUsuario.getHeight() > 0);
            assertTrue(telaUsuario.getWidth() >= 800); // Mínimo esperado
            assertTrue(telaUsuario.getHeight() >= 500); // Mínimo esperado
        });
    }
    
    // Métodos auxiliares para encontrar componentes
    private Component encontrarComponentePorNome(Container container, String nome) {
        for (Component componente : container.getComponents()) {
            if (nome.equals(componente.getName())) {
                return componente;
            }
            if (componente instanceof Container) {
                Component encontrado = encontrarComponentePorNome((Container) componente, nome);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        return null;
    }
    
    private Component encontrarComponentePorClasse(Container container, String className) {
        for (Component componente : container.getComponents()) {
            if (className.equals(componente.getClass().getName())) {
                return componente;
            }
            if (componente instanceof Container) {
                Component encontrado = encontrarComponentePorClasse((Container) componente, className);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }
        return null;
    }
    
    private Component[] encontrarBotoes(Container container) {
        java.util.List<Component> botoes = new java.util.ArrayList<>();
        encontrarComponentesPorClasse(container, "javax.swing.JButton", botoes);
        return botoes.toArray(new Component[0]);
    }
    
    private Component[] encontrarCamposTexto(Container container) {
        java.util.List<Component> campos = new java.util.ArrayList<>();
        encontrarComponentesPorClasse(container, "javax.swing.JTextField", campos);
        encontrarComponentesPorClasse(container, "javax.swing.JPasswordField", campos);
        return campos.toArray(new Component[0]);
    }
    
    private void encontrarComponentesPorClasse(Container container, String className, java.util.List<Component> resultado) {
        for (Component componente : container.getComponents()) {
            if (className.equals(componente.getClass().getName())) {
                resultado.add(componente);
            }
            if (componente instanceof Container) {
                encontrarComponentesPorClasse((Container) componente, className, resultado);
            }
        }
    }
}
