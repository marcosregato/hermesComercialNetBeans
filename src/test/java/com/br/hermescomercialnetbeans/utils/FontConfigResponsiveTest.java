package com.br.hermescomercialnetbeans.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.awt.Dimension;
import java.awt.Toolkit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste para verificar o sistema de fontes responsivas
 * @author marcos
 */
class FontConfigResponsiveTest {

    @Test
    @DisplayName("Deve calcular tamanhos de fonte responsivos")
    void testTamanhosResponsivos() {
        // Act
        int tamanhoPadrao = FontConfig.TAMANHO_PADRAO;
        int tamanhoTitulo = FontConfig.TAMANHO_TITULO;
        int tamanhoSubtitulo = FontConfig.TAMANHO_SUBTITULO;
        
        // Assert
        assertTrue(tamanhoPadrao > 0, "Tamanho padrão deve ser maior que 0");
        assertTrue(tamanhoTitulo > tamanhoPadrao, "Título deve ser maior que padrão");
        assertTrue(tamanhoSubtitulo > tamanhoPadrao, "Subtítulo deve ser maior que padrão");
        assertTrue(tamanhoTitulo > tamanhoSubtitulo, "Título deve ser maior que subtítulo");
    }

    @Test
    @DisplayName("Deve ter fator de escala válido")
    void testFatorEscala() {
        // Act
        double fatorEscala = FontConfig.getFatorEscala();
        
        // Assert
        assertTrue(fatorEscala > 0, "Fator de escala deve ser positivo");
        assertTrue(fatorEscala >= 0.7, "Fator de escala mínimo é 0.7");
        assertTrue(fatorEscala <= 1.5, "Fator de escala máximo é 1.5");
    }

    @Test
    @DisplayName("Deve retornar informações responsivas")
    void testInfoResponsivo() {
        // Act
        String info = FontConfig.getInfoResponsivo();
        
        // Assert
        assertNotNull(info, "Informações responsivas não devem ser nulas");
        assertTrue(info.contains("CONFIGURAÇÃO RESPONSIVA"), "Deve conter título");
        assertTrue(info.contains("Resolução atual"), "Deve conter resolução atual");
        assertTrue(info.contains("Fator de escala"), "Deve conter fator de escala");
        assertTrue(info.contains("Tamanhos Calculados"), "Deve conter tamanhos calculados");
    }

    @Test
    @DisplayName("Deve retornar fontes válidas")
    void testFontesValidas() {
        // Act
        assertNotNull(FontConfig.getFontePadrao(), "Fonte padrão não deve ser nula");
        assertNotNull(FontConfig.getFonteTitulo(), "Fonte título não deve ser nula");
        assertNotNull(FontConfig.getFonteSubtitulo(), "Fonte subtítulo não deve ser nula");
        assertNotNull(FontConfig.getFonteBotao(), "Fonte botão não deve ser nula");
        assertNotNull(FontConfig.getFonteCampoTexto(), "Fonte campo texto não deve ser nula");
        assertNotNull(FontConfig.getFonteTabela(), "Fonte tabela não deve ser nula");
        assertNotNull(FontConfig.getFonteMenu(), "Fonte menu não deve ser nula");
    }

    @Test
    @DisplayName("Deve recalculular fontes sem erro")
    void testRecalcularFontes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            FontConfig.recalcularFontes();
        }, "Recalcular fontes não deve lançar exceção");
    }

    @Test
    @DisplayName("Deve mostrar informações da configuração atual")
    void testMostrarConfiguracaoAtual() {
        // Act
        String info = FontConfig.getInfoResponsivo();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Assert
        System.out.println("=== TESTE DE FONTES RESPONSIVAS ===");
        System.out.println("Resolução detectada: " + screenSize.width + "x" + screenSize.height);
        System.out.println("Fator de escala: " + String.format("%.2f", FontConfig.getFatorEscala()));
        System.out.println("\nTamanhos calculados:");
        System.out.println("- Padrão: " + FontConfig.TAMANHO_PADRAO + "px");
        System.out.println("- Título: " + FontConfig.TAMANHO_TITULO + "px");
        System.out.println("- Subtítulo: " + FontConfig.TAMANHO_SUBTITULO + "px");
        System.out.println("- Botão: " + FontConfig.TAMANHO_BOTAO + "px");
        System.out.println("- Campo Texto: " + FontConfig.TAMANHO_CAMPO_TEXTO + "px");
        System.out.println("- Tabela: " + FontConfig.TAMANHO_TABELA + "px");
        System.out.println("- Menu: " + FontConfig.TAMANHO_MENU + "px");
        
        // Verificar se os tamanhos são coerentes com a resolução
        if (screenSize.width >= 1920) {
            assertTrue(FontConfig.TAMANHO_PADRAO >= 13, "Em Full HD ou maior, padrão deve ser pelo menos 13px");
        } else if (screenSize.width >= 1366) {
            assertTrue(FontConfig.TAMANHO_PADRAO >= 11, "Em HD ou maior, padrão deve ser pelo menos 11px");
        }
        
        assertNotNull(info, "Informações não devem ser nulas");
    }
}
