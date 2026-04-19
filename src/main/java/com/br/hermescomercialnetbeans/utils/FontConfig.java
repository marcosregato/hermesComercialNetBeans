package com.br.hermescomercialnetbeans.utils;

import java.awt.*;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.List;

/**
 * Configuração de fontes para o sistema PDV Hermes Comercial
 * @author marcos
 */
public class FontConfig {
    
    // Lista de fontes preferenciais em ordem de prioridade
    private static final List<String> FONTES_PREFERENCIAIS = Arrays.asList(
        "Roboto",
        "Open Sans", 
        "Lato",
        "Montserrat",
        "Helvetica",
        "Arial", // fallback
        "SansSerif" // fallback final
    );
    
    // Tamanhos base para cálculo responsivo (referência 1920x1080)
    private static final int TAMANHO_BASE_PADRAO = 13;
    private static final int TAMANHO_BASE_TITULO = 18;
    private static final int TAMANHO_BASE_SUBTITULO = 15;
    private static final int TAMANHO_BASE_PEQUENO = 11;
    private static final int TAMANHO_BASE_GRANDE = 20;
    private static final int TAMANHO_BASE_BOTAO = 14;
    private static final int TAMANHO_BASE_CAMPO_TEXTO = 13;
    private static final int TAMANHO_BASE_TABELA = 12;
    private static final int TAMANHO_BASE_MENU = 13;
    
    // Resolução de referência (Full HD)
    private static final int RESOLUCAO_REFERENCIA_WIDTH = 1920;
    
    // Fator de escala dinâmico
    private static double fatorEscala = 1.0;
    
    // Tamanhos calculados dinamicamente
    public static int TAMANHO_PADRAO;
    public static int TAMANHO_TITULO;
    public static int TAMANHO_SUBTITULO;
    public static int TAMANHO_PEQUENO;
    public static int TAMANHO_GRANDE;
    public static int TAMANHO_BOTAO;
    public static int TAMANHO_CAMPO_TEXTO;
    public static int TAMANHO_TABELA;
    public static int TAMANHO_MENU;
    
    // Fontes pré-configuradas
    private static Font fontePadrao;
    private static Font fonteTitulo;
    private static Font fonteSubtitulo;
    private static Font fontePequena;
    private static Font fonteGrande;
    
    static {
        calcularFatorEscala();
        inicializarFontes();
    }
    
    /**
     * Calcula o fator de escala baseado na resolução da tela
     */
    private static void calcularFatorEscala() {
        try {
            // Obter resolução da tela
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            
            // Calcular fator de escala baseado na largura (mais importante para PDV)
            fatorEscala = (double) screenWidth / RESOLUCAO_REFERENCIA_WIDTH;
            
            // Limitar o fator de escala para evitar fontes muito pequenas ou muito grandes
            fatorEscala = Math.max(0.85, Math.min(1.5, fatorEscala));
            
            // Calcular tamanhos responsivos
            TAMANHO_PADRAO = calcularTamanhoResponsivo(TAMANHO_BASE_PADRAO);
            TAMANHO_TITULO = calcularTamanhoResponsivo(TAMANHO_BASE_TITULO);
            TAMANHO_SUBTITULO = calcularTamanhoResponsivo(TAMANHO_BASE_SUBTITULO);
            TAMANHO_PEQUENO = calcularTamanhoResponsivo(TAMANHO_BASE_PEQUENO);
            TAMANHO_GRANDE = calcularTamanhoResponsivo(TAMANHO_BASE_GRANDE);
            TAMANHO_BOTAO = calcularTamanhoResponsivo(TAMANHO_BASE_BOTAO);
            TAMANHO_CAMPO_TEXTO = calcularTamanhoResponsivo(TAMANHO_BASE_CAMPO_TEXTO);
            TAMANHO_TABELA = calcularTamanhoResponsivo(TAMANHO_BASE_TABELA);
            TAMANHO_MENU = calcularTamanhoResponsivo(TAMANHO_BASE_MENU);
            
        } catch (Exception e) {
            // Fallback para tamanhos padrão em caso de erro
            usarTamanhosPadrao();
        }
    }
    
    /**
     * Calcula tamanho responsivo baseado no fator de escala
     * @param tamanhoBase Tamanho base para cálculo
     * @return Tamanho calculado
     */
    private static int calcularTamanhoResponsivo(int tamanhoBase) {
        return (int) Math.round(tamanhoBase * fatorEscala);
    }
    
    /**
     * Usa tamanhos padrão como fallback
     */
    private static void usarTamanhosPadrao() {
        fatorEscala = 1.0;
        TAMANHO_PADRAO = TAMANHO_BASE_PADRAO;
        TAMANHO_TITULO = TAMANHO_BASE_TITULO;
        TAMANHO_SUBTITULO = TAMANHO_BASE_SUBTITULO;
        TAMANHO_PEQUENO = TAMANHO_BASE_PEQUENO;
        TAMANHO_GRANDE = TAMANHO_BASE_GRANDE;
        TAMANHO_BOTAO = TAMANHO_BASE_BOTAO;
        TAMANHO_CAMPO_TEXTO = TAMANHO_BASE_CAMPO_TEXTO;
        TAMANHO_TABELA = TAMANHO_BASE_TABELA;
        TAMANHO_MENU = TAMANHO_BASE_MENU;
    }
    
    /**
     * Inicializa as fontes do sistema
     */
    private static void inicializarFontes() {
        fontePadrao = criarFonte(TAMANHO_PADRAO, Font.PLAIN);
        fonteTitulo = criarFonte(TAMANHO_TITULO, Font.BOLD);
        fonteSubtitulo = criarFonte(TAMANHO_SUBTITULO, Font.BOLD);
        fontePequena = criarFonte(TAMANHO_PEQUENO, Font.PLAIN);
        fonteGrande = criarFonte(TAMANHO_GRANDE, Font.BOLD);
    }
    
    /**
     * Cria uma fonte com a primeira fonte disponível da lista de preferências
     * @param tamanho Tamanho da fonte
     * @param estilo Estilo da fonte (Font.PLAIN, Font.BOLD, Font.ITALIC)
     * @return Font configurada
     */
    private static Font criarFonte(int tamanho, int estilo) {
        String[] fontesDisponiveis = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        for (String fontePreferencial : FONTES_PREFERENCIAIS) {
            for (String fonteDisponivel : fontesDisponiveis) {
                if (fonteDisponivel.equalsIgnoreCase(fontePreferencial)) {
                    return new Font(fonteDisponivel, estilo, tamanho);
                }
            }
        }
        
        // Fallback para a fonte padrão do sistema
        return new Font(Font.SANS_SERIF, estilo, tamanho);
    }
    
    /**
     * Retorna a fonte padrão do sistema
     * @return Font padrão (tamanho 12)
     */
    public static Font getFontePadrao() {
        return fontePadrao;
    }
    
    /**
     * Retorna a fonte para títulos
     * @return Font para títulos (tamanho 16, negrito)
     */
    public static Font getFonteTitulo() {
        return fonteTitulo;
    }
    
    /**
     * Retorna a fonte para subtítulos
     * @return Font para subtítulos (tamanho 14, negrito)
     */
    public static Font getFonteSubtitulo() {
        return fonteSubtitulo;
    }
    
    /**
     * Retorna a fonte pequena
     * @return Font pequena (tamanho 10)
     */
    public static Font getFontePequena() {
        return fontePequena;
    }
    
    /**
     * Retorna a fonte grande
     * @return Font grande (tamanho 18, negrito)
     */
    public static Font getFonteGrande() {
        return fonteGrande;
    }
    
    /**
     * Retorna uma fonte personalizada
     * @param tamanho Tamanho da fonte
     * @param estilo Estilo da fonte
     * @return Font personalizada
     */
    public static Font getFontePersonalizada(int tamanho, int estilo) {
        return criarFonte(tamanho, estilo);
    }
    
    /**
     * Retorna fonte para botões de ação
     * @return Font para botões (tamanho 14)
     */
    public static Font getFonteBotao() {
        return criarFonte(TAMANHO_BOTAO, Font.BOLD);
    }
    
    /**
     * Retorna fonte para campos de texto
     * @return Font para campos de texto (tamanho 13)
     */
    public static Font getFonteCampoTexto() {
        return criarFonte(TAMANHO_CAMPO_TEXTO, Font.PLAIN);
    }
    
    /**
     * Retorna fonte para dados em tabelas
     * @return Font para tabelas (tamanho 12)
     */
    public static Font getFonteTabela() {
        return criarFonte(TAMANHO_TABELA, Font.PLAIN);
    }
    
    /**
     * Retorna fonte para itens de menu
     * @return Font para menu (tamanho 13)
     */
    public static Font getFonteMenu() {
        return criarFonte(TAMANHO_MENU, Font.PLAIN);
    }
    
    /**
     * Aplica a fonte padrão a um array de componentes
     * @param componentes Array de componentes para aplicar a fonte
     */
    public static void aplicarFontePadrao(java.awt.Component[] componentes) {
        if (componentes != null) {
            for (java.awt.Component componente : componentes) {
                if (componente != null) {
                    componente.setFont(fontePadrao);
                }
            }
        }
    }
    
    /**
     * Aplica a fonte padrão a um container e todos seus componentes filhos
     * @param container Container para aplicar a fonte recursivamente
     */
    public static void aplicarFontePadraoRecursivo(java.awt.Container container) {
        if (container == null) return;
        
        // Aplicar ao container
        container.setFont(fontePadrao);
        
        // Aplicar a todos os componentes filhos
        for (java.awt.Component componente : container.getComponents()) {
            if (componente instanceof java.awt.Container) {
                aplicarFontePadraoRecursivo((java.awt.Container) componente);
            } else {
                // Aplicar fonte padrão a componentes de texto
                if (componente instanceof javax.swing.JLabel ||
                    componente instanceof javax.swing.JButton ||
                    componente instanceof javax.swing.JTextField ||
                    componente instanceof javax.swing.JTextArea ||
                    componente instanceof javax.swing.JPasswordField ||
                    componente instanceof javax.swing.JComboBox ||
                    componente instanceof javax.swing.JCheckBox ||
                    componente instanceof javax.swing.JRadioButton ||
                    componente instanceof javax.swing.JMenuItem ||
                    componente instanceof javax.swing.JMenu ||
                    componente instanceof javax.swing.JTabbedPane) {
                    componente.setFont(fontePadrao);
                }
            }
        }
    }
    
    /**
     * Retorna informações sobre as fontes disponíveis no sistema
     * @return String com informações das fontes
     */
    public static String getInfoFontes() {
        StringBuilder info = new StringBuilder();
        info.append("Fonte padrão do sistema: ").append(fontePadrao.getName()).append("\n");
        info.append("Fonte título: ").append(fonteTitulo.getName()).append("\n");
        info.append("Fontes disponíveis: ");
        
        String[] fontesDisponiveis = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i = 0; i < Math.min(10, fontesDisponiveis.length); i++) {
            if (i > 0) info.append(", ");
            info.append(fontesDisponiveis[i]);
        }
        
        return info.toString();
    }
    
    /**
     * Retorna informações sobre a configuração responsiva
     * @return String com informações da configuração responsiva
     */
    public static String getInfoResponsivo() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        StringBuilder info = new StringBuilder();
        info.append("=== CONFIGURAÇÃO RESPONSIVA DE FONTES ===\n");
        info.append("Resolução atual: ").append(screenSize.width).append("x").append(screenSize.height).append("\n");
        info.append("Resolução referência: ").append(RESOLUCAO_REFERENCIA_WIDTH).append("x1080\n");
        info.append("Fator de escala: ").append(String.format("%.2f", fatorEscala)).append("\n");
        info.append("\n--- Tamanhos Calculados ---\n");
        info.append("Padrão: ").append(TAMANHO_PADRAO).append("px (base: ").append(TAMANHO_BASE_PADRAO).append("px)\n");
        info.append("Título: ").append(TAMANHO_TITULO).append("px (base: ").append(TAMANHO_BASE_TITULO).append("px)\n");
        info.append("Subtítulo: ").append(TAMANHO_SUBTITULO).append("px (base: ").append(TAMANHO_BASE_SUBTITULO).append("px)\n");
        info.append("Pequeno: ").append(TAMANHO_PEQUENO).append("px (base: ").append(TAMANHO_BASE_PEQUENO).append("px)\n");
        info.append("Grande: ").append(TAMANHO_GRANDE).append("px (base: ").append(TAMANHO_BASE_GRANDE).append("px)\n");
        info.append("Botão: ").append(TAMANHO_BOTAO).append("px (base: ").append(TAMANHO_BASE_BOTAO).append("px)\n");
        info.append("Campo Texto: ").append(TAMANHO_CAMPO_TEXTO).append("px (base: ").append(TAMANHO_BASE_CAMPO_TEXTO).append("px)\n");
        info.append("Tabela: ").append(TAMANHO_TABELA).append("px (base: ").append(TAMANHO_BASE_TABELA).append("px)\n");
        info.append("Menu: ").append(TAMANHO_MENU).append("px (base: ").append(TAMANHO_BASE_MENU).append("px)\n");
        
        return info.toString();
    }
    
    /**
     * Recalcula os tamanhos de fonte baseado na resolução atual
     * Útil para telas que podem mudar de resolução dinamicamente
     */
    public static void recalcularFontes() {
        calcularFatorEscala();
        inicializarFontes();
    }
    
    /**
     * Retorna o fator de escala atual
     * @return Fator de escala (1.0 = 100%)
     */
    public static double getFatorEscala() {
        return fatorEscala;
    }
}
